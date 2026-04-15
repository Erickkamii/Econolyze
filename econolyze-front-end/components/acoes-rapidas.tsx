import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Plus, Minus, Repeat, Wallet, Clock } from 'lucide-react'

export function AcoesRapidas() {
  return (
    <div className="space-y-3">
      <h2 className="text-lg font-semibold">Ações Rápidas</h2>

      <div className="grid grid-cols-3 gap-3">
        <Button
          asChild
          variant="outline"
          className="h-20 w-full flex-col gap-2 border-success/30 bg-transparent hover:bg-success/10"
        >
          <Link href="/transacao/nova?tipo=receita">
            <Plus className="h-5 w-5 text-success" />
            <span className="text-xs">Receita</span>
          </Link>
        </Button>

        <Button
          asChild
          variant="outline"
          className="h-20 w-full flex-col gap-2 border-destructive/30 bg-transparent hover:bg-destructive/10"
        >
          <Link href="/transacao/nova?tipo=gasto">
            <Minus className="h-5 w-5 text-destructive" />
            <span className="text-xs">Gasto</span>
          </Link>
        </Button>

        <Button
          asChild
          variant="outline"
          className="h-20 w-full flex-col gap-2 border-primary/30 bg-transparent hover:bg-primary/10"
        >
          <Link href="/recorrentes">
            <Repeat className="h-5 w-5 text-primary" />
            <span className="text-xs">Recorrentes</span>
          </Link>
        </Button>
      </div>

      <div className="grid grid-cols-2 gap-3">
        <Button
          asChild
          variant="outline"
          className="h-20 w-full flex-col gap-2 border-blue-500/30 bg-transparent hover:bg-blue-500/10"
        >
          <Link href="/contas">
            <Wallet className="h-5 w-5 text-blue-500" />
            <span className="text-xs">Contas</span>
          </Link>
        </Button>

        <Button
          asChild
          variant="outline"
          className="h-20 w-full flex-col gap-2 border-orange-500/30 bg-transparent hover:bg-orange-500/10"
        >
          <Link href="/pagamentos">
            <Clock className="h-5 w-5 text-orange-500" />
            <span className="text-xs">Em Aberto</span>
          </Link>
        </Button>
      </div>
    </div>
  )
}
