import { Outlet, Link, useNavigate } from "react-router-dom";
import { useAuth } from "../providers/AuthProvider";
import { ToastProvider } from "../components/Toast";

export const AppLayout = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  return (
    <ToastProvider>
      <header className="border-b border-gray-200 dark:border-gray-800">
        <div className="container-page flex items-center justify-between">
          <Link to="/" className="font-semibold">Password & Notes Vault</Link>
          <div className="flex items-center gap-3">
            <button onClick={() => document.documentElement.classList.toggle("dark")} className="btn">Theme</button>
            {user ? (
              <>
                <Link to="/settings" className="btn">Settings</Link>
                <button className="btn" onClick={() => { logout(); navigate("/login"); }}>Logout</button>
              </>
            ) : (
              <>
                <Link to="/login" className="btn">Login</Link>
                <Link to="/signup" className="btn">Sign up</Link>
              </>
            )}
          </div>
        </div>
      </header>
      <main className="container-page">
        <Outlet />
      </main>
    </ToastProvider>
  );
};

