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
  const paidAmount = Number(transaction.paidAmount ?? transaction.initialPayment ?? 0);
  const remainingBalance = Number(transaction.remainingBalance ?? Math.max(amount - paidAmount, 0));
  const paymentProgress = amount > 0 ? Math.min(100, (paidAmount / amount) * 100) : 0;
  const payments = transaction.payments ?? [];

  const formatDate = (value: string | null | undefined) => {
    if (!value) return "-";
    return new Date(`${value}T00:00:00`).toLocaleDateString("pt-BR");
  };

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

          {(transaction.status === "PAID_PARTIALLY" || payments.length > 0) && (
            <div className="space-y-4 border-t border-border pt-4">
              <div className="grid grid-cols-2 gap-4">
                <div className="rounded-md border border-primary/20 bg-primary/5 p-3 dark:bg-primary/[0.08]">
                  <p className="text-xs text-muted-foreground">Já pago</p>
                  <p className="text-lg font-semibold text-primary">R$ {formatCurrency(paidAmount)}</p>
                </div>
                <div className="rounded-md border border-destructive/20 bg-destructive/5 p-3 dark:bg-destructive/[0.08]">
                  <p className="text-xs text-muted-foreground">Falta pagar</p>
                  <p className="text-lg font-semibold text-destructive">R$ {formatCurrency(remainingBalance)}</p>
                </div>
              </div>

              <div className="space-y-2">
                <div className="flex justify-between text-sm">
                  <span className="text-muted-foreground">Progresso do pagamento</span>
                  <span className="font-medium">{paymentProgress.toFixed(0)}%</span>
                </div>
                <div className="h-2 overflow-hidden rounded-full bg-secondary">
                  <div className="h-full rounded-full bg-primary transition-all" style={{ width: `${paymentProgress}%` }} />
                </div>
              </div>
            </div>
          )}

          {payments.length > 0 && (
            <div className="space-y-3 border-t border-border pt-4">
              <div className="flex items-center justify-between">
                <span className="font-semibold">Pagamentos</span>
                <span className="text-sm text-muted-foreground">
                  {payments.length} pagamento{payments.length === 1 ? "" : "s"}
                </span>
              </div>

              <div className="space-y-2">
                {payments.map((payment, index) => (
                  <div
                    key={`${payment.paidAt ?? "sem-data"}-${payment.amount ?? 0}-${index}`}
                    className="flex items-center justify-between rounded-md border border-border/70 bg-secondary/40 p-3"
                  >
                    <div>
                      <p className="font-medium">{payment.description ?? `Pagamento ${index + 1}`}</p>
                      <p className="text-xs text-muted-foreground">
                        {formatDate(payment.paidAt)} · {payment.method ?? "Método não informado"}
                      </p>
                    </div>
                    <div className="text-right">
                      <p className="font-semibold text-primary">R$ {formatCurrency(Number(payment.amount ?? 0))}</p>
                      {payment.status && <p className="text-xs text-muted-foreground">{payment.status}</p>}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

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
