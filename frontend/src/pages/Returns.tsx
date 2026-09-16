import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { fetchActive, returnBook } from "../services/borrowService";
import Loading from "../components/Loading";

export default function Returns() {
  const queryClient = useQueryClient();
  const { data, isLoading } = useQuery({ queryKey: ["borrow-active"], queryFn: fetchActive });
  const mutation = useMutation({
    mutationFn: returnBook,
    onSuccess: () => queryClient.invalidateQueries()
  });

  if (isLoading || !data) return <Loading />;

  return (
    <div>
      <h1 className="text-2xl font-bold">Returns</h1>
      <div className="mt-6 overflow-hidden rounded-xl bg-white shadow-sm">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-50 text-slate-500">
            <tr>
              <th className="px-4 py-3">Book</th>
              <th>Member</th>
              <th>Due</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {data.content.map((record) => (
              <tr key={record.id} className="border-t">
                <td className="px-4 py-3">{record.bookTitle}</td>
                <td>{record.memberName}</td>
                <td>{record.dueDate}</td>
                <td>{record.overdue ? "OVERDUE" : record.status}</td>
                <td>
                  <button className="text-indigo-600" onClick={() => mutation.mutate(record.id)}>
                    Accept return
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
