"use client"

import { useEffect, useState } from "react"
import { MainNav } from "@/components/main-nav"
import { ChatbotButton } from "@/components/chatbot-button"
import {
    ArrowLeft,
    CreditCard,
    Wallet,
    Banknote,
    PiggyBank,
    Landmark,
    TrendingUp,
    Pencil,
    Trash2,
    Check,
    X
} from "lucide-react"
import Link from "next/link"
import { use } from "react"
import { useRouter } from "next/navigation"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue
} from "@/components/ui/select"

import { Account, AccountType } from "@/lib/types/account.types"
import { AccountService } from "@/lib/services/account.service"
import { useAuth } from "@/context/auth.context"
import { CurrencyInput } from "@/components/currency-input"

const iconMap = {
    CHECKING_ACCOUNT: Landmark,
    SAVINGS_ACCOUNT: PiggyBank,
    CREDIT_CARD: CreditCard,
    INVESTMENT_ACCOUNT: Wallet,
    MONEY: Banknote,
}

const typeLabels = {
    CHECKING_ACCOUNT: "Conta Corrente",
    SAVINGS_ACCOUNT: "Poupança",
    CREDIT_CARD: "Cartão de Crédito",
    INVESTMENT_ACCOUNT: "Investimento",
    MONEY: "Dinheiro",
}

export default function ContaDetailPage({
                                            params,
                                        }: {
    params: Promise<{ id: string }>
}) {
    const { accessToken } = useAuth()
    const { id } = use(params)
    const router = useRouter()

    const [account, setAccount] = useState<Account | null>(null)
    const [loading, setLoading] = useState(true)
    const [salvando, setSalvando] = useState(false)
    const [deletando, setDeletando] = useState(false)
    const [confirmDelete, setConfirmDelete] = useState(false)

    const [editando, setEditando] = useState(false)
    const [nome, setNome] = useState("")
    const [tipo, setTipo] = useState<AccountType>("CHECKING_ACCOUNT")
    const [saldo, setSaldo] = useState("")

    useEffect(() => {
        if (!accessToken) return

        async function fetchAccount() {
            try {
                const data = await AccountService.getById(Number(id), accessToken)
                setAccount(data)
                setNome(data.name)
                setTipo(data.type)
                setSaldo(String(Math.round(Number(data.actualBalance) * 100)))
            } catch (error) {
                console.error("Erro ao buscar conta:", error)
            } finally {
                setLoading(false)
            }
        }

        fetchAccount()
    }, [id, accessToken])

    if (loading) return null
    if (!account) return null

    const Icon = iconMap[account.type]

    function handleCancelar() {
        setNome(account!.name)
        setTipo(account!.type)
        setSaldo(String(Math.round(account!.actualBalance * 100)))
        setEditando(false)
    }

    async function handleSalvar() {
        if (!account) return
        setSalvando(true)

        try {
            await AccountService.update(
                account.id,
                {
                    name: nome,
                    type: tipo,
                    actualBalance: (Number(saldo) || 0) / 100,
                    creditLimit: account.creditLimit,
                },
                accessToken
            )

            setAccount((prev) =>
                prev ? { ...prev, name: nome, type: tipo, actualBalance: (Number(saldo) || 0) / 100 } : prev
            )
            setEditando(false)
        } catch (error) {
            console.error("Erro ao salvar conta:", error)
        } finally {
            setSalvando(false)
        }
    }

    async function handleDelete() {
        if (!account) return
        setDeletando(true)

        try {
            await AccountService.delete(account.id, accessToken)
            router.push("/contas")
        } catch (error) {
            console.error("Erro ao deletar conta:", error)
            setDeletando(false)
            setConfirmDelete(false)
        }
    }

    return (
        <div className="min-h-screen bg-black pb-24">
            <div className="max-w-2xl mx-auto p-6 space-y-6 text-white">

                {/* HEADER */}
                <div className="flex items-center justify-between">
                    <div className="flex items-center gap-4">
                        <Link href="/contas">
                            <ArrowLeft className="h-5 w-5 cursor-pointer" />
                        </Link>

                        <h1 className="text-xl font-semibold">
                            {editando ? "Editar Conta" : account.name}
                        </h1>
                    </div>

                    {!editando && (
                        <div className="flex items-center gap-3">
                            <button
                                onClick={() => setEditando(true)}
                                className="opacity-70 hover:opacity-100"
                            >
                                <Pencil className="h-4 w-4" />
                            </button>

                            <button
                                onClick={() => setConfirmDelete(true)}
                                className="opacity-70 hover:opacity-100"
                            >
                                <Trash2 className="h-4 w-4 text-pink-500" />
                            </button>
                        </div>
                    )}
                </div>

                {/* CONFIRM DELETE */}
                {confirmDelete && (
                    <Card className="border border-pink-800/50 bg-[#1a0a0a]">
                        <CardContent className="pt-6 space-y-4">
                            <p className="text-sm text-gray-300">
                                Tem certeza que deseja excluir a conta{" "}
                                <span className="font-semibold text-white">{account.name}</span>?
                            </p>

                            <div className="flex gap-3">
                                <button
                                    onClick={handleDelete}
                                    disabled={deletando}
                                    className="flex items-center gap-2 bg-pink-700 hover:bg-pink-600 disabled:opacity-50 px-4 py-2 rounded-lg text-sm"
                                >
                                    <Trash2 className="h-4 w-4" />
                                    {deletando ? "Excluindo..." : "Confirmar"}
                                </button>

                                <button
                                    onClick={() => setConfirmDelete(false)}
                                    disabled={deletando}
                                    className="flex items-center gap-2 border border-gray-700 hover:border-gray-500 disabled:opacity-50 px-4 py-2 rounded-lg text-sm"
                                >
                                    <X className="h-4 w-4" />
                                    Cancelar
                                </button>
                            </div>
                        </CardContent>
                    </Card>
                )}

                {editando ? (

                    /* FORM EDIT */
                    <Card className="rounded-2xl border border-blue-900/40 bg-gradient-to-br from-[#06202f] to-[#04131d] shadow-xl">
                        <CardContent className="pt-6 space-y-5">

                            <div className="space-y-2">
                                <Label className="text-gray-300 text-sm">Nome da Conta</Label>
                                <Input
                                    value={nome}
                                    onChange={(e) => setNome(e.target.value)}
                                    className="bg-[#0d2c3f] border-blue-900/40 text-white"
                                />
                            </div>

                            <div className="space-y-2">
                                <Label className="text-gray-300 text-sm">Tipo de Conta</Label>
                                <Select value={tipo} onValueChange={(v) => setTipo(v as AccountType)}>
                                    <SelectTrigger className="bg-[#0d2c3f] border-blue-900/40 text-white">
                                        <SelectValue />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="CHECKING_ACCOUNT">Conta Corrente</SelectItem>
                                        <SelectItem value="SAVINGS_ACCOUNT">Poupança</SelectItem>
                                        <SelectItem value="CREDIT_CARD">Cartão de Crédito</SelectItem>
                                        <SelectItem value="INVESTMENT_ACCOUNT">Investimento</SelectItem>
                                        <SelectItem value="MONEY">Dinheiro</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>

                            <div className="space-y-2">
                                <Label className="text-gray-300 text-sm">Saldo Atual</Label>
                                <CurrencyInput
                                    value={saldo}
                                    onChange={setSaldo}
                                    className="bg-[#0d2c3f] border-blue-900/40 text-white"
                                />
                            </div>

                            <div className="flex gap-3 pt-1">
                                <button
                                    onClick={handleSalvar}
                                    disabled={salvando}
                                    className="flex-1 flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-500 disabled:opacity-50 py-2.5 rounded-lg text-sm font-medium"
                                >
                                    <Check className="h-4 w-4" />
                                    {salvando ? "Salvando..." : "Salvar"}
                                </button>

                                <button
                                    onClick={handleCancelar}
                                    disabled={salvando}
                                    className="flex-1 flex items-center justify-center gap-2 bg-[#0d2c3f] border border-blue-900/40 hover:border-blue-700 disabled:opacity-50 py-2.5 rounded-lg text-sm font-medium"
                                >
                                    <X className="h-4 w-4" />
                                    Cancelar
                                </button>
                            </div>

                        </CardContent>
                    </Card>

                ) : (

                    /* VIEW NORMAL */
                    <Card className="rounded-2xl border border-blue-900/40 bg-gradient-to-br from-[#06202f] to-[#04131d] shadow-xl">
                        <CardContent className="pt-6">
                            <div className="flex items-center justify-between">
                                <div>
                                    <div className="flex items-center gap-3 mb-4">
                                        <div className="p-3 rounded-full bg-[#0d2c3f]">
                                            <Icon className="h-5 w-5 text-blue-400" />
                                        </div>
                                        <span className="text-sm text-gray-400">
                                            {typeLabels[account.type]}
                                        </span>
                                    </div>

                                    <p className="text-sm text-gray-400">Saldo Atual</p>

                                    <p className="text-3xl font-bold">
                                        R$ {account.actualBalance.toLocaleString("pt-BR", {
                                        minimumFractionDigits: 2,
                                    })}
                                    </p>

                                    {account.type === "CREDIT_CARD" && account.creditLimit != null && (
                                        <p className="text-sm text-gray-400 mt-2">
                                            Limite: R$ {account.creditLimit.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                                        </p>
                                    )}
                                </div>

                                <TrendingUp className="h-8 w-8 text-blue-400 opacity-60" />
                            </div>
                        </CardContent>
                    </Card>

                )}

            </div>

            <MainNav />
            <ChatbotButton />
        </div>
    )
}