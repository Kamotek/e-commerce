// Plik: /app/auth/sign-in/page.tsx

"use client"

import Link from "next/link"
import { useRouter } from "next/navigation"
import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Checkbox } from "@/components/ui/checkbox"
import { LoginRequest } from "@/lib/user-auth"
import { useAuth } from "@/contexts/AuthContext"

export default function SignInPage() {
  const router = useRouter()
  const { login } = useAuth()

  const [form, setForm] = useState<{ email: string; password: string }>({
    email: "",
    password: "",
  })
  const [rememberMe, setRememberMe] = useState<boolean>(false)

  const [error, setError] = useState<string>("")
  const [success, setSuccess] = useState<string>("")

  const [isSubmitting, setIsSubmitting] = useState<boolean>(false)

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    setError("")
    setSuccess("")
    setIsSubmitting(true)

    const creds: LoginRequest = {
      email: form.email,
      password: form.password,
    }

    try {
      await login(creds)

      setSuccess("Logged in successfully. Redirecting…")

      setTimeout(() => {
        router.push("/catalog")
      }, 1500)
    } catch (err: any) {
      setError(err.message || "Login failed")
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
      <div className="flex min-h-screen">
        <div
            className="hidden lg:block lg:w-1/2 bg-cover bg-left relative"
            style={{
              backgroundImage:
                  'url("https://image.coolblue.nl/content/3ac262b7142ab454fd52c56f068f721e")',
            }}
        >
          <div className="absolute inset-0 bg-primary/10 flex flex-col justify-between p-12 text-primary-foreground">
          </div>
        </div>

        <div className="w-full lg:w-1/2 flex items-center justify-center p-8">
          <div className="w-full max-w-md space-y-8">
            <div>
              <h1 className="text-2xl font-semibold tracking-tight">
                Sign in to your account
              </h1>
              <p className="text-sm text-muted-foreground mt-2">
                Enter your credentials to access your account
              </p>
            </div>

            <div className="space-y-4">
              {error && (
                  <div className="p-2 bg-red-100 text-red-700 rounded">
                    {error}
                  </div>
              )}
              {success && (
                  <div className="p-2 bg-green-100 text-green-700 rounded">
                    {success}
                  </div>
              )}
            </div>

            <form onSubmit={handleSubmit} className="space-y-6">
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                    id="email"
                    type="email"
                    placeholder="m@example.com"
                    required
                    value={form.email}
                    onChange={(e) =>
                        setForm((prev) => ({ ...prev, email: e.target.value }))
                    }
                />
              </div>

              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <Label htmlFor="password">Password</Label>
                  <Link
                      href="/auth/forgot-password"
                      className="text-sm text-primary underline-offset-4 hover:underline"
                  >
                    Forgot password?
                  </Link>
                </div>
                <Input
                    id="password"
                    type="password"
                    required
                    value={form.password}
                    onChange={(e) =>
                        setForm((prev) => ({ ...prev, password: e.target.value }))
                    }
                />
              </div>

              <div className="flex items-center space-x-2">
                <Checkbox
                    id="remember"
                    checked={rememberMe}
                    onCheckedChange={(checked) => setRememberMe(Boolean(checked))}
                />
                <label
                    htmlFor="remember"
                    className="text-sm font-medium leading-none"
                >
                  Remember me
                </label>
              </div>

              <Button className="w-full" type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Signing in…" : "Sign In"}
              </Button>

              <p className="text-center text-sm text-muted-foreground">
                Don't have an account?{" "}
                <Link
                    href="/auth/sign-up"
                    className="text-primary underline-offset-4 hover:underline"
                >
                  Sign up
                </Link>
              </p>
            </form>
          </div>
        </div>
      </div>
  )
}