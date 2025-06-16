// app/catalog/[id]/page.tsx

import type { Metadata } from "next";
import Link from "next/link";
import { notFound } from "next/navigation";
import ProductImageGallery from "@/components/product-image-gallery";
import { Button } from "@/components/ui/button";
import ProductActions from "@/components/product-actions"
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from "@/components/ui/tabs";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import {
  Star,
  ShoppingCart,
  Heart,
  Share2,
  ArrowLeft,
  Truck,
  Shield,
  RotateCcw,
} from "lucide-react";
import ProductRecommendations from "@/components/product-recommendations";
import { fetchProduct, Product } from "@/lib/fetch-products";

interface Props {
  params: { id: string };
}

export async function generateMetadata({ params }: Props): Promise<Metadata> {
  try {
    const product = await fetchProduct(params.id);
    return {
      title: product.name,
      description: product.description.slice(0, 150),
    };
  } catch {
    return {
      title: "Produkt nie znaleziony",
      description: "Nie udało się znaleźć produktu o podanym ID",
    };
  }
}

export default async function ProductDetailPage({ params }: Props) {
  let product: Product;
  try {
    product = await fetchProduct(params.id);
  } catch {
    notFound();
  }

  return (
      <div className="container mx-auto px-4 py-8">
        <div className="mb-6">
          <Link
              href="/catalog"
              className="flex items-center text-muted-foreground hover:text-foreground"
          >
            <ArrowLeft className="mr-2 h-4 w-4" />
            Wróć do katalogu
          </Link>
        </div>

        <div className="grid md:grid-cols-2 gap-8 mb-12">
          <div className="space-y-4">
            <ProductImageGallery
                imageUrls={product.imageUrls ?? []}
                altText={product.name}
            />
          </div>

          <div className="space-y-6">
            <div>
              <div className="flex items-center justify-between">
                <Badge variant="outline">{product.category}</Badge>
                <div className="flex items-center">
                  <Button variant="ghost" size="icon">
                    <Heart className="h-5 w-5" />
                    <span className="sr-only">Dodaj do ulubionych</span>
                  </Button>
                  <Button variant="ghost" size="icon">
                    <Share2 className="h-5 w-5" />
                    <span className="sr-only">Udostępnij produkt</span>
                  </Button>
                </div>
              </div>

              <h1 className="text-3xl font-bold mt-2">{product.name}</h1>
              {product.brand && (
                  <p className="text-sm text-muted-foreground mb-2">
                    Marka: {product.brand}
                  </p>
              )}

              {typeof product.rating === "number" && (
                  <div className="flex items-center mt-2">
                    <div className="flex items-center mr-2">
                      {Array(5)
                          .fill(0)
                          .map((_, i) => (
                              <Star
                                  key={i}
                                  className={`h-4 w-4 ${
                                      i < Math.floor(product.rating as number)
                                          ? "text-yellow-500 fill-yellow-500"
                                          : "text-muted-foreground"
                                  }`}
                              />
                          ))}
                    </div>
                    <span className="text-sm font-medium">
                  {product.rating.toFixed(1)}
                </span>
                    <span className="mx-2 text-muted-foreground">•</span>
                    <Link
                        href="#reviews"
                        className="text-sm text-muted-foreground hover:text-foreground"
                    >
                      {product.reviewCount ?? 0} recenzji
                    </Link>
                  </div>
              )}
            </div>

            {/* CENA */}
            <div>
              <div className="flex items-baseline">
              <span className="text-3xl font-bold">
                ${product.price.toFixed(2)}
              </span>
                {product.originalPrice && (
                    <span className="ml-2 text-muted-foreground line-through">
                  ${product.originalPrice.toFixed(2)}
                </span>
                )}
                {product.originalPrice && (
                    <Badge className="ml-2 bg-green-100 text-green-800 hover:bg-green-100">
                      Oszczędź $
                      {(product.originalPrice - product.price).toFixed(2)}
                    </Badge>
                )}
              </div>
              <p className="text-sm text-muted-foreground mt-1">
                Cena zawiera VAT. Darmowa wysyłka od $50.
              </p>
            </div>

            <Separator />

            <div className="space-y-4">
              <div className="flex items-center">
                <div
                    className={`w-4 h-4 rounded-full mr-2 ${
                        (product.inventory ?? 0) > 0
                            ? "bg-green-500"
                            : "bg-red-500"
                    }`}
                />
                <span className="font-medium">
                {product.inventory && product.inventory > 0
                    ? `Dostępne (${product.inventory} szt.)`
                    : "Brak w magazynie"}
              </span>
              </div>

              <ProductActions product={product} />

            </div>

            <Separator />

            <div className="space-y-4">
              <div className="flex items-start">
                <Truck className="h-5 w-5 mr-3 mt-0.5 text-muted-foreground" />
                <div>
                  <h4 className="font-medium">Darmowa dostawa</h4>
                  <p className="text-sm text-muted-foreground">
                    Darmowa wysyłka od $50
                  </p>
                </div>
              </div>
              <div className="flex items-start">
                <RotateCcw className="h-5 w-5 mr-3 mt-0.5 text-muted-foreground" />
                <div>
                  <h4 className="font-medium">30-dniowe zwroty</h4>
                  <p className="text-sm text-muted-foreground">
                    Bezproblemowe zwroty, jeśli zmienisz zdanie
                  </p>
                </div>
              </div>
              <div className="flex items-start">
                <Shield className="h-5 w-5 mr-3 mt-0.5 text-muted-foreground" />
                <div>
                  <h4 className="font-medium">2-letnia gwarancja</h4>
                  <p className="text-sm text-muted-foreground">
                    Gwarancja producencka
                  </p>
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
              Opis
            </TabsTrigger>
            <TabsTrigger
                value="specifications"
                className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-primary py-3"
            >
              Specyfikacja
            </TabsTrigger>
            <TabsTrigger
                value="reviews"
                className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-primary py-3"
            >
              Recenzje
            </TabsTrigger>
          </TabsList>

          <TabsContent value="description" className="pt-6">
            <p className="text-lg">{product.description}</p>
          </TabsContent>

          <TabsContent value="specifications" className="pt-6">
            {product.specifications ? (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {Object.entries(product.specifications).map(([key, val]) => (
                      <div key={key} className="py-2 border-b">
                        <div className="font-medium">{key}</div>
                        <div className="text-muted-foreground">{val}</div>
                      </div>
                  ))}
                </div>
            ) : (
                <p className="text-sm text-muted-foreground">
                  Brak dodatkowych specyfikacji.
                </p>
            )}
          </TabsContent>

          <TabsContent value="reviews" className="pt-6" id="reviews">
            <div className="space-y-6">
              <div className="flex items-center justify-between">
                <h3 className="text-xl font-semibold">Opinie klientów</h3>
                <Button>Dodaj recenzję</Button>
              </div>

              {typeof product.rating === "number" && (
                  <div className="flex items-center space-x-2">
                    <div className="flex">
                      {Array(5)
                          .fill(0)
                          .map((_, i) => (
                              <Star
                                  key={i}
                                  className={`h-5 w-5 ${
                                      i < Math.floor(product.rating as number)
                                          ? "text-yellow-500 fill-yellow-500"
                                          : "text-muted-foreground"
                                  }`}
                              />
                          ))}
                    </div>
                    <span className="font-medium">
                  {product.rating.toFixed(1)} / 5
                </span>
                    <span className="text-muted-foreground">
                  ({product.reviewCount ?? 0} recenzji)
                </span>
                  </div>
              )}

              <Separator />

              <div className="space-y-6">
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <div className="font-medium">Jan K.</div>
                    <div className="text-sm text-muted-foreground">2 dni temu</div>
                  </div>
                  <div className="flex">
                    {Array(5)
                        .fill(0)
                        .map((_, i) => (
                            <Star
                                key={i}
                                className={`h-4 w-4 ${
                                    i < 5
                                        ? "text-yellow-500 fill-yellow-500"
                                        : "text-muted-foreground"
                                }`}
                            />
                        ))}
                  </div>
                  <p>
                    Świetny produkt, działa bez zarzutu. Polecam każdemu, kto szuka wysokiej wydajności.
                  </p>
                </div>

                <Separator />

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <div className="font-medium">Anna M.</div>
                    <div className="text-sm text-muted-foreground">1 tydzień temu</div>
                  </div>
                  <div className="flex">
                    {Array(5)
                        .fill(0)
                        .map((_, i) => (
                            <Star
                                key={i}
                                className={`h-4 w-4 ${
                                    i < 4
                                        ? "text-yellow-500 fill-yellow-500"
                                        : "text-muted-foreground"
                                }`}
                            />
                        ))}
                  </div>
                  <p>
                    Dobra jakość, ale lekko się nagrzewa przy dłuższym użytkowaniu.
                    Ogólnie na plus.
                  </p>
                </div>

                <Button variant="outline" className="w-full">
                  Załaduj więcej recenzji
                </Button>
              </div>
            </div>
          </TabsContent>
        </Tabs>

        <div className="mb-12">
          <h2 className="text-2xl font-bold mb-6">
            Może Cię również zainteresować
          </h2>
          <ProductRecommendations
              category={product.category}
              currentProductId={product.id}
          />
        </div>
      </div>
  );
}
