import React from 'react';
import { useParams, Navigate } from 'react-router-dom';
import { useVault } from '@/hooks/useVault';

export default function VaultItemPage() {
  const { id } = useParams<{ id: string }>();
  const { getItemById } = useVault();

  const itemId = parseInt(id || '0', 10);
  const item = getItemById(itemId);

  if (!item) {
    return <Navigate to="/vault" replace />;
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-gray-100">
          {item.title}
        </h1>
        <button className="btn btn-secondary">
          Edit
        </button>
      </div>

      <div className="card p-6">
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">
              Type
            </label>
            <p className="mt-1 text-sm text-gray-900 dark:text-gray-100">
              {item.type}
            </p>
          </div>

          {item.username && (
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                Username
              </label>
              <p className="mt-1 text-sm text-gray-900 dark:text-gray-100">
                {item.username}
              </p>
            </div>
          )}

          {item.email && (
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                Email
              </label>
              <p className="mt-1 text-sm text-gray-900 dark:text-gray-100">
                {item.email}
              </p>
            </div>
          )}

          {item.url && (
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                URL
              </label>
              <p className="mt-1 text-sm text-gray-900 dark:text-gray-100">
                {item.url}
              </p>
            </div>
          )}

          {item.notes && (
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">
                Notes
              </label>
              <p className="mt-1 text-sm text-gray-900 dark:text-gray-100 whitespace-pre-wrap">
                {item.notes}
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}