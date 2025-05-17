"use client"

import { useState } from "react"
import Link from "next/link"
import { usePathname } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  NavigationMenuTrigger,
} from "@/components/ui/navigation-menu"
import { ThemeToggle } from "@/components/theme-toggle"
import { ShoppingCart, User, Search, Menu, X } from "lucide-react"
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet"

const categories = [
  {
    title: "Computers",
    items: [
      { title: "Laptops", href: "/catalog?category=laptops" },
      { title: "Desktops", href: "/catalog?category=desktops" },
      { title: "All-in-Ones", href: "/catalog?category=all-in-ones" },
    ],
  },
  {
    title: "Phones",
    items: [
      { title: "Smartphones", href: "/catalog?category=smartphones" },
      { title: "Feature Phones", href: "/catalog?category=feature-phones" },
      { title: "Accessories", href: "/catalog?category=phone-accessories" },
    ],
  },
  {
    title: "PC Parts",
    items: [
      { title: "CPUs", href: "/catalog?category=cpus" },
      { title: "GPUs", href: "/catalog?category=gpus" },
      { title: "Motherboards", href: "/catalog?category=motherboards" },
      { title: "RAM", href: "/catalog?category=ram" },
      { title: "Storage", href: "/catalog?category=storage" },
    ],
  },
  {
    title: "Accessories",
    items: [
      { title: "Keyboards", href: "/catalog?category=keyboards" },
      { title: "Mice", href: "/catalog?category=mice" },
      { title: "Headphones", href: "/catalog?category=headphones" },
      { title: "Monitors", href: "/catalog?category=monitors" },
    ],
  },
]

export default function Navbar() {
  const pathname = usePathname()
  const [isSearchOpen, setIsSearchOpen] = useState(false)

  // Mock authentication state - in a real app, this would come from your auth provider
  const isLoggedIn = false
  const isAdmin = false

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="container flex h-16 items-center">
        <Sheet>
          <SheetTrigger asChild>
            <Button variant="ghost" size="icon" className="md:hidden">
              <Menu className="h-5 w-5" />
              <span className="sr-only">Toggle menu</span>
            </Button>
          </SheetTrigger>
          <SheetContent side="left" className="w-[300px] sm:w-[400px]">
            <nav className="flex flex-col gap-4">
              <Link href="/" className="flex items-center gap-2 font-bold text-xl">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  className="h-6 w-6"
                >
                  <path d="M15 6v12a3 3 0 1 0 3-3H6a3 3 0 1 0 3 3V6a3 3 0 1 0-3 3h12a3 3 0 1 0-3-3" />
                </svg>
                ComputeroShop
              </Link>
              <div className="space-y-3">
                <Link href="/" className="block py-2 text-lg font-medium">
                  Home
                </Link>
                {categories.map((category) => (
                  <div key={category.title} className="py-2">
                    <h3 className="font-medium text-lg mb-1">{category.title}</h3>
                    <div className="grid gap-1 pl-4">
                      {category.items.map((item) => (
                        <Link
                          key={item.title}
                          href={item.href}
                          className="text-muted-foreground hover:text-foreground py-1"
                        >
                          {item.title}
                        </Link>
                      ))}
                    </div>
                  </div>
                ))}
                <Link href="/cart" className="flex items-center gap-2 py-2 text-lg font-medium">
                  <ShoppingCart className="h-5 w-5" />
                  Cart
                </Link>
                {isLoggedIn ? (
                  <>
                    <Link href="/account" className="block py-2 text-lg font-medium">
                      My Account
                    </Link>
                    {isAdmin && (
                      <Link href="/admin" className="block py-2 text-lg font-medium">
                        Admin Panel
                      </Link>
                    )}
                    <Button variant="ghost" className="w-full justify-start px-2">
                      Sign Out
                    </Button>
                  </>
                ) : (
                  <Link href="/auth/sign-in" className="block py-2 text-lg font-medium">
                    Sign In
                  </Link>
                )}
              </div>
            </nav>
          </SheetContent>
        </Sheet>

        <Link href="/" className="mr-6 flex items-center space-x-2">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
            className="h-6 w-6"
          >
            <path d="M15 6v12a3 3 0 1 0 3-3H6a3 3 0 1 0 3 3V6a3 3 0 1 0-3 3h12a3 3 0 1 0-3-3" />
          </svg>
          <span className="hidden font-bold sm:inline-block">ComputeroShop</span>
        </Link>

        <div className="hidden md:flex">
          <NavigationMenu>
            <NavigationMenuList>
              {categories.map((category) => (
                <NavigationMenuItem key={category.title}>
                  <NavigationMenuTrigger>{category.title}</NavigationMenuTrigger>
                  <NavigationMenuContent>
                    <ul className="grid w-[400px] gap-3 p-4 md:w-[500px] md:grid-cols-2 lg:w-[600px]">
                      {category.items.map((item) => (
                        <li key={item.title}>
                          <NavigationMenuLink asChild>
                            <Link
                              href={item.href}
                              className="block select-none space-y-1 rounded-md p-3 leading-none no-underline outline-none transition-colors hover:bg-accent hover:text-accent-foreground focus:bg-accent focus:text-accent-foreground"
                            >
                              <div className="text-sm font-medium leading-none">{item.title}</div>
                            </Link>
                          </NavigationMenuLink>
                        </li>
                      ))}
                    </ul>
                  </NavigationMenuContent>
                </NavigationMenuItem>
              ))}
            </NavigationMenuList>
          </NavigationMenu>
        </div>

        <div className="flex flex-1 items-center justify-end space-x-4">
          <div className={`${isSearchOpen ? "flex" : "hidden"} md:flex items-center`}>
            <form className="relative w-full max-w-sm">
              <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
              <Input
                type="search"
                placeholder="Search products..."
                className="w-full bg-background pl-8 md:w-[300px] lg:w-[300px]"
              />
            </form>
            <Button variant="ghost" size="icon" className="md:hidden" onClick={() => setIsSearchOpen(false)}>
              <X className="h-5 w-5" />
              <span className="sr-only">Close search</span>
            </Button>
          </div>

          <Button
            variant="ghost"
            size="icon"
            className={`${isSearchOpen ? "hidden" : "flex"} md:hidden`}
            onClick={() => setIsSearchOpen(true)}
          >
            <Search className="h-5 w-5" />
            <span className="sr-only">Search</span>
          </Button>

          <ThemeToggle />

          <Link href="/cart">
            <Button variant="ghost" size="icon">
              <ShoppingCart className="h-5 w-5" />
              <span className="sr-only">Cart</span>
            </Button>
          </Link>

          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" size="icon">
                <User className="h-5 w-5" />
                <span className="sr-only">User menu</span>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              {isLoggedIn ? (
                <>
                  <DropdownMenuLabel>My Account</DropdownMenuLabel>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem asChild>
                    <Link href="/account">Profile</Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link href="/account/orders">Orders</Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link href="/account/settings">Settings</Link>
                  </DropdownMenuItem>
                  {isAdmin && (
                    <>
                      <DropdownMenuSeparator />
                      <DropdownMenuItem asChild>
                        <Link href="/admin">Admin Panel</Link>
                      </DropdownMenuItem>
                    </>
                  )}
                  <DropdownMenuSeparator />
                  <DropdownMenuItem>Sign out</DropdownMenuItem>
                </>
              ) : (
                <>
                  <DropdownMenuItem asChild>
                    <Link href="/auth/sign-in">Sign In</Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link href="/auth/sign-up">Create Account</Link>
                  </DropdownMenuItem>
                </>
              )}
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>
    </header>
  )
}
