import { useToast } from "./Toast";

export const CopyButton = ({ value, label = "Copy" }: { value: string; label?: string }) => {
  const { show } = useToast();
  async function copy() {
    await navigator.clipboard.writeText(value);
    show("Copied to clipboard");
  }
  return (
    <button className="btn" type="button" onClick={copy}>{label}</button>
  );
};

