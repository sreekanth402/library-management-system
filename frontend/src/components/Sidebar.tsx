import { NavLink } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

const item =
  "block rounded-lg px-3 py-2 text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white";
const active = "bg-indigo-600 text-white hover:bg-indigo-500";

export default function Sidebar() {
  const { user } = useAuth();
  const staff = user?.role === "ADMIN" || user?.role === "LIBRARIAN";

  return (
    <aside className="flex h-full w-60 flex-col bg-slate-950 px-4 py-6 text-white">
      <div className="mb-8 px-2">
        <p className="text-xs uppercase tracking-widest text-indigo-300">Library</p>
        <h1 className="text-lg font-semibold">Management System</h1>
      </div>
      <nav className="space-y-1">
        <NavLink to="/" end className={({ isActive }) => `${item} ${isActive ? active : ""}`}>
          Dashboard
        </NavLink>
        <NavLink to="/books" className={({ isActive }) => `${item} ${isActive ? active : ""}`}>
          Books
        </NavLink>
        {staff && (
          <NavLink to="/members" className={({ isActive }) => `${item} ${isActive ? active : ""}`}>
            Members
          </NavLink>
        )}
        {staff && (
          <NavLink to="/borrow" className={({ isActive }) => `${item} ${isActive ? active : ""}`}>
            Borrow
          </NavLink>
        )}
        {staff && (
          <NavLink to="/returns" className={({ isActive }) => `${item} ${isActive ? active : ""}`}>
            Returns
          </NavLink>
        )}
        <NavLink to="/transactions" className={({ isActive }) => `${item} ${isActive ? active : ""}`}>
          Transactions
        </NavLink>
      </nav>
    </aside>
  );
}
