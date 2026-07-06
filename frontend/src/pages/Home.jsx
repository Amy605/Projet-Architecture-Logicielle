import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import client from "../api/client";
import Pagination from "../components/Pagination";

const PAGE_SIZE = 5;

export default function Home() {
  const [articles, setArticles] = useState([]);
  const [page, setPage] = useState(0);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    Promise.all([
      client.get(`/articles?page=${page}&size=${PAGE_SIZE}`),
      client.get(`/articles/count`),
    ])
      .then(([articlesRes, countRes]) => {
        setArticles(articlesRes.data);
        setTotal(countRes.data.total);
      })
      .finally(() => setLoading(false));
  }, [page]);

  const hasNext = (page + 1) * PAGE_SIZE < total;

  return (
    <div className="page">
      {loading ? (
        <div className="loading-state">Chargement des dernieres actualites…</div>
      ) : articles.length === 0 ? (
        <div className="empty-state">Aucun article publie pour le moment.</div>
      ) : (
        <>
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
          <Pagination
            page={page}
            hasNext={hasNext}
            onPrevious={() => setPage((p) => Math.max(0, p - 1))}
            onNext={() => setPage((p) => p + 1)}
          />
        </>
      )}
    </div>
  );
}
