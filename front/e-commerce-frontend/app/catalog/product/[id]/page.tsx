import type { Metadata } from "next"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { Star, ShoppingCart, Heart, Share2, ArrowLeft, Truck, Shield, RotateCcw } from "lucide-react"
import ProductRecommendations from "@/components/product-recommendations"

export const metadata: Metadata = {
  title: "Product Details - TechHub",
  description: "View detailed product information",
}

// Mock product data
const product = {
  id: 1,
  name: "Ultra Gaming Laptop",
  description:
    "Experience gaming like never before with the Ultra Gaming Laptop. Featuring the latest NVIDIA RTX 4080 graphics card, a powerful Intel Core i9 processor, and a stunning 17-inch 240Hz display, this laptop delivers exceptional performance for gaming and creative work.",
  price: 1999.99,
  originalPrice: 2299.99,
  rating: 4.8,
  reviewCount: 124,
  stock: 15,
  images: [
    "/placeholder.svg?height=600&width=600",
    "/placeholder.svg?height=600&width=600",
    "/placeholder.svg?height=600&width=600",
    "/placeholder.svg?height=600&width=600",
  ],
  category: "Laptops",
  brand: "TechPro",
  features: [
    "NVIDIA RTX 4080 16GB GDDR6X",
    "Intel Core i9-13900H (24 cores, up to 5.4GHz)",
    "32GB DDR5 RAM (upgradable to 64GB)",
    "1TB NVMe SSD (expandable)",
    '17.3" QHD 240Hz IPS Display',
    "RGB Mechanical Keyboard",
    "Wi-Fi 6E and Bluetooth 5.3",
    "Thunderbolt 4, HDMI 2.1, USB 3.2",
    "Advanced Cooling System with Liquid Metal",
    "Windows 11 Pro",
  ],
  specifications: {
    Processor: "Intel Core i9-13900H (24 cores, up to 5.4GHz)",
    Graphics: "NVIDIA RTX 4080 16GB GDDR6X",
    Memory: "32GB DDR5 RAM (upgradable to 64GB)",
    Storage: "1TB NVMe SSD (expandable)",
    Display: '17.3" QHD (2560 x 1440) 240Hz IPS Display',
    Keyboard: "RGB Mechanical Keyboard with per-key lighting",
    Connectivity: "Wi-Fi 6E, Bluetooth 5.3, Gigabit Ethernet",
    Ports: "1x Thunderbolt 4, 3x USB 3.2 Gen 2, 1x HDMI 2.1, 1x SD Card Reader, 1x 3.5mm Audio Jack",
    Battery: "99.9Whr Battery, up to 5 hours of gaming, 8 hours of productivity",
    Dimensions: "395.7 x 277.8 x 23.4 mm",
    Weight: "2.9 kg (6.4 lbs)",
    OperatingSystem: "Windows 11 Pro",
  },
}

export default function ProductDetailPage({ params }: { params: { id: string } }) {
  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <Link href="/catalog" className="flex items-center text-muted-foreground hover:text-foreground">
          <ArrowLeft className="mr-2 h-4 w-4" />
          Back to Catalog
        </Link>
      </div>

      <div className="grid md:grid-cols-2 gap-8 mb-12">
        {/* Product Images */}
        <div className="space-y-4">
          <div className="border rounded-lg overflow-hidden">
            <img
              src={product.images[0] || "/placeholder.svg"}
              alt={product.name}
              className="w-full h-auto object-cover aspect-square"
            />
          </div>
          <div className="grid grid-cols-4 gap-2">
            {product.images.map((image, index) => (
              <div key={index} className="border rounded-lg overflow-hidden">
                <img
                  src={image || "/placeholder.svg"}
                  alt={`${product.name} - View ${index + 1}`}
                  className="w-full h-auto object-cover"
                />
              </div>
            ))}
          </div>
        </div>

        {/* Product Info */}
        <div className="space-y-6">
          <div>
            <div className="flex items-center justify-between">
              <Badge variant="outline">{product.category}</Badge>
              <div className="flex items-center">
                <Button variant="ghost" size="icon">
                  <Heart className="h-5 w-5" />
                  <span className="sr-only">Add to wishlist</span>
                </Button>
                <Button variant="ghost" size="icon">
                  <Share2 className="h-5 w-5" />
                  <span className="sr-only">Share product</span>
                </Button>
              </div>
            </div>
            <h1 className="text-3xl font-bold mt-2">{product.name}</h1>
            <div className="flex items-center mt-2">
              <div className="flex items-center mr-2">
                {Array(5)
                  .fill(0)
                  .map((_, i) => (
                    <Star
                      key={i}
                      className={`h-4 w-4 ${i < Math.floor(product.rating) ? "text-yellow-500 fill-yellow-500" : "text-muted-foreground"}`}
                    />
                  ))}
              </div>
              <span className="text-sm font-medium">{product.rating}</span>
              <span className="mx-2 text-muted-foreground">•</span>
              <Link href="#reviews" className="text-sm text-muted-foreground hover:text-foreground">
                {product.reviewCount} reviews
              </Link>
            </div>
          </div>

          <div>
            <div className="flex items-baseline">
              <span className="text-3xl font-bold">${product.price.toFixed(2)}</span>
              {product.originalPrice && (
                <span className="ml-2 text-muted-foreground line-through">${product.originalPrice.toFixed(2)}</span>
              )}
              {product.originalPrice && (
                <Badge className="ml-2 bg-green-100 text-green-800 hover:bg-green-100">
                  Save ${(product.originalPrice - product.price).toFixed(2)}
                </Badge>
              )}
            </div>
            <p className="text-sm text-muted-foreground mt-1">Price includes VAT. Free shipping on orders over $50.</p>
          </div>

          <Separator />

          <div className="space-y-4">
            <div className="flex items-center">
              <div className="w-4 h-4 rounded-full bg-green-500 mr-2"></div>
              <span className="font-medium">
                {product.stock > 0 ? `In Stock (${product.stock} available)` : "Out of Stock"}
              </span>
            </div>

            <div className="flex flex-col sm:flex-row gap-4">
              <Button className="flex-1" size="lg">
                <ShoppingCart className="mr-2 h-5 w-5" />
                Add to Cart
              </Button>
              <Button variant="outline" size="lg">
                Buy Now
              </Button>
            </div>
          </div>

          <Separator />

          <div className="space-y-4">
            <div className="flex items-start">
              <Truck className="h-5 w-5 mr-3 mt-0.5 text-muted-foreground" />
              <div>
                <h4 className="font-medium">Free Delivery</h4>
                <p className="text-sm text-muted-foreground">Free shipping on orders over $50</p>
              </div>
            </div>
            <div className="flex items-start">
              <RotateCcw className="h-5 w-5 mr-3 mt-0.5 text-muted-foreground" />
              <div>
                <h4 className="font-medium">30-Day Returns</h4>
                <p className="text-sm text-muted-foreground">Hassle-free returns if you change your mind</p>
              </div>
            </div>
            <div className="flex items-start">
              <Shield className="h-5 w-5 mr-3 mt-0.5 text-muted-foreground" />
              <div>
                <h4 className="font-medium">2-Year Warranty</h4>
                <p className="text-sm text-muted-foreground">Extended warranty with manufacturer support</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <Tabs defaultValue="description" className="mb-12">
        <TabsList className="w-full justify-start border-b rounded-none h-auto p-0">
          <TabsTrigger
            value="description"
            className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-primary py-3"
          >
            Description
          </TabsTrigger>
          <TabsTrigger
            value="specifications"
            className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-primary py-3"
          >
            Specifications
          </TabsTrigger>
          <TabsTrigger
            value="reviews"
            className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-primary py-3"
          >
            Reviews
          </TabsTrigger>
        </TabsList>
        <TabsContent value="description" className="pt-6">
          <div className="space-y-4">
            <p className="text-lg">{product.description}</p>
            <h3 className="text-xl font-semibold mt-6">Key Features</h3>
            <ul className="list-disc pl-5 space-y-2">
              {product.features.map((feature, index) => (
                <li key={index}>{feature}</li>
              ))}
            </ul>
          </div>
        </TabsContent>
        <TabsContent value="specifications" className="pt-6">
          <div className="space-y-4">
            <h3 className="text-xl font-semibold">Technical Specifications</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {Object.entries(product.specifications).map(([key, value]) => (
                <div key={key} className="py-2 border-b">
                  <div className="font-medium">{key}</div>
                  <div className="text-muted-foreground">{value}</div>
                </div>
              ))}
            </div>
          </div>
        </TabsContent>
        <TabsContent value="reviews" className="pt-6" id="reviews">
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <h3 className="text-xl font-semibold">Customer Reviews</h3>
              <Button>Write a Review</Button>
            </div>
            <div className="flex items-center space-x-2">
              <div className="flex">
                {Array(5)
                  .fill(0)
                  .map((_, i) => (
                    <Star
                      key={i}
                      className={`h-5 w-5 ${i < Math.floor(product.rating) ? "text-yellow-500 fill-yellow-500" : "text-muted-foreground"}`}
                    />
                  ))}
              </div>
              <span className="font-medium">{product.rating} out of 5</span>
              <span className="text-muted-foreground">({product.reviewCount} reviews)</span>
            </div>

            <Separator />

            <div className="space-y-6">
              {/* Sample reviews - in a real app, these would be loaded from a database */}
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <div className="font-medium">John D.</div>
                  <div className="text-sm text-muted-foreground">2 days ago</div>
                </div>
                <div className="flex">
                  {Array(5)
                    .fill(0)
                    .map((_, i) => (
                      <Star
                        key={i}
                        className={`h-4 w-4 ${i < 5 ? "text-yellow-500 fill-yellow-500" : "text-muted-foreground"}`}
                      />
                    ))}
                </div>
                <p>
                  This laptop exceeds all my expectations. The performance is incredible, and I can run all my games at
                  max settings without any issues. The display is gorgeous, and the keyboard feels great to type on.
                </p>
              </div>

              <Separator />

              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <div className="font-medium">Sarah M.</div>
                  <div className="text-sm text-muted-foreground">1 week ago</div>
                </div>
                <div className="flex">
                  {Array(5)
                    .fill(0)
                    .map((_, i) => (
                      <Star
                        key={i}
                        className={`h-4 w-4 ${i < 4 ? "text-yellow-500 fill-yellow-500" : "text-muted-foreground"}`}
                      />
                    ))}
                </div>
                <p>
                  Great laptop overall, but it does run a bit hot during extended gaming sessions. The performance is
                  top-notch, and the build quality is excellent. Battery life could be better, but that's expected for a
                  gaming laptop.
                </p>
              </div>

              <Separator />

              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <div className="font-medium">Michael T.</div>
                  <div className="text-sm text-muted-foreground">3 weeks ago</div>
                </div>
                <div className="flex">
                  {Array(5)
                    .fill(0)
                    .map((_, i) => (
                      <Star
                        key={i}
                        className={`h-4 w-4 ${i < 5 ? "text-yellow-500 fill-yellow-500" : "text-muted-foreground"}`}
                      />
                    ))}
                </div>
                <p>
                  Absolutely worth every penny! The RTX 4080 handles everything I throw at it, and the 240Hz display is
                  a game-changer for competitive gaming. The cooling system is impressive, keeping temperatures under
                  control even during intense gaming sessions.
                </p>
              </div>

              <Button variant="outline" className="w-full">
                Load More Reviews
              </Button>
            </div>
          </div>
        </TabsContent>
      </Tabs>

      <div className="mb-12">
        <h2 className="text-2xl font-bold mb-6">You Might Also Like</h2>
        <ProductRecommendations />
      </div>
    </div>
  )
}
