import React from 'react';
import { useVault } from '@/hooks/useVault';

export default function VaultPage() {
  const { items, isLoading } = useVault();

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600 mx-auto"></div>
          <p className="mt-2 text-gray-600 dark:text-gray-400">Loading vault...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-gray-100">
          Vault
        </h1>
        <button className="btn btn-primary">
          Add Item
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {items.map((item) => (
          <div key={item.id} className="card p-4">
            <h3 className="font-medium text-gray-900 dark:text-gray-100">
              {item.title}
            </h3>
            <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
              {item.type}
            </p>
          </div>
        ))}
      </div>

      {items.length === 0 && (
        <div className="text-center py-12">
          <p className="text-gray-500 dark:text-gray-400">
            No items in your vault yet. Add your first item to get started.
          </p>
        </div>
      )}
    </div>
  );
}