"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { CurrencyInput } from "@/components/currency-input";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent } from "@/components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

import { useAuth } from "@/context/auth.context";
import { AccountService } from "@/lib/services/account.service";
import type { CreateAccountPayload, AccountType } from "@/lib/types/account.types";
import { ACCOUNT_TYPES } from "@/lib/constants/account-constants";

export function ContaForm() {
  const router = useRouter();
  const { accessToken } = useAuth();

  const [name, setName] = useState("");
  const [type, setType] = useState<AccountType | "">("");
  const [actualBalance, setActualBalance] = useState("");
  const [creditLimit, setCreditLimit] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!accessToken) {
      toast.error("Você precisa estar autenticado.");
      return;
    }

    if (!type) {
      toast.error("Selecione o tipo de conta.");
      return;
    }

    const payload: CreateAccountPayload = {
      name,
      type: type as AccountType,
      actualBalance: (Number.parseInt(actualBalance) || 0) / 100,
      creditLimit: (Number.parseInt(creditLimit) || 0) / 100,
    };

    try {
      await AccountService.create(payload, accessToken);
      toast.success("Conta criada com sucesso!");
      router.push("/contas");
    } catch (error: any) {
      toast.error(error.message ?? "Erro ao criar conta.");
    }
  };

  const isCreditCard = type === "CREDIT_CARD";

  return (
      <Card className="w-full">
        <CardContent className="pt-6 w-full">
          <form onSubmit={handleSubmit} className="space-y-6 w-full">
            {/* Nome */}
            <div className="space-y-2 w-full">
              <Label>Nome da Conta</Label>
              <Input
                  type="text"
                  placeholder="Ex: Nubank, Banco Inter..."
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  className="bg-secondary w-full"
                  required
              />
            </div>

            {/* Tipo */}
            <div className="space-y-2 w-full">
              <Label>Tipo de Conta</Label>
              <Select
                  value={type}
                  onValueChange={(value) => setType(value as AccountType)}
              >
                <SelectTrigger className="bg-secondary w-full">
                  <SelectValue placeholder="Selecione o tipo" />
                </SelectTrigger>
                <SelectContent>
                  {ACCOUNT_TYPES.map((accountType) => (
                      <SelectItem
                          key={accountType.value}
                          value={accountType.value}
                      >
                        {accountType.label}
                      </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* Saldo Atual */}
            <div className="space-y-2 w-full">
              <Label>
                {isCreditCard ? "Fatura Atual" : "Saldo Atual"}
              </Label>
              <CurrencyInput
                  value={actualBalance}
                  onChange={setActualBalance}
                  required
                  className="w-full"
              />
            </div>

            {/* Limite de Crédito (só para cartão) */}
            {isCreditCard && (
                <div className="space-y-2 w-full">
                  <Label>Limite de Crédito</Label>
                  <CurrencyInput
                      value={creditLimit}
                      onChange={setCreditLimit}
                      required
                      className="w-full"
                  />
                </div>
            )}

            {/* Botão */}
            <Button type="submit" className="w-full" size="lg">
              Criar Conta
            </Button>
          </form>
        </CardContent>
      </Card>
  );
}