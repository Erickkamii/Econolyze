"use client";

import React, {
    createContext,
    useCallback,
    useContext,
    useEffect,
    useMemo,
    useRef,
    useState,
} from "react";
import { useRouter, usePathname } from "next/navigation";
import { jwtDecode } from "jwt-decode";
import { toast } from "sonner";

import { AuthService } from "@/lib/services/auth.service";
import { TokenStore } from "@/lib/utils/token-store";
import type {
    AuthContextValue,
    JwtPayload,
    LoginRequest,
    LoginResponse,
    RefreshResponse,
    RegisterRequest,
    User,
} from "@/lib/types/auth.types";

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
                                                                          children,
                                                                      }) => {
    const [accessToken, setAccessToken] = useState<string | null>(() =>
        TokenStore.getAccess()
    );
    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    const hasInitialized = useRef(false);

    const router = useRouter();
    const pathname = usePathname();

    const updateAuthStates = useCallback(
        (token: string, response: LoginResponse | RefreshResponse) => {
            TokenStore.setAccess(token);
            TokenStore.setRefresh(response.refreshToken ?? null);
            setAccessToken(token);

            try {
                const decoded = jwtDecode<JwtPayload>(token);
                if (decoded.username) {
                    setUser({ username: decoded.username });
                } else if (response.username) {
                    setUser({ username: response.username });
                }
            } catch {
                if (response.username) {
                    setUser({ username: response.username });
                }
            }
        },
        []
    );

    useEffect(() => {
        if (hasInitialized.current) return;
        hasInitialized.current = true;

        const initializeAuth = async () => {
            if (pathname.startsWith("/login")) {
                setIsLoading(false);
                return;
            }

            const token = TokenStore.getAccess();
            const refreshToken = TokenStore.getRefresh();

            if (!token && !refreshToken) {
                TokenStore.clear();
                setAccessToken(null);
                setUser(null);
                setIsLoading(false);
                return;
            }

            let needsRefresh = false;

            if (token) {
                try {
                    const decoded = jwtDecode<JwtPayload>(token);
                    const now = Date.now() / 1000;

                    if (decoded.exp && decoded.exp < now) {
                        needsRefresh = true;
                    } else {
                        if (decoded.username) {
                            setUser({ username: decoded.username });
                        }
                        setIsLoading(false);
                        return;
                    }
                } catch (error) {
                    needsRefresh = true;
                }
            } else if (refreshToken) {
                needsRefresh = true;
            }

            if (needsRefresh && refreshToken) {
                try {
                    const response = await AuthService.refresh();
                    updateAuthStates(response.authToken, response);
                } catch (error) {
                    TokenStore.clear();
                    setAccessToken(null);
                    setUser(null);
                    toast.warning("Sessão expirada. Faça login novamente.");
                    router.replace("/login");
                }
            }

            setIsLoading(false);
        };

        initializeAuth();
    }, []);


    const doLogin = useCallback(
        async (payload: LoginRequest) => {
            const response = await AuthService.login(payload);

            if (response.authToken) {
                updateAuthStates(response.authToken, response);
                router.push("/dashboard");
            } else {
                setAccessToken(null);
                setUser(null);
            }
        },
        [router, updateAuthStates]
    );

    const doRefresh = useCallback(async () => {
        const response = await AuthService.refresh();

        if (response.authToken) {
            updateAuthStates(response.authToken, response);
        } else {
            throw new Error("Refresh failed to return a new token.");
        }
    }, [updateAuthStates]);

    const doLogout = useCallback(async () => {
        try {
            await AuthService.logout();
        } catch {}

        TokenStore.clear();
        setAccessToken(null);
        setUser(null);

        window.location.href = "/login";
    }, []);

    const doRegister = useCallback(async (payload: RegisterRequest) => {
        return await AuthService.register(payload);
    }, []);

    const value = useMemo(
        () => ({
            accessToken,
            user,
            login: doLogin,
            refresh: doRefresh,
            logout: doLogout,
            register: doRegister,
            isAuthenticated: !!accessToken,
            isLoading,
        }),
        [accessToken, user, doLogin, doRefresh, doLogout, doRegister, isLoading]
    );

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};


export function useAuth(): AuthContextValue {
    const context = useContext(AuthContext);

    if (context === undefined) {
        throw new Error("useAuth must be used within AuthProvider");
    }

    return context;
}