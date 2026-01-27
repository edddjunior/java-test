export interface ApiResponse<T> {
  data: T;
  timestamp: string;
}

export interface ApiError {
  code: string;
  message: string;
  path: string;
  timestamp: string;
}

export interface PagedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}
