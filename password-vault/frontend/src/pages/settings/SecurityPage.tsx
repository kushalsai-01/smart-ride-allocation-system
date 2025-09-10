import React from 'react';

export default function SecurityPage() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-gray-100">
          Security Settings
        </h1>
      </div>

      <div className="card p-6">
        <h2 className="text-lg font-medium text-gray-900 dark:text-gray-100 mb-4">
          Password & Authentication
        </h2>
        
        <div className="space-y-6">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-medium text-gray-900 dark:text-gray-100">
                Master Password
              </h3>
              <p className="text-sm text-gray-500 dark:text-gray-400">
                Change your master password
              </p>
            </div>
            <button className="btn btn-outline btn-sm">
              Change Password
            </button>
          </div>

          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-medium text-gray-900 dark:text-gray-100">
                Two-Factor Authentication
              </h3>
              <p className="text-sm text-gray-500 dark:text-gray-400">
                Add an extra layer of security
              </p>
            </div>
            <button className="btn btn-outline btn-sm">
              Enable 2FA
            </button>
          </div>

          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-medium text-gray-900 dark:text-gray-100">
                Active Sessions
              </h3>
              <p className="text-sm text-gray-500 dark:text-gray-400">
                Manage your active sessions
              </p>
            </div>
            <button className="btn btn-outline btn-sm">
              View Sessions
            </button>
          </div>
        </div>
      </div>

      <div className="card p-6">
        <h2 className="text-lg font-medium text-gray-900 dark:text-gray-100 mb-4">
          Data & Privacy
        </h2>
        
        <div className="space-y-6">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-medium text-gray-900 dark:text-gray-100">
                Export Data
              </h3>
              <p className="text-sm text-gray-500 dark:text-gray-400">
                Download a copy of your vault data
              </p>
            </div>
            <button className="btn btn-outline btn-sm">
              Export
            </button>
          </div>

          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-sm font-medium text-red-900 dark:text-red-100">
                Delete Account
              </h3>
              <p className="text-sm text-red-600 dark:text-red-400">
                Permanently delete your account and all data
              </p>
            </div>
            <button className="btn btn-danger btn-sm">
              Delete Account
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}