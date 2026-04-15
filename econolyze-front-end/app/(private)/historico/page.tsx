"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { Plus } from "lucide-react";
import { toast } from "sonner";

import { MainNav } from "@/components/main-nav";
import { ChatbotButton } from "@/components/chatbot-button";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

import { useAuth } from "@/context/auth.context";
import { TransactionService } from "@/lib/services/transaction.service";
import type { Transaction, TransactionType } from "@/lib/types/transaction.types";
import {
  TRANSACTION_ICON_MAP,
  TRANSACTION_LABEL_MAP,
} from "@/lib/constants/transaction.constants";
import { isIncomeTransaction, formatCurrency } from "@/lib/utils/transaction-mappers";
import { cn } from "@/lib/utils";

type FilterType = "todos" | "INCOME" | "EXPENSE";

export default function HistoricoPage() {
  const { accessToken } = useAuth();
  const [filtro, setFiltro] = useState<FilterType>("todos");
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!accessToken) return;

    async function loadTransactions() {
      setLoading(true);
      try {
        const type = filtro === "todos" ? undefined : filtro;
        const data = await TransactionService.getAll(accessToken, 0, 50, type);
        setTransactions(data);
      } catch (error: any) {
        toast.error(error.message ?? "Erro ao carregar transações");
      } finally {
        setLoading(false);
      }
    }

    loadTransactions();
  }, [accessToken, filtro]);

  const formatDate = (dateString?: string) => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    return date.toLocaleDateString("pt-BR");
  };

  return (
      <div className="min-h-screen pb-20">
        <div className="max-w-2xl mx-auto p-6 space-y-6">
          <div className="flex items-center justify-between">
            <h1 className="text-2xl font-bold">Histórico</h1>
            <Link href="/transacao/nova">
              <Button size="icon" className="rounded-full">
                <Plus className="h-5 w-5" />
              </Button>
            </Link>
          </div>

          <div className="flex gap-2">
            <Button
                variant={filtro === "todos" ? "default" : "outline"}
                size="sm"
                onClick={() => setFiltro("todos")}
                className="flex-1"
            >
              Todos
            </Button>
            <Button
                variant={filtro === "INCOME" ? "default" : "outline"}
                size="sm"
                onClick={() => setFiltro("INCOME")}
                className="flex-1"
            >
              Receitas
            </Button>
            <Button
                variant={filtro === "EXPENSE" ? "default" : "outline"}
                size="sm"
                onClick={() => setFiltro("EXPENSE")}
                className="flex-1"
            >
              Gastos
            </Button>
          </div>

          <Card>
            <CardContent className="pt-6">
              {loading ? (
                  <div className="space-y-3">
                    {[...Array(5)].map((_, i) => (
                        <Skeleton key={i} className="h-16 w-full" />
                    ))}
                  </div>
              ) : transactions.length === 0 ? (
                  <p className="text-center text-muted-foreground py-8">
                    Nenhuma transação encontrada
                  </p>
              ) : (
                  <div className="space-y-3">
                    {transactions.map((transaction) => {
                      const category = transaction.category ?? "OTHER";
                      const Icon = TRANSACTION_ICON_MAP[category];
                      const categoryLabel = TRANSACTION_LABEL_MAP[category];
                      const isIncome = isIncomeTransaction(transaction.type);
                      const amount =
                          typeof transaction.amount === "number"
                              ? transaction.amount
                              : Number(transaction.amount ?? 0);

                      return (
                          <Link
                              key={transaction.id}
                              href={`/historico/${transaction.id}`}
                          >
                            <div className="flex items-center gap-4 p-3 rounded-lg hover:bg-secondary/50 transition-colors cursor-pointer">
                              <div
                                  className={cn(
                                      "h-10 w-10 rounded-full flex items-center justify-center",
                                      isIncome ? "bg-success/20" : "bg-destructive/20"
                                  )}
                              >
                                <Icon
                                    className={cn(
                                        "h-5 w-5",
                                        isIncome
                                            ? "text-success"
                                            : "text-destructive"
                                    )}
                                />
                              </div>

                              <div className="flex-1 min-w-0">
                                <p className="text-sm font-medium truncate">
                                  {transaction.description ?? "Sem descrição"}
                                </p>
                                <div className="flex items-center gap-2 text-xs text-muted-foreground">
                                  <span>{categoryLabel}</span>
                                  <span>•</span>
                                  <span>{formatDate(transaction.date)}</span>
                                </div>
                              </div>

                              <div
                                  className={cn(
                                      "text-sm font-semibold",
                                      isIncome ? "text-success" : "text-destructive"
                                  )}
                              >
                                {isIncome ? "+" : "-"} R${" "}
                                {formatCurrency(amount)}
                              </div>
                            </div>
                          </Link>
                      );
                    })}
                  </div>
              )}
            </CardContent>
          </Card>
        </div>

        <MainNav />
        <ChatbotButton />
      </div>
  );
}