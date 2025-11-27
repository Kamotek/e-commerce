"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { getOrder } from "@/lib/order-api";
import { processPayment } from "@/lib/payment-api";
import { OrderResource } from "@/types/orders";
import OrderActions from "@/components/order-actions";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export default function PaymentOrderPage() {
  const params = useParams();
  const orderId = params.orderId as string;
  const [order, setOrder] = useState<OrderResource | null>(null);
  const [loading, setLoading] = useState(true);
  const [cardNumber, setCardNumber] = useState("");
  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    refreshOrder();
  }, [orderId]);

  const refreshOrder = () => {
    setLoading(true);
    getOrder(orderId).then((data) => {
      setOrder(data);
      setLoading(false);
    }).catch(() => setLoading(false));
  };

  const handlePayment = async () => {
    if (!cardNumber) return;
    setProcessing(true);
    setError(null);
    try {
      await processPayment(orderId, cardNumber);
      refreshOrder();
    } catch (err: any) {
      setError(err.message);
    } finally {
      setProcessing(false);
    }
  };

  const handleDownloadInvoice = () => {
     window.open(`http://localhost:8085/api/invoices/${orderId}/pdf?email=user@example.com`, '_blank');
  };

  if (loading || !order) return <div>Loading...</div>;

  return (
    <div className="max-w-xl mx-auto py-8">
      <h2 className="text-2xl font-bold mb-4">Order #{order.id}</h2>
      <div className="mb-2">Status: <span className="font-semibold">{order.paymentStatus}</span></div>
      <div className="mb-2">Total: <span className="font-semibold">${order.totalAmount.toFixed(2)}</span></div>
      <div className="mb-4">
        <h3 className="font-bold mb-2">Items:</h3>
        <ul>
          {order.items.map((item, idx) => (
            <li key={idx}>Product: {item.productId}, Quantity: {item.quantity}</li>
          ))}
        </ul>
      </div>

      {order.paymentStatus === "PENDING" && (
        <div className="mb-4">
          <Label htmlFor="card">Card Number</Label>
          <Input id="card" value={cardNumber} onChange={(e) => setCardNumber(e.target.value)} placeholder="1111-2222-3333-4444" />
          <Button onClick={handlePayment} disabled={processing} className="mt-2">
            {processing ? "Processing..." : "Pay Now"}
          </Button>
          {error && <p className="text-red-500 mt-2">{error}</p>}
        </div>
      )}

      {order.paymentStatus === "PAID" && (
        <Button onClick={handleDownloadInvoice} className="mt-4">Download Invoice</Button>
      )}

      <OrderActions order={order} onAction={refreshOrder} />
    </div>
  );
}