"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { CurrencyInput } from "@/components/currency-input";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent } from "@/components/ui/card";
import { Switch } from "@/components/ui/switch";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";

import { useAuth } from "@/context/auth.context";
import { TransactionService } from "@/lib/services/transaction.service";
import type { DependenciesResponse } from "@/lib/types/account.types";
import type { CreateTransactionPayload } from "@/lib/types/transaction.types";
import {
    FORM_CATEGORIES,
    PAYMENT_METHODS,
} from "@/lib/constants/transaction.constants";
import {
    mapCategoryToBackend,
    mapTypeToBackend,
    mapAccountId,
} from "@/lib/utils/transaction-mappers";

type Props = {
    tipoInicial?: "receita" | "gasto";
};

export function TransacaoForm({ tipoInicial = "gasto" }: Props) {
    const router = useRouter();
    const { accessToken } = useAuth();

    const [tipo, setTipo] = useState<"receita" | "gasto">(tipoInicial);
    const [valor, setValor] = useState("");
    const [descricao, setDescricao] = useState("");
    const [categoria, setCategoria] = useState("");
    const [metodo, setMetodo] = useState("");
    const [conta, setConta] = useState("");
    const [recorrente, setRecorrente] = useState(false);
    const [pagamentoParcial, setPagamentoParcial] = useState(false);
    const [meta, setMeta] = useState("");
    const [valorPago, setValorPago] = useState("");

    const [deps, setDeps] = useState<DependenciesResponse | null>(null);

    useEffect(() => {
        if (!accessToken) return;

        async function loadDependencies() {
            try {
                const data = await TransactionService.getDependencies(accessToken);
                setDeps(data);
            } catch (error: any) {
                toast.error(error.message ?? "Erro ao carregar dependências.");
            }
        }

        loadDependencies();
    }, [accessToken]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!accessToken) {
            toast.error("Você precisa estar autenticado.");
            return;
        }

        const amount = (Number.parseInt(valor) || 0) / 100;
        const initialPayment = pagamentoParcial
            ? (Number.parseInt(valorPago) || 0) / 100
            : 0;

        const payload: CreateTransactionPayload = {
            amount,
            category: mapCategoryToBackend(categoria),
            type: mapTypeToBackend(tipo),
            date: new Date().toISOString().split("T")[0],
            description: descricao,
            accountId: mapAccountId(conta),
            financialGoalId: meta ? Number(meta) : null,
            recurringTemplateId: null,
            isRecurring: recorrente,
            method: (metodo as any) || "PIX",
            initialPayment,
        };

        try {
            await TransactionService.create(payload, accessToken);
            toast.success("Transação registrada com sucesso!");
            router.replace("/carteira");
        } catch (error: any) {
            toast.error(error.message ?? "Erro ao registrar transação.");
        }
    };

    return (
        <Card className="w-full">
            <CardContent className="pt-6 w-full">
                <form onSubmit={handleSubmit} className="space-y-6 w-full">
                    {/* Tipo */}
                    <div className="flex gap-2 w-full">
                        <Button
                            type="button"
                            variant={tipo === "receita" ? "default" : "outline"}
                            className="flex-1"
                            onClick={() => setTipo("receita")}
                        >
                            Receita
                        </Button>
                        <Button
                            type="button"
                            variant={tipo === "gasto" ? "default" : "outline"}
                            className="flex-1"
                            onClick={() => setTipo("gasto")}
                        >
                            Gasto
                        </Button>
                    </div>

                    {/* Valor */}
                    <div className="space-y-2 w-full">
                        <Label>Valor Total</Label>
                        <CurrencyInput
                            value={valor}
                            onChange={setValor}
                            required
                            className="w-full"
                        />
                    </div>

                    {/* Descrição */}
                    <div className="space-y-2 w-full">
                        <Label>Descrição</Label>
                        <Input
                            type="text"
                            placeholder="Ex: Almoço no restaurante"
                            value={descricao}
                            onChange={(e) => setDescricao(e.target.value)}
                            className="bg-secondary w-full"
                            required
                        />
                    </div>

                    {/* Categoria e Método */}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 w-full">
                        {/* Categoria */}
                        <div className="space-y-2 w-full">
                            <Label>Categoria</Label>
                            <Select value={categoria} onValueChange={setCategoria}>
                                <SelectTrigger className="bg-secondary w-full">
                                    <SelectValue placeholder="Selecione" />
                                </SelectTrigger>
                                <SelectContent>
                                    {FORM_CATEGORIES.map((cat) => (
                                        <SelectItem key={cat.value} value={cat.value}>
                                            {cat.label}
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>

                        {/* Método */}
                        <div className="space-y-2 w-full">
                            <Label>Método de Pagamento</Label>
                            <Select value={metodo} onValueChange={setMetodo}>
                                <SelectTrigger className="bg-secondary w-full">
                                    <SelectValue placeholder="Selecione" />
                                </SelectTrigger>
                                <SelectContent>
                                    {PAYMENT_METHODS.map((method) => (
                                        <SelectItem key={method.value} value={method.value}>
                                            {method.label}
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>
                    </div>

                    {/* Conta e Meta */}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 w-full">
                        {/* Conta */}
                        <div className="space-y-2 w-full">
                            <Label>Conta / Origem</Label>
                            <Select value={conta} onValueChange={setConta}>
                                <SelectTrigger className="bg-secondary w-full">
                                    <SelectValue placeholder="Selecione" />
                                </SelectTrigger>
                                <SelectContent className="w-full">
                                    {deps?.accounts.map((acc) => (
                                        <SelectItem key={acc.id} value={String(acc.id)}>
                                            {acc.name} — R${" "}
                                            {acc.actualBalance.toLocaleString("pt-BR")}
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>

                        {/* Meta */}
                        <div className="space-y-2 w-full">
                            <Label>Meta</Label>
                            <Select value={meta} onValueChange={setMeta}>
                                <SelectTrigger className="bg-secondary w-full">
                                    <SelectValue placeholder="Opcional" />
                                </SelectTrigger>
                                <SelectContent className="w-full">
                                    {deps?.goals.length ? (
                                        deps.goals.map((g) => (
                                            <SelectItem key={g.id} value={String(g.id)}>
                                                {g.name}
                                            </SelectItem>
                                        ))
                                    ) : (
                                        <SelectItem disabled value="none">
                                            Nenhuma meta cadastrada
                                        </SelectItem>
                                    )}
                                </SelectContent>
                            </Select>
                        </div>
                    </div>

                    {/* Recorrência e Pagamento Parcial */}
                    <div className="space-y-4 pt-2 w-full">
                        <div className="flex items-center justify-between w-full">
                            <Label className="cursor-pointer">Tornar Recorrente</Label>
                            <Switch
                                checked={recorrente}
                                onCheckedChange={setRecorrente}
                            />
                        </div>

                        <div className="flex items-center justify-between w-full">
                            <div>
                                <Label className="cursor-pointer">
                                    {tipo === "receita"
                                        ? "Recebimento Parcial"
                                        : "Pagamento Parcial"}
                                </Label>
                                <p className="text-xs text-muted-foreground">
                                    {tipo === "receita"
                                        ? "Registrar apenas parte do recebimento agora"
                                        : "Registrar apenas parte do pagamento agora"}
                                </p>
                            </div>

                            <Switch
                                checked={pagamentoParcial}
                                onCheckedChange={setPagamentoParcial}
                            />
                        </div>

                        {pagamentoParcial && (
                            <div className="space-y-2 pl-4 border-l-2 border-primary/30 w-full">
                                <Label>
                                    {tipo === "receita"
                                        ? "Valor Recebido Agora"
                                        : "Valor Pago Agora"}
                                </Label>
                                <CurrencyInput
                                    value={valorPago}
                                    onChange={setValorPago}
                                    max={(Number.parseInt(valor) || 0) / 100}
                                    required
                                    className="w-full"
                                />
                                <p className="text-xs text-muted-foreground">
                                    O restante ficará em aberto
                                </p>
                            </div>
                        )}
                    </div>

                    {/* Botão */}
                    <Button type="submit" className="w-full" size="lg">
                        Salvar Transação
                    </Button>
                </form>
            </CardContent>
        </Card>
    );
}