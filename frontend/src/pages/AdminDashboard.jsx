import { useEffect, useState } from "react";
import client from "../api/client";

export default function AdminDashboard() {
  const [tab, setTab] = useState("utilisateurs");
  const [users, setUsers] = useState([]);
  const [tokens, setTokens] = useState([]);
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);
  const [editingUser, setEditingUser] = useState(null);
  const [userForm, setUserForm] = useState({ username: "", password: "", role: "EDITEUR" });
  const [tokenDescription, setTokenDescription] = useState("");

  const loadAll = () => {
    client.get("/admin/utilisateurs").then((res) => setUsers(res.data));
    client.get("/admin/jetons").then((res) => setTokens(res.data));
  };

  useEffect(loadAll, []);

  const flash = (msg) => { setMessage(msg); setError(null); setTimeout(() => setMessage(null), 3000); };
  const failWith = (msg) => { setError(msg); setMessage(null); };

  const startNewUser = () => {
    setEditingUser("new");
    setUserForm({ username: "", password: "", role: "EDITEUR" });
  };

  const startEditUser = (u) => {
    setEditingUser(u.id);
    setUserForm({ username: u.username, password: "", role: u.role });
  };

  const saveUser = async (e) => {
    e.preventDefault();
    try {
      if (editingUser === "new") {
        await client.post("/admin/utilisateurs", userForm);
        flash("Utilisateur cree.");
      } else {
        await client.put(`/admin/utilisateurs/${editingUser}`, userForm);
        flash("Utilisateur mis a jour.");
      }
      setEditingUser(null);
      loadAll();
    } catch {
      failWith("Impossible d'enregistrer cet utilisateur (nom peut-etre deja pris).");
    }
  };

  const deleteUser = async (id) => {
    if (!window.confirm("Supprimer cet utilisateur ?")) return;
    try {
      await client.delete(`/admin/utilisateurs/${id}`);
      flash("Utilisateur supprime.");
      loadAll();
    } catch {
      failWith("Suppression impossible.");
    }
  };

  const generateToken = async (e) => {
    e.preventDefault();
    try {
      await client.post("/admin/jetons", { description: tokenDescription });
      flash("Jeton genere.");
      setTokenDescription("");
      loadAll();
    } catch {
      failWith("Impossible de generer le jeton.");
    }
  };

  const deleteToken = async (id) => {
    if (!window.confirm("Revoquer ce jeton ? Les applications qui l'utilisent perdront l'acces au service SOAP.")) return;
    try {
      await client.delete(`/admin/jetons/${id}`);
      flash("Jeton revoque.");
      loadAll();
    } catch {
      failWith("Suppression impossible.");
    }
  };

  return (
    <div className="page">
      <div className="dashboard-header">
        <h1 className="dashboard-title">Administration</h1>
        {tab === "utilisateurs" && !editingUser && (
          <button className="btn btn--primary" onClick={startNewUser}>+ Nouvel utilisateur</button>
        )}
      </div>

      <div className="tabs">
        <button className={tab === "utilisateurs" ? "active" : ""} onClick={() => { setTab("utilisateurs"); setEditingUser(null); }}>Utilisateurs</button>
        <button className={tab === "jetons" ? "active" : ""} onClick={() => setTab("jetons")}>Jetons SOAP</button>
      </div>

      {message && <div className="success-banner">{message}</div>}
      {error && <div className="error-banner">{error}</div>}

      {tab === "utilisateurs" && (
        editingUser ? (
          <div className="card" style={{ maxWidth: 480 }}>
            <h2 className="form-title">{editingUser === "new" ? "Nouvel utilisateur" : "Modifier l'utilisateur"}</h2>
            <form onSubmit={saveUser}>
              <div className="field">
                <label>Nom d'utilisateur</label>
                <input value={userForm.username} onChange={(e) => setUserForm({ ...userForm, username: e.target.value })} required />
              </div>
              <div className="field">
                <label>Mot de passe {editingUser !== "new" && "(laisser vide pour ne pas changer)"}</label>
                <input type="password" value={userForm.password} onChange={(e) => setUserForm({ ...userForm, password: e.target.value })} required={editingUser === "new"} />
              </div>
              <div className="field">
                <label>Role</label>
                <select value={userForm.role} onChange={(e) => setUserForm({ ...userForm, role: e.target.value })}>
                  <option value="EDITEUR">Editeur</option>
                  <option value="ADMIN">Administrateur</option>
                </select>
              </div>
              <div className="row-actions">
                <button className="btn btn--primary" type="submit">Enregistrer</button>
                <button className="btn btn--ghost" type="button" onClick={() => setEditingUser(null)}>Annuler</button>
              </div>
            </form>
          </div>
        ) : (
          <table>
            <thead><tr><th>Nom d'utilisateur</th><th>Role</th><th></th></tr></thead>
            <tbody>
              {users.map((u) => (
                <tr key={u.id}>
                  <td>{u.username}</td>
                  <td><span className={`tag-pill ${u.role === "ADMIN" ? "tag-pill--gold" : ""}`}>{u.role}</span></td>
                  <td className="row-actions">
                    <button className="btn btn--ghost btn--sm" onClick={() => startEditUser(u)}>Modifier</button>
                    <button className="btn btn--danger btn--sm" onClick={() => deleteUser(u.id)}>Supprimer</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )
      )}

      {tab === "jetons" && (
        <>
          <p style={{ color: "var(--slate)", fontSize: "0.9rem", marginBottom: 20 }}>
            Ces jetons permettent aux applications externes (comme l'application client) d'acceder
            au service web SOAP de gestion des utilisateurs.
          </p>
          <div className="card card--tight" style={{ marginBottom: 24, maxWidth: 520 }}>
            <form onSubmit={generateToken} style={{ display: "flex", gap: 10, alignItems: "flex-end" }}>
              <div className="field" style={{ marginBottom: 0, flex: 1 }}>
                <label>Description du jeton</label>
                <input value={tokenDescription} onChange={(e) => setTokenDescription(e.target.value)} placeholder="Ex : Application client desktop" required />
              </div>
              <button className="btn btn--primary" type="submit">Generer</button>
            </form>
          </div>
          <table>
            <thead><tr><th>Jeton</th><th>Description</th><th>Cree le</th><th></th></tr></thead>
            <tbody>
              {tokens.map((t) => (
                <tr key={t.id}>
                  <td><span className="token-value">{t.token}</span></td>
                  <td>{t.description}</td>
                  <td>{new Date(t.dateCreation).toLocaleDateString("fr-FR")}</td>
                  <td><button className="btn btn--danger btn--sm" onClick={() => deleteToken(t.id)}>Revoquer</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
}
