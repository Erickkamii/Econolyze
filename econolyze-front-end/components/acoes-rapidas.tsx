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
          className="h-20 w-full flex-col gap-2 border-success/25 bg-success/5 hover:bg-success/10 dark:border-success/40 dark:bg-success/[0.08] dark:hover:bg-success/[0.12]"
        >
          <Link href="/transacao/nova?tipo=receita">
            <Plus className="h-5 w-5 text-success" />
            <span className="text-xs">Receita</span>
          </Link>
        </Button>

        <Button
          asChild
          variant="outline"
          className="h-20 w-full flex-col gap-2 border-destructive/25 bg-destructive/5 hover:bg-destructive/10 dark:border-destructive/40 dark:bg-destructive/[0.08] dark:hover:bg-destructive/[0.12]"
        >
          <Link href="/transacao/nova?tipo=gasto">
            <Minus className="h-5 w-5 text-destructive" />
            <span className="text-xs">Gasto</span>
          </Link>
        </Button>

        <Button
          asChild
          variant="outline"
          className="h-20 w-full flex-col gap-2 border-primary/25 bg-primary/5 hover:bg-primary/10 dark:border-primary/40 dark:bg-primary/[0.08] dark:hover:bg-primary/[0.12]"
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
          className="h-20 w-full flex-col gap-2 border-blue-500/25 bg-blue-500/5 hover:bg-blue-500/10 dark:border-blue-500/40 dark:bg-blue-500/[0.08] dark:hover:bg-blue-500/[0.12]"
        >
          <Link href="/contas">
            <Wallet className="h-5 w-5 text-blue-500" />
            <span className="text-xs">Contas</span>
          </Link>
        </Button>

        <Button
          asChild
          variant="outline"
          className="h-20 w-full flex-col gap-2 border-orange-500/25 bg-orange-500/5 hover:bg-orange-500/10 dark:border-orange-500/40 dark:bg-orange-500/[0.08] dark:hover:bg-orange-500/[0.12]"
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
