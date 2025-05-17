import type { Metadata } from "next"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Checkbox } from "@/components/ui/checkbox"

export const metadata: Metadata = {
  title: "Sign In - ComputeroShop",
  description: "Sign in to your TechHub account",
}

export default function SignInPage() {
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
                <path d="M15 6v12a3 3 0 1 0 3-3H6a3 3 0 1 0 3 3V6a3 3 0 1 0-3 3h12a3 3 0 1 0-3-3"/>
              </svg>
              ComputeroShop
            </div>

            <div>
              <blockquote className="space-y-2">
                <p className="text-xg text-white text-right">"ComputeroShop is the best shop ever!!!"</p>
                <footer className="text-sm text-white text-right">Gall Anonim</footer>
              </blockquote>
            </div>
          </div>
        </div>


        <div className="w-full lg:w-1/2 flex items-center justify-center p-8">
          <div className="w-full max-w-md space-y-8">
            <div>
              <h1 className="text-2xl font-semibold tracking-tight">Sign in to your account</h1>
              <p className="text-sm text-muted-foreground mt-2">Enter your credentials to access your account</p>
            </div>

            <div className="space-y-6">
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input id="email" type="email" placeholder="m@example.com" required/>
              </div>

              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <Label htmlFor="password">Password</Label>
                  <Link href="/auth/forgot-password"
                        className="text-sm text-primary underline-offset-4 hover:underline">
                    Forgot password?
                  </Link>
                </div>
                <Input id="password" type="password" required/>
              </div>

              <div className="flex items-center space-x-2">
                <Checkbox id="remember"/>
                <label htmlFor="remember" className="text-sm font-medium leading-none">
                  Remember me
                </label>
              </div>

              <Button className="w-full">Sign In</Button>

              <p className="text-center text-sm text-muted-foreground">
                Don't have an account?{" "}
                <Link href="/auth/sign-up" className="text-primary underline-offset-4 hover:underline">
                  Sign up
                </Link>
              </p>
            </div>
          </div>
        </div>
      </div>
  )
}
