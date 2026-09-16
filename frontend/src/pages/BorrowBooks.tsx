import { useForm } from "react-hook-form";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchBooks } from "../services/bookService";
import { fetchMembers } from "../services/memberService";
import { borrowBook } from "../services/borrowService";
import Loading from "../components/Loading";

interface BorrowForm {
  bookId: number;
  memberId: number;
}

export default function BorrowBooks() {
  const queryClient = useQueryClient();
  const form = useForm<BorrowForm>();
  const books = useQuery({ queryKey: ["books", "all"], queryFn: () => fetchBooks({ page: 0 }) });
  const members = useQuery({ queryKey: ["members"], queryFn: fetchMembers });
  const mutation = useMutation({
    mutationFn: (values: BorrowForm) => borrowBook(Number(values.bookId), Number(values.memberId)),
    onSuccess: () => {
      queryClient.invalidateQueries();
      form.reset();
    }
  });

  if (books.isLoading || members.isLoading || !books.data || !members.data) return <Loading />;

  return (
    <div>
      <h1 className="text-2xl font-bold">Issue a book</h1>
      <form
        className="mt-6 max-w-lg space-y-4 rounded-xl bg-white p-6 shadow-sm"
        onSubmit={form.handleSubmit((values) => mutation.mutate(values))}
      >
        <select className="w-full rounded-lg border px-3 py-2" {...form.register("bookId", { required: true, valueAsNumber: true })}>
          <option value="">Select book</option>
          {books.data.content.map((book) => (
            <option key={book.id} value={book.id}>
              {book.title} ({book.availableQuantity} available)
            </option>
          ))}
        </select>
        <select className="w-full rounded-lg border px-3 py-2" {...form.register("memberId", { required: true, valueAsNumber: true })}>
          <option value="">Select member</option>
          {members.data.map((member) => (
            <option key={member.id} value={member.id}>
              {member.name}
            </option>
          ))}
        </select>
        {mutation.isError && <p className="text-sm text-red-600">Could not issue the book. Check availability and member status.</p>}
        {mutation.isSuccess && <p className="text-sm text-emerald-600">Book issued successfully.</p>}
        <button className="rounded-lg bg-indigo-600 px-4 py-2 text-white">Issue book</button>
      </form>
    </div>
  );
}
