import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { login, register as registerUser } from "../services/authService";
import { useAuth } from "../hooks/useAuth";

interface LoginForm {
  username: string;
  password: string;
}

interface RegisterForm {
  username: string;
  email: string;
  password: string;
  name: string;
}

export default function Login() {
  const { loginUser } = useAuth();
  const navigate = useNavigate();
  const [mode, setMode] = useState<"login" | "register">("login");
  const [error, setError] = useState("");
  const loginForm = useForm<LoginForm>();
  const registerForm = useForm<RegisterForm>();

  const onLogin = loginForm.handleSubmit(async (values) => {
    try {
      setError("");
      const user = await login(values.username, values.password);
      loginUser(user);
      navigate("/");
    } catch {
      setError("Invalid username or password");
    }
  });

  const onRegister = registerForm.handleSubmit(async (values) => {
    try {
      setError("");
      const user = await registerUser(values);
      loginUser(user);
      navigate("/");
    } catch {
      setError("Registration failed. Username or email may already exist.");
    }
  });

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-950 px-4">
      <div className="w-full max-w-md rounded-2xl bg-white p-8 shadow-xl">
        <p className="text-sm font-medium uppercase tracking-widest text-indigo-600">Library System</p>
        <h1 className="mt-2 text-2xl font-bold">Sign in to continue</h1>
        <p className="mt-2 text-sm text-slate-500">
          Demo: admin / Admin@123 · librarian / Librarian@123 · member / Member@123
        </p>
        <div className="mt-4 flex gap-2">
          <button
            className={`rounded-full px-3 py-1 text-sm ${mode === "login" ? "bg-slate-900 text-white" : "bg-slate-100"}`}
            onClick={() => setMode("login")}
          >
            Login
          </button>
          <button
            className={`rounded-full px-3 py-1 text-sm ${mode === "register" ? "bg-slate-900 text-white" : "bg-slate-100"}`}
            onClick={() => setMode("register")}
          >
            Register as member
          </button>
        </div>
        {error && <p className="mt-4 text-sm text-red-600">{error}</p>}
        {mode === "login" ? (
          <form className="mt-6 space-y-4" onSubmit={onLogin}>
            <input className="w-full rounded-lg border px-3 py-2" placeholder="Username" {...loginForm.register("username", { required: true })} />
            <input className="w-full rounded-lg border px-3 py-2" type="password" placeholder="Password" {...loginForm.register("password", { required: true })} />
            <button className="w-full rounded-lg bg-indigo-600 py-2 font-medium text-white">Login</button>
          </form>
        ) : (
          <form className="mt-6 space-y-4" onSubmit={onRegister}>
            <input className="w-full rounded-lg border px-3 py-2" placeholder="Name" {...registerForm.register("name", { required: true })} />
            <input className="w-full rounded-lg border px-3 py-2" placeholder="Username" {...registerForm.register("username", { required: true })} />
            <input className="w-full rounded-lg border px-3 py-2" placeholder="Email" {...registerForm.register("email", { required: true })} />
            <input className="w-full rounded-lg border px-3 py-2" type="password" placeholder="Password (min 8)" {...registerForm.register("password", { required: true, minLength: 8 })} />
            <button className="w-full rounded-lg bg-indigo-600 py-2 font-medium text-white">Create account</button>
          </form>
        )}
      </div>
    </div>
  );
}
