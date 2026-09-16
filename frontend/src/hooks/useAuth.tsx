import { createContext, useContext, useMemo, useState, type ReactNode } from "react";
import type { AuthUser } from "../types";

interface AuthContextValue {
  user: AuthUser | null;
  loginUser: (user: AuthUser) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    const raw = localStorage.getItem("lms-auth");
    return raw ? (JSON.parse(raw) as AuthUser) : null;
  });

  const value = useMemo(
    () => ({
      user,
      loginUser: (next: AuthUser) => {
        localStorage.setItem("lms-auth", JSON.stringify(next));
        setUser(next);
      },
      logout: () => {
        localStorage.removeItem("lms-auth");
        setUser(null);
      }
    }),
    [user]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return ctx;
}
