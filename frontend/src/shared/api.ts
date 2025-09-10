import axios from "axios";

const baseURL = import.meta.env.VITE_API_URL ?? "http://localhost:8080";

const client = axios.create({ baseURL });

function setToken(token: string | null) {
  if (token) client.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  else delete client.defaults.headers.common["Authorization"];
}

type AuthResponse = { token: string; email: string; userId: number };

async function login(email: string, password: string): Promise<AuthResponse> {
  const { data } = await client.post("/api/auth/login", { email, password });
  return data;
}

async function signup(email: string, password: string): Promise<AuthResponse> {
  const { data } = await client.post("/api/auth/register", { email, password });
  return data;
}

async function me(): Promise<{ subject: string }> {
  const { data } = await client.get("/api/user/me");
  return data;
}

type VaultItem = { id?: number; title: string; username?: string; url?: string; favorite: boolean; password?: string; notes?: string };

async function getVaultList(params: { includeSecrets: boolean }): Promise<VaultItem[]> {
  const { data } = await client.get("/api/vault", { params });
  return data;
}

async function deleteVaultItem(id: number): Promise<void> {
  await client.delete(`/api/vault/${id}`);
}

export const api = { setToken, login, signup, me, getVaultList, deleteVaultItem };
async function createVaultItem(body: { title: string; username?: string; url?: string; favorite?: boolean; password: string; notes?: string }) {
  const { data } = await client.post("/api/vault", { ...body, favorite: !!body.favorite });
  return data as VaultItem;
}

async function updateVaultItem(id: number, body: { title: string; username?: string; url?: string; favorite?: boolean; password?: string; notes?: string }) {
  const { data } = await client.put(`/api/vault/${id}`, { ...body });
  return data as VaultItem;
}

export type { VaultItem };
export const vaultApi = { createVaultItem, updateVaultItem };

