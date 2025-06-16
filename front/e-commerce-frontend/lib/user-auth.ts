// /lib/user-auth.ts
export interface LoginRequest {
    email: string
    password: string
}

export interface LoginResponse {
    access_token: string
    token_type: string
    expires_in: number
    userId: string
}

export interface RegisterRequest {
    email: string
    password: string
    firstName: string
    lastName: string
}

export interface UserProfile {
    email: string;
    userId: string;
    roles: string[];
}


export async function RegisterUser(user: RegisterRequest): Promise<void> {
    const res = await fetch("http://localhost:8085/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user),
    })
    if (!res.ok) {
        const error = await res.text()
        throw new Error(`Registration failed: ${error}`)
    }
}

export async function LoginUser(creds: LoginRequest): Promise<void> {
    const res = await fetch("http://localhost:8085/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(creds),
        credentials: "include",
    });

    if (!res.ok) {
        try {
            const errorBody = await res.json();
            throw new Error(errorBody.message || "Login failed");
        } catch (e) {
            const errorText = await res.text();
            throw new Error(errorText || "Login failed");
        }
    }
}



export async function GetCurrentUser(): Promise<UserProfile> {
    const res = await fetch("http://localhost:8085/api/auth/me", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
    });

    if (!res.ok) {
        throw new Error("User not authenticated");
    }

    const rawUserData = await res.json();

    const userProfile: UserProfile = {
        email: rawUserData.email,
        userId: rawUserData.userId,
        roles: rawUserData.roles || [],
    };

    return userProfile;
}

export async function LogoutUser(): Promise<void> {
    const res = await fetch("http://localhost:8085/api/auth/logout", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
    });

    if (!res.ok) {
        throw new Error("Logout failed");
    }
}
