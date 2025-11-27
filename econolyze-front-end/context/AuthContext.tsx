"use client";

import { createContext, useContext, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import {jwtDecode} from "jwt-decode";

type AuthContextType = {
    token: string | null;
    isAuthenticated: boolean;
    logout: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [token, setToken] = useState<string | null>(null);
    const router = useRouter();

    useEffect(() => {
        const stored = localStorage.getItem("token");
        if (!stored) return;

        try {
            const { exp } = jwtDecode<{ exp: number }>(stored);

            if (exp * 1000 < Date.now()) {
                localStorage.removeItem("token");
                router.push("/login");
            } else {
                setToken(stored);
            }
        } catch {
            localStorage.removeItem("token");
            router.push("/login");
        }
    }, []);

    const logout = () => {
        localStorage.removeItem("token");
        setToken(null);
        router.push("/login");
    };

    return (
        <AuthContext.Provider value={{ token, isAuthenticated: !!token, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useAuth must be used within AuthProvider");
    return ctx;
};
