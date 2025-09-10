import React from 'react';
import { Link } from 'react-router-dom';
import { User, Shield, Bell, Palette, HelpCircle, LogOut } from 'lucide-react';
import { useAuth } from '@/hooks/useAuth';

const settingsItems = [
  {
    title: 'Profile',
    description: 'Manage your account information and preferences',
    href: '/settings/profile',
    icon: User,
    color: 'text-blue-600 dark:text-blue-400 bg-blue-100 dark:bg-blue-900/50',
  },
  {
    title: 'Security',
    description: 'Password, two-factor authentication, and security settings',
    href: '/settings/security',
    icon: Shield,
    color: 'text-green-600 dark:text-green-400 bg-green-100 dark:bg-green-900/50',
  },
  {
    title: 'Notifications',
    description: 'Configure email and push notification preferences',
    href: '/settings/notifications',
    icon: Bell,
    color: 'text-yellow-600 dark:text-yellow-400 bg-yellow-100 dark:bg-yellow-900/50',
  },
  {
    title: 'Appearance',
    description: 'Theme, language, and display preferences',
    href: '/settings/appearance',
    icon: Palette,
    color: 'text-purple-600 dark:text-purple-400 bg-purple-100 dark:bg-purple-900/50',
  },
];

export default function SettingsPage() {
  const { user, logout } = useAuth();

  const handleLogout = async () => {
    await logout();
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-gray-100">
          Settings
        </h1>
      </div>

      {/* User Info Card */}
      <div className="card p-6">
        <div className="flex items-center space-x-4">
          <div className="w-16 h-16 bg-primary-600 dark:bg-primary-500 rounded-full flex items-center justify-center">
            <User className="w-8 h-8 text-white" />
          </div>
          <div>
            <h2 className="text-xl font-semibold text-gray-900 dark:text-gray-100">
              {user?.fullName || user?.username}
            </h2>
            <p className="text-gray-500 dark:text-gray-400">{user?.email}</p>
            <p className="text-sm text-gray-400 dark:text-gray-500 mt-1">
              Member since {user?.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'Unknown'}
            </p>
          </div>
        </div>
      </div>

      {/* Settings Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {settingsItems.map((item) => (
          <Link
            key={item.href}
            to={item.href}
            className="card card-hover p-6 group"
          >
            <div className="flex items-start space-x-4">
              <div className={`w-12 h-12 rounded-lg flex items-center justify-center ${item.color} group-hover:scale-110 transition-transform`}>
                <item.icon className="w-6 h-6" />
              </div>
              <div className="flex-1">
                <h3 className="text-lg font-medium text-gray-900 dark:text-gray-100 group-hover:text-primary-600 dark:group-hover:text-primary-400 transition-colors">
                  {item.title}
                </h3>
                <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
                  {item.description}
                </p>
              </div>
            </div>
          </Link>
        ))}
      </div>

      {/* Quick Actions */}
      <div className="space-y-4">
        <h3 className="text-lg font-medium text-gray-900 dark:text-gray-100">
          Quick Actions
        </h3>
        
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Link
            to="/help"
            className="card card-hover p-4 group"
          >
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 bg-gray-100 dark:bg-gray-700 rounded-lg flex items-center justify-center group-hover:bg-gray-200 dark:group-hover:bg-gray-600 transition-colors">
                <HelpCircle className="w-5 h-5 text-gray-600 dark:text-gray-400" />
              </div>
              <div>
                <h4 className="font-medium text-gray-900 dark:text-gray-100">
                  Help & Support
                </h4>
                <p className="text-sm text-gray-500 dark:text-gray-400">
                  Get help and contact support
                </p>
              </div>
            </div>
          </Link>

          <button
            onClick={handleLogout}
            className="card card-hover p-4 group text-left hover:bg-red-50 dark:hover:bg-red-900/20 border-red-200 dark:border-red-800"
          >
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 bg-red-100 dark:bg-red-900/50 rounded-lg flex items-center justify-center group-hover:bg-red-200 dark:group-hover:bg-red-800/70 transition-colors">
                <LogOut className="w-5 h-5 text-red-600 dark:text-red-400" />
              </div>
              <div>
                <h4 className="font-medium text-red-900 dark:text-red-100">
                  Sign Out
                </h4>
                <p className="text-sm text-red-600 dark:text-red-400">
                  Sign out of your account
                </p>
              </div>
            </div>
          </button>
        </div>
      </div>
    </div>
  );
}