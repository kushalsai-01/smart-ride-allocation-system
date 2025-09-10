import React from 'react';
import { Link } from 'react-router-dom';
import { 
  Plus, 
  Shield, 
  Key, 
  FileText, 
  Star, 
  Clock, 
  TrendingUp,
  Activity
} from 'lucide-react';
import { useAuth } from '@/hooks/useAuth';
import { useVault } from '@/hooks/useVault';
import { formatRelativeTime } from '@/lib/utils';

export default function DashboardPage() {
  const { user } = useAuth();
  const { items, stats } = useVault();

  const recentItems = items.slice(0, 5);
  const favoriteItems = items.filter(item => item.isFavorite).slice(0, 5);

  return (
    <div className="space-y-6">
      {/* Welcome Header */}
      <div className="bg-gradient-to-r from-primary-600 to-primary-700 rounded-lg p-6 text-white">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold">
              Welcome back, {user?.fullName || user?.username}!
            </h1>
            <p className="text-primary-100 mt-1">
              Your vault is secure and up to date
            </p>
          </div>
          <div className="hidden sm:block">
            <div className="flex items-center space-x-2">
              <Shield className="w-8 h-8 text-primary-200" />
              <div className="text-right">
                <div className="text-2xl font-bold">{stats?.totalItems || 0}</div>
                <div className="text-sm text-primary-200">Total Items</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <Link
          to="/vault?new=password"
          className="card card-hover p-6 text-center group"
        >
          <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900/50 rounded-lg flex items-center justify-center mx-auto mb-3 group-hover:bg-blue-200 dark:group-hover:bg-blue-800/70 transition-colors">
            <Key className="w-6 h-6 text-blue-600 dark:text-blue-400" />
          </div>
          <h3 className="font-medium text-gray-900 dark:text-gray-100">Add Password</h3>
          <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
            Store a new password
          </p>
        </Link>

        <Link
          to="/vault?new=note"
          className="card card-hover p-6 text-center group"
        >
          <div className="w-12 h-12 bg-green-100 dark:bg-green-900/50 rounded-lg flex items-center justify-center mx-auto mb-3 group-hover:bg-green-200 dark:group-hover:bg-green-800/70 transition-colors">
            <FileText className="w-6 h-6 text-green-600 dark:text-green-400" />
          </div>
          <h3 className="font-medium text-gray-900 dark:text-gray-100">Add Note</h3>
          <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
            Save a secure note
          </p>
        </Link>

        <Link
          to="/vault?favorites=true"
          className="card card-hover p-6 text-center group"
        >
          <div className="w-12 h-12 bg-yellow-100 dark:bg-yellow-900/50 rounded-lg flex items-center justify-center mx-auto mb-3 group-hover:bg-yellow-200 dark:group-hover:bg-yellow-800/70 transition-colors">
            <Star className="w-6 h-6 text-yellow-600 dark:text-yellow-400" />
          </div>
          <h3 className="font-medium text-gray-900 dark:text-gray-100">Favorites</h3>
          <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
            {stats?.favoriteItems || 0} favorite items
          </p>
        </Link>

        <Link
          to="/settings/security"
          className="card card-hover p-6 text-center group"
        >
          <div className="w-12 h-12 bg-purple-100 dark:bg-purple-900/50 rounded-lg flex items-center justify-center mx-auto mb-3 group-hover:bg-purple-200 dark:group-hover:bg-purple-800/70 transition-colors">
            <Shield className="w-6 h-6 text-purple-600 dark:text-purple-400" />
          </div>
          <h3 className="font-medium text-gray-900 dark:text-gray-100">Security</h3>
          <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
            Manage security settings
          </p>
        </Link>
      </div>

      {/* Stats Cards */}
      {stats && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="card p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                  Total Items
                </p>
                <p className="text-3xl font-bold text-gray-900 dark:text-gray-100">
                  {stats.totalItems}
                </p>
              </div>
              <div className="w-12 h-12 bg-primary-100 dark:bg-primary-900/50 rounded-lg flex items-center justify-center">
                <Activity className="w-6 h-6 text-primary-600 dark:text-primary-400" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-sm">
              <TrendingUp className="w-4 h-4 text-green-500 mr-1" />
              <span className="text-green-600 dark:text-green-400">
                Active vault
              </span>
            </div>
          </div>

          <div className="card p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                  Passwords
                </p>
                <p className="text-3xl font-bold text-gray-900 dark:text-gray-100">
                  {stats.passwordItems}
                </p>
              </div>
              <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900/50 rounded-lg flex items-center justify-center">
                <Key className="w-6 h-6 text-blue-600 dark:text-blue-400" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-sm">
              <Shield className="w-4 h-4 text-blue-500 mr-1" />
              <span className="text-blue-600 dark:text-blue-400">
                Encrypted & secure
              </span>
            </div>
          </div>

          <div className="card p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                  Favorites
                </p>
                <p className="text-3xl font-bold text-gray-900 dark:text-gray-100">
                  {stats.favoriteItems}
                </p>
              </div>
              <div className="w-12 h-12 bg-yellow-100 dark:bg-yellow-900/50 rounded-lg flex items-center justify-center">
                <Star className="w-6 h-6 text-yellow-600 dark:text-yellow-400" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-sm">
              <Star className="w-4 h-4 text-yellow-500 mr-1" />
              <span className="text-yellow-600 dark:text-yellow-400">
                Quick access
              </span>
            </div>
          </div>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Recent Items */}
        <div className="card">
          <div className="p-6 border-b border-gray-200 dark:border-gray-700">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-gray-900 dark:text-gray-100">
                Recent Items
              </h2>
              <Link
                to="/vault"
                className="text-sm text-primary-600 dark:text-primary-400 hover:text-primary-700 dark:hover:text-primary-300"
              >
                View all
              </Link>
            </div>
          </div>
          <div className="divide-y divide-gray-200 dark:divide-gray-700">
            {recentItems.length > 0 ? (
              recentItems.map((item) => (
                <Link
                  key={item.id}
                  to={`/vault/${item.id}`}
                  className="block p-4 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors"
                >
                  <div className="flex items-center space-x-3">
                    <div className="w-8 h-8 bg-primary-100 dark:bg-primary-900/50 rounded-lg flex items-center justify-center">
                      {item.type === 'PASSWORD' ? (
                        <Key className="w-4 h-4 text-primary-600 dark:text-primary-400" />
                      ) : (
                        <FileText className="w-4 h-4 text-primary-600 dark:text-primary-400" />
                      )}
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium text-gray-900 dark:text-gray-100 truncate">
                        {item.title}
                      </p>
                      <div className="flex items-center space-x-2 mt-1">
                        <Clock className="w-3 h-3 text-gray-400" />
                        <p className="text-xs text-gray-500 dark:text-gray-400">
                          {formatRelativeTime(item.updatedAt)}
                        </p>
                      </div>
                    </div>
                    {item.isFavorite && (
                      <Star className="w-4 h-4 text-yellow-400 fill-current" />
                    )}
                  </div>
                </Link>
              ))
            ) : (
              <div className="p-8 text-center">
                <div className="w-12 h-12 bg-gray-100 dark:bg-gray-700 rounded-lg flex items-center justify-center mx-auto mb-3">
                  <Plus className="w-6 h-6 text-gray-400" />
                </div>
                <p className="text-sm text-gray-500 dark:text-gray-400">
                  No items yet. Add your first password or note.
                </p>
                <Link
                  to="/vault?new=true"
                  className="inline-flex items-center space-x-1 text-sm text-primary-600 dark:text-primary-400 hover:text-primary-700 dark:hover:text-primary-300 mt-2"
                >
                  <Plus className="w-4 h-4" />
                  <span>Add item</span>
                </Link>
              </div>
            )}
          </div>
        </div>

        {/* Favorite Items */}
        <div className="card">
          <div className="p-6 border-b border-gray-200 dark:border-gray-700">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-gray-900 dark:text-gray-100">
                Favorites
              </h2>
              <Link
                to="/vault?favorites=true"
                className="text-sm text-primary-600 dark:text-primary-400 hover:text-primary-700 dark:hover:text-primary-300"
              >
                View all
              </Link>
            </div>
          </div>
          <div className="divide-y divide-gray-200 dark:divide-gray-700">
            {favoriteItems.length > 0 ? (
              favoriteItems.map((item) => (
                <Link
                  key={item.id}
                  to={`/vault/${item.id}`}
                  className="block p-4 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors"
                >
                  <div className="flex items-center space-x-3">
                    <div className="w-8 h-8 bg-yellow-100 dark:bg-yellow-900/50 rounded-lg flex items-center justify-center">
                      <Star className="w-4 h-4 text-yellow-600 dark:text-yellow-400 fill-current" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium text-gray-900 dark:text-gray-100 truncate">
                        {item.title}
                      </p>
                      <div className="flex items-center space-x-2 mt-1">
                        <span className="text-xs text-gray-500 dark:text-gray-400 capitalize">
                          {item.type.toLowerCase()}
                        </span>
                        {item.category && (
                          <>
                            <span className="text-xs text-gray-400">•</span>
                            <span className="text-xs text-gray-500 dark:text-gray-400">
                              {item.category}
                            </span>
                          </>
                        )}
                      </div>
                    </div>
                  </div>
                </Link>
              ))
            ) : (
              <div className="p-8 text-center">
                <div className="w-12 h-12 bg-yellow-100 dark:bg-yellow-900/50 rounded-lg flex items-center justify-center mx-auto mb-3">
                  <Star className="w-6 h-6 text-yellow-400" />
                </div>
                <p className="text-sm text-gray-500 dark:text-gray-400">
                  No favorite items yet. Star items to see them here.
                </p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}