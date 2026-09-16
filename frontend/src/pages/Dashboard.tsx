import { useQuery } from "@tanstack/react-query";
import { fetchStats } from "../services/borrowService";
import Loading from "../components/Loading";

export default function Dashboard() {
  const { data, isLoading } = useQuery({ queryKey: ["stats"], queryFn: fetchStats });
  if (isLoading || !data) return <Loading />;

  const cards = [
    { label: "Books", value: data.totalBooks },
    { label: "Members", value: data.totalMembers },
    { label: "Borrowed", value: data.activeBorrows },
    { label: "Fine", value: `₹${data.totalFines}` }
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold">Dashboard</h1>
      <p className="mt-1 text-slate-500">Overview of library activity</p>
      <div className="mt-6 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {cards.map((card) => (
          <div key={card.label} className="rounded-2xl bg-white p-6 shadow-sm">
            <p className="text-sm text-slate-500">{card.label}</p>
            <p className="mt-2 text-3xl font-semibold">{card.value}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
