"use client";

import { use, useEffect, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { ArrowLeft } from "lucide-react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

import { MainNav } from "@/components/main-nav";
import { ChatbotButton } from "@/components/chatbot-button";

import { useAuth } from "@/context/auth.context";
import { TransactionService } from "@/lib/services/transaction.service";
import type { Transaction } from "@/lib/types/transaction.types";
import {
  TRANSACTION_ICON_MAP,
  TRANSACTION_LABEL_MAP,
} from "@/lib/constants/transaction.constants";
import { isIncomeTransaction, formatCurrency } from "@/lib/utils/transaction-mappers";

type PageProps = {
  params: Promise<{ id: string }>;
};

export default function TransacaoDetalhesPage({ params }: PageProps) {
  const router = useRouter();
  const { accessToken } = useAuth();
  const resolvedParams = use(params);
  const transactionId = parseInt(resolvedParams.id, 10);

  const [transaction, setTransaction] = useState<Transaction | null>(null);
  const [loading, setLoading] = useState(true);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    if (!accessToken || Number.isNaN(transactionId)) return;

    async function loadTransaction() {
      setLoading(true);
      try {
        const data = await TransactionService.getById(transactionId, accessToken);
        setTransaction(data);
      } catch (error: any) {
        toast.error(error?.message ?? "Erro ao carregar transacao");
        router.replace("/historico");
      } finally {
        setLoading(false);
      }
    }

    loadTransaction();
  }, [accessToken, transactionId, router]);

  async function handleDelete() {
    if (!accessToken || !transaction) return;

    setDeleting(true);
    try {
      await TransactionService.delete(transaction.id, accessToken);
      toast.success("Transacao excluida com sucesso!");
      router.replace("/historico");
    } catch (error: any) {
      toast.error(error?.message ?? "Erro ao excluir transacao");
      setDeleting(false);
    }
  }

  if (loading) {
    return (
      <div className="min-h-screen p-6 pb-32">
        <div className="mx-auto max-w-2xl space-y-6">
          <div className="flex items-center gap-3">
            <Skeleton className="h-10 w-10 rounded-md" />
            <Skeleton className="h-7 w-48" />
          </div>
          <Skeleton className="h-[420px] w-full rounded-2xl" />
        </div>
      </div>
    );
  }

  if (!transaction) return null;

  const category = transaction.category ?? "OTHER";
  const Icon = TRANSACTION_ICON_MAP[category];
  const isIncome = isIncomeTransaction(transaction.type);
  const amount = typeof transaction.amount === "number"
    ? transaction.amount
    : Number(transaction.amount ?? 0);

  return (
    <div className="relative mx-auto min-h-screen max-w-2xl p-6 pb-32">
      <div className="mb-6 flex items-center gap-3">
        <Button asChild variant="ghost" size="icon">
          <Link href="/historico" aria-label="Voltar para historico">
            <ArrowLeft className="h-5 w-5" />
          </Link>
        </Button>
        <div>
          <h1 className="text-xl font-semibold">Detalhes da Transacao</h1>
          <p className="text-sm text-muted-foreground">Revise, edite ou remova este lancamento.</p>
        </div>
      </div>

      <Card className="border-border/60 bg-card/95 shadow-xl backdrop-blur">
        <CardContent className="space-y-6 p-6">
          <div className="flex items-center gap-4">
            <div className="flex h-14 w-14 items-center justify-center rounded-full bg-secondary">
              <Icon className="h-7 w-7 text-primary" />
            </div>

            <div className="flex min-w-0 flex-col">
              <span className="truncate text-lg font-medium">{transaction.description ?? "Sem descricao"}</span>
              <span className="text-sm text-muted-foreground">{TRANSACTION_LABEL_MAP[category]}</span>
            </div>
          </div>

          <div className="flex items-center justify-between border-t border-border pt-4">
            <span className="text-muted-foreground">Valor</span>
            <span className={`text-2xl font-bold ${isIncome ? "text-success" : "text-destructive"}`}>
              {isIncome ? "+" : "-"} R$ {formatCurrency(amount)}
            </span>
          </div>

          <div className="flex items-center justify-between border-t border-border pt-4">
            <span className="text-muted-foreground">Data</span>
            <span>{transaction.date ? new Date(transaction.date).toLocaleDateString("pt-BR") : "-"}</span>
          </div>

          <div className="flex items-center justify-between border-t border-border pt-4">
            <span className="text-muted-foreground">Metodo</span>
            <span>{transaction.method ?? "-"}</span>
          </div>

          <div className="flex items-center justify-between border-t border-border pt-4">
            <span className="text-muted-foreground">Conta</span>
            <span>{transaction.accountId ? `#${transaction.accountId}` : "Nao informada"}</span>
          </div>

          {transaction.description && (
            <div className="border-t border-border pt-4">
              <span className="text-sm text-muted-foreground">Observacoes</span>
              <p className="mt-1 text-foreground">{transaction.description}</p>
            </div>
          )}

          <div className="space-y-3 border-t border-border pt-4">
            <Button asChild className="w-full" variant="secondary">
              <Link href={`/historico/${transaction.id}/editar`}>Editar Transacao</Link>
            </Button>

            <Button
              className="w-full"
              variant="destructive"
              disabled={deleting}
              onClick={handleDelete}
            >
              {deleting ? "Excluindo..." : "Excluir Transacao"}
            </Button>
          </div>
        </CardContent>
      </Card>

      <MainNav />
      <ChatbotButton />
    </div>
  );
}
