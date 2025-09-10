import axios, { AxiosResponse, AxiosError } from 'axios';
import { toast } from 'react-hot-toast';
import type {
  ApiResponse,
  User,
  LoginRequest,
  RegisterRequest,
  VaultItem,
  VaultItemRequest,
  VaultItemStats,
  PaginatedResponse,
  PaginationParams,
} from '@/types';

// Create axios instance with default config
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for debugging and auth
api.interceptors.request.use(
  (config) => {
    // Add timestamp to prevent caching issues
    if (config.method === 'get') {
      config.params = { ...config.params, _t: Date.now() };
    }
    
    console.log(`🚀 ${config.method?.toUpperCase()} ${config.url}`, config.data || config.params);
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    console.log(`✅ ${response.config.method?.toUpperCase()} ${response.config.url}`, response.data);
    return response;
  },
  (error: AxiosError<ApiResponse>) => {
    console.error(`❌ ${error.config?.method?.toUpperCase()} ${error.config?.url}`, error.response?.data || error.message);
    
    // Handle specific error cases
    if (error.response?.status === 401) {
      // Unauthorized - redirect to login if not already there
      if (!window.location.pathname.includes('/login')) {
        window.location.href = '/login';
      }
    } else if (error.response?.status === 403) {
      // Forbidden
      toast.error('Access denied');
    } else if (error.response?.status >= 500) {
      // Server errors
      toast.error('Server error. Please try again later.');
    } else if (error.code === 'NETWORK_ERROR') {
      // Network errors
      toast.error('Network error. Please check your connection.');
    }
    
    return Promise.reject(error);
  }
);

// Helper function to handle API responses
const handleResponse = <T>(response: AxiosResponse<ApiResponse<T>>): T => {
  if (response.data.success) {
    return response.data.data as T;
  } else {
    throw new Error(response.data.error || response.data.message || 'API request failed');
  }
};

// Auth API
export const authApi = {
  // Register new user
  register: async (data: RegisterRequest): Promise<User> => {
    const response = await api.post<ApiResponse<User>>('/auth/register', data);
    return handleResponse(response);
  },

  // Login user
  login: async (credentials: LoginRequest): Promise<User> => {
    // Use form data for Spring Security
    const formData = new FormData();
    formData.append('usernameOrEmail', credentials.usernameOrEmail);
    formData.append('password', credentials.password);
    
    const response = await api.post<ApiResponse<User>>('/auth/login', formData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    return handleResponse(response);
  },

  // Logout user
  logout: async (): Promise<void> => {
    await api.post<ApiResponse<void>>('/auth/logout');
  },

  // Get current user
  me: async (): Promise<User> => {
    const response = await api.get<ApiResponse<User>>('/auth/me');
    return handleResponse(response);
  },

  // Check username availability
  checkUsername: async (username: string): Promise<boolean> => {
    const response = await api.get<ApiResponse<{ available: boolean }>>('/auth/check-username', {
      params: { username },
    });
    return handleResponse(response).available;
  },

  // Check email availability
  checkEmail: async (email: string): Promise<boolean> => {
    const response = await api.get<ApiResponse<{ available: boolean }>>('/auth/check-email', {
      params: { email },
    });
    return handleResponse(response).available;
  },

  // Get auth status
  getStatus: async (): Promise<{ authenticated: boolean; username?: string }> => {
    const response = await api.get<ApiResponse<{ authenticated: boolean; username?: string }>>('/auth/status');
    return handleResponse(response);
  },
};

// Vault API
export const vaultApi = {
  // Create vault item
  createItem: async (item: VaultItemRequest): Promise<VaultItem> => {
    const response = await api.post<ApiResponse<VaultItem>>('/vault/items', item);
    return handleResponse(response);
  },

  // Get all vault items
  getAllItems: async (): Promise<VaultItem[]> => {
    const response = await api.get<ApiResponse<VaultItem[]>>('/vault/items');
    return handleResponse(response);
  },

  // Get vault items with pagination
  getItemsPaginated: async (params: PaginationParams): Promise<PaginatedResponse<VaultItem>> => {
    const response = await api.get<ApiResponse<PaginatedResponse<VaultItem>>>('/vault/items/paginated', {
      params,
    });
    return handleResponse(response);
  },

  // Get vault item by ID
  getItemById: async (id: number): Promise<VaultItem> => {
    const response = await api.get<ApiResponse<VaultItem>>(`/vault/items/${id}`);
    return handleResponse(response);
  },

  // Update vault item
  updateItem: async (id: number, item: VaultItemRequest): Promise<VaultItem> => {
    const response = await api.put<ApiResponse<VaultItem>>(`/vault/items/${id}`, item);
    return handleResponse(response);
  },

  // Delete vault item
  deleteItem: async (id: number): Promise<void> => {
    await api.delete<ApiResponse<void>>(`/vault/items/${id}`);
  },

  // Toggle favorite status
  toggleFavorite: async (id: number): Promise<VaultItem> => {
    const response = await api.patch<ApiResponse<VaultItem>>(`/vault/items/${id}/favorite`);
    return handleResponse(response);
  },

  // Get favorite items
  getFavorites: async (): Promise<VaultItem[]> => {
    const response = await api.get<ApiResponse<VaultItem[]>>('/vault/items/favorites');
    return handleResponse(response);
  },

  // Get items by type
  getItemsByType: async (type: string): Promise<VaultItem[]> => {
    const response = await api.get<ApiResponse<VaultItem[]>>(`/vault/items/type/${type}`);
    return handleResponse(response);
  },

  // Get items by category
  getItemsByCategory: async (category: string): Promise<VaultItem[]> => {
    const response = await api.get<ApiResponse<VaultItem[]>>(`/vault/items/category/${category}`);
    return handleResponse(response);
  },

  // Search items
  searchItems: async (query: string): Promise<VaultItem[]> => {
    const response = await api.get<ApiResponse<VaultItem[]>>('/vault/items/search', {
      params: { query },
    });
    return handleResponse(response);
  },

  // Get categories
  getCategories: async (): Promise<string[]> => {
    const response = await api.get<ApiResponse<string[]>>('/vault/categories');
    return handleResponse(response);
  },

  // Get recent items
  getRecentItems: async (days: number = 7): Promise<VaultItem[]> => {
    const response = await api.get<ApiResponse<VaultItem[]>>('/vault/items/recent', {
      params: { days },
    });
    return handleResponse(response);
  },

  // Get vault statistics
  getStats: async (): Promise<VaultItemStats> => {
    const response = await api.get<ApiResponse<VaultItemStats>>('/vault/stats');
    return handleResponse(response);
  },

  // Get vault item types
  getTypes: async (): Promise<string[]> => {
    const response = await api.get<ApiResponse<string[]>>('/vault/types');
    return handleResponse(response);
  },
};

// User API
export const userApi = {
  // Get user profile
  getProfile: async (): Promise<User> => {
    const response = await api.get<ApiResponse<User>>('/user/profile');
    return handleResponse(response);
  },

  // Update user profile
  updateProfile: async (data: { fullName?: string; email?: string }): Promise<User> => {
    const response = await api.put<ApiResponse<User>>('/user/profile', data);
    return handleResponse(response);
  },

  // Change password
  changePassword: async (data: {
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;
  }): Promise<void> => {
    await api.put<ApiResponse<void>>('/user/password', data);
  },

  // Get user stats
  getStats: async (): Promise<{ totalVaultItems: number; favoriteItems: number }> => {
    const response = await api.get<ApiResponse<{ totalVaultItems: number; favoriteItems: number }>>('/user/stats');
    return handleResponse(response);
  },

  // Delete account
  deleteAccount: async (password: string): Promise<void> => {
    await api.delete<ApiResponse<void>>('/user/account', {
      data: { password, confirmation: 'DELETE' },
    });
  },
};

// Health API
export const healthApi = {
  // Check application health
  check: async (): Promise<{ status: string; timestamp: string }> => {
    const response = await api.get<ApiResponse<{ status: string; timestamp: string }>>('/health');
    return handleResponse(response);
  },
};

export default api;