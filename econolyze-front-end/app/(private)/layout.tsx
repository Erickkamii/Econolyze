"use client";

import { useAuth } from "@/context/auth.context";
import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { Skeleton } from "@/components/ui/skeleton";
import { PrivateHeader } from "@/components/private-header";

export default function PrivateLayout({ children }: { children: React.ReactNode }) {
    const { isAuthenticated, isLoading } = useAuth();
    const router = useRouter();

    useEffect(() => {
        if (!isLoading && !isAuthenticated) {
            router.replace("/login");
        }
    }, [isAuthenticated, isLoading, router]);

    if (isLoading || !isAuthenticated) {
        return (
            <div className="flex min-h-screen items-center justify-center px-6">
                <div className="flex w-full max-w-sm items-center gap-3 rounded-xl border border-border/60 bg-card/80 p-4 shadow-sm backdrop-blur">
                    <Skeleton className="h-10 w-10 rounded-full" />
                    <div className="flex-1 space-y-2">
                        <Skeleton className="h-4 w-32" />
                        <p className="text-sm text-muted-foreground">
                            Validando sua sessão...
                        </p>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <>
            <PrivateHeader />
            {children}
        </>
    );
}
