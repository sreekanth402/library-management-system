import { Navigate, Route, Routes } from "react-router-dom";
import AppLayout from "./components/AppLayout";
import StaffRoute from "./components/StaffRoute";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Books from "./pages/Books";
import Members from "./pages/Members";
import BorrowBooks from "./pages/BorrowBooks";
import Returns from "./pages/Returns";
import Transactions from "./pages/Transactions";
import { useAuth } from "./hooks/useAuth";

export default function App() {
  const { user } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={user ? <Navigate to="/" replace /> : <Login />} />
      <Route element={<AppLayout />}>
        <Route path="/" element={<Dashboard />} />
        <Route path="/books" element={<Books />} />
        <Route path="/transactions" element={<Transactions />} />
        <Route element={<StaffRoute />}>
          <Route path="/members" element={<Members />} />
          <Route path="/borrow" element={<BorrowBooks />} />
          <Route path="/returns" element={<Returns />} />
        </Route>
      </Route>
    </Routes>
  );
}
