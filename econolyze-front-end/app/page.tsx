import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Wallet, TrendingUp, Calendar, PieChart, Shield, Zap } from "lucide-react"
import { ParallaxHero } from "@/components/parallax-hero"

export default function HomePage() {
  return (
    <div className="min-h-screen">
      {/* Header Navigation */}
      <header className="border-b border-border bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 sticky top-0 z-50">
        <div className="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">
          <Link href="/" className="flex items-center gap-2">
            <Wallet className="h-5 w-5 text-primary" />
            <span className="font-semibold text-primary text-lg">Econolyze</span>
          </Link>
          <Link href="/carteira">
            <Button variant="outline" className="bg-transparent">
              Entrar no App
            </Button>
          </Link>
        </div>
      </header>

      {/* Parallax Hero Section */}
      <div className="relative">
        <ParallaxHero />

        <div className="absolute inset-0 flex items-center justify-center z-10">
          <div className="max-w-6xl mx-auto px-6 text-center space-y-6">
            <div className="flex items-center justify-center gap-3">
              <Wallet className="h-10 w-10 text-primary" />
              <h1 className="text-5xl font-bold text-primary">Econolyze</h1>
            </div>
            <h2 className="text-3xl md:text-4xl font-bold text-balance max-w-3xl mx-auto leading-tight">
              Controle Total das Suas Finanças em Um Só Lugar
            </h2>
            <p className="text-lg text-muted-foreground max-w-2xl mx-auto text-pretty">
              Gerencie transações, acompanhe gastos recorrentes e analise seus investimentos com uma interface moderna e
              intuitiva
            </p>

            <div className="flex gap-4 justify-center flex-wrap">
              <Link href="/registro">
                <Button size="lg" className="h-12 px-8 text-base">
                  Começar Agora
                </Button>
              </Link>
              <Link href="/login">
                <Button size="lg" variant="outline" className="h-12 px-8 text-base bg-background/80 backdrop-blur-sm">
                  Já Tenho Conta
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="bg-gradient-to-br from-background via-primary/5 to-accent/5">
        <div className="max-w-6xl mx-auto px-6 py-20">
          <h3 className="text-2xl font-bold text-center mb-12">Tudo Que Você Precisa Para Gerenciar Seu Dinheiro</h3>

          <div className="grid md:grid-cols-3 gap-8">
            <div className="space-y-3">
              <div className="h-12 w-12 rounded-lg bg-primary/10 flex items-center justify-center">
                <TrendingUp className="h-6 w-6 text-primary" />
              </div>
              <h4 className="text-xl font-semibold">Controle de Transações</h4>
              <p className="text-muted-foreground text-pretty">
                Registre receitas e gastos rapidamente com categorização automática e visualize seu saldo em tempo real
              </p>
            </div>

            <div className="space-y-3">
              <div className="h-12 w-12 rounded-lg bg-primary/10 flex items-center justify-center">
                <Calendar className="h-6 w-6 text-primary" />
              </div>
              <h4 className="text-xl font-semibold">Gastos Recorrentes</h4>
              <p className="text-muted-foreground text-pretty">
                Configure e acompanhe despesas fixas mensais como aluguel, assinaturas e contas para nunca perder um
                pagamento
              </p>
            </div>

            <div className="space-y-3">
              <div className="h-12 w-12 rounded-lg bg-primary/10 flex items-center justify-center">
                <PieChart className="h-6 w-6 text-primary" />
              </div>
              <h4 className="text-xl font-semibold">Análise de Investimentos</h4>
              <p className="text-muted-foreground text-pretty">
                Compare rendimentos de CDB vs CDI e tome decisões informadas sobre seus investimentos
              </p>
            </div>

            <div className="space-y-3">
              <div className="h-12 w-12 rounded-lg bg-primary/10 flex items-center justify-center">
                <Shield className="h-6 w-6 text-primary" />
              </div>
              <h4 className="text-xl font-semibold">Seguro e Privado</h4>
              <p className="text-muted-foreground text-pretty">
                Seus dados financeiros são protegidos com criptografia e você tem controle total da sua privacidade
              </p>
            </div>

            <div className="space-y-3">
              <div className="h-12 w-12 rounded-lg bg-primary/10 flex items-center justify-center">
                <Zap className="h-6 w-6 text-primary" />
              </div>
              <h4 className="text-xl font-semibold">Interface Rápida</h4>
              <p className="text-muted-foreground text-pretty">
                Design moderno e responsivo inspirado nos melhores aplicativos financeiros do mercado
              </p>
            </div>

            <div className="space-y-3">
              <div className="h-12 w-12 rounded-lg bg-primary/10 flex items-center justify-center">
                <Wallet className="h-6 w-6 text-primary" />
              </div>
              <h4 className="text-xl font-semibold">Uma Conta, Tudo Simples</h4>
              <p className="text-muted-foreground text-pretty">
                Sem complicações de múltiplas contas. Uma carteira centralizada para todas as suas transações
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* CTA Section */}
      <div className="bg-gradient-to-br from-primary/10 to-accent/5 py-20">
        <div className="max-w-4xl mx-auto px-6 text-center space-y-6">
          <h3 className="text-3xl font-bold text-balance">Pronto Para Ter Controle Total das Suas Finanças?</h3>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Crie sua conta gratuitamente e comece a gerenciar seu dinheiro de forma inteligente hoje mesmo
          </p>
          <Link href="/registro">
            <Button size="lg" className="h-12 px-8 text-base">
              Criar Conta Gratuita
            </Button>
          </Link>
        </div>
      </div>

      {/* Footer */}
      <div className="border-t border-border">
        <div className="max-w-6xl mx-auto px-6 py-8">
          <div className="flex flex-col md:flex-row items-center justify-between gap-4">
            <span className="font-semibold text-foreground">Econolyze</span>
            <p className="text-sm text-muted-foreground">© 2025 Econolyze. Sistema de Gestão Financeira Pessoal.</p>
          </div>
        </div>
      </div>
    </div>
  )
}
