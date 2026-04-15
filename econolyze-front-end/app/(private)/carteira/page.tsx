"use client";

import { useEffect, useState } from "react";
import { LogOut } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/context/auth.context";
import { toast } from "sonner";

import { MainNav } from "@/components/main-nav";
import { SaldoCard } from "@/components/saldo-card";
import { AcoesRapidas } from "@/components/acoes-rapidas";
import { UltimasTransacoes } from "@/components/ultimas-transacoes";
import { ChatbotButton } from "@/components/chatbot-button";
import { apiRequest } from "@/lib/services/api-client";
import type { Transaction } from "@/lib/types/transaction.types";

export default function CarteiraPage() {
    const { logout, isLoading, accessToken } = useAuth();

    const [saldo, setSaldo] = useState<number>(0);
    const [transactions, setTransactions] = useState<Transaction[]>([]);
    const [loading, setLoading] = useState(true);

    const handleLogout = async () => {
        if (isLoading) return;

        const logoutPromise = logout();

        toast.promise(logoutPromise, {
            loading: "Saindo...",
            success: "Logout bem-sucedido!",
            error: "Erro ao sair. Tente novamente.",
        });
    };

    useEffect(() => {
        if (!accessToken) return;

        async function load() {
            setLoading(true);
            try {
                const dashboardData = await apiRequest<{
                    balance?: { balance?: number };
                    transactions?: Transaction[];
                }>(
                    "/dashboard",
                    {
                        headers: {
                            "Content-Type": "application/json",
                        },
                    },
                    "Erro ao carregar dashboard."
                );

                setSaldo(dashboardData.balance?.balance ?? 0);
                setTransactions((dashboardData.transactions ?? []).slice(0, 5));
            } catch (error: any) {
                toast.error(error?.message ?? "Erro inesperado.");
            } finally {
                setLoading(false);
            }
        }

        load();
    }, [accessToken]);

    return (
        <div className="min-h-screen pb-20">
            <div className="max-w-5xl mx-auto p-6 space-y-6">
                <div className="flex items-center justify-between mb-8">
                    <h1 className="text-2xl font-bold text-primary">Econolyze</h1>
                    <Button
                        variant="ghost"
                        size="sm"
                        onClick={handleLogout}
                        disabled={isLoading}
                        className="text-primary hover:bg-primary/10 transition-colors duration-200"
                    >
                        <LogOut className="mr-2 h-4 w-4" />
                        Sair
                    </Button>
                </div>

                <SaldoCard saldo={saldo} loading={loading} />
                <AcoesRapidas />

                <UltimasTransacoes list={transactions} loading={loading} />
            </div>

            <MainNav />
            <ChatbotButton />
        </div>
    );
}
