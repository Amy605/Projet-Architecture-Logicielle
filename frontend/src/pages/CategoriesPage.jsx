import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import client from "../api/client";

export default function CategoriesPage() {
  const { id } = useParams();
  const [categories, setCategories] = useState([]);
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    client.get("/categories").then((res) => setCategories(res.data));
  }, []);

  useEffect(() => {
    setLoading(true);
    const request = id
      ? client.get(`/articles/categorie/${id}`)
      : client.get(`/articles?page=0&size=50`);
    request.then((res) => setArticles(res.data)).finally(() => setLoading(false));
  }, [id]);

  return (
    <div className="page">
      <div className="category-chips">
        <Link to="/categories" className={`category-chip ${!id ? "active" : ""}`}>Toutes</Link>
        {categories.map((c) => (
          <Link key={c.id} to={`/categories/${c.id}`} className={`category-chip ${String(id) === String(c.id) ? "active" : ""}`}>
            {c.nom}
          </Link>
        ))}
      </div>

      {loading ? (
        <div className="loading-state">Chargement…</div>
      ) : articles.length === 0 ? (
        <div className="empty-state">Aucun article dans cette categorie.</div>
      ) : (
        <div className="article-list">
          {articles.map((a) => (
            <div className="article-row" key={a.id}>
              <span className="article-row__tag">{a.categorie}</span>
              <Link to={`/articles/${a.id}`}>
                <h2 className="article-row__title">{a.titre}</h2>
                <p className="article-row__summary">{a.resume}</p>
                <span className="article-row__date">
                  {new Date(a.datePublication).toLocaleDateString("fr-FR", { day: "2-digit", month: "long", year: "numeric" })}
                </span>
              </Link>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
