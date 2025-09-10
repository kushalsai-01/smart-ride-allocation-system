// API Response Types
export interface ApiResponse<T = any> {
  success: boolean;
  message: string;
  data?: T;
  error?: string;
  timestamp: string;
}

// User Types
export interface User {
  id: number;
  username: string;
  email: string;
  fullName?: string;
  enabled: boolean;
  createdAt: string;
  updatedAt: string;
  lastLoginAt?: string;
  vaultItemCount: number;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  fullName?: string;
}

export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
  rememberMe?: boolean;
}

// Vault Item Types
export enum VaultItemType {
  PASSWORD = 'PASSWORD',
  NOTE = 'NOTE',
  CREDIT_CARD = 'CREDIT_CARD',
  IDENTITY = 'IDENTITY',
  SECURE_NOTE = 'SECURE_NOTE',
}

export interface VaultItem {
  id: number;
  title: string;
  type: VaultItemType;
  username?: string;
  password?: string;
  email?: string;
  url?: string;
  notes?: string;
  description?: string;
  isFavorite: boolean;
  category?: string;
  tags?: string;
  createdAt: string;
  updatedAt: string;
  lastAccessedAt?: string;
}

export interface VaultItemRequest {
  title: string;
  type: VaultItemType;
  username?: string;
  password?: string;
  email?: string;
  url?: string;
  notes?: string;
  description?: string;
  isFavorite?: boolean;
  category?: string;
  tags?: string;
}

export interface VaultItemStats {
  totalItems: number;
  passwordItems: number;
  noteItems: number;
  favoriteItems: number;
}

// UI Types
export interface Theme {
  mode: 'light' | 'dark';
  toggle: () => void;
}

export interface Toast {
  id: string;
  type: 'success' | 'error' | 'info' | 'warning';
  title: string;
  message?: string;
  duration?: number;
}

// Form Types
export interface FormField {
  name: string;
  label: string;
  type: 'text' | 'email' | 'password' | 'textarea' | 'select' | 'checkbox';
  placeholder?: string;
  required?: boolean;
  validation?: any;
  options?: { value: string; label: string }[];
}

// Search and Filter Types
export interface SearchFilters {
  query?: string;
  type?: VaultItemType;
  category?: string;
  isFavorite?: boolean;
  sortBy?: 'title' | 'createdAt' | 'updatedAt' | 'lastAccessedAt';
  sortOrder?: 'asc' | 'desc';
}

// Pagination Types
export interface PaginationParams {
  page: number;
  size: number;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
}

// Error Types
export interface ApiError {
  message: string;
  status?: number;
  code?: string;
  details?: Record<string, any>;
}

// Auth Context Types
export interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => Promise<void>;
  refreshUser: () => Promise<void>;
}

// Vault Context Types
export interface VaultContextType {
  items: VaultItem[];
  stats: VaultItemStats | null;
  categories: string[];
  isLoading: boolean;
  searchFilters: SearchFilters;
  setSearchFilters: (filters: SearchFilters) => void;
  createItem: (item: VaultItemRequest) => Promise<VaultItem>;
  updateItem: (id: number, item: VaultItemRequest) => Promise<VaultItem>;
  deleteItem: (id: number) => Promise<void>;
  toggleFavorite: (id: number) => Promise<VaultItem>;
  refreshItems: () => Promise<void>;
  getItemById: (id: number) => VaultItem | undefined;
}

// Component Props Types
export interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  children: React.ReactNode;
  size?: 'sm' | 'md' | 'lg' | 'xl';
}

export interface ButtonProps {
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  loading?: boolean;
  disabled?: boolean;
  children: React.ReactNode;
  onClick?: () => void;
  type?: 'button' | 'submit' | 'reset';
  className?: string;
}

export interface InputProps {
  label?: string;
  error?: string;
  helperText?: string;
  required?: boolean;
  className?: string;
}

// Utility Types
export type Optional<T, K extends keyof T> = Omit<T, K> & Partial<Pick<T, K>>;
export type RequiredFields<T, K extends keyof T> = T & Required<Pick<T, K>>;

// Constants
export const VAULT_ITEM_TYPE_LABELS: Record<VaultItemType, string> = {
  [VaultItemType.PASSWORD]: 'Password',
  [VaultItemType.NOTE]: 'Secure Note',
  [VaultItemType.CREDIT_CARD]: 'Credit Card',
  [VaultItemType.IDENTITY]: 'Identity',
  [VaultItemType.SECURE_NOTE]: 'Secure Note',
};

export const VAULT_ITEM_TYPE_ICONS: Record<VaultItemType, string> = {
  [VaultItemType.PASSWORD]: 'key',
  [VaultItemType.NOTE]: 'file-text',
  [VaultItemType.CREDIT_CARD]: 'credit-card',
  [VaultItemType.IDENTITY]: 'user',
  [VaultItemType.SECURE_NOTE]: 'lock',
};