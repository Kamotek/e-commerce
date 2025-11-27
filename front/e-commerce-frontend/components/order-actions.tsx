"use client";

export default function OrderActions({
  order,
  onAction,
}: {
  order: OrderResource;
  onAction?: () => void;
}) {
  const handleAction = async (link: OrderLink) => {
    if (link.rel === "pay") {
      window.location.href = `/payment/${order.id}`;
      return;
    }
    await fetch(link.href, {
      method: link.method,
      credentials: "include",
    });
    if (onAction) onAction();
  };

  const handleDownloadInvoice = () => {
    window.location.href = `/invoice/${order.id}`;
  };

  return (
    <div className="flex gap-2 mt-4">
      {order.links.map((link) => (
        <button
          key={link.rel}
          onClick={() => handleAction(link)}
          className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
        >
          {link.rel === "pay" ? "Pay" : link.rel}
        </button>
      ))}
      {order.paymentStatus === "PAID" && (
        <button
          onClick={handleDownloadInvoice}
          className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
        >
          Download Invoice
        </button>
      )}
    </div>
  );
}