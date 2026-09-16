export type Role = "ADMIN" | "LIBRARIAN" | "MEMBER";
export type MemberStatus = "ACTIVE" | "INACTIVE" | "SUSPENDED";
export type BorrowStatus = "BORROWED" | "RETURNED";

export interface AuthUser {
  token: string;
  username: string;
  role: Role;
  memberId?: number | null;
}

export interface Book {
  id: number;
  title: string;
  author: string;
  isbn: string;
  category: string;
  publisher?: string;
  publicationYear?: number;
  totalQuantity: number;
  availableQuantity: number;
}

export interface Member {
  id: number;
  name: string;
  email: string;
  phone?: string;
  address?: string;
  membershipDate: string;
  status: MemberStatus;
}

export interface BorrowRecord {
  id: number;
  bookId: number;
  bookTitle: string;
  memberId: number;
  memberName: string;
  borrowDate: string;
  dueDate: string;
  returnDate?: string | null;
  status: BorrowStatus;
  overdue: boolean;
  fine: number;
}

export interface DashboardStats {
  totalBooks: number;
  totalMembers: number;
  activeBorrows: number;
  totalFines: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
