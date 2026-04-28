"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/auth.context";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { TextFormField } from "@/components/form-fields";
import type { RegisterRequest } from "@/lib/types";
import {toast} from "sonner";

export function RegistroForm() {
    const router = useRouter();
    const { register } = useAuth();

    const [form, setForm] = useState<RegisterRequest>({
        username: "",
        email: "",
        password: "",
    });

    const [confirmPassword, setConfirmPassword] = useState("");

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        if (form.password !== confirmPassword) {
            setError("As senhas não coincidem");
            return;
        }

        setLoading(true);

        try {
            const response = await register(form);
            toast.success(response.message, {
                description: `Usuário: ${response.username}`,
                duration: 4000,
            });
            router.push("/login");
        } catch (err: any) {
            setError(err?.message ?? "Erro ao registrar");
            toast.error("Erro ao criar conta", {
                description: err?.message ?? "Tente novamente",
            });
        } finally {
            setLoading(false);
        }
    };

    return (
        <Card className="border-border/50">
            <CardContent className="pt-6">
                <form onSubmit={handleSubmit} className="form-shell">

                    <TextFormField
                        id="username"
                        label="Nome de Usuário"
                        type="text"
                        value={form.username}
                        placeholder="seuuser"
                        onChange={(e) => setForm({ ...form, username: e.target.value })}
                        required
                    />

                    <TextFormField
                        id="email"
                        label="E-mail"
                        type="email"
                        value={form.email}
                        placeholder="seu@email.com"
                        onChange={(e) => setForm({ ...form, email: e.target.value })}
                        required
                    />

                    <TextFormField
                        id="password"
                        label="Senha"
                        type="password"
                        value={form.password}
                        placeholder="••••••••"
                        onChange={(e) => setForm({ ...form, password: e.target.value })}
                        required
                    />

                    <TextFormField
                        id="confirm"
                        label="Confirmar Senha"
                        type="password"
                        value={confirmPassword}
                        placeholder="••••••••"
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />

                    {error && (
                        <p className="text-sm text-destructive" role="alert" aria-live="polite">
                            {error}
                        </p>
                    )}

                    <Button type="submit" className="w-full" size="lg" disabled={loading}>
                        {loading ? "Criando conta..." : "Registrar"}
                    </Button>
                </form>
            </CardContent>
        </Card>
    );
}
