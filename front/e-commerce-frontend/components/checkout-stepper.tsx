// Plik: components/CheckoutStepper.tsx

"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useCart } from "@/contexts/CartContext";
import { useAuth } from "@/contexts/AuthContext";
import { createOrder } from "@/lib/order-api";
import { CreateOrderCommand } from "@/types/orders";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { CreditCard, Truck, ShoppingBag, Loader2 } from "lucide-react";
import Link from "next/link";

const steps = [
  { id: "cart",       label: "Cart",     icon: ShoppingBag },
  { id: "shipping",   label: "Shipping", icon: Truck },
  { id: "payment",    label: "Payment",  icon: CreditCard },
] as const;
type StepId = typeof steps[number]["id"];

export default function CheckoutStepper() {
  const router = useRouter();
  const { items, updateQuantity, removeFromCart } = useCart();
  const { user } = useAuth();

  const [currentStep, setCurrentStep] = useState<StepId>("cart");
  const [shippingStreet, setShippingStreet] = useState("");
  const [shippingCity, setShippingCity] = useState("");
  const [shippingPostalCode, setShippingPostalCode] = useState("");
  const [shippingCountry, setShippingCountry] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [isCreatingOrder, setIsCreatingOrder] = useState(false);

  const goNext = () => {
    const idx = steps.findIndex(s => s.id === currentStep);
    if (idx < steps.length - 1) {
      setError(null);
      setCurrentStep(steps[idx + 1].id);
    }
  };

  const goBack = () => {
    const idx = steps.findIndex(s => s.id === currentStep);
    if (idx > 0) {
      setError(null);
      setCurrentStep(steps[idx - 1].id);
    }
  };

  const handlePlaceOrder = async () => {
    if (!user) {
      setError("You must be logged in to place an order.");
      return;
    }
    if (!shippingStreet || !shippingCity || !shippingPostalCode || !shippingCountry) {
      setError("Please fill in all shipping details.");
      return;
    }

    setIsCreatingOrder(true);
    setError(null);

    const cmd: CreateOrderCommand = {
      userId: user.userId,
      orderDate: new Date().toISOString(),
      shippingStreet,
      shippingCity,
      shippingPostalCode,
      shippingCountry,
      items: items.map(i => ({
        productId: i.product.id,
        quantity: i.quantity
      }))
    };

    try {
      const createdOrder = await createOrder(cmd);
      router.push(`/payment/${createdOrder.id}`);
    } catch (e: any) {
      setError(e.message || "Failed to create order. Please try again.");
      setIsCreatingOrder(false);
    }
  };

  return (
      <div className="space-y-8">
        {/* Stepper Header (bez zmian) */}
        <div className="flex justify-between">
          {steps.map((step, i) => {
            const currentIndex = steps.findIndex(s => s.id === currentStep);
            const isActive = i === currentIndex;
            const isCompleted = i < currentIndex;
            const Icon = step.icon;
            return (
                <div key={step.id} className="flex flex-col items-center gap-2">
                  <div className={`flex h-10 w-10 items-center justify-center rounded-full border-2 ${isActive || isCompleted ? 'border-primary bg-primary text-primary-foreground' : 'border-muted'}`}>
                    <Icon className="h-5 w-5" />
                  </div>
                  <span className={`text-sm font-medium ${isActive ? 'text-primary' : 'text-muted-foreground'}`}>{step.label}</span>
                </div>
            );
          })}
        </div>

        {/* ========================================================== */}
        {/* ✅ KROK 1: KOSZYK - TEN FRAGMENT ZOSTAŁ PRZYWRÓCONY   */}
        {/* ========================================================== */}
        {currentStep === 'cart' && (
            <Card>
              <CardHeader><CardTitle>Review Your Cart</CardTitle></CardHeader>
              <CardContent className="space-y-4">
                {items.length === 0 ? (
                    <p>Your cart is empty. <Link href="/catalog" className="text-primary underline">Continue shopping</Link>.</p>
                ) : (
                    items.map(({ product, quantity }) => (
                        <div key={product.id} className="flex items-center gap-4 justify-between">
                          <div className="flex items-center gap-4">
                            <img
                                src={product.imageUrls?.[0] || "/placeholder.svg"}
                                alt={product.name}
                                className="h-20 w-20 rounded-md object-cover"
                            />
                            <div>
                              <h3 className="font-medium">{product.name}</h3>
                              <div className="flex items-center gap-2 mt-1">
                                <Label htmlFor={`quantity-${product.id}`} className="sr-only">Quantity</Label>
                                <Input
                                    id={`quantity-${product.id}`}
                                    type="number"
                                    value={quantity}
                                    min={1}
                                    className="w-16 h-8"
                                    onChange={e => updateQuantity(product.id, parseInt(e.target.value, 10))}
                                />
                              </div>
                            </div>
                          </div>
                          <div className="text-right">
                            <p className="font-semibold text-lg">
                              ${(product.price * quantity).toFixed(2)}
                            </p>
                            <Button
                                size="sm"
                                variant="link"
                                className="text-red-500 hover:text-red-700 px-0 h-auto"
                                onClick={() => removeFromCart(product.id)}
                            >
                              Remove
                            </Button>
                          </div>
                        </div>
                    ))
                )}
              </CardContent>
              <CardFooter className="flex justify-between">
                <Button variant="outline" asChild>
                  <Link href="/catalog">Continue Shopping</Link>
                </Button>
                <Button onClick={goNext} disabled={items.length === 0}>
                  Proceed to Shipping
                </Button>
              </CardFooter>
            </Card>
        )}

        {/* Krok 2: Wysyłka */}
        {currentStep === 'shipping' && (
            <Card>
              <CardHeader><CardTitle>Shipping Information</CardTitle></CardHeader>
              <CardContent className="space-y-4">
                <div>
                  <Label htmlFor="street">Street</Label>
                  <Input id="street" value={shippingStreet} onChange={e => setShippingStreet(e.target.value)} />
                </div>
                <div>
                  <Label htmlFor="city">City</Label>
                  <Input id="city" value={shippingCity} onChange={e => setShippingCity(e.target.value)} />
                </div>
                <div>
                  <Label htmlFor="postal">Postal Code</Label>
                  <Input id="postal" value={shippingPostalCode} onChange={e => setShippingPostalCode(e.target.value)} />
                </div>
                <div>
                  <Label htmlFor="country">Country</Label>
                  <Input id="country" value={shippingCountry} onChange={e => setShippingCountry(e.target.value)} />
                </div>
              </CardContent>
              <CardFooter className="flex justify-between">
                <Button variant="outline" onClick={goBack}>Back to Cart</Button>
                <Button onClick={goNext} disabled={!shippingStreet || !shippingCity || !shippingPostalCode || !shippingCountry}>
                  Continue to Payment
                </Button>
              </CardFooter>
            </Card>
        )}

        {/* Krok 3: Płatność - ostatni krok steppera */}
        {currentStep === "payment" && (
            <Card>
              <CardHeader><CardTitle>Review and Confirm</CardTitle></CardHeader>
              <CardContent className="space-y-4">
                <p>You are about to create an order. You will be redirected to our secure payment gateway to complete the purchase.</p>
                {error && <p className="text-red-600 font-medium">{error}</p>}
              </CardContent>
              <CardFooter className="flex justify-between">
                <Button variant="outline" onClick={goBack}>Back to Shipping</Button>
                <Button onClick={handlePlaceOrder} disabled={isCreatingOrder}>
                  {isCreatingOrder && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Create Order & Proceed to Payment
                </Button>
              </CardFooter>
            </Card>
        )}
      </div>
  );
}