export default function Pagination({ page, hasNext, onPrevious, onNext }) {
  return (
    <div className="pagination">
      <button onClick={onPrevious} disabled={page === 0}>&larr; Precedent</button>
      <span className="pagination__page">Page {page + 1}</span>
      <button onClick={onNext} disabled={!hasNext}>Suivant &rarr;</button>
    </div>
  );
}
