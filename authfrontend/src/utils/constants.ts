
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
    id:string
    email: string
    gender: string
    image: string
    name: string
    enabled: boolean
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

export interface Sort {
    empty: boolean
    sorted: boolean
    unsorted: boolean
}

export interface Pageable {
    offset: number
    pageNumber: number
    pageSize: number
    paged: boolean
    unpaged: boolean
    sort: Sort
}

export interface PaginatedResponse<T> {
    content: T[]
    empty: boolean
    first: boolean
    last: boolean
    number: number
    numberOfElements: number
    pageable: Pageable
    size: number
    sort: Sort
    totalElements: number
    totalPages: number
}
