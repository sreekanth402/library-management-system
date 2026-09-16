import type { Book } from "../types";

export default function BookCard({ book }: { book: Book }) {
  return (
    <article className="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
      <h3 className="font-semibold text-slate-900">{book.title}</h3>
      <p className="text-sm text-slate-500">{book.author}</p>
      <p className="mt-2 text-xs uppercase tracking-wide text-indigo-600">{book.category}</p>
      <p className="mt-3 text-sm">
        Available: <span className="font-medium">{book.availableQuantity}</span> / {book.totalQuantity}
      </p>
    </article>
  );
}
