import React from 'react';
import { Link } from 'react-router-dom';
import { Shield, Moon, Sun } from 'lucide-react';
import { useTheme } from '@/hooks/useTheme';
import { cn } from '@/lib/utils';

interface AuthLayoutProps {
  children: React.ReactNode;
}

export default function AuthLayout({ children }: AuthLayoutProps) {
  const { theme, toggleTheme } = useTheme();

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-50 to-primary-100 dark:from-gray-900 dark:to-gray-800 flex flex-col">
      {/* Header */}
      <header className="p-6">
        <div className="flex items-center justify-between max-w-6xl mx-auto">
          {/* Logo */}
          <Link 
            to="/" 
            className="flex items-center space-x-3 text-primary-600 dark:text-primary-400 hover:text-primary-700 dark:hover:text-primary-300 transition-colors"
          >
            <div className="p-2 bg-primary-600 dark:bg-primary-500 rounded-lg shadow-lg">
              <Shield className="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 className="text-xl font-bold">Password Vault</h1>
              <p className="text-xs text-gray-600 dark:text-gray-400">Secure Password Manager</p>
            </div>
          </Link>

          {/* Theme Toggle */}
          <button
            onClick={toggleTheme}
            className={cn(
              'p-2 rounded-lg transition-colors',
              'bg-white/80 dark:bg-gray-800/80 backdrop-blur-sm',
              'hover:bg-white dark:hover:bg-gray-800',
              'border border-gray-200 dark:border-gray-700',
              'text-gray-600 dark:text-gray-400',
              'hover:text-gray-900 dark:hover:text-gray-100'
            )}
            aria-label={`Switch to ${theme === 'light' ? 'dark' : 'light'} theme`}
          >
            {theme === 'light' ? (
              <Moon className="w-5 h-5" />
            ) : (
              <Sun className="w-5 h-5" />
            )}
          </button>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1 flex items-center justify-center p-6">
        <div className="w-full max-w-md">
          {/* Auth Card */}
          <div className="bg-white/95 dark:bg-gray-800/95 backdrop-blur-sm rounded-2xl shadow-xl border border-white/20 dark:border-gray-700/50 p-8">
            {children}
          </div>

          {/* Footer Links */}
          <div className="mt-8 text-center">
            <div className="flex items-center justify-center space-x-6 text-sm text-gray-600 dark:text-gray-400">
              <a 
                href="#" 
                className="hover:text-primary-600 dark:hover:text-primary-400 transition-colors"
              >
                Privacy Policy
              </a>
              <span className="w-1 h-1 bg-gray-400 rounded-full"></span>
              <a 
                href="#" 
                className="hover:text-primary-600 dark:hover:text-primary-400 transition-colors"
              >
                Terms of Service
              </a>
              <span className="w-1 h-1 bg-gray-400 rounded-full"></span>
              <a 
                href="#" 
                className="hover:text-primary-600 dark:hover:text-primary-400 transition-colors"
              >
                Help
              </a>
            </div>
            <p className="mt-4 text-xs text-gray-500 dark:text-gray-500">
              © 2024 Password Vault. All rights reserved.
            </p>
          </div>
        </div>
      </main>

      {/* Background Pattern */}
      <div className="fixed inset-0 -z-10 overflow-hidden">
        <div className="absolute -top-1/2 -right-1/2 w-full h-full bg-gradient-to-br from-primary-200/20 to-primary-400/20 dark:from-primary-800/10 dark:to-primary-600/10 rounded-full blur-3xl"></div>
        <div className="absolute -bottom-1/2 -left-1/2 w-full h-full bg-gradient-to-tr from-primary-300/20 to-primary-500/20 dark:from-primary-700/10 dark:to-primary-500/10 rounded-full blur-3xl"></div>
      </div>
    </div>
  );
}