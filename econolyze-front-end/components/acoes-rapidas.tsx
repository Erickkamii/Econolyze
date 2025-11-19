import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Plus, Minus, Repeat, Wallet, Clock } from 'lucide-react'

export function AcoesRapidas() {
  return (
    <div className="space-y-3">
      <h2 className="text-lg font-semibold">Ações Rápidas</h2>

      <div className="grid grid-cols-3 gap-3">
        {/* <CHANGE> Updated links to /transacao/nova */}
        <Link href="/transacao/nova?tipo=receita">
          <Button
            variant="outline"
            className="w-full h-20 flex flex-col gap-2 border-success/30 hover:bg-success/10 bg-transparent"
          >
            <Plus className="h-5 w-5 text-success" />
            <span className="text-xs">Receita</span>
          </Button>
        </Link>

        <Link href="/transacao/nova?tipo=gasto">
          <Button
            variant="outline"
            className="w-full h-20 flex flex-col gap-2 border-destructive/30 hover:bg-destructive/10 bg-transparent"
          >
            <Minus className="h-5 w-5 text-destructive" />
            <span className="text-xs">Gasto</span>
          </Button>
        </Link>

        <Link href="/recorrentes">
          <Button
            variant="outline"
            className="w-full h-20 flex flex-col gap-2 border-primary/30 hover:bg-primary/10 bg-transparent"
          >
            <Repeat className="h-5 w-5 text-primary" />
            <span className="text-xs">Recorrentes</span>
          </Button>
        </Link>
      </div>

      <div className="grid grid-cols-2 gap-3">
        <Link href="/contas">
          <Button
            variant="outline"
            className="w-full h-20 flex flex-col gap-2 border-blue-500/30 hover:bg-blue-500/10 bg-transparent"
          >
            <Wallet className="h-5 w-5 text-blue-500" />
            <span className="text-xs">Contas</span>
          </Button>
        </Link>

        <Link href="/pagamentos">
          <Button
            variant="outline"
            className="w-full h-20 flex flex-col gap-2 border-orange-500/30 hover:bg-orange-500/10 bg-transparent"
          >
            <Clock className="h-5 w-5 text-orange-500" />
            <span className="text-xs">Em Aberto</span>
          </Button>
        </Link>
      </div>
    </div>
  )
}
