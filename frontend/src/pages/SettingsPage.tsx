import { useAuth } from "../providers/AuthProvider";

export const SettingsPage = () => {
  const { user } = useAuth();
  return (
    <div>
      <h1 className="text-2xl font-semibold mb-4">Settings</h1>
      <div>User ID: {user?.userId}</div>
      <div>Email: {user?.email}</div>
    </div>
  );
};

