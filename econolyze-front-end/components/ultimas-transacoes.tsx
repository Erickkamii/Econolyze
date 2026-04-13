"use client";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import type { Transaction } from "@/lib/types/transaction.types";
import {
    TRANSACTION_ICON_MAP,
    TRANSACTION_LABEL_MAP,
} from "@/lib/constants/transaction.constants";
import {
    isIncomeTransaction,
    formatCurrency,
} from "@/lib/utils/transaction-mappers";

type Props = {
    list?: Transaction[];
    loading?: boolean;
};

export function UltimasTransacoes({ list = [], loading = false }: Props) {
    if (loading) {
        return (
            <Card>
                <CardHeader>
                    <CardTitle className="text-lg">Últimas Transações</CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                    <Skeleton className="h-12 w-full" />
                    <Skeleton className="h-12 w-full" />
                </CardContent>
            </Card>
        );
    }

    return (
        <Card>
            <CardHeader>
                <CardTitle className="text-lg">Últimas Transações</CardTitle>
            </CardHeader>
            <CardContent>
                <div className="space-y-3">
                    {list.length === 0 ? (
                        <p className="text-muted-foreground text-center py-4">
                            Nenhuma transação encontrada.
                        </p>
                    ) : (
                        list.map((transaction) => {
                            const category = transaction.category ?? "OTHER";
                            const Icon = TRANSACTION_ICON_MAP[category];
                            const categoryLabel = TRANSACTION_LABEL_MAP[category];

                            const isIncome = isIncomeTransaction(transaction.type);

                            const amount =
                                typeof transaction.amount === "number"
                                    ? transaction.amount
                                    : Number(transaction.amount ?? 0);

                            return (
                                <div
                                    key={transaction.id}
                                    className="flex items-center gap-4 p-3 rounded-lg hover:bg-secondary/50 transition-colors"
                                >
                                    <div
                                        className={`h-10 w-10 rounded-full flex items-center justify-center ${
                                            isIncome ? "bg-success/20" : "bg-muted"
                                        }`}
                                    >
                                        <Icon
                                            className={`h-5 w-5 ${
                                                isIncome
                                                    ? "text-success"
                                                    : "text-muted-foreground"
                                            }`}
                                        />
                                    </div>

                                    <div className="flex-1 min-w-0">
                                        <p className="text-sm font-medium truncate">
                                            {transaction.description ?? "Sem descrição"}
                                        </p>
                                        <p className="text-xs text-muted-foreground">
                                            {categoryLabel}
                                        </p>
                                    </div>

                                    <div
                                        className={`text-sm font-semibold ${
                                            isIncome ? "text-success" : "text-foreground"
                                        }`}
                                    >
                                        {isIncome ? "+" : "-"} R$ {formatCurrency(amount)}
                                    </div>
                                </div>
                            );
                        })
                    )}
                </div>
            </CardContent>
        </Card>
    );
}