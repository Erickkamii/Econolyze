"use client";

import React, { useEffect, useState } from "react";
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
import type {
    CreateTransactionPayload,
    Transaction,
    UpdateTransactionPayload,
} from "@/lib/types/transaction.types";
import {
    FORM_CATEGORIES,
    PAYMENT_METHODS,
} from "@/lib/constants/transaction.constants";
import {
    mapAccountId,
    mapCategoryFromBackend,
    mapCategoryToBackend,
    mapTypeFromBackend,
    mapTypeToBackend,
    toCurrencyInputValue,
} from "@/lib/utils/transaction-mappers";

type Props = {
    tipoInicial?: "receita" | "gasto";
    mode?: "create" | "edit";
    transactionId?: number;
};

export function TransacaoForm({
    tipoInicial = "gasto",
    mode = "create",
    transactionId,
}: Props) {
    const router = useRouter();
    const { accessToken } = useAuth();
    const isEditMode = mode === "edit";

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
    const [depsLoading, setDepsLoading] = useState(true);
    const [depsError, setDepsError] = useState<string | null>(null);
    const [initialLoading, setInitialLoading] = useState(isEditMode);
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        if (!accessToken) return;

        async function loadDependencies() {
            setDepsLoading(true);
            setDepsError(null);

            try {
                const data = await TransactionService.getDependencies(accessToken);
                setDeps(data);
            } catch (error: any) {
                const message = error.message ?? "Erro ao carregar dependencias.";
                setDepsError(message);
                toast.error(message);
            } finally {
                setDepsLoading(false);
            }
        }

        loadDependencies();
    }, [accessToken]);

    useEffect(() => {
        if (!isEditMode || !transactionId || !accessToken) {
            setInitialLoading(false);
            return;
        }

        const currentTransactionId = transactionId;

        async function loadTransaction() {
            setInitialLoading(true);

            try {
                const transaction = await TransactionService.getById(currentTransactionId, accessToken);
                hydrateForm(transaction);
            } catch (error: any) {
                toast.error(error.message ?? "Erro ao carregar transacao.");
                router.replace(`/historico/${currentTransactionId}`);
            } finally {
                setInitialLoading(false);
            }
        }

        loadTransaction();
    }, [accessToken, isEditMode, router, transactionId]);

    function hydrateForm(transaction: Transaction) {
        setTipo(mapTypeFromBackend(transaction.type));
        setValor(toCurrencyInputValue(transaction.amount));
        setDescricao(transaction.description ?? "");
        setCategoria(mapCategoryFromBackend(transaction.category));
        setMetodo(transaction.method ?? "PIX");
        setConta(transaction.accountId ? String(transaction.accountId) : "");
        setRecorrente(Boolean(transaction.isRecurring));
        setPagamentoParcial(Boolean(transaction.initialPayment && Number(transaction.initialPayment) > 0));
        setValorPago(toCurrencyInputValue(transaction.initialPayment ?? 0));
        setMeta(transaction.financialGoalId ? String(transaction.financialGoalId) : "");
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!accessToken) {
            toast.error("Voce precisa estar autenticado.");
            return;
        }

        const amount = (Number.parseInt(valor) || 0) / 100;
        const initialPayment = pagamentoParcial
            ? (Number.parseInt(valorPago) || 0) / 100
            : 0;

        const payload: CreateTransactionPayload | UpdateTransactionPayload = {
            amount,
            category: mapCategoryToBackend(categoria),
            type: mapTypeToBackend(tipo),
            date: new Date().toISOString().split("T")[0],
            description: descricao,
            accountId: mapAccountId(conta),
            financialGoalId: meta ? Number(meta) : null,
            recurringTemplateId: null,
            isRecurring: recorrente,
            method: (metodo as CreateTransactionPayload["method"]) || "PIX",
            initialPayment,
        };

        try {
            setIsSubmitting(true);

            if (isEditMode && transactionId) {
                await TransactionService.update(transactionId, payload, accessToken);
                toast.success("Transacao atualizada com sucesso!");
                router.replace(`/historico/${transactionId}`);
                return;
            }

            await TransactionService.create(payload, accessToken);
            toast.success("Transacao registrada com sucesso!");
            router.replace("/carteira");
        } catch (error: any) {
            toast.error(error.message ?? (isEditMode ? "Erro ao atualizar transacao." : "Erro ao registrar transacao."));
        } finally {
            setIsSubmitting(false);
        }
    };

    if (initialLoading) {
        return (
            <Card className="w-full">
                <CardContent className="space-y-4 pt-6">
                    <div className="h-10 w-full animate-pulse rounded-md bg-muted" />
                    <div className="h-10 w-full animate-pulse rounded-md bg-muted" />
                    <div className="h-10 w-full animate-pulse rounded-md bg-muted" />
                    <div className="h-10 w-full animate-pulse rounded-md bg-muted" />
                </CardContent>
            </Card>
        );
    }

    return (
        <Card className="w-full">
            <CardContent className="pt-6 w-full">
                <form onSubmit={handleSubmit} className="space-y-6 w-full">
                    {depsError && (
                        <div className="rounded-lg border border-destructive/30 bg-destructive/5 px-4 py-3 text-sm text-destructive" role="alert">
                            {depsError}
                        </div>
                    )}

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

                    <div className="space-y-2 w-full">
                        <Label>Valor Total</Label>
                        <CurrencyInput
                            value={valor}
                            onChange={setValor}
                            required
                            className="w-full"
                        />
                    </div>

                    <div className="space-y-2 w-full">
                        <Label>Descricao</Label>
                        <Input
                            type="text"
                            placeholder="Ex: Almoco no restaurante"
                            value={descricao}
                            onChange={(e) => setDescricao(e.target.value)}
                            className="bg-secondary w-full"
                            required
                        />
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 w-full">
                        <div className="space-y-2 w-full">
                            <Label>Categoria</Label>
                            <Select value={categoria} onValueChange={setCategoria}>
                                <SelectTrigger className="bg-secondary w-full" disabled={depsLoading}>
                                    <SelectValue placeholder={depsLoading ? "Carregando..." : "Selecione"} />
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

                        <div className="space-y-2 w-full">
                            <Label>Metodo de Pagamento</Label>
                            <Select value={metodo} onValueChange={setMetodo}>
                                <SelectTrigger className="bg-secondary w-full" disabled={depsLoading}>
                                    <SelectValue placeholder={depsLoading ? "Carregando..." : "Selecione"} />
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

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 w-full">
                        <div className="space-y-2 w-full">
                            <Label>Conta / Origem</Label>
                            <Select value={conta} onValueChange={setConta}>
                                <SelectTrigger className="bg-secondary w-full" disabled={depsLoading}>
                                    <SelectValue placeholder={depsLoading ? "Carregando contas..." : "Selecione"} />
                                </SelectTrigger>
                                <SelectContent className="w-full">
                                    {deps?.accounts?.length ? (
                                        deps.accounts.map((acc) => (
                                            <SelectItem key={acc.id} value={String(acc.id)}>
                                                {acc.name} - R$ {acc.actualBalance.toLocaleString("pt-BR")}
                                            </SelectItem>
                                        ))
                                    ) : (
                                        <SelectItem disabled value="no-accounts">
                                            Nenhuma conta disponivel
                                        </SelectItem>
                                    )}
                                </SelectContent>
                            </Select>
                        </div>

                        <div className="space-y-2 w-full">
                            <Label>Meta</Label>
                            <Select value={meta} onValueChange={setMeta}>
                                <SelectTrigger className="bg-secondary w-full" disabled={depsLoading}>
                                    <SelectValue placeholder={depsLoading ? "Carregando metas..." : "Opcional"} />
                                </SelectTrigger>
                                <SelectContent className="w-full">
                                    {deps?.goals?.length ? (
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
                                    {tipo === "receita" ? "Recebimento Parcial" : "Pagamento Parcial"}
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
                                    {tipo === "receita" ? "Valor Recebido Agora" : "Valor Pago Agora"}
                                </Label>
                                <CurrencyInput
                                    value={valorPago}
                                    onChange={setValorPago}
                                    max={(Number.parseInt(valor) || 0) / 100}
                                    required
                                    className="w-full"
                                />
                                <p className="text-xs text-muted-foreground">
                                    O restante ficara em aberto
                                </p>
                            </div>
                        )}
                    </div>

                    <Button type="submit" className="w-full" size="lg" disabled={depsLoading || isSubmitting}>
                        {isSubmitting
                            ? isEditMode
                                ? "Salvando alteracoes..."
                                : "Salvando transacao..."
                            : isEditMode
                                ? "Salvar Alteracoes"
                                : "Salvar Transacao"}
                    </Button>
                </form>
            </CardContent>
        </Card>
    );
}
