import React, { createContext, useContext, useEffect, useMemo, useState } from "react";
import { api } from "../shared/api";

type User = { userId: number; email: string } | null;

type AuthContextType = {
  user: User;
  token: string | null;
  login: (email: string, password: string) => Promise<void>;
  signup: (email: string, password: string) => Promise<void>;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem("token"));
  const [user, setUser] = useState<User>(null);

  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token);
      api.setToken(token);
      api.me().then((me) => setUser({ userId: Number(me.subject), email: "" })).catch(() => setUser(null));
    } else {
      localStorage.removeItem("token");
      api.setToken(null);
      setUser(null);
    }
  }, [token]);

  const login = async (email: string, password: string) => {
    const res = await api.login(email, password);
    setToken(res.token);
    setUser({ userId: res.userId, email: res.email });
  };

  const signup = async (email: string, password: string) => {
    const res = await api.signup(email, password);
    setToken(res.token);
    setUser({ userId: res.userId, email: res.email });
  };

  const logout = () => setToken(null);

  const value = useMemo(() => ({ user, token, login, signup, logout }), [user, token]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
};

