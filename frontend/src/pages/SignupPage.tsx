import { useState } from "react";
import { useAuth } from "../providers/AuthProvider";
import { z } from "zod";
import { useNavigate, Link } from "react-router-dom";

const schema = z.object({
  email: z.string().email(),
  password: z.string().min(8),
});

export const SignupPage = () => {
  const { signup } = useAuth();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    const parsed = schema.safeParse({ email, password });
    if (!parsed.success) {
      setError("Invalid input");
      return;
    }
    try {
      await signup(parsed.data.email, parsed.data.password);
      navigate("/");
    } catch (e: any) {
      setError(e?.message ?? "Signup failed");
    }
  }

  return (
    <form className="max-w-md mx-auto" onSubmit={onSubmit}>
      <h1 className="text-2xl font-semibold mb-4">Sign up</h1>
      {error && <div className="text-red-600 mb-2">{error}</div>}
      <div className="space-y-3">
        <input className="input" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
        <input className="input" placeholder="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
      </div>
      <button className="btn mt-4" type="submit">Create account</button>
      <div className="mt-2 text-sm">Have an account? <Link to="/login" className="text-blue-600">Login</Link></div>
    </form>
  );
};

