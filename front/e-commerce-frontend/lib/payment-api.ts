// /lib/paymentApi.ts

const BASE_URL = process.env.NEXT_PUBLIC_BFF_URL || "http://localhost:8085/api";

interface ProcessPaymentResponse {
    paymentStatus: "COMPLETED" | "FAILED";
}

export async function processPayment(orderId: string, cardNumber: string): Promise<ProcessPaymentResponse> {
    const res = await fetch(`${BASE_URL}/payments/${orderId}/process`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cardNumber }),
        credentials: "include",
    });

    if (!res.ok) {
        const text = await res.text();
        throw new Error(`Payment processing failed ${res.status}: ${text}`);
    }
    return res.json();
}