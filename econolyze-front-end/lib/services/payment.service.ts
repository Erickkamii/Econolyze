import { apiRequest } from "@/lib/services/api-client"
import type { PaymentResponse } from "@/lib/types/transaction.types"

export type CreatePaymentPayload = {
  transactionId: number;
  amount: number;
  method: string;
  paidAt: string;
  status: "COMPLETED";
  accountId: number;
}

export class PaymentService {
  private static readonly PAYMENT_PATH = "/financial/payment"

  private static ensureToken(accessToken: string | null) {
    if (!accessToken) {
      throw new Error("Access token is required")
    }
  }

  static create(payload: CreatePaymentPayload, accessToken: string | null) {
    this.ensureToken(accessToken)

    return apiRequest<PaymentResponse>(
      this.PAYMENT_PATH,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      },
      "Erro ao registrar pagamento",
    )
  }
}
