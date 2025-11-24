"use client";

import React, {
    createContext,
    useCallback,
    useContext,
    useEffect,
    useMemo,
    useState
} from "react";

import type { LoginRequest, LoginResponse, RefreshResponse } from "@/lib/types";

// BASE URL do backend
// const API_BASE = "http://localhost:8080/api";
const API_BASE = process.env.NEXT_PUBLIC_API_BASE_URL?.replace(/\/+$/, "") ?? "";
const AUTH_PATH = "/auth";
console.log("API_BASE carregado:", API_BASE);
console.log("Todas as env:", process.env.NEXT_PUBLIC_API_BASE_URL);

// Keys do localStorage
const STORAGE_ACCESS_KEY = "econolyze_access_token";
const STORAGE_REFRESH_KEY = "econolyze_refresh_token";

// ---------------------------
// TokenStore (localStorage)
// ---------------------------
export const tokenStore = {
    getAccess: () => (typeof window === "undefined" ? null : localStorage.getItem(STORAGE_ACCESS_KEY)),
    setAccess: (token: string | null) => {
        if (typeof window === "undefined") return;
        token === null
            ? localStorage.removeItem(STORAGE_ACCESS_KEY)
            : localStorage.setItem(STORAGE_ACCESS_KEY, token);
    },
    getRefresh: () => (typeof window === "undefined" ? null : localStorage.getItem(STORAGE_REFRESH_KEY)),
    setRefresh: (token: string | null) => {
        if (typeof window === "undefined") return;
        token === null
            ? localStorage.removeItem(STORAGE_REFRESH_KEY)
            : localStorage.setItem(STORAGE_REFRESH_KEY, token);
    },
    clear: () => {
        if (typeof window === "undefined") return;
        localStorage.removeItem(STORAGE_ACCESS_KEY);
        localStorage.removeItem(STORAGE_REFRESH_KEY);
    }
};

// ---------------------------
// POST helper
// ---------------------------
async function postAuth<T>(path: string, body?: any): Promise<T> {
    const url = `${API_BASE}${AUTH_PATH}${path}`;

    const res = await fetch(url, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body ?? {})
    });

    if (!res.ok) {
        const text = await res.text();
        let json;

        try {
            json = JSON.parse(text);
        } catch {
            json = text;
        }

        const err: any = new Error(json?.message ?? "Erro na requisição auth");
        err.status = res.status;
        err.details = json;
        throw err;
    }

    return (await res.json()) as T;
}

// ---------------------------
// High-level API
// ---------------------------

export async function login(payload: LoginRequest): Promise<LoginResponse> {
    const data = await postAuth<LoginResponse>("/login", payload);

    if (data.authToken) tokenStore.setAccess(data.authToken);
    if (data.refreshToken) tokenStore.setRefresh(data.refreshToken);

    return data;
}

export async function refresh(): Promise<RefreshResponse> {
    const refreshToken = tokenStore.getRefresh();
    if (!refreshToken) throw new Error("No refresh token");

    const data = await postAuth<RefreshResponse>("/refresh", { refreshToken });

    if (data.authToken) tokenStore.setAccess(data.authToken);
    if (data.refreshToken) tokenStore.setRefresh(data.refreshToken);

    return data;
}

export async function logout(): Promise<void> {
    const refreshToken = tokenStore.getRefresh();
    try {
        await postAuth("/logout", { refreshToken });
    } catch {}
    tokenStore.clear();
}

// ---------------------------
// Auth Context
// ---------------------------

type User = { username: string };

type AuthContextValue = {
    accessToken: string | null;
    user: User | null;
    login: (p: LoginRequest) => Promise<void>;
    refresh: () => Promise<void>;
    logout: () => Promise<void>;
    isAuthenticated: boolean;
};

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [accessToken, setAccessToken] = useState<string | null>(() => tokenStore.getAccess());
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        (async () => {
            if (!accessToken && tokenStore.getRefresh()) {
                try {
                    const r = await refresh();
                    if (r.username) setUser({ username: r.username });
                    setAccessToken(tokenStore.getAccess());
                } catch {
                    tokenStore.clear();
                }
            }
        })();
    }, []);

    // LOGIN
    const doLogin = useCallback(async (p: LoginRequest) => {
        const resp = await login(p);
        setAccessToken(resp.authToken ?? null);
        if (resp.username) setUser({ username: resp.username });
    }, []);

    // REFRESH
    const doRefresh = useCallback(async () => {
        const resp = await refresh();
        setAccessToken(resp.authToken ?? null);
        if (resp.username) setUser({ username: resp.username });
    }, []);

    // LOGOUT
    const doLogout = useCallback(async () => {
        await logout();
        setAccessToken(null);
        setUser(null);
    }, []);

    const value = useMemo(
        () => ({
            accessToken,
            user,
            login: doLogin,
            refresh: doRefresh,
            logout: doLogout,
            isAuthenticated: !!accessToken
        }),
        [accessToken, user, doLogin, doRefresh, doLogout]
    );

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// Hook
export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useAuth must be used within AuthProvider");
    return ctx;
}
