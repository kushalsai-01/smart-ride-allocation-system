import { useState } from "react";
import { z } from "zod";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useToast } from "./Toast";
import { vaultApi, type VaultItem } from "../shared/api";

const schema = z.object({
  title: z.string().min(1).max(200),
  username: z.string().max(200).optional().or(z.literal("")),
  url: z.string().url().max(500).optional().or(z.literal("")),
  favorite: z.boolean().optional(),
  password: z.string().min(1),
  notes: z.string().optional(),
});

export function VaultItemForm({ initial, onDone }: { initial?: VaultItem; onDone?: () => void }) {
  const [form, setForm] = useState({
    title: initial?.title ?? "",
    username: initial?.username ?? "",
    url: initial?.url ?? "",
    favorite: initial?.favorite ?? false,
    password: "",
    notes: initial?.notes ?? "",
  });
  const { show } = useToast();
  const qc = useQueryClient();

  const create = useMutation({
    mutationFn: () => vaultApi.createVaultItem({ ...form }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ["vault", "list"] });
      show("Item created");
      onDone?.();
    },
  });
  const update = useMutation({
    mutationFn: () => vaultApi.updateVaultItem(initial!.id!, { ...form }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ["vault", "list"] });
      show("Item updated");
      onDone?.();
    },
  });

  function submit(e: React.FormEvent) {
    e.preventDefault();
    const parsed = schema.safeParse(form);
    if (!parsed.success) {
      show("Invalid input");
      return;
    }
    if (initial?.id) update.mutate(); else create.mutate();
  }

  return (
    <form className="space-y-3" onSubmit={submit}>
      <input className="input" placeholder="Title" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} />
      <input className="input" placeholder="Username" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} />
      <input className="input" placeholder="URL" value={form.url} onChange={(e) => setForm({ ...form, url: e.target.value })} />
      <input className="input" placeholder="Password" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
      <textarea className="textarea" placeholder="Notes" value={form.notes} onChange={(e) => setForm({ ...form, notes: e.target.value })} />
      <label className="flex items-center gap-2">
        <input type="checkbox" checked={form.favorite} onChange={(e) => setForm({ ...form, favorite: e.target.checked })} /> Favorite
      </label>
      <button className="btn" type="submit">{initial?.id ? "Update" : "Create"}</button>
    </form>
  );
}

