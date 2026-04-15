"use client";

import { use } from "react";
import Link from "next/link";
import { ArrowLeft } from "lucide-react";

import { MainNav } from "@/components/main-nav";
import { TransacaoForm } from "@/components/transacao-form";
import { ChatbotButton } from "@/components/chatbot-button";
import { Button } from "@/components/ui/button";

type PageProps = {
  params: Promise<{ id: string }>;
};

export default function EditarTransacaoPage({ params }: PageProps) {
  const resolvedParams = use(params);
  const transactionId = Number(resolvedParams.id);

  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-2xl mx-auto p-6 space-y-6">
        <div className="flex items-center gap-4">
          <Button asChild variant="ghost" size="icon">
            <Link href={`/historico/${transactionId}`} aria-label="Voltar para detalhes da transacao">
              <ArrowLeft className="h-5 w-5" />
            </Link>
          </Button>
          <div>
            <h1 className="text-2xl font-bold">Editar Transacao</h1>
            <p className="text-sm text-muted-foreground">Atualize os dados e salve com seguranca.</p>
          </div>
        </div>

        <TransacaoForm mode="edit" transactionId={transactionId} />
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  );
}
