import type React from "react"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "./globals.css"
import { ThemeProvider } from "@/components/theme-provider"
import Navbar from "@/components/navbar"
import Footer from "@/components/footer"
import { AuthProvider } from "@/contexts/AuthContext"
import { CartProvider } from "@/contexts/CartContext"

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
    title: "ComputeroShop - Your One-Stop Tech Shop",
    description: "Shop for computers, phones, accessories and PC parts",
}

export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode
}>) {
    return (
        <html lang="en" suppressHydrationWarning>
        <body className={inter.className}>
        <ThemeProvider attribute="class" defaultTheme="system" enableSystem>
            <AuthProvider>
                <CartProvider>
                    <div className="flex min-h-screen flex-col">
                        <Navbar />
                        <main className="flex-1 bg-muted/20">
                            {children}
                        </main>
                        <Footer />
                    </div>
                </CartProvider>
            </AuthProvider>
        </ThemeProvider>
        </body>
        </html>
    )
}