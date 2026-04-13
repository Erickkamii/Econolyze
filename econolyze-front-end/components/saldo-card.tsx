"use client";

import { Card } from "@/components/ui/card";

export function SaldoCard({ saldo, loading }: { saldo: number; loading: boolean }) {
    return (
        <Card className="p-4">
            <div className="text-sm text-muted-foreground">Saldo Atual</div>

            {loading ? (
                <div className="h-6 w-24 bg-muted animate-pulse rounded"></div>
            ) : (
                <div className="text-2xl font-bold">
                    R$ {saldo?.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                </div>
            )}
        </Card>
    );
}
