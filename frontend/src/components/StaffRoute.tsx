import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function StaffRoute() {
  const { user } = useAuth();
  if (user?.role !== "ADMIN" && user?.role !== "LIBRARIAN") {
    return <Navigate to="/" replace />;
  }
  return <Outlet />;
}
