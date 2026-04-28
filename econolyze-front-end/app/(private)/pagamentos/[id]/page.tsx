"use client"

import { use, useEffect, useState } from "react"
import { ArrowLeft } from "lucide-react"
import Link from "next/link"
import { toast } from "sonner"

import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Skeleton } from "@/components/ui/skeleton"
import { MainNav } from "@/components/main-nav"
import { ChatbotButton } from "@/components/chatbot-button"
import { PagamentoForm } from "@/components/pagamento-form"
import { useAuth } from "@/context/auth.context"
import { TRANSACTION_LABEL_MAP } from "@/lib/constants/transaction.constants"
import { TransactionService } from "@/lib/services/transaction.service"
import type { Transaction } from "@/lib/types/transaction.types"
import { formatCurrency } from "@/lib/utils/transaction-mappers"

type PageProps = {
  params: Promise<{ id: string }>
}

function toNumber(value: number | string | null | undefined) {
  return typeof value === "number" ? value : Number(value ?? 0)
}

function getPaidAmount(transaction: Transaction) {
  const amount = toNumber(transaction.amount)

  if (transaction.paidAmount !== undefined && transaction.paidAmount !== null) {
    return Math.min(toNumber(transaction.paidAmount), amount)
  }

  if (transaction.status === "PAID") return amount
  if (transaction.status === "PENDING") return 0

  return Math.min(toNumber(transaction.initialPayment), amount)
}

function getRemainingBalance(transaction: Transaction) {
  if (transaction.remainingBalance !== undefined && transaction.remainingBalance !== null) {
    return Math.max(toNumber(transaction.remainingBalance), 0)
  }

  return Math.max(toNumber(transaction.amount) - getPaidAmount(transaction), 0)
}

function formatDate(value: string | null | undefined) {
  if (!value) return "-"
  return new Date(`${value}T00:00:00`).toLocaleDateString("pt-BR")
}

export default function NovoPagamentoPage({ params }: PageProps) {
  const { accessToken } = useAuth()
  const resolvedParams = use(params)
  const transactionId = Number(resolvedParams.id)

  const [transaction, setTransaction] = useState<Transaction | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!accessToken || Number.isNaN(transactionId)) return

    async function loadTransaction() {
      setLoading(true)

      try {
        const data = await TransactionService.getById(transactionId, accessToken)
        setTransaction(data)
      } catch (error: any) {
        toast.error(error.message ?? "Erro ao carregar transação")
      } finally {
        setLoading(false)
      }
    }

    loadTransaction()
  }, [accessToken, transactionId])

  if (loading) {
    return (
      <div className="min-h-screen pb-20">
        <div className="max-w-2xl mx-auto p-6 space-y-6">
          <Skeleton className="h-10 w-56" />
          <Skeleton className="h-48 w-full rounded-lg" />
          <Skeleton className="h-72 w-full rounded-lg" />
        </div>
      </div>
    )
  }

  if (!transaction) return null

  const amount = toNumber(transaction.amount)
  const paid = getPaidAmount(transaction)
  const remaining = getRemainingBalance(transaction)
  const paymentPercent = amount > 0 ? Math.min(100, (paid / amount) * 100) : 0
  const category = transaction.category ?? "OTHER"
  const payments = transaction.payments ?? []

  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-2xl mx-auto p-6 space-y-6">
        <div className="flex items-center gap-4">
          <Link href="/pagamentos">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <h1 className="text-2xl font-bold">Registrar Pagamento</h1>
        </div>

        <Card className="border-primary/25 bg-primary/5 dark:bg-primary/[0.08]">
          <CardContent className="pt-6 space-y-3">
            <div className="flex justify-between items-start gap-4">
              <div>
                <p className="text-sm text-muted-foreground">Transação</p>
                <p className="text-lg font-semibold">{transaction.description ?? "Transação sem descrição"}</p>
                <p className="text-sm text-muted-foreground">{TRANSACTION_LABEL_MAP[category]}</p>
              </div>
              <div className="text-right">
                <p className="text-sm text-muted-foreground">Valor Total</p>
                <p className="text-lg font-bold">R$ {formatCurrency(amount)}</p>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4 pt-2">
              <div>
                <p className="text-sm text-muted-foreground">Já Pago</p>
                <p className="text-base font-semibold text-primary">R$ {formatCurrency(paid)}</p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground">Restante</p>
                <p className="text-base font-semibold text-destructive">R$ {formatCurrency(remaining)}</p>
              </div>
            </div>

            <div className="space-y-2 pt-2">
              <div className="flex justify-between text-sm">
                <span className="text-muted-foreground">Progresso do pagamento</span>
                <span className="font-medium">{paymentPercent.toFixed(0)}%</span>
              </div>
              <div className="h-2 overflow-hidden rounded-full bg-secondary">
                <div className="h-full rounded-full bg-primary transition-all" style={{ width: `${paymentPercent}%` }} />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="pt-6">
            <div className="mb-4 flex items-center justify-between">
              <h2 className="font-semibold">Pagamentos realizados</h2>
              <span className="text-sm text-muted-foreground">
                {payments.length} pagamento{payments.length === 1 ? "" : "s"}
              </span>
            </div>

            {payments.length === 0 ? (
              <p className="text-sm text-muted-foreground">Nenhum pagamento registrado ainda.</p>
            ) : (
              <div className="space-y-3">
                {payments.map((payment, index) => (
                  <div
                    key={`${payment.transactionId}-${payment.paidAt ?? index}-${index}`}
                    className="flex items-center justify-between rounded-md border border-border/70 bg-secondary/40 p-3"
                  >
                    <div>
                      <p className="font-medium">
                        {payment.description || `Pagamento ${index + 1}`}
                      </p>
                      <p className="text-xs text-muted-foreground">
                        {formatDate(payment.paidAt)} · {payment.method ?? "Método não informado"}
                      </p>
                    </div>
                    <div className="text-right">
                      <p className="font-semibold text-primary">R$ {formatCurrency(toNumber(payment.amount))}</p>
                      {payment.status && (
                        <p className="text-xs text-muted-foreground">{payment.status}</p>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>

        <PagamentoForm transacaoId={transaction.id} valorRestante={remaining} />
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
