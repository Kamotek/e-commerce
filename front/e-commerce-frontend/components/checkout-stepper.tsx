"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Separator } from "@/components/ui/separator"
import { Check, CreditCard, Truck, ShoppingBag } from "lucide-react"

const steps = [
  { id: "cart", label: "Cart", icon: ShoppingBag },
  { id: "shipping", label: "Shipping", icon: Truck },
  { id: "payment", label: "Payment", icon: CreditCard },
  { id: "confirmation", label: "Confirmation", icon: Check },
]

export default function CheckoutStepper() {
  const [currentStep, setCurrentStep] = useState("cart")

  // Mock cart data
  const cartItems = [
    {
      id: 1,
      name: "Ultra Gaming Laptop",
      price: 1999.99,
      quantity: 1,
      image: "/placeholder.svg?height=80&width=80",
    },
    {
      id: 3,
      name: "Mechanical Keyboard",
      price: 149.99,
      quantity: 1,
      image: "/placeholder.svg?height=80&width=80",
    },
  ]

  const handleNextStep = () => {
    const currentIndex = steps.findIndex((step) => step.id === currentStep)
    if (currentIndex < steps.length - 1) {
      setCurrentStep(steps[currentIndex + 1].id)
    }
  }

  const handlePreviousStep = () => {
    const currentIndex = steps.findIndex((step) => step.id === currentStep)
    if (currentIndex > 0) {
      setCurrentStep(steps[currentIndex - 1].id)
    }
  }

  return (
    <div className="space-y-8">
      <div className="flex justify-between">
        {steps.map((step, index) => {
          const isActive = step.id === currentStep
          const isPast = steps.findIndex((s) => s.id === currentStep) > index

          return (
            <div key={step.id} className="flex flex-col items-center">
              <div
                className={`
                flex h-10 w-10 items-center justify-center rounded-full border-2
                ${
                  isActive
                    ? "border-primary bg-primary text-primary-foreground"
                    : isPast
                      ? "border-primary bg-primary text-primary-foreground"
                      : "border-muted bg-background"
                }
              `}
              >
                <step.icon className="h-5 w-5" />
              </div>
              <span className={`mt-2 text-sm font-medium ${isActive ? "text-foreground" : "text-muted-foreground"}`}>
                {step.label}
              </span>
              {index < steps.length - 1 && (
                <div className="hidden sm:block absolute left-0 top-0 h-0.5 w-full bg-muted">
                  <div className="h-full bg-primary" style={{ width: isPast ? "100%" : "0%" }} />
                </div>
              )}
            </div>
          )
        })}
      </div>

      {currentStep === "cart" && (
        <Card>
          <CardHeader>
            <CardTitle>Your Cart</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {cartItems.map((item) => (
              <div key={item.id} className="flex items-center gap-4">
                <img
                  src={item.image || "/placeholder.svg"}
                  alt={item.name}
                  className="h-20 w-20 rounded-md object-cover"
                />
                <div className="flex-1">
                  <h3 className="font-medium">{item.name}</h3>
                  <p className="text-muted-foreground">Quantity: {item.quantity}</p>
                </div>
                <div className="font-medium">${item.price.toFixed(2)}</div>
                <Button variant="outline" size="sm">
                  Remove
                </Button>
              </div>
            ))}
          </CardContent>
          <CardFooter className="flex justify-between">
            <Button variant="outline" onClick={() => window.history.back()}>
              Continue Shopping
            </Button>
            <Button onClick={handleNextStep}>Proceed to Shipping</Button>
          </CardFooter>
        </Card>
      )}

      {currentStep === "shipping" && (
        <Card>
          <CardHeader>
            <CardTitle>Shipping Information</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="first-name">First Name</Label>
                <Input id="first-name" placeholder="John" />
              </div>
              <div className="space-y-2">
                <Label htmlFor="last-name">Last Name</Label>
                <Input id="last-name" placeholder="Doe" />
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input id="email" type="email" placeholder="john.doe@example.com" />
            </div>

            <div className="space-y-2">
              <Label htmlFor="phone">Phone Number</Label>
              <Input id="phone" type="tel" placeholder="(123) 456-7890" />
            </div>

            <div className="space-y-2">
              <Label htmlFor="address">Address</Label>
              <Input id="address" placeholder="123 Main St" />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="city">City</Label>
                <Input id="city" placeholder="New York" />
              </div>
              <div className="space-y-2">
                <Label htmlFor="state">State</Label>
                <Select>
                  <SelectTrigger id="state">
                    <SelectValue placeholder="Select state" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="ca">California</SelectItem>
                    <SelectItem value="ny">New York</SelectItem>
                    <SelectItem value="tx">Texas</SelectItem>
                    <SelectItem value="fl">Florida</SelectItem>
                    <SelectItem value="il">Illinois</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="zip">ZIP Code</Label>
                <Input id="zip" placeholder="10001" />
              </div>
              <div className="space-y-2">
                <Label htmlFor="country">Country</Label>
                <Select defaultValue="us">
                  <SelectTrigger id="country">
                    <SelectValue placeholder="Select country" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="us">United States</SelectItem>
                    <SelectItem value="ca">Canada</SelectItem>
                    <SelectItem value="uk">United Kingdom</SelectItem>
                    <SelectItem value="au">Australia</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="notes">Order Notes (Optional)</Label>
              <Textarea id="notes" placeholder="Special instructions for delivery" />
            </div>
          </CardContent>
          <CardFooter className="flex justify-between">
            <Button variant="outline" onClick={handlePreviousStep}>
              Back to Cart
            </Button>
            <Button onClick={handleNextStep}>Continue to Payment</Button>
          </CardFooter>
        </Card>
      )}

      {currentStep === "payment" && (
        <Card>
          <CardHeader>
            <CardTitle>Payment Method</CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            <RadioGroup defaultValue="credit-card">
              <div className="flex items-center space-x-2 border rounded-md p-4">
                <RadioGroupItem value="credit-card" id="credit-card" />
                <Label htmlFor="credit-card" className="flex-1">
                  Credit Card
                </Label>
                <div className="flex gap-2">
                  <img src="/placeholder.svg?height=30&width=40" alt="Visa" className="h-8" />
                  <img src="/placeholder.svg?height=30&width=40" alt="Mastercard" className="h-8" />
                  <img src="/placeholder.svg?height=30&width=40" alt="Amex" className="h-8" />
                </div>
              </div>

              <div className="flex items-center space-x-2 border rounded-md p-4">
                <RadioGroupItem value="paypal" id="paypal" />
                <Label htmlFor="paypal" className="flex-1">
                  PayPal
                </Label>
                <img src="/placeholder.svg?height=30&width=40" alt="PayPal" className="h-8" />
              </div>

              <div className="flex items-center space-x-2 border rounded-md p-4">
                <RadioGroupItem value="apple-pay" id="apple-pay" />
                <Label htmlFor="apple-pay" className="flex-1">
                  Apple Pay
                </Label>
                <img src="/placeholder.svg?height=30&width=40" alt="Apple Pay" className="h-8" />
              </div>
            </RadioGroup>

            <div className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="card-number">Card Number</Label>
                <Input id="card-number" placeholder="1234 5678 9012 3456" />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="expiry">Expiration Date</Label>
                  <Input id="expiry" placeholder="MM/YY" />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="cvv">CVV</Label>
                  <Input id="cvv" placeholder="123" />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="name-on-card">Name on Card</Label>
                <Input id="name-on-card" placeholder="John Doe" />
              </div>
            </div>

            <Separator />

            <div className="space-y-2">
              <div className="flex items-center space-x-2">
                <input type="checkbox" id="billing-same" className="rounded border-gray-300" />
                <Label htmlFor="billing-same">Billing address same as shipping address</Label>
              </div>
            </div>
          </CardContent>
          <CardFooter className="flex justify-between">
            <Button variant="outline" onClick={handlePreviousStep}>
              Back to Shipping
            </Button>
            <Button onClick={handleNextStep}>Place Order</Button>
          </CardFooter>
        </Card>
      )}

      {currentStep === "confirmation" && (
        <Card>
          <CardHeader>
            <CardTitle className="text-center">Order Confirmed!</CardTitle>
          </CardHeader>
          <CardContent className="space-y-6 text-center">
            <div className="mx-auto flex h-20 w-20 items-center justify-center rounded-full bg-green-100">
              <Check className="h-10 w-10 text-green-600" />
            </div>

            <div className="space-y-2">
              <h3 className="text-xl font-medium">Thank you for your order!</h3>
              <p className="text-muted-foreground">Your order #12345 has been placed successfully.</p>
            </div>

            <div className="rounded-md bg-muted p-4">
              <p className="text-sm">
                A confirmation email has been sent to your email address. You can track your order status in your
                account.
              </p>
            </div>

            <div className="space-y-4">
              <h4 className="font-medium">Order Summary</h4>
              {cartItems.map((item) => (
                <div key={item.id} className="flex justify-between">
                  <span>
                    {item.name} x {item.quantity}
                  </span>
                  <span>${(item.price * item.quantity).toFixed(2)}</span>
                </div>
              ))}
              <Separator />
              <div className="flex justify-between font-medium">
                <span>Total</span>
                <span>
                  ${(cartItems.reduce((total, item) => total + item.price * item.quantity, 0) + 15.99).toFixed(2)}
                </span>
              </div>
            </div>
          </CardContent>
          <CardFooter className="flex justify-center">
            <Button asChild>
              <a href="/">Continue Shopping</a>
            </Button>
          </CardFooter>
        </Card>
      )}
    </div>
  )
}
