import { MainNav } from "@/components/main-nav"
import { RecorrenciaForm } from "@/components/recorrencia-form"
import { ChatbotButton } from "@/components/chatbot-button"
import { Button } from "@/components/ui/button"
import { ArrowLeft } from "lucide-react"
import Link from "next/link"

export default function EditarRecorrenciaPage({ params }: { params: { id: string } }) {
  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-3xl mx-auto p-6 space-y-6">
        <div className="flex items-center gap-4">
          <Link href="/recorrentes">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <h1 className="text-2xl font-bold">Editar Recorrência</h1>
        </div>

        <RecorrenciaForm isEdit={true} />
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
