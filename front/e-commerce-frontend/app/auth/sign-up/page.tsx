"use client"

import Link from "next/link"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useState } from "react"
import { RegisterRequest, RegisterUser } from "@/lib/user-auth"


export default function SignUpPage() {
  const router = useRouter()

  const [form, setForm] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    firstName: "",
    lastName: "",
  })
  const [error, setError] = useState<string>("")
  const [success, setSuccess] = useState<string>("")
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false)

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>): Promise<void> => {
    e.preventDefault()

    setError("")
    setSuccess("")

    if (form.password !== form.confirmPassword) {
      setError("Passwords don't match")
      return
    }

    setIsSubmitting(true)

    const registerRequest: RegisterRequest = {
      email: form.email,
      password: form.password,
      firstName: form.firstName,
      lastName: form.lastName,
    }

    try {
      await RegisterUser(registerRequest)
      setSuccess("Registration successful! Redirecting to Sign In…")
      setForm({
        email: "",
        password: "",
        confirmPassword: "",
        firstName: "",
        lastName: "",
      })

      setTimeout(() => {
        router.push("/auth/sign-in")
      }, 2000)
    } catch (err: any) {
      setError(err.message || "Registration failed")
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
          <div className="flex flex-col bg-primary/10 justify-between h-full p-12 text-primary-foreground">
            <div className="flex items-center text-3xl font-medium">
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  className="mr-2 h-6 w-6"
              >
                <path d="M15 6v12a3 3 0 1 0 3-3H6a3 3 0 1 0 3 3V6a3 3 0 1 0-3 3h12a3 3 0 1 0-3-3" />
              </svg>
              ComputeroShop
            </div>

            <div>
              <blockquote className="space-y-2">
                <p className="text-xl text-white text-right">
                  "Wohoho, i love this website, i buy new pc everyday. I'm soo
                  addicted"
                </p>
                <footer className="text-sm text-white text-right">
                  Henry Cavill
                </footer>
              </blockquote>
            </div>
          </div>
        </div>

        <div className="w-full lg:w-1/2 flex items-center justify-center p-8">
          <form onSubmit={handleSubmit} className="w-full max-w-md space-y-8">
            <div className="w-full max-w-md space-y-8">
              <div>
                <h1 className="text-2xl font-semibold tracking-tight">
                  Create an account
                </h1>
                <p className="text-sm text-muted-foreground mt-2">
                  Enter your details below to create your account
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

              <div className="space-y-6">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="first-name">First name</Label>
                    <Input
                        id="first-name"
                        placeholder="John"
                        required
                        value={form.firstName}
                        onChange={(e) =>
                            setForm({ ...form, firstName: e.target.value })
                        }
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="last-name">Last name</Label>
                    <Input
                        id="last-name"
                        placeholder="Doe"
                        value={form.lastName}
                        onChange={(e) =>
                            setForm({ ...form, lastName: e.target.value })
                        }
                        required
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="email">Email</Label>
                  <Input
                      id="email"
                      type="email"
                      placeholder="m@example.com"
                      value={form.email}
                      onChange={(e) => setForm({ ...form, email: e.target.value })}
                      required
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="password">Password</Label>
                  <Input
                      id="password"
                      type="password"
                      value={form.password}
                      onChange={(e) =>
                          setForm({ ...form, password: e.target.value })
                      }
                      required
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="confirm-password">Confirm Password</Label>
                  <Input
                      id="confirm-password"
                      type="password"
                      value={form.confirmPassword}
                      onChange={(e) =>
                          setForm({ ...form, confirmPassword: e.target.value })
                      }
                      required
                  />
                </div>

                <Button
                    className="w-full"
                    type="submit"
                    disabled={isSubmitting}
                >
                  {isSubmitting ? "Submitting…" : "Create Account"}
                </Button>

                <p className="text-center text-sm text-muted-foreground">
                  Already have an account?{" "}
                  <Link
                      href="/auth/sign-in"
                      className="text-primary underline-offset-4 hover:underline"
                  >
                    Sign in
                  </Link>
                </p>
              </div>
            </div>
          </form>
        </div>
      </div>
  )
}
