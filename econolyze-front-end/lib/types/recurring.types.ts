export type RecurringType = "INCOME" | "EXPENSE"

export type RecurringFrequency = "DAILY" | "WEEKLY" | "MONTHLY" | "YEARLY"

export type RecurringTemplate = {
    id: number
    amount: number
    type: RecurringType
    category: string
    method: string
    description: string
    frequency: RecurringFrequency
    startDate: string
    endDate: string | null
    maxOccurrences: number | null
    nextOccurrence: string | null
    timesProcessed: number
    isActive: boolean
    transactions: RecurringTransaction[]
}

export type RecurringTransaction = {
    id: number
    amount: number
    date: string
    description: string
    status: string
}

export type CreateRecurringPayload = {
    amount: number
    type: RecurringType
    category: string
    paymentMethod: string
    description: string
    frequency: RecurringFrequency
    startDate: string
    endDate: string | null
    maxOccurrences: number | null
}

export type UpdateRecurringPayload = {
    id: number
    amount: number
    category: string
    method: string
    description: string
    endDate: string | null
    maxOccurrences: number | null
}