"use client";

import { useState } from "react";
import { useAuth } from "@/context/auth.context";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent } from "@/components/ui/card";
import {toast} from "sonner";

export function LoginForm() {
    const { login } = useAuth()

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            await login({ username, password });
            toast.success("Login bem-sucedido", {
                description: `Usuário: ${username}`,
                duration: 4000
            });
        } catch (err: any) {
            setError(err?.message ?? "Erro ao fazer login");
            toast.error("Erro ao fazer login", {
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
                        <Label htmlFor="email">E-mail ou Usuário</Label>
                        <Input
                            id="email"
                            type="text"
                            placeholder="seu@email.com"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            className="bg-secondary border-border"
                        />
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="password">Senha</Label>
                        <Input
                            id="password"
                            type="password"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            className="bg-secondary border-border"
                        />
                    </div>

                    {error && (
                        <p className="text-sm text-destructive" role="alert" aria-live="polite">{error}</p>
                    )}

                    <Button type="submit" className="w-full" size="lg" disabled={loading}>
                        {loading ? "Entrando..." : "Entrar"}
                    </Button>
                </form>
            </CardContent>
        </Card>
    );
}
