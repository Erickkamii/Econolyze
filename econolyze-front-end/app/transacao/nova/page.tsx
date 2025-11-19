"use client"

import { MainNav } from "@/components/main-nav"
import { TransacaoForm } from "@/components/transacao-form"
import { ChatbotButton } from "@/components/chatbot-button"
import { ArrowLeft } from "lucide-react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { useSearchParams } from "next/navigation"
import { useEffect, useState } from "react"

export default function NovaTransacaoPage() {
  const searchParams = useSearchParams()
  const [tipoInicial, setTipoInicial] = useState<"receita" | "gasto" | undefined>(undefined)

  useEffect(() => {
    const tipo = searchParams.get("tipo")
    if (tipo === "receita" || tipo === "gasto") {
      setTipoInicial(tipo)
    }
  }, [searchParams])

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

        {tipoInicial !== undefined && <TransacaoForm tipoInicial={tipoInicial} />}
        {tipoInicial === undefined && <TransacaoForm />}
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
