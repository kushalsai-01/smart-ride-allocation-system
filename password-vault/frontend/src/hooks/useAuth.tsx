import React, { createContext, useContext, useEffect, useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-hot-toast';
import { authApi } from '@/lib/api';
import type { User, LoginRequest, RegisterRequest, AuthContextType } from '@/types';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isInitialized, setIsInitialized] = useState(false);
  const queryClient = useQueryClient();

  // Query to check current authentication status
  const { data: currentUser, isLoading: isCheckingAuth } = useQuery({
    queryKey: ['auth', 'me'],
    queryFn: authApi.me,
    retry: false,
    enabled: !isInitialized,
  });

  // Login mutation
  const loginMutation = useMutation({
    mutationFn: authApi.login,
    onSuccess: (userData) => {
      setUser(userData);
      queryClient.setQueryData(['auth', 'me'], userData);
      queryClient.invalidateQueries({ queryKey: ['vault'] });
      toast.success(`Welcome back, ${userData.username}!`);
    },
    onError: (error: any) => {
      console.error('Login failed:', error);
      const message = error?.response?.data?.message || 'Login failed. Please check your credentials.';
      toast.error(message);
    },
  });

  // Register mutation
  const registerMutation = useMutation({
    mutationFn: authApi.register,
    onSuccess: (userData) => {
      setUser(userData);
      queryClient.setQueryData(['auth', 'me'], userData);
      toast.success(`Welcome to Password Vault, ${userData.username}!`);
    },
    onError: (error: any) => {
      console.error('Registration failed:', error);
      const message = error?.response?.data?.message || 'Registration failed. Please try again.';
      toast.error(message);
    },
  });

  // Logout mutation
  const logoutMutation = useMutation({
    mutationFn: authApi.logout,
    onSuccess: () => {
      setUser(null);
      queryClient.clear();
      toast.success('Logged out successfully');
    },
    onError: (error: any) => {
      console.error('Logout failed:', error);
      // Clear user data even if logout fails
      setUser(null);
      queryClient.clear();
      toast.error('Logout failed, but you have been signed out locally');
    },
  });

  // Initialize authentication state
  useEffect(() => {
    if (!isCheckingAuth) {
      setUser(currentUser || null);
      setIsInitialized(true);
    }
  }, [currentUser, isCheckingAuth]);

  // Auth functions
  const login = async (credentials: LoginRequest) => {
    await loginMutation.mutateAsync(credentials);
  };

  const register = async (data: RegisterRequest) => {
    await registerMutation.mutateAsync(data);
  };

  const logout = async () => {
    await logoutMutation.mutateAsync();
  };

  const refreshUser = async () => {
    try {
      const userData = await authApi.me();
      setUser(userData);
      queryClient.setQueryData(['auth', 'me'], userData);
    } catch (error) {
      console.error('Failed to refresh user:', error);
      setUser(null);
      queryClient.removeQueries({ queryKey: ['auth', 'me'] });
    }
  };

  const value: AuthContextType = {
    user,
    isAuthenticated: !!user,
    isLoading: !isInitialized || isCheckingAuth || loginMutation.isPending || registerMutation.isPending || logoutMutation.isPending,
    login,
    register,
    logout,
    refreshUser,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}