"use client";

import { useAuth } from "@/context/auth.context";
import { redirect } from "next/navigation";
import { Skeleton } from "@/components/ui/skeleton";

export default function PrivateLayout({ children }: { children: React.ReactNode }) {
    const { isAuthenticated, isLoading } = useAuth();


    if (isLoading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <Skeleton className="h-10 w-40" />
                <p className="ml-2 text-muted-foreground">Carregando...</p>
            </div>
        );
    }

    if (!isAuthenticated) {
        redirect("/login");
    }
    return <>{children}</>;
}