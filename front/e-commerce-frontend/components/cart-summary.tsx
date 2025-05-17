"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useState } from "react"

export default function CartSummary() {
  const [promoCode, setPromoCode] = useState("")
  const [promoApplied, setPromoApplied] = useState(false)

  // Mock cart data
  const cartItems = [
    {
      id: 1,
      name: "Ultra Gaming Laptop",
      price: 1999.99,
      quantity: 1,
    },
    {
      id: 3,
      name: "Mechanical Keyboard",
      price: 149.99,
      quantity: 1,
    },
  ]

  const subtotal = cartItems.reduce((total, item) => total + item.price * item.quantity, 0)
  const shipping = 15.99
  const discount = promoApplied ? 50 : 0
  const tax = (subtotal - discount) * 0.08
  const total = subtotal + shipping + tax - discount

  const handleApplyPromo = () => {
    if (promoCode.toLowerCase() === "tech50") {
      setPromoApplied(true)
    } else {
      alert("Invalid promo code")
    }
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Order Summary</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {cartItems.map((item) => (
          <div key={item.id} className="flex justify-between">
            <span>
              {item.name} x {item.quantity}
            </span>
            <span>${(item.price * item.quantity).toFixed(2)}</span>
          </div>
        ))}

        <Separator />

        <div className="space-y-1.5">
          <div className="flex justify-between">
            <span>Subtotal</span>
            <span>${subtotal.toFixed(2)}</span>
          </div>
          <div className="flex justify-between">
            <span>Shipping</span>
            <span>${shipping.toFixed(2)}</span>
          </div>
          <div className="flex justify-between">
            <span>Tax</span>
            <span>${tax.toFixed(2)}</span>
          </div>
          {promoApplied && (
            <div className="flex justify-between text-green-600">
              <span>Discount</span>
              <span>-${discount.toFixed(2)}</span>
            </div>
          )}
        </div>

        <Separator />

        <div className="flex justify-between font-bold text-lg">
          <span>Total</span>
          <span>${total.toFixed(2)}</span>
        </div>

        <div className="pt-4">
          <Label htmlFor="promo-code">Promo Code</Label>
          <div className="flex gap-2 mt-1.5">
            <Input
              id="promo-code"
              value={promoCode}
              onChange={(e) => setPromoCode(e.target.value)}
              placeholder="Enter code"
              disabled={promoApplied}
            />
            <Button
              onClick={handleApplyPromo}
              disabled={promoApplied || !promoCode}
              variant={promoApplied ? "outline" : "default"}
            >
              {promoApplied ? "Applied" : "Apply"}
            </Button>
          </div>
          {promoApplied && <p className="text-sm text-green-600 mt-1">Promo code applied successfully!</p>}
        </div>
      </CardContent>
      <CardFooter>
        <Button className="w-full">Proceed to Checkout</Button>
      </CardFooter>
    </Card>
  )
}
