"use client";

import { Suspense } from "react";
import { useSearchParams } from "next/navigation";
import Link from "next/link";
import { ArrowLeft } from "lucide-react";

import { MainNav } from "@/components/main-nav";
import { TransacaoForm } from "@/components/transacao-form";
import { ChatbotButton } from "@/components/chatbot-button";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";

function NovaTransacaoContent() {
    const searchParams = useSearchParams();
    const tipo = searchParams.get("tipo") as "receita" | "gasto" | null;

    return (
        <div className="min-h-screen pb-20">
            <div className="max-w-2xl mx-auto p-6 space-y-6">
                <div className="flex items-center gap-4">
                    <Link href="/carteira">
                        <Button variant="ghost" size="icon">
                            <ArrowLeft className="h-5 w-5" />
                        </Button>
                    </Link>
                    <h1 className="text-2xl font-bold">Registrar Transação</h1>
                </div>

                <TransacaoForm tipoInicial={tipo ?? "gasto"} />
            </div>

            <MainNav />
            <ChatbotButton />
        </div>
    );
}

export default function NovaTransacaoPage() {
    return (
        <Suspense
            fallback={
                <div className="min-h-screen pb-20">
                    <div className="max-w-2xl mx-auto p-6 space-y-6">
                        <div className="flex items-center gap-4">
                            <Skeleton className="h-10 w-10 rounded-md" />
                            <Skeleton className="h-8 w-48" />
                        </div>
                        <Skeleton className="h-96 w-full" />
                    </div>
                </div>
            }
        >
            <NovaTransacaoContent />
        </Suspense>
    );
}