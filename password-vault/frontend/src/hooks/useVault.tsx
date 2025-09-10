import React, { createContext, useContext, useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-hot-toast';
import { vaultApi } from '@/lib/api';
import type { 
  VaultItem, 
  VaultItemRequest, 
  VaultItemStats, 
  SearchFilters, 
  VaultContextType 
} from '@/types';
import { useAuth } from './useAuth';

const VaultContext = createContext<VaultContextType | undefined>(undefined);

export function VaultProvider({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth();
  const queryClient = useQueryClient();
  const [searchFilters, setSearchFilters] = useState<SearchFilters>({
    sortBy: 'updatedAt',
    sortOrder: 'desc',
  });

  // Fetch all vault items
  const { data: items = [], isLoading: isLoadingItems } = useQuery({
    queryKey: ['vault', 'items'],
    queryFn: vaultApi.getAllItems,
    enabled: isAuthenticated,
    staleTime: 2 * 60 * 1000, // 2 minutes
  });

  // Fetch vault statistics
  const { data: stats = null } = useQuery({
    queryKey: ['vault', 'stats'],
    queryFn: vaultApi.getStats,
    enabled: isAuthenticated,
    staleTime: 5 * 60 * 1000, // 5 minutes
  });

  // Fetch categories
  const { data: categories = [] } = useQuery({
    queryKey: ['vault', 'categories'],
    queryFn: vaultApi.getCategories,
    enabled: isAuthenticated,
    staleTime: 10 * 60 * 1000, // 10 minutes
  });

  // Create item mutation
  const createItemMutation = useMutation({
    mutationFn: vaultApi.createItem,
    onSuccess: (newItem) => {
      queryClient.setQueryData(['vault', 'items'], (old: VaultItem[] = []) => [newItem, ...old]);
      queryClient.invalidateQueries({ queryKey: ['vault', 'stats'] });
      queryClient.invalidateQueries({ queryKey: ['vault', 'categories'] });
      toast.success('Item created successfully');
    },
    onError: (error: any) => {
      console.error('Failed to create item:', error);
      const message = error?.response?.data?.message || 'Failed to create item';
      toast.error(message);
    },
  });

  // Update item mutation
  const updateItemMutation = useMutation({
    mutationFn: ({ id, item }: { id: number; item: VaultItemRequest }) => 
      vaultApi.updateItem(id, item),
    onSuccess: (updatedItem) => {
      queryClient.setQueryData(['vault', 'items'], (old: VaultItem[] = []) =>
        old.map(item => item.id === updatedItem.id ? updatedItem : item)
      );
      queryClient.invalidateQueries({ queryKey: ['vault', 'stats'] });
      queryClient.invalidateQueries({ queryKey: ['vault', 'categories'] });
      toast.success('Item updated successfully');
    },
    onError: (error: any) => {
      console.error('Failed to update item:', error);
      const message = error?.response?.data?.message || 'Failed to update item';
      toast.error(message);
    },
  });

  // Delete item mutation
  const deleteItemMutation = useMutation({
    mutationFn: vaultApi.deleteItem,
    onSuccess: (_, deletedId) => {
      queryClient.setQueryData(['vault', 'items'], (old: VaultItem[] = []) =>
        old.filter(item => item.id !== deletedId)
      );
      queryClient.invalidateQueries({ queryKey: ['vault', 'stats'] });
      queryClient.invalidateQueries({ queryKey: ['vault', 'categories'] });
      toast.success('Item deleted successfully');
    },
    onError: (error: any) => {
      console.error('Failed to delete item:', error);
      const message = error?.response?.data?.message || 'Failed to delete item';
      toast.error(message);
    },
  });

  // Toggle favorite mutation
  const toggleFavoriteMutation = useMutation({
    mutationFn: vaultApi.toggleFavorite,
    onSuccess: (updatedItem) => {
      queryClient.setQueryData(['vault', 'items'], (old: VaultItem[] = []) =>
        old.map(item => item.id === updatedItem.id ? updatedItem : item)
      );
      queryClient.invalidateQueries({ queryKey: ['vault', 'stats'] });
      const action = updatedItem.isFavorite ? 'added to' : 'removed from';
      toast.success(`Item ${action} favorites`);
    },
    onError: (error: any) => {
      console.error('Failed to toggle favorite:', error);
      const message = error?.response?.data?.message || 'Failed to update favorite status';
      toast.error(message);
    },
  });

  // Vault functions
  const createItem = async (item: VaultItemRequest): Promise<VaultItem> => {
    return await createItemMutation.mutateAsync(item);
  };

  const updateItem = async (id: number, item: VaultItemRequest): Promise<VaultItem> => {
    return await updateItemMutation.mutateAsync({ id, item });
  };

  const deleteItem = async (id: number): Promise<void> => {
    await deleteItemMutation.mutateAsync(id);
  };

  const toggleFavorite = async (id: number): Promise<VaultItem> => {
    return await toggleFavoriteMutation.mutateAsync(id);
  };

  const refreshItems = async (): Promise<void> => {
    await queryClient.invalidateQueries({ queryKey: ['vault', 'items'] });
  };

  const getItemById = (id: number): VaultItem | undefined => {
    return items.find(item => item.id === id);
  };

  // Filter and sort items based on search filters
  const filteredItems = React.useMemo(() => {
    let filtered = [...items];

    // Apply search query
    if (searchFilters.query) {
      const query = searchFilters.query.toLowerCase();
      filtered = filtered.filter(item =>
        item.title.toLowerCase().includes(query) ||
        item.description?.toLowerCase().includes(query) ||
        item.username?.toLowerCase().includes(query) ||
        item.email?.toLowerCase().includes(query) ||
        item.category?.toLowerCase().includes(query)
      );
    }

    // Apply type filter
    if (searchFilters.type) {
      filtered = filtered.filter(item => item.type === searchFilters.type);
    }

    // Apply category filter
    if (searchFilters.category) {
      filtered = filtered.filter(item => item.category === searchFilters.category);
    }

    // Apply favorite filter
    if (searchFilters.isFavorite !== undefined) {
      filtered = filtered.filter(item => item.isFavorite === searchFilters.isFavorite);
    }

    // Apply sorting
    if (searchFilters.sortBy) {
      filtered.sort((a, b) => {
        const aVal = a[searchFilters.sortBy as keyof VaultItem];
        const bVal = b[searchFilters.sortBy as keyof VaultItem];
        
        if (aVal === null || aVal === undefined) return 1;
        if (bVal === null || bVal === undefined) return -1;
        
        let comparison = 0;
        if (aVal > bVal) comparison = 1;
        if (aVal < bVal) comparison = -1;
        
        return searchFilters.sortOrder === 'desc' ? -comparison : comparison;
      });
    }

    return filtered;
  }, [items, searchFilters]);

  const value: VaultContextType = {
    items: filteredItems,
    stats,
    categories,
    isLoading: isLoadingItems || 
               createItemMutation.isPending || 
               updateItemMutation.isPending || 
               deleteItemMutation.isPending || 
               toggleFavoriteMutation.isPending,
    searchFilters,
    setSearchFilters,
    createItem,
    updateItem,
    deleteItem,
    toggleFavorite,
    refreshItems,
    getItemById,
  };

  return <VaultContext.Provider value={value}>{children}</VaultContext.Provider>;
}

export function useVault() {
  const context = useContext(VaultContext);
  if (context === undefined) {
    throw new Error('useVault must be used within a VaultProvider');
  }
  return context;
}