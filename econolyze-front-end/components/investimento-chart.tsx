"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts"

const dados = [
  { mes: "Jan", cdb: 10000, cdi: 10000 },
  { mes: "Fev", cdb: 10350, cdi: 10280 },
  { mes: "Mar", cdb: 10715, cdi: 10565 },
  { mes: "Abr", cdb: 11095, cdi: 10855 },
  { mes: "Mai", cdb: 11490, cdi: 11150 },
  { mes: "Jun", cdb: 11900, cdi: 11450 },
  { mes: "Jul", cdb: 12325, cdi: 11755 },
  { mes: "Ago", cdb: 12765, cdi: 12065 },
  { mes: "Set", cdb: 13220, cdi: 12380 },
  { mes: "Out", cdb: 13690, cdi: 12700 },
  { mes: "Nov", cdb: 14175, cdi: 13025 },
  { mes: "Dez", cdb: 14675, cdi: 13355 },
]

export function InvestimentoChart() {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Evolução do Investimento</CardTitle>
      </CardHeader>
      <CardContent>
        <ResponsiveContainer width="100%" height={350}>
          <LineChart data={dados}>
            <CartesianGrid strokeDasharray="3 3" stroke="hsl(var(--border))" />
            <XAxis dataKey="mes" stroke="hsl(var(--muted-foreground))" style={{ fontSize: "12px" }} />
            <YAxis
              stroke="hsl(var(--muted-foreground))"
              style={{ fontSize: "12px" }}
              tickFormatter={(value) => `R$ ${(value / 1000).toFixed(0)}k`}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: "hsl(var(--popover))",
                border: "1px solid hsl(var(--border))",
                borderRadius: "8px",
                color: "hsl(var(--popover-foreground))",
              }}
              formatter={(value: number) => [`R$ ${value.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`, ""]}
            />
            <Legend />
            <Line
              type="monotone"
              dataKey="cdb"
              stroke="#22c55e"
              strokeWidth={2}
              name="CDB"
              dot={{ fill: "#22c55e", r: 4 }}
            />
            <Line
              type="monotone"
              dataKey="cdi"
              stroke="#ef4444"
              strokeWidth={2}
              name="CDI"
              dot={{ fill: "#ef4444", r: 4 }}
              strokeDasharray="5 5"
            />
          </LineChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  )
}
