import { Link, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { auth, logout } = useAuth();
  const location = useLocation();
  const isActive = (path) => location.pathname === path;

  return (
    <header className="masthead">
      <div className="masthead__top">
        <div className="masthead__wordmark">Le <span>Phare</span></div>
        <div className="masthead__meta">
          Edition numerique<br />
          {new Date().toLocaleDateString("fr-FR", { weekday: "long", year: "numeric", month: "long", day: "numeric" })}
        </div>
      </div>
      <nav className="masthead__nav">
        <Link to="/" className={isActive("/") ? "active" : ""}>Accueil</Link>
        <Link to="/categories" className={isActive("/categories") ? "active" : ""}>Categories</Link>
        {auth && (auth.role === "EDITEUR" || auth.role === "ADMIN") && (
          <Link to="/editeur" className={isActive("/editeur") ? "active" : ""}>Espace editeur</Link>
        )}
        {auth && auth.role === "ADMIN" && (
          <Link to="/admin" className={isActive("/admin") ? "active" : ""}>Administration</Link>
        )}
        <div className="masthead__spacer" />
        <div className="masthead__auth">
          {auth ? (
            <>
              <span className="role-badge">{auth.role}</span>
              <span>{auth.username}</span>
              <button className="btn btn--ghost btn--sm" onClick={logout}>Deconnexion</button>
            </>
          ) : (
            <Link to="/login" className="btn btn--primary btn--sm">Connexion</Link>
          )}
        </div>
      </nav>
    </header>
  );
}
