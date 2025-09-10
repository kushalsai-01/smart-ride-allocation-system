import React from 'react';
import { useAuth } from '@/hooks/useAuth';

export default function ProfilePage() {
  const { user } = useAuth();

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-gray-100">
          Profile Settings
        </h1>
      </div>

      <div className="card p-6">
        <h2 className="text-lg font-medium text-gray-900 dark:text-gray-100 mb-4">
          Account Information
        </h2>
        
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">
              Username
            </label>
            <p className="mt-1 text-sm text-gray-900 dark:text-gray-100">
              {user?.username}
            </p>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">
              Email
            </label>
            <p className="mt-1 text-sm text-gray-900 dark:text-gray-100">
              {user?.email}
            </p>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">
              Full Name
            </label>
            <p className="mt-1 text-sm text-gray-900 dark:text-gray-100">
              {user?.fullName || 'Not set'}
            </p>
          </div>
        </div>

        <div className="mt-6">
          <button className="btn btn-primary">
            Edit Profile
          </button>
        </div>
      </div>
    </div>
  );
}