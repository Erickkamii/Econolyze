"use client";

import { use, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { ArrowLeft } from "lucide-react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";

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
    if (!accessToken || isNaN(transactionId)) return;

    async function loadTransaction() {
      setLoading(true);
      try {
        const data = await TransactionService.getById(transactionId, accessToken);
        setTransaction(data);
      } catch {
        toast.error("Erro ao carregar transação");
        router.push("/historico");
      } finally {
        setLoading(false);
      }
    }

    loadTransaction();
  }, [accessToken, transactionId, router]);

  if (!transaction) return null;

  const category = transaction.category ?? "OTHER";
  const Icon = TRANSACTION_ICON_MAP[category];

  const isIncome = isIncomeTransaction(transaction.type);
  const amount =
      typeof transaction.amount === "number"
          ? transaction.amount
          : Number(transaction.amount ?? 0);

  return (
      <div className="min-h-screen p-6 pb-32 max-w-2xl mx-auto relative">

        {/* HEADER */}
        <div className="flex items-center gap-3 mb-6">
          <button onClick={() => router.back()}>
            <ArrowLeft className="h-6 w-6 text-white" />
          </button>
          <h1 className="text-xl font-semibold text-white">
            Detalhes da Transação
          </h1>
        </div>

        {/* CARD */}
        <Card className="bg-[#0f0f11] border border-[#1a1a1d] rounded-2xl text-white shadow-xl">
          <CardContent className="p-6 space-y-6">

            {/* Ícone + Nome */}
            <div className="flex items-center gap-4">
              <div className="h-14 w-14 rounded-full bg-[#1c1c1f] flex items-center justify-center">
                <Icon className="h-7 w-7 text-gray-300" />
              </div>

              <div className="flex flex-col">
                <span className="text-lg font-medium">{transaction.description}</span>
                <span className="text-sm text-gray-400">
                {TRANSACTION_LABEL_MAP[category]}
              </span>
              </div>
            </div>

            {/* Valor */}
            <div className="flex justify-between items-center border-t border-[#1f1f23] pt-4">
              <span className="text-gray-400">Valor</span>
              <span
                  className={`text-2xl font-bold ${
                      isIncome ? "text-green-400" : "text-red-500"
                  }`}
              >
              {isIncome ? "+" : "-"} R$ {formatCurrency(amount)}
            </span>
            </div>

            {/* Data */}
            <div className="flex justify-between items-center border-t border-[#1f1f23] pt-4">
              <span className="text-gray-400">Data</span>
              <span>{transaction.date? new Date(transaction.date).toLocaleDateString("pt-BR"): "-"}</span>
            </div>

            {/* Hora */}
            {/*<div className="flex justify-between items-center border-t border-[#1f1f23] pt-4">*/}
            {/*  <span className="text-gray-400">Hora</span>*/}
            {/*  <span>*/}
            {/*  {new Date(transaction.date).toLocaleTimeString("pt-BR", {*/}
            {/*    hour: "2-digit",*/}
            {/*    minute: "2-digit",*/}
            {/*  })}*/}
            {/*</span>*/}
            {/*</div>*/}

            {/* Conta */}
            <div className="flex justify-between items-center border-t border-[#1f1f23] pt-4">
              <span className="text-gray-400">Conta</span>
              <span>{transaction.method ?? "-"}</span>
            </div>

            {/* Observações */}
            {transaction.description && (
                <div className="border-t border-[#1f1f23] pt-4">
                  <span className="text-gray-400 text-sm">Observações</span>
                  <p className="text-gray-200 mt-1">
                    {transaction.description}
                  </p>
                </div>
            )}

            {/* Botões */}
            <div className="space-y-3 pt-4 border-t border-[#1f1f23]">
              <Button className="w-full bg-[#1f1f23] hover:bg-[#2a2a2e]">
                Editar Transação
              </Button>

              <Button
                  className="w-full bg-red-600 hover:bg-red-700"
                  onClick={() => alert("Excluir")}
              >
                Excluir Transação
              </Button>
            </div>
          </CardContent>
        </Card>

        <MainNav />
        <ChatbotButton />
      </div>
  );
}
