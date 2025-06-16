// Plik: /contexts/AuthContext.tsx

"use client"

import { createContext, useContext, useState, useEffect, ReactNode } from "react"
import { useRouter } from "next/navigation"
import { GetCurrentUser, LoginUser, LogoutUser, LoginRequest } from "@/lib/user-auth"

interface User {
    email: string
    userId: string
    roles: string[]
}

interface AuthContextType {
    user: User | null
    isLoading: boolean
    login: (creds: LoginRequest) => Promise<void>
    logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<User | null>(null)
    const [isLoading, setIsLoading] = useState(true)
    const router = useRouter()

    const checkAuthStatus = async () => {
        try {
            const currentUser = await GetCurrentUser()
            setUser(currentUser)
        } catch (error) {
            setUser(null)
        } finally {
            setIsLoading(false)
        }
    }

    useEffect(() => {
        checkAuthStatus()
    }, [])

    const login = async (creds: LoginRequest) => {
        await LoginUser(creds)
        await checkAuthStatus()
    }

    const logout = async () => {
        await LogoutUser()
        setUser(null)
        router.push("/auth/sign-in")
        router.refresh()
    }

    const value = { user, isLoading, login, logout }

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
    const context = useContext(AuthContext)
    if (context === undefined) {
        throw new Error("useAuth must be used within an AuthProvider")
    }
    return context
}