import type { Metadata } from "next"
import CartSummary from "@/components/cart-summary"
import CheckoutStepper from "@/components/checkout-stepper"

export const metadata: Metadata = {
  title: "Shopping Cart - TechHub",
  description: "Review your cart and checkout",
}

export default function CartPage() {
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Your Shopping Cart</h1>

      <div className="grid md:grid-cols-3 gap-8">
        <div className="md:col-span-2">
          <CheckoutStepper />
        </div>

        <div>
          <CartSummary />
        </div>
      </div>
    </div>
  )
}
