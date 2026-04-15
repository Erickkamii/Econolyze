"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/auth.context";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent } from "@/components/ui/card";
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
                <form onSubmit={handleSubmit} className="space-y-6">

                    <div className="space-y-2">
                        <Label htmlFor="username">Nome de Usuário</Label>
                        <Input
                            id="username"
                            type="text"
                            value={form.username}
                            placeholder="seuuser"
                            onChange={(e) => setForm({ ...form, username: e.target.value })}
                            required
                            className="bg-secondary border-border"
                        />
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="email">E-mail</Label>
                        <Input
                            id="email"
                            type="email"
                            value={form.email}
                            placeholder="seu@email.com"
                            onChange={(e) => setForm({ ...form, email: e.target.value })}
                            required
                            className="bg-secondary border-border"
                        />
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="password">Senha</Label>
                        <Input
                            id="password"
                            type="password"
                            value={form.password}
                            placeholder="••••••••"
                            onChange={(e) => setForm({ ...form, password: e.target.value })}
                            required
                            className="bg-secondary border-border"
                        />
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="confirm">Confirmar Senha</Label>
                        <Input
                            id="confirm"
                            type="password"
                            value={confirmPassword}
                            placeholder="••••••••"
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                            className="bg-secondary border-border"
                        />
                    </div>

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
