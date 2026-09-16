import api from "./api";
import type { Member } from "../types";

export async function fetchMembers(): Promise<Member[]> {
  const { data } = await api.get("/members");
  return data;
}

export async function createMember(payload: {
  name: string;
  email: string;
  phone?: string;
  address?: string;
}): Promise<Member> {
  const { data } = await api.post("/members", payload);
  return data;
}
