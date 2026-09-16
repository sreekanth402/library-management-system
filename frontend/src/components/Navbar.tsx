import { useAuth } from "../hooks/useAuth";

export default function Navbar() {
  const { user, logout } = useAuth();
  return (
    <header className="flex items-center justify-between border-b border-slate-200 bg-white px-6 py-4">
      <h2 className="text-lg font-semibold text-slate-800">Library Administration</h2>
      <div className="flex items-center gap-4">
        <span className="rounded-full bg-slate-100 px-3 py-1 text-sm text-slate-700">
          {user?.username} · {user?.role}
        </span>
        <button
          onClick={logout}
          className="rounded-lg bg-slate-900 px-3 py-1.5 text-sm text-white hover:bg-slate-700"
        >
          Logout
        </button>
      </div>
    </header>
  );
}
