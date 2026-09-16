import { useForm } from "react-hook-form";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createMember, fetchMembers } from "../services/memberService";
import Loading from "../components/Loading";

interface MemberForm {
  name: string;
  email: string;
  phone?: string;
  address?: string;
}

export default function Members() {
  const queryClient = useQueryClient();
  const { data, isLoading } = useQuery({ queryKey: ["members"], queryFn: fetchMembers });
  const form = useForm<MemberForm>();
  const mutation = useMutation({
    mutationFn: createMember,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["members"] });
      queryClient.invalidateQueries({ queryKey: ["stats"] });
      form.reset();
    }
  });

  if (isLoading || !data) return <Loading />;

  return (
    <div>
      <h1 className="text-2xl font-bold">Members</h1>
      <form className="mt-6 grid gap-3 rounded-xl bg-white p-4 shadow-sm md:grid-cols-2" onSubmit={form.handleSubmit((v) => mutation.mutate(v))}>
        <input className="rounded-lg border px-3 py-2" placeholder="Name" {...form.register("name", { required: true })} />
        <input className="rounded-lg border px-3 py-2" placeholder="Email" {...form.register("email", { required: true })} />
        <input className="rounded-lg border px-3 py-2" placeholder="Phone" {...form.register("phone")} />
        <input className="rounded-lg border px-3 py-2" placeholder="Address" {...form.register("address")} />
        <button className="rounded-lg bg-indigo-600 px-4 py-2 text-white">Register member</button>
      </form>
      <div className="mt-6 overflow-hidden rounded-xl bg-white shadow-sm">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-50 text-slate-500">
            <tr>
              <th className="px-4 py-3">Name</th>
              <th>Email</th>
              <th>Status</th>
              <th>Joined</th>
            </tr>
          </thead>
          <tbody>
            {data.map((member) => (
              <tr key={member.id} className="border-t">
                <td className="px-4 py-3 font-medium">{member.name}</td>
                <td>{member.email}</td>
                <td>{member.status}</td>
                <td>{member.membershipDate}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
