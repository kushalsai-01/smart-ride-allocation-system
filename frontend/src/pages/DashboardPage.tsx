import { useEffect, useMemo, useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "../shared/api";
import { useAuth } from "../providers/AuthProvider";
import { CopyButton } from "../components/CopyButton";
import { VaultItemForm } from "../components/VaultItemForm";

export const DashboardPage = () => {
  const { user } = useAuth();
  const qc = useQueryClient();
  const [query, setQuery] = useState("");
  const { data, isLoading } = useQuery({
    queryKey: ["vault", "list"],
    queryFn: () => api.getVaultList({ includeSecrets: false }),
    enabled: !!user,
  });

  const filtered = useMemo(() => {
    const q = query.toLowerCase();
    return (data ?? []).filter((i) =>
      [i.title, i.username, i.url].filter(Boolean).some((v) => v!.toLowerCase().includes(q))
    );
  }, [data, query]);

  const del = useMutation({
    mutationFn: (id: number) => api.deleteVaultItem(id),
    onSuccess: () => qc.invalidateQueries({ queryKey: ["vault", "list"] }),
  });

  useEffect(() => {
    if (!user) return;
  }, [user]);

  if (!user) return <div>Please login.</div>;
  if (isLoading) return <div>Loading…</div>;

  return (
    <div>
      <div className="mb-6">
        <details className="rounded border border-gray-200 dark:border-gray-800">
          <summary className="px-3 py-2 cursor-pointer select-none">Add item</summary>
          <div className="p-3">
            <VaultItemForm onDone={() => qc.invalidateQueries({ queryKey: ["vault", "list"] })} />
          </div>
        </details>
      </div>
      <div className="flex items-center gap-3 mb-4">
        <input className="input" placeholder="Search…" value={query} onChange={(e) => setQuery(e.target.value)} />
        <button className="btn" onClick={() => qc.invalidateQueries({ queryKey: ["vault", "list"] })}>Refresh</button>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {filtered.map((i) => (
          <div key={i.id} className="rounded-lg border border-gray-200 dark:border-gray-800 p-4">
            <div className="flex items-center justify-between">
              <div className="font-semibold">{i.title}</div>
              <button className="btn" onClick={() => del.mutate(i.id!)}>Delete</button>
            </div>
            <div className="text-sm text-gray-600 dark:text-gray-300 mt-1">{i.username}</div>
            {i.url && (
              <a href={i.url} target="_blank" className="text-blue-600">{i.url}</a>
            )}
            <div className="mt-2 flex gap-2">
              {i.username && <CopyButton value={i.username} label="Copy user" />}
              {i.url && <CopyButton value={i.url} label="Copy URL" />}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

