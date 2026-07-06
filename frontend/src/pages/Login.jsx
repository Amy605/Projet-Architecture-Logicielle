import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    try {
      const data = await login(username, password);
      navigate(data.role === "ADMIN" ? "/admin" : "/editeur");
    } catch {
      setError("Identifiants incorrects. Verifiez votre nom d'utilisateur et mot de passe.");
    }
  };

  return (
    <div className="page page--narrow">
      <div className="card" style={{ maxWidth: 420, margin: "40px auto" }}>
        <h1 className="form-title">Connexion</h1>
        <p className="form-subtitle">Reserve aux editeurs et administrateurs du journal.</p>
        {error && <div className="error-banner">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label>Nom d'utilisateur</label>
            <input value={username} onChange={(e) => setUsername(e.target.value)} required autoFocus />
          </div>
          <div className="field">
            <label>Mot de passe</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
          </div>
          <button className="btn btn--primary" type="submit" style={{ width: "100%" }}>Se connecter</button>
        </form>
      
      </div>
    </div>
  );
}
