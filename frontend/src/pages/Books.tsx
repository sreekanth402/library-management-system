import { useState } from "react";
import { useForm } from "react-hook-form";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createBook, deleteBook, fetchBooks } from "../services/bookService";
import BookCard from "../components/BookCard";
import Loading from "../components/Loading";
import { useAuth } from "../hooks/useAuth";

interface BookForm {
  title: string;
  author: string;
  isbn: string;
  category: string;
  publisher?: string;
  publicationYear?: number;
  totalQuantity: number;
}

export default function Books() {
  const { user } = useAuth();
  const staff = user?.role === "ADMIN" || user?.role === "LIBRARIAN";
  const [title, setTitle] = useState("");
  const queryClient = useQueryClient();
  const { data, isLoading } = useQuery({
    queryKey: ["books", title],
    queryFn: () => fetchBooks({ title })
  });
  const form = useForm<BookForm>();
  const createMutation = useMutation({
    mutationFn: createBook,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["books"] });
      queryClient.invalidateQueries({ queryKey: ["stats"] });
      form.reset();
    }
  });
  const deleteMutation = useMutation({
    mutationFn: deleteBook,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["books"] });
      queryClient.invalidateQueries({ queryKey: ["stats"] });
    }
  });

  return (
    <div>
      <h1 className="text-2xl font-bold">Books</h1>
      <input
        className="mt-4 w-full max-w-md rounded-lg border px-3 py-2"
        placeholder="Search by title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      {staff && (
        <form
          className="mt-6 grid gap-3 rounded-xl bg-white p-4 shadow-sm md:grid-cols-3"
          onSubmit={form.handleSubmit((values) => createMutation.mutate(values))}
        >
          <input className="rounded-lg border px-3 py-2" placeholder="Title" {...form.register("title", { required: true })} />
          <input className="rounded-lg border px-3 py-2" placeholder="Author" {...form.register("author", { required: true })} />
          <input className="rounded-lg border px-3 py-2" placeholder="ISBN" {...form.register("isbn", { required: true })} />
          <input className="rounded-lg border px-3 py-2" placeholder="Category" {...form.register("category", { required: true })} />
          <input className="rounded-lg border px-3 py-2" placeholder="Publisher" {...form.register("publisher")} />
          <input className="rounded-lg border px-3 py-2" type="number" placeholder="Quantity" {...form.register("totalQuantity", { valueAsNumber: true, required: true })} />
          <button className="rounded-lg bg-indigo-600 px-4 py-2 text-white">Add book</button>
        </form>
      )}
      {isLoading || !data ? (
        <Loading />
      ) : (
        <div className="mt-6 grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {data.content.map((book) => (
            <div key={book.id} className="relative">
              <BookCard book={book} />
              {user?.role === "ADMIN" && (
                <button
                  className="absolute right-3 top-3 text-xs text-red-600"
                  onClick={() => deleteMutation.mutate(book.id)}
                >
                  Delete
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
