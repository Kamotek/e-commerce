"use client"

import { useState } from "react"
import { Card, CardContent, CardFooter } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { ShoppingCart, Eye } from "lucide-react"
import Link from "next/link"

// Mock data for featured products
const featuredProducts = [
  {
    id: 1,
    name: "Ultra Gaming Laptop",
    description: "High-performance gaming laptop with RTX 4080",
    price: 1999.99,
    image: "/placeholder.svg?height=300&width=300",
    category: "laptops",
    badge: "New",
  },
  {
    id: 2,
    name: "Pro Smartphone",
    description: "Latest flagship smartphone with 108MP camera",
    price: 899.99,
    image: "/placeholder.svg?height=300&width=300",
    category: "smartphones",
    badge: "Popular",
  },
  {
    id: 3,
    name: "Mechanical Keyboard",
    description: "RGB mechanical keyboard with custom switches",
    price: 149.99,
    image: "/placeholder.svg?height=300&width=300",
    category: "accessories",
  },
  {
    id: 4,
    name: "4K Gaming Monitor",
    description: "32-inch 4K monitor with 144Hz refresh rate",
    price: 499.99,
    image: "/placeholder.svg?height=300&width=300",
    category: "monitors",
    badge: "Sale",
  },
  {
    id: 5,
    name: "RTX 4090 Graphics Card",
    description: "Next-gen GPU for ultimate gaming performance",
    price: 1499.99,
    image: "/placeholder.svg?height=300&width=300",
    category: "gpus",
  },
  {
    id: 6,
    name: "Wireless Earbuds",
    description: "Premium wireless earbuds with noise cancellation",
    price: 129.99,
    image: "/placeholder.svg?height=300&width=300",
    category: "accessories",
    badge: "Best Seller",
  },
]

export default function FeaturedProducts() {
  const [addedToCart, setAddedToCart] = useState<Record<number, boolean>>({})

  const handleAddToCart = (productId: number) => {
    setAddedToCart((prev) => ({
      ...prev,
      [productId]: true,
    }))

    // Reset the "Added" state after 2 seconds
    setTimeout(() => {
      setAddedToCart((prev) => ({
        ...prev,
        [productId]: false,
      }))
    }, 2000)
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
      {featuredProducts.map((product) => (
        <Card key={product.id} className="overflow-hidden">
          <div className="relative">
            <img src={product.image || "/placeholder.svg"} alt={product.name} className="w-full h-48 object-cover" />
            {product.badge && <Badge className="absolute top-2 right-2">{product.badge}</Badge>}
          </div>
          <CardContent className="p-4">
            <h3 className="font-semibold text-lg mb-1">{product.name}</h3>
            <p className="text-muted-foreground text-sm mb-2">{product.description}</p>
            <p className="font-bold text-lg">${product.price.toFixed(2)}</p>
          </CardContent>
          <CardFooter className="p-4 pt-0 flex gap-2">
            <Button variant="default" size="sm" className="flex-1" onClick={() => handleAddToCart(product.id)}>
              {addedToCart[product.id] ? (
                "Added!"
              ) : (
                <>
                  <ShoppingCart className="mr-2 h-4 w-4" />
                  Buy Now
                </>
              )}
            </Button>
            <Button variant="outline" size="sm" asChild>
              <Link href={`/catalog/product/${product.id}`}>
                <Eye className="mr-2 h-4 w-4" />
                Details
              </Link>
            </Button>
          </CardFooter>
        </Card>
      ))}
    </div>
  )
}
