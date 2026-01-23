
export interface RegisterResponse {
    id: string;
    email: string;
    name: string;
    image: string | null;
    gender: 'MALE' | 'FEMALE' | 'OTHER';
    provider: 'LOCAL' | 'GOOGLE' | 'GITHUB';
}
export interface RegisterRequest {
    email: string;
    password: string;
    name: string;
    gender: 'MALE' | 'FEMALE' | 'OTHERS';
    image: string|null;
}

export interface User {
    email: string
    gender: string
    image: string
    name: string
    roles: string[]
}

export interface ApiError {
    errors: string[] | null;
    message: string;
    status: string;
    statusCode: number;
    timestamp: string;
}

export interface LoginResponse {
    email: string;
    jwt: string;
    refreshToken: string | null;
    expiresIn: number;
    tokenType: string;
}

export interface LoginRequest{
    email: string;
    password: string;
}