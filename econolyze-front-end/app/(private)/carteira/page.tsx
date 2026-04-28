"use client";

import { useEffect, useState } from "react";
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
    const { accessToken } = useAuth();

    const [saldo, setSaldo] = useState<number>(0);
    const [transactions, setTransactions] = useState<Transaction[]>([]);
    const [loading, setLoading] = useState(true);

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
                <SaldoCard saldo={saldo} loading={loading} />
                <AcoesRapidas />

                <UltimasTransacoes list={transactions} loading={loading} />
            </div>

            <MainNav />
            <ChatbotButton />
        </div>
    );
}
