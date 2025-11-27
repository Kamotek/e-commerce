"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { getOrder } from "@/lib/order-api";
import { OrderResource } from "@/types/orders";

export default function InvoiceOrderPage() {
  const params = useParams();
  const orderId = params.orderId as string;
  const [order, setOrder] = useState<OrderResource | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getOrder(orderId).then((data) => {
      if (data.paymentStatus !== "PAID") {
        setError("Invoice not available. Payment not completed.");
        setLoading(false);
        return;
      }
      setOrder(data);
      setLoading(false);
    }).catch(() => {
      setError("Failed to load order.");
      setLoading(false);
    });
  }, [orderId]);

const handleDownload = async () => {
  const res = await fetch(`/api/invoices/${orderId}/pdf?email=user@example.com`);
  const blob = await res.blob();
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `invoice-${orderId}.pdf`;
  document.body.appendChild(a);
  a.click();
  a.remove();
  window.URL.revokeObjectURL(url);
};

  if (loading) return <div>Loading...</div>;
  if (error || !order) return <div className="text-red-500">{error || "Order not found."}</div>;

  return (
    <div className="max-w-xl mx-auto py-8">
      <h2 className="text-2xl font-bold mb-4">Invoice for Order #{order.id}</h2>
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
      <button
        onClick={handleDownload}
        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
      >
        Download PDF Invoice
      </button>
    </div>
  );
}