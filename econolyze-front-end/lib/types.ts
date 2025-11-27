// lib/types.ts

// export type User = {
//   id: string;
//   name: string;
//   email?: string;
//   roles?: string[];
// };

export type LoginRequest = {
  username: string;
  password: string;
};

export type LoginResponse = {
  authToken: string;
  refreshToken: string;
  username?: string;
    // expiresIn?: number;
  // user?: User;
};

export type RegisterRequest = {
    username: string;
    email: string;
    password: string;
}

export type RegisterResponse = {
    message: string;
    username: string
};

export type RefreshRequest = {
  refreshToken: string;
};

export type RefreshResponse = {
  authToken: string;
  refreshToken?: string;
  username?: string;
  // expiresIn?: number;
  // user?: User;
};

export type LogoutRequest = {
  refreshToken: string;
};

export type ApiError = {
  status: number;
  message: string;
  details?: any;
};

