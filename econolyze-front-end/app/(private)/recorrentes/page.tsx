import { MainNav } from "@/components/main-nav"
import { RecorrentesList } from "@/components/recorrentes-list"
import { ChatbotButton } from "@/components/chatbot-button"
import { Button } from "@/components/ui/button"
import { ArrowLeft, Plus } from "lucide-react"
import Link from "next/link"

export default function RecorrentesPage() {
  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-5xl mx-auto p-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/carteira">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="h-5 w-5" />
              </Button>
            </Link>
            <h1 className="text-2xl font-bold">Transações Recorrentes</h1>
          </div>
          <Link href="/recorrentes/nova">
            <Button>
              <Plus className="h-4 w-4 mr-2" />
              Nova Recorrência
            </Button>
          </Link>
        </div>

        <RecorrentesList />
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
