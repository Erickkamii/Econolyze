"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { ArrowLeft, Plus } from "lucide-react";
import { toast } from "sonner";

import { MainNav } from "@/components/main-nav";
import { ChatbotButton } from "@/components/chatbot-button";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

import { useAuth } from "@/context/auth.context";
import { AccountService } from "@/lib/services/account.service";
import type { Account } from "@/lib/types/account.types";
import {
  ACCOUNT_ICON_MAP,
  ACCOUNT_COLOR_MAP,
  ACCOUNT_LABEL_MAP,
} from "@/lib/constants/account-constants";
import { formatCurrency } from "@/lib/utils/transaction-mappers";

export default function ContasPage() {
  const { accessToken } = useAuth();
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!accessToken) return;

    async function loadAccounts() {
      setLoading(true);
      try {
        const data = await AccountService.getAll(accessToken);
        setAccounts(data);
      } catch (error: any) {
        toast.error(error.message ?? "Erro ao carregar contas");
      } finally {
        setLoading(false);
      }
    }

    loadAccounts();
  }, [accessToken]);

  const saldoTotal = accounts.reduce((acc, conta) => acc + conta.actualBalance, 0);

  return (
      <div className="min-h-screen pb-20">
        <div className="max-w-2xl mx-auto p-6 space-y-6">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <Link href="/carteira">
                <Button variant="ghost" size="icon">
                  <ArrowLeft className="h-5 w-5" />
                </Button>
              </Link>
              <h1 className="text-2xl font-bold">Minhas Contas</h1>
            </div>
            <Link href="/contas/nova">
              <Button size="icon" className="rounded-full">
                <Plus className="h-5 w-5" />
              </Button>
            </Link>
          </div>

          <Card className="bg-gradient-to-br from-primary/20 to-primary/5 border-primary/30">
            <CardContent className="pt-6">
              <p className="text-sm text-muted-foreground mb-2">Saldo Total</p>
              {loading ? (
                  <Skeleton className="h-9 w-48" />
              ) : (
                  <p className="text-3xl font-bold">
                    R$ {formatCurrency(saldoTotal)}
                  </p>
              )}
            </CardContent>
          </Card>

          <div className="space-y-3">
            {loading ? (
                [...Array(3)].map((_, i) => (
                    <Skeleton key={i} className="h-24 w-full" />
                ))
            ) : accounts.length === 0 ? (
                <Card>
                  <CardContent className="pt-6 text-center text-muted-foreground">
                    Nenhuma conta cadastrada
                  </CardContent>
                </Card>
            ) : (
                accounts.map((account) => {
                  const Icon = ACCOUNT_ICON_MAP[account.type];
                  const color = ACCOUNT_COLOR_MAP[account.type];
                  const label = ACCOUNT_LABEL_MAP[account.type];

                  return (
                      <Link href={`/contas/${account.id}`} key={account.id}>
                        <Card className="hover:bg-accent/50 transition-colors cursor-pointer">
                          <CardContent className="pt-6 flex items-center justify-between">
                            <div className="flex items-center gap-4">
                              <div
                                  className={`p-3 rounded-full bg-secondary ${color}`}
                              >
                                <Icon className="h-6 w-6" />
                              </div>
                              <div>
                                <p className="font-semibold">{account.name}</p>
                                <p className="text-sm text-muted-foreground">
                                  {label}
                                </p>
                              </div>
                            </div>
                            <div className="text-right">
                              <p className="text-lg font-bold">
                                R$ {formatCurrency(account.actualBalance)}
                              </p>
                            </div>
                          </CardContent>
                        </Card>
                      </Link>
                  );
                })
            )}
          </div>
        </div>

        <MainNav />
        <ChatbotButton />
      </div>
  );
}