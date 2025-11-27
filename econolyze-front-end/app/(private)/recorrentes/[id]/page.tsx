import { MainNav } from "@/components/main-nav"
import { RecorrenciaHistorico } from "@/components/recorrencia-historico"
import { ChatbotButton } from "@/components/chatbot-button"
import { Button } from "@/components/ui/button"
import { ArrowLeft } from "lucide-react"
import Link from "next/link"

export default function RecorrenciaDetalhePage({ params }: { params: { id: string } }) {
  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-5xl mx-auto p-6 space-y-6">
        <div className="flex items-center gap-4">
          <Link href="/recorrentes">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <h1 className="text-2xl font-bold">Histórico de Transações</h1>
        </div>

        <RecorrenciaHistorico recorrenciaId={params.id} />
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
