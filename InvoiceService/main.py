from spyne import Application, rpc, ServiceBase, Unicode, Integer, Float, Array, ComplexModel
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication
from reportlab.lib.pagesizes import A4
from reportlab.pdfgen import canvas
import base64
import io
import logging
import traceback

# Konfiguracja logowania
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

class OrderItem(ComplexModel):
    productId = Unicode
    quantity = Integer
    price = Float

class InvoiceRequest(ComplexModel):
    orderId = Unicode
    userId = Unicode
    items = Array(OrderItem)
    totalAmount = Float
    email = Unicode

class InvoiceResponse(ComplexModel):
    invoiceId = Unicode
    pdfBase64 = Unicode
    message = Unicode

def generate_invoice_pdf(invoice_id, order_id, user_id, items, total_amount):
    logger.info(f"Generating PDF for invoice {invoice_id}")
    try:
        buffer = io.BytesIO()
        c = canvas.Canvas(buffer, pagesize=A4)
        c.setFont("Helvetica", 12)
        y = 800

        c.drawString(50, y, f"Invoice ID: {invoice_id}")
        y -= 20
        c.drawString(50, y, f"Order ID: {order_id}")
        y -= 20
        c.drawString(50, y, f"User ID: {user_id}")
        y -= 20
        c.drawString(50, y, f"Total Amount: {total_amount} PLN")
        y -= 30
        c.drawString(50, y, "Items:")
        y -= 20

        for item in items:
            c.drawString(60, y, f"- {item.productId}: {item.quantity} x {item.price} PLN")
            y -= 20
            if y < 50:
                c.showPage()
                y = 800

        c.save()
        pdf_bytes = buffer.getvalue()
        buffer.close()
        logger.info(f"PDF generated for invoice {invoice_id}, size: {len(pdf_bytes)} bytes")
        return base64.b64encode(pdf_bytes).decode("utf-8")
    except Exception as e:
        logger.error(f"Error generating PDF for invoice {invoice_id}: {e}")
        logger.error(traceback.format_exc())
        raise

class InvoiceService(ServiceBase):
    @rpc(InvoiceRequest, _returns=InvoiceResponse)
    def generate_invoice(ctx, request):
        logger.info("==== SOAP REQUEST RAW XML ====")
        try:
            raw_xml = getattr(ctx, "in_string", None)
            if raw_xml:
                logger.info(raw_xml)
            else:
                logger.info("No raw XML available in ctx.in_string")
        except Exception as e:
            logger.error(f"Error logging raw XML: {e}")
            logger.error(traceback.format_exc())

        logger.info("==== Parsed InvoiceRequest ====")
        try:
            logger.info(f"orderId: {request.orderId}")
            logger.info(f"userId: {request.userId}")
            logger.info(f"totalAmount: {request.totalAmount}")
            logger.info(f"email: {request.email}")
            logger.info(f"items: {request.items}")
            for idx, item in enumerate(request.items or []):
                logger.info(f"Item {idx}: productId={item.productId}, quantity={item.quantity}, price={item.price}")
        except Exception as e:
            logger.error(f"Error logging parsed request: {e}")
            logger.error(traceback.format_exc())

        try:
            invoice_id = f"INV-{request.orderId}"
            pdf_b64 = generate_invoice_pdf(invoice_id, request.orderId, request.userId, request.items, request.totalAmount)
            logger.info(f"Invoice {invoice_id} generated successfully")
            response = InvoiceResponse(invoiceId=invoice_id, pdfBase64=pdf_b64, message="Invoice generated")
            logger.info("==== SOAP RESPONSE OBJECT ====")
            logger.info(f"invoiceId: {response.invoiceId}")
            logger.info(f"pdfBase64: {response.pdfBase64[:50]}... (truncated)")
            logger.info(f"message: {response.message}")
            return response
        except Exception as e:
            logger.error(f"Error in generate_invoice for order {request.orderId}: {e}")
            logger.error(traceback.format_exc())
            raise

    @rpc(Unicode, _returns=InvoiceResponse)
    def get_invoice_pdf(ctx, invoice_id):
        logger.info(f"SOAP call: get_invoice_pdf for {invoice_id}")
        return InvoiceResponse(invoiceId=invoice_id, pdfBase64="", message="Not implemented")

application = Application([InvoiceService], 'w3c.invoice.soap',
    in_protocol=Soap11(validator=None),
    out_protocol=Soap11()
)

def logging_app(environ, start_response):
    logger.info(f"WSGI PATH: {environ['PATH_INFO']}, METHOD: {environ['REQUEST_METHOD']}")
    return wsgi_app(environ, start_response)

if __name__ == '__main__':
    logger.info("Starting InvoiceService SOAP server on http://0.0.0.0:8090")
    from wsgiref.simple_server import make_server
    wsgi_app = WsgiApplication(application)
    print("InvoiceService SOAP running on http://0.0.0.0:8090")
    server = make_server('0.0.0.0', 8090, logging_app)
    server.serve_forever()