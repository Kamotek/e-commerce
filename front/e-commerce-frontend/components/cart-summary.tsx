"use client";

import Link from "next/link"; // ✅ DODANY IMPORT
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { useCart } from "@/contexts/CartContext";

export default function CartSummary() {
  // Pobieramy tylko te dane, których faktycznie potrzebujemy
  const { items, totalPrice } = useCart();

  // Cała logika związana z kodami promo, podatkami i wysyłką została usunięta

  return (
      <Card className="sticky top-20"> {/* Dodano sticky, aby podsumowanie było zawsze widoczne */}
        <CardHeader>
          <CardTitle>Order Summary</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Sekcja listy produktów - pozostaje bez zmian */}
          {items.length > 0 ? (
              items.map(({ product, quantity }) => (
                  <div key={product.id} className="flex justify-between text-sm">
              <span>
                {product.name} (x{quantity})
              </span>
                    <span>${(product.price * quantity).toFixed(2)}</span>
                  </div>
              ))
          ) : (
              <p className="text-muted-foreground">Your cart is empty.</p>
          )}

          {/* Separator tylko jeśli są produkty */}
          {items.length > 0 && <Separator />}

          {/* ✅ UPROSZCZONE PODSUMOWANIE */}
          {/* Wyświetlamy tylko łączną wartość produktów w koszyku */}
          <div className="flex justify-between font-bold text-lg">
            <span>Total</span>
            <span>${totalPrice.toFixed(2)}</span>
          </div>
        </CardContent>
        <CardFooter>
          {/* ✅ PRZYCISK TERAZ PROWADZI DO KOSZYKA */}
          <Button className="w-full" asChild disabled={items.length === 0}>
            <Link href="/cart">Proceed to Checkout</Link>
          </Button>
        </CardFooter>
      </Card>
  );
}