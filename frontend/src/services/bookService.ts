import api from "./api";
import type { Book, PageResponse } from "../types";

export async function fetchBooks(params: {
  page?: number;
  title?: string;
  author?: string;
}): Promise<PageResponse<Book>> {
  const { data } = await api.get("/books", { params: { size: 10, ...params } });
  return data;
}

export async function createBook(payload: Omit<Book, "id" | "availableQuantity">) {
  const { data } = await api.post("/books", payload);
  return data as Book;
}

export async function updateBook(id: number, payload: Omit<Book, "id" | "availableQuantity">) {
  const { data } = await api.put(`/books/${id}`, payload);
  return data as Book;
}

export async function deleteBook(id: number) {
  await api.delete(`/books/${id}`);
}
