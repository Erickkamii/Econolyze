"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { CurrencyFormField, SelectFormField, TextFormField } from "@/components/form-fields";

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
          <form onSubmit={handleSubmit} className="form-shell">
            <TextFormField
                label="Nome da Conta"
                type="text"
                placeholder="Ex: Nubank, Banco Inter..."
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />

            <SelectFormField
                label="Tipo de Conta"
                value={type}
                onValueChange={(value) => setType(value as AccountType)}
                placeholder="Selecione o tipo"
                options={ACCOUNT_TYPES}
            />

            <CurrencyFormField
                label={isCreditCard ? "Fatura Atual" : "Saldo Atual"}
                value={actualBalance}
                onChange={setActualBalance}
                required
            />

            {isCreditCard && (
                <CurrencyFormField
                    label="Limite de Crédito"
                    value={creditLimit}
                    onChange={setCreditLimit}
                    required
                />
            )}

            <Button type="submit" className="w-full" size="lg">
              Criar Conta
            </Button>
          </form>
        </CardContent>
      </Card>
  );
}
