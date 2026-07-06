import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import client from "../api/client";

export default function ArticleDetail() {
  const { id } = useParams();
  const [article, setArticle] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    client.get(`/articles/${id}`)
      .then((res) => setArticle(res.data))
      .catch(() => setError("Cet article est introuvable."));
  }, [id]);

  if (error) return <div className="page page--narrow"><div className="error-banner">{error}</div></div>;
  if (!article) return <div className="page loading-state">Chargement…</div>;

  return (
    <div className="page page--narrow">
      <Link to="/" className="back-link">&larr; Retour aux actualites</Link>
      <span className="article-detail__tag">{article.categorie}</span>
      <h1 className="article-detail__title">{article.titre}</h1>
      <div className="article-detail__date">
        Publie le {new Date(article.datePublication).toLocaleDateString("fr-FR", { day: "2-digit", month: "long", year: "numeric" })}
      </div>
      <div className="article-detail__body">{article.contenu}</div>
    </div>
  );
}
