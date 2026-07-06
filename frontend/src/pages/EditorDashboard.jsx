import { useEffect, useState } from "react";
import client from "../api/client";

export default function EditorDashboard() {
  const [tab, setTab] = useState("articles");
  const [articles, setArticles] = useState([]);
  const [categories, setCategories] = useState([]);
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);
  const [editingArticle, setEditingArticle] = useState(null);
  const [articleForm, setArticleForm] = useState({ titre: "", resume: "", contenu: "", categorieId: "" });
  const [newCategoryName, setNewCategoryName] = useState("");
  const [editingCategory, setEditingCategory] = useState(null);

  const loadAll = () => {
    client.get("/articles?page=0&size=100").then((res) => setArticles(res.data));
    client.get("/categories").then((res) => setCategories(res.data));
  };

  useEffect(loadAll, []);

  const flash = (msg) => {
    setMessage(msg);
    setError(null);
    setTimeout(() => setMessage(null), 3000);
  };

  const failWith = (msg) => {
    setError(msg);
    setMessage(null);
  };

  const startNewArticle = () => {
    setEditingArticle("new");
    setArticleForm({ titre: "", resume: "", contenu: "", categorieId: categories[0]?.id ?? "" });
  };

  const startEditArticle = (a) => {
    const cat = categories.find((c) => c.nom === a.categorie);
    setEditingArticle(a.id);
    setArticleForm({ titre: a.titre, resume: a.resume, contenu: a.contenu, categorieId: cat?.id ?? "" });
  };

  const saveArticle = async (e) => {
    e.preventDefault();
    try {
      const payload = { ...articleForm, categorieId: Number(articleForm.categorieId) };
      if (editingArticle === "new") {
        await client.post("/editeur/articles", payload);
        flash("Article publie avec succes.");
      } else {
        await client.put(`/editeur/articles/${editingArticle}`, payload);
        flash("Article mis a jour.");
      }
      setEditingArticle(null);
      loadAll();
    } catch {
      failWith("Impossible d'enregistrer l'article. Verifiez les champs.");
    }
  };

  const deleteArticle = async (id) => {
    if (!window.confirm("Supprimer definitivement cet article ?")) return;
    try {
      await client.delete(`/editeur/articles/${id}`);
      flash("Article supprime.");
      loadAll();
    } catch {
      failWith("Suppression impossible.");
    }
  };

  const saveCategory = async (e) => {
    e.preventDefault();
    try {
      if (editingCategory) {
        await client.put(`/editeur/categories/${editingCategory.id}`, { id: editingCategory.id, nom: newCategoryName });
        flash("Categorie mise a jour.");
      } else {
        await client.post("/editeur/categories", { nom: newCategoryName });
        flash("Categorie creee.");
      }
      setNewCategoryName("");
      setEditingCategory(null);
      loadAll();
    } catch {
      failWith("Impossible d'enregistrer la categorie (nom peut-etre deja utilise).");
    }
  };

  const deleteCategory = async (id) => {
    if (!window.confirm("Supprimer cette categorie ? Les articles associes doivent etre reassignes au prealable.")) return;
    try {
      await client.delete(`/editeur/categories/${id}`);
      flash("Categorie supprimee.");
      loadAll();
    } catch {
      failWith("Suppression impossible : des articles utilisent probablement cette categorie.");
    }
  };

  return (
    <div className="page">
      <div className="dashboard-header">
        <h1 className="dashboard-title">Espace editeur</h1>
        {tab === "articles" && !editingArticle && (
          <button className="btn btn--primary" onClick={startNewArticle}>+ Nouvel article</button>
        )}
      </div>

      <div className="tabs">
        <button className={tab === "articles" ? "active" : ""} onClick={() => { setTab("articles"); setEditingArticle(null); }}>Articles</button>
        <button className={tab === "categories" ? "active" : ""} onClick={() => setTab("categories")}>Categories</button>
      </div>

      {message && <div className="success-banner">{message}</div>}
      {error && <div className="error-banner">{error}</div>}

      {tab === "articles" && (
        editingArticle ? (
          <div className="card">
            <h2 className="form-title">{editingArticle === "new" ? "Nouvel article" : "Modifier l'article"}</h2>
            <form onSubmit={saveArticle}>
              <div className="field">
                <label>Titre</label>
                <input value={articleForm.titre} onChange={(e) => setArticleForm({ ...articleForm, titre: e.target.value })} required />
              </div>
              <div className="field">
                <label>Resume</label>
                <input value={articleForm.resume} onChange={(e) => setArticleForm({ ...articleForm, resume: e.target.value })} required />
              </div>
              <div className="field">
                <label>Categorie</label>
                <select value={articleForm.categorieId} onChange={(e) => setArticleForm({ ...articleForm, categorieId: e.target.value })} required>
                  <option value="" disabled>Choisir…</option>
                  {categories.map((c) => <option key={c.id} value={c.id}>{c.nom}</option>)}
                </select>
              </div>
              <div className="field">
                <label>Contenu</label>
                <textarea value={articleForm.contenu} onChange={(e) => setArticleForm({ ...articleForm, contenu: e.target.value })} required />
              </div>
              <div className="row-actions">
                <button className="btn btn--primary" type="submit">Enregistrer</button>
                <button className="btn btn--ghost" type="button" onClick={() => setEditingArticle(null)}>Annuler</button>
              </div>
            </form>
          </div>
        ) : (
          <table>
            <thead>
              <tr><th>Titre</th><th>Categorie</th><th>Date</th><th></th></tr>
            </thead>
            <tbody>
              {articles.map((a) => (
                <tr key={a.id}>
                  <td>{a.titre}</td>
                  <td><span className="tag-pill">{a.categorie}</span></td>
                  <td>{new Date(a.datePublication).toLocaleDateString("fr-FR")}</td>
                  <td className="row-actions">
                    <button className="btn btn--ghost btn--sm" onClick={() => startEditArticle(a)}>Modifier</button>
                    <button className="btn btn--danger btn--sm" onClick={() => deleteArticle(a.id)}>Supprimer</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )
      )}

      {tab === "categories" && (
        <>
          <div className="card card--tight" style={{ marginBottom: 24, maxWidth: 480 }}>
            <form onSubmit={saveCategory} style={{ display: "flex", gap: 10, alignItems: "flex-end" }}>
              <div className="field" style={{ marginBottom: 0, flex: 1 }}>
                <label>{editingCategory ? "Renommer la categorie" : "Nouvelle categorie"}</label>
                <input value={newCategoryName} onChange={(e) => setNewCategoryName(e.target.value)} required />
              </div>
              <button className="btn btn--primary" type="submit">{editingCategory ? "Renommer" : "Creer"}</button>
              {editingCategory && (
                <button className="btn btn--ghost" type="button" onClick={() => { setEditingCategory(null); setNewCategoryName(""); }}>Annuler</button>
              )}
            </form>
          </div>
          <table>
            <thead><tr><th>Nom</th><th></th></tr></thead>
            <tbody>
              {categories.map((c) => (
                <tr key={c.id}>
                  <td>{c.nom}</td>
                  <td className="row-actions">
                    <button className="btn btn--ghost btn--sm" onClick={() => { setEditingCategory(c); setNewCategoryName(c.nom); }}>Renommer</button>
                    <button className="btn btn--danger btn--sm" onClick={() => deleteCategory(c.id)}>Supprimer</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
}
