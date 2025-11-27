import { MainNav } from "@/components/main-nav"
import { SaldoCard } from "@/components/saldo-card"
import { AcoesRapidas } from "@/components/acoes-rapidas"
import { UltimasTransacoes } from "@/components/ultimas-transacoes"
import { ChatbotButton } from "@/components/chatbot-button"

export default function CarteiraPage() {
  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-5xl mx-auto p-6 space-y-6">
        <div className="flex items-center justify-between mb-8">
          <h1 className="text-2xl font-bold text-primary">Econolyze</h1>
        </div>

        <SaldoCard saldo={15847.32} />
        <AcoesRapidas />

        <UltimasTransacoes />
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
