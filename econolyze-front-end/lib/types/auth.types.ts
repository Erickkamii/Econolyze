// JWT Payload
export type JwtPayload = {
    exp: number;
    sub: string;
    username: string;
};

// User
export type User = {
    username: string;
};

// Login
export type LoginRequest = {
    username: string;
    password: string;
};

export type LoginResponse = {
    authToken: string;
    refreshToken: string;
    username?: string;
};

// Register
export type RegisterRequest = {
    username: string;
    password: string;
    email?: string;
};

export type RegisterResponse = {
    message: string;
    username?: string;
};

// Refresh
export type RefreshResponse = {
    authToken: string;
    refreshToken: string;
    username?: string;
};

// Auth Context
export type AuthContextValue = {
    accessToken: string | null;
    user: User | null;
    login: (payload: LoginRequest) => Promise<void>;
    register: (payload: RegisterRequest) => Promise<RegisterResponse>;
    refresh: () => Promise<void>;
    logout: () => Promise<void>;
    isAuthenticated: boolean;
    isLoading: boolean;
};