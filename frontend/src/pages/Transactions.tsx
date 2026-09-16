import { useQuery } from "@tanstack/react-query";
import { fetchHistory, fetchMine } from "../services/borrowService";
import Loading from "../components/Loading";
import { useAuth } from "../hooks/useAuth";

export default function Transactions() {
  const { user } = useAuth();
  const staff = user?.role === "ADMIN" || user?.role === "LIBRARIAN";
  const history = useQuery({
    queryKey: ["borrow-history"],
    queryFn: fetchHistory,
    enabled: staff
  });
  const mine = useQuery({
    queryKey: ["borrow-mine"],
    queryFn: fetchMine,
    enabled: !staff
  });

  const rows = staff ? history.data?.content : mine.data;
  const loading = staff ? history.isLoading : mine.isLoading;
  if (loading || !rows) return <Loading />;

  return (
    <div>
      <h1 className="text-2xl font-bold">Transactions</h1>
      <div className="mt-6 overflow-hidden rounded-xl bg-white shadow-sm">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-50 text-slate-500">
            <tr>
              <th className="px-4 py-3">Book</th>
              <th>Member</th>
              <th>Borrowed</th>
              <th>Due</th>
              <th>Returned</th>
              <th>Fine</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((record) => (
              <tr key={record.id} className="border-t">
                <td className="px-4 py-3">{record.bookTitle}</td>
                <td>{record.memberName}</td>
                <td>{record.borrowDate}</td>
                <td>{record.dueDate}</td>
                <td>{record.returnDate ?? "-"}</td>
                <td>₹{record.fine}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
