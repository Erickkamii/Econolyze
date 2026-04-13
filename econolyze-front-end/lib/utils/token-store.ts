const STORAGE_ACCESS_KEY = "econolyze_access_token";
const STORAGE_REFRESH_KEY = "econolyze_refresh_token";


export class TokenStore {
    static getAccess(): string | null {
        if (typeof window === "undefined") return null;
        return localStorage.getItem(STORAGE_ACCESS_KEY);
    }

    static setAccess(token: string | null): void {
        if (typeof window === "undefined") return;

        if (token === null) {
            localStorage.removeItem(STORAGE_ACCESS_KEY);
        } else {
            localStorage.setItem(STORAGE_ACCESS_KEY, token);
        }
    }

    static getRefresh(): string | null {
        if (typeof window === "undefined") return null;
        return localStorage.getItem(STORAGE_REFRESH_KEY);
    }

    static setRefresh(token: string | null): void {
        if (typeof window === "undefined") return;

        if (token === null) {
            localStorage.removeItem(STORAGE_REFRESH_KEY);
        } else {
            localStorage.setItem(STORAGE_REFRESH_KEY, token);
        }
    }


    static clear(): void {
        if (typeof window === "undefined") return;
        localStorage.removeItem(STORAGE_ACCESS_KEY);
        localStorage.removeItem(STORAGE_REFRESH_KEY);
    }


    static hasTokens(): boolean {
        return !!(this.getAccess() || this.getRefresh());
    }
}