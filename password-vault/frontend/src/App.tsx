import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from '@/hooks/useAuth';
import { ThemeProvider } from '@/hooks/useTheme';
import { VaultProvider } from '@/hooks/useVault';

// Layout components
import AuthLayout from '@/components/layout/AuthLayout';
import DashboardLayout from '@/components/layout/DashboardLayout';
import ProtectedRoute from '@/components/auth/ProtectedRoute';

// Page components
import LoginPage from '@/pages/auth/LoginPage';
import RegisterPage from '@/pages/auth/RegisterPage';
import DashboardPage from '@/pages/dashboard/DashboardPage';
import VaultPage from '@/pages/vault/VaultPage';
import VaultItemPage from '@/pages/vault/VaultItemPage';
import SettingsPage from '@/pages/settings/SettingsPage';
import ProfilePage from '@/pages/settings/ProfilePage';
import SecurityPage from '@/pages/settings/SecurityPage';

// Error components
import NotFoundPage from '@/pages/error/NotFoundPage';

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <VaultProvider>
          <Routes>
            {/* Public routes */}
            <Route path="/login" element={
              <AuthLayout>
                <LoginPage />
              </AuthLayout>
            } />
            
            <Route path="/register" element={
              <AuthLayout>
                <RegisterPage />
              </AuthLayout>
            } />

            {/* Protected routes */}
            <Route path="/" element={
              <ProtectedRoute>
                <DashboardLayout>
                  <Navigate to="/dashboard" replace />
                </DashboardLayout>
              </ProtectedRoute>
            } />

            <Route path="/dashboard" element={
              <ProtectedRoute>
                <DashboardLayout>
                  <DashboardPage />
                </DashboardLayout>
              </ProtectedRoute>
            } />

            <Route path="/vault" element={
              <ProtectedRoute>
                <DashboardLayout>
                  <VaultPage />
                </DashboardLayout>
              </ProtectedRoute>
            } />

            <Route path="/vault/:id" element={
              <ProtectedRoute>
                <DashboardLayout>
                  <VaultItemPage />
                </DashboardLayout>
              </ProtectedRoute>
            } />

            <Route path="/settings" element={
              <ProtectedRoute>
                <DashboardLayout>
                  <SettingsPage />
                </DashboardLayout>
              </ProtectedRoute>
            } />

            <Route path="/settings/profile" element={
              <ProtectedRoute>
                <DashboardLayout>
                  <ProfilePage />
                </DashboardLayout>
              </ProtectedRoute>
            } />

            <Route path="/settings/security" element={
              <ProtectedRoute>
                <DashboardLayout>
                  <SecurityPage />
                </DashboardLayout>
              </ProtectedRoute>
            } />

            {/* 404 route */}
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </VaultProvider>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;