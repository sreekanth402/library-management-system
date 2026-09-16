import api from "./api";
import type { AuthUser, Role } from "../types";

export async function login(username: string, password: string): Promise<AuthUser> {
  const { data } = await api.post("/auth/login", { username, password });
  return {
    token: data.token,
    username: data.username,
    role: data.role as Role,
    memberId: data.memberId
  };
}

export async function register(payload: {
  username: string;
  email: string;
  password: string;
  name: string;
  phone?: string;
  address?: string;
}): Promise<AuthUser> {
  const { data } = await api.post("/auth/register", payload);
  return {
    token: data.token,
    username: data.username,
    role: data.role as Role,
    memberId: data.memberId
  };
}
