import api from "./api";
import type { BorrowRecord, DashboardStats, PageResponse } from "../types";

export async function fetchStats(): Promise<DashboardStats> {
  const { data } = await api.get("/dashboard/stats");
  return data;
}

export async function borrowBook(bookId: number, memberId: number): Promise<BorrowRecord> {
  const { data } = await api.post("/borrow", { bookId, memberId });
  return data;
}

export async function returnBook(borrowId: number): Promise<BorrowRecord> {
  const { data } = await api.post(`/return/${borrowId}`);
  return data;
}

export async function fetchActive(): Promise<PageResponse<BorrowRecord>> {
  const { data } = await api.get("/borrow/active", { params: { size: 20 } });
  return data;
}

export async function fetchHistory(): Promise<PageResponse<BorrowRecord>> {
  const { data } = await api.get("/borrow/history", { params: { size: 20 } });
  return data;
}

export async function fetchMine(): Promise<BorrowRecord[]> {
  const { data } = await api.get("/borrow/me");
  return data;
}
