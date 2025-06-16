// components/product-recommendations.tsx
"use client";

import { useState, useEffect } from "react";
import { Card, CardContent, CardFooter } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ShoppingCart, Eye } from "lucide-react";
import Link from "next/link";
import { fetchProductsByCategory, Product } from "@/lib/fetch-products";

interface RecommendationsProps {
  category: string;
  currentProductId: string;
}

export default function ProductRecommendations({
                                                 category,
                                                 currentProductId,
                                               }: RecommendationsProps) {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [addedToCart, setAddedToCart] = useState<Record<string, boolean>>({});

  useEffect(() => {
    setLoading(true);
    setError(null);

    fetchProductsByCategory(category, 4)
        .then((list) => {
          // odrzucamy bieżący produkt, ewentualnie dopadamy do 4
          const filtered = list
              .filter((p) => p.id !== currentProductId)
              .slice(0, 4);
          setProducts(filtered);
        })
        .catch((e: any) => setError(e.message))
        .finally(() => setLoading(false));
  }, [category, currentProductId]);

  const handleAddToCart = (id: string) => {
    setAddedToCart((prev) => ({ ...prev, [id]: true }));
    setTimeout(() => {
      setAddedToCart((prev) => ({ ...prev, [id]: false }));
    }, 2000);
  };

  if (loading) return <p>Loading recommendations…</p>;
  if (error) return <p className="text-red-500">Error: {error}</p>;
  if (products.length === 0) return <p>Brak rekomendacji w tej kategorii.</p>;

  return (
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
        {products.map((product) => (
            <Card key={product.id} className="overflow-hidden">
              <div className="relative">
                <img
                    src={product.imageUrls?.[0] || "/placeholder.svg"}
                    alt={product.name}
                    className="w-full h-48 object-cover"
                />
                {product.badge && (
                    <Badge className="absolute top-2 right-2">
                      {product.badge}
                    </Badge>
                )}
              </div>
              <CardContent className="p-4">
                <h3 className="font-semibold text-lg mb-1">{product.name}</h3>
                <p className="text-muted-foreground text-sm mb-2">
                  {product.description}
                </p>
                <p className="font-bold text-lg">
                  ${product.price.toFixed(2)}
                </p>
              </CardContent>
              <CardFooter className="p-4 pt-0 flex gap-2">
                <Button
                    variant="default"
                    size="sm"
                    className="flex-1"
                    onClick={() => handleAddToCart(product.id)}
                >
                  {addedToCart[product.id] ? (
                      "Added!"
                  ) : (
                      <>
                        <ShoppingCart className="mr-2 h-4 w-4" /> Buy Now
                      </>
                  )}
                </Button>
                <Button variant="outline" size="sm" asChild>
                  <Link href={`/catalog/product/${product.id}`}>
                    <Eye className="mr-2 h-4 w-4" /> Details
                  </Link>
                </Button>
              </CardFooter>
            </Card>
        ))}
      </div>
  );
}
