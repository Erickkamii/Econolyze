"use client"

import { useEffect, useState } from "react"
import { MainNav } from "@/components/main-nav"
import { ChatbotButton } from "@/components/chatbot-button"
import { ArrowLeft, AlertCircle } from "lucide-react"
import Link from "next/link"
import { toast } from "sonner"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Skeleton } from "@/components/ui/skeleton"
import { useAuth } from "@/context/auth.context"
import { TRANSACTION_LABEL_MAP } from "@/lib/constants/transaction.constants"
import { TransactionService } from "@/lib/services/transaction.service"
import type { Transaction } from "@/lib/types/transaction.types"
import { formatCurrency } from "@/lib/utils/transaction-mappers"

const OPEN_STATUSES = ["PENDING", "PAID_PARTIALLY"] as const

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

function isOpenTransaction(transaction: Transaction) {
  if (transaction.status) {
    return OPEN_STATUSES.includes(transaction.status as (typeof OPEN_STATUSES)[number])
  }

  const amount = toNumber(transaction.amount)
  const remaining = getRemainingBalance(transaction)

  return amount > 0 && remaining > 0
}

export default function PagamentosPage() {
  const { accessToken } = useAuth()
  const [transactions, setTransactions] = useState<Transaction[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!accessToken) return

    async function loadOpenTransactions() {
      setLoading(true)

      try {
        const data = await TransactionService.getAll(accessToken, 0, 100)
        setTransactions(data.filter(isOpenTransaction))
      } catch (error: any) {
        toast.error(error.message ?? "Erro ao carregar transações em aberto")
      } finally {
        setLoading(false)
      }
    }

    loadOpenTransactions()
  }, [accessToken])

  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-2xl mx-auto p-6 space-y-6">
        <div className="flex items-center gap-4">
          <Link href="/carteira">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <h1 className="text-2xl font-bold">Transações em Aberto</h1>
        </div>

        <Card className="bg-destructive/10 border-destructive/30">
          <CardContent className="pt-6 flex items-start gap-3">
            <AlertCircle className="h-5 w-5 text-destructive mt-0.5 shrink-0" />
            <div>
              <p className="font-semibold text-destructive">Atenção</p>
              <p className="text-sm text-muted-foreground">
                Você possui {loading ? "..." : transactions.length} transações com pagamentos pendentes
              </p>
            </div>
          </CardContent>
        </Card>

        <div className="space-y-3">
          {loading ? (
            [...Array(3)].map((_, index) => (
              <Skeleton key={index} className="h-40 w-full rounded-lg" />
            ))
          ) : transactions.length === 0 ? (
            <Card>
              <CardContent className="pt-6 text-center text-muted-foreground">
                Nenhuma transação em aberto encontrada
              </CardContent>
            </Card>
          ) : (
            transactions.map((transaction) => {
            const amount = toNumber(transaction.amount)
            const paid = getPaidAmount(transaction)
            const remaining = getRemainingBalance(transaction)
            const paymentPercent = amount > 0 ? Math.min(100, (paid / amount) * 100) : 0
            const category = transaction.category ?? "OTHER"
            const categoryLabel = TRANSACTION_LABEL_MAP[category]
            const statusLabel = transaction.status === "PENDING" ? "Pendente" : "Pago parcialmente"
            const paymentsCount = transaction.payments?.length ?? 0

            return (
              <Link href={`/pagamentos/${transaction.id}`} key={transaction.id}>
                <Card className="hover:bg-accent/50 transition-colors cursor-pointer">
                  <CardContent className="pt-6 space-y-3">
                    <div className="flex items-start justify-between">
                      <div>
                        <p className="font-semibold">{transaction.description ?? "Transação sem descrição"}</p>
                        <p className="text-sm text-muted-foreground">
                          {categoryLabel}
                          {paymentsCount > 0 ? ` · ${paymentsCount} pagamento${paymentsCount > 1 ? "s" : ""}` : ""}
                        </p>
                      </div>
                      <Badge variant="outline" className="border-destructive/30 text-destructive">
                        {statusLabel}
                      </Badge>
                    </div>

                    <div className="space-y-2">
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground">Progresso</span>
                        <span className="font-medium">{paymentPercent.toFixed(0)}%</span>
                      </div>
                      <div className="w-full bg-secondary rounded-full h-2 overflow-hidden">
                        <div className="bg-primary h-full transition-all" style={{ width: `${paymentPercent}%` }} />
                      </div>
                      <div className="flex justify-between text-xs text-muted-foreground">
                        <span>Pago: R$ {formatCurrency(paid)}</span>
                        <span>Restante: R$ {formatCurrency(remaining)}</span>
                      </div>
                    </div>

                    <div className="pt-2">
                      <p className="text-lg font-bold text-destructive">
                        Total: R$ {formatCurrency(amount)}
                      </p>
                    </div>
                  </CardContent>
                </Card>
              </Link>
            )
            })
          )}
        </div>
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
