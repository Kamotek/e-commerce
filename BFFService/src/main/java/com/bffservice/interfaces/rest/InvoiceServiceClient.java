package com.bffservice.interfaces.rest;

import com.bffservice.domain.model.Order;
import com.bffservice.domain.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class InvoiceServiceClient {

    private final WebServiceTemplate webServiceTemplate;
    private final CatalogServiceClient catalogServiceClient;
    private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceClient.class);

    public byte[] generateInvoice(Order order, String email) {
        String soapRequest = buildSoapRequest(order, email);

        logger.info("\n==================== SOAP REQUEST TO INVOICE SERVICE ====================\n{}\n==========================================================================", soapRequest);

        ClientInterceptor loggingInterceptor = new ClientInterceptor() {
            @Override
            public boolean handleRequest(MessageContext messageContext) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    messageContext.getRequest().writeTo(baos);
                    String raw = baos.toString(StandardCharsets.UTF_8);
                    logger.info("Outgoing SOAP message (raw):\n{}", raw);
                } catch (Exception e) {
                    logger.error("Failed to log outgoing SOAP message: {}", e.getMessage(), e);
                }
                return true;
            }
            @Override
            public boolean handleResponse(MessageContext messageContext) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    messageContext.getResponse().writeTo(baos);
                    String raw = baos.toString(StandardCharsets.UTF_8);
                    logger.info("Incoming SOAP response (raw):\n{}", raw);
                } catch (Exception e) {
                    logger.error("Failed to log incoming SOAP response: {}", e.getMessage(), e);
                }
                return true;
            }
            @Override
            public boolean handleFault(MessageContext messageContext) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    messageContext.getResponse().writeTo(baos);
                    String raw = baos.toString(StandardCharsets.UTF_8);
                    logger.error("Incoming SOAP fault (raw):\n{}", raw);
                } catch (Exception e) {
                    logger.error("Failed to log SOAP fault: {}", e.getMessage(), e);
                }
                return true;
            }
            @Override
            public void afterCompletion(MessageContext messageContext, Exception ex) {
                logger.info("SOAP interceptor afterCompletion. Exception: {}", ex == null ? "none" : ex.getMessage());
            }
        };

        ClientInterceptor[] existing = webServiceTemplate.getInterceptors();
        if (existing == null) {
            webServiceTemplate.setInterceptors(new ClientInterceptor[]{loggingInterceptor});
        } else {
            ClientInterceptor[] combined = Arrays.copyOf(existing, existing.length + 1);
            combined[existing.length] = loggingInterceptor;
            webServiceTemplate.setInterceptors(combined);
        }

        StreamSource source = new StreamSource(new StringReader(soapRequest));
        StringWriter responseWriter = new StringWriter();
        StreamResult result = new StreamResult(responseWriter);

        try {
            webServiceTemplate.sendSourceAndReceiveToResult(
                "http://invoice-service:8090/",
                source,
                new SoapActionCallback("generate_invoice"),
                result
            );
        } catch (Exception e) {
            logger.error("SOAP call failed: {}", e.getMessage(), e);
            throw e;
        }

        String responseXml = responseWriter.toString();
        logger.info("SOAP response received from InvoiceService:\n{}", responseXml);

        String pdfBase64 = extractPdfBase64(responseXml);
        logger.info("Extracted pdfBase64 length: {}", pdfBase64.length());
        byte[] pdfBytes = Base64.getDecoder().decode(pdfBase64);
        logger.info("Decoded PDF bytes length: {}", pdfBytes.length);
        return pdfBytes;
    }

    private String buildSoapRequest(Order order, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("<w3c:generate_invoice xmlns:w3c=\"w3c.invoice.soap\">");
        sb.append("<w3c:request>");
        sb.append("<w3c:orderId>").append(order.getId()).append("</w3c:orderId>");
        sb.append("<w3c:userId>").append(order.getUserId()).append("</w3c:userId>");
        sb.append("<w3c:totalAmount>").append(order.getTotalAmount()).append("</w3c:totalAmount>");
        sb.append("<w3c:email>").append(email).append("</w3c:email>");
        sb.append("<w3c:items>");
        for (OrderItem item : order.getItems()) {
            sb.append("<w3c:OrderItem>");
            sb.append("<w3c:productId>").append(item.getProductId()).append("</w3c:productId>");
            sb.append("<w3c:quantity>").append(item.getQuantity()).append("</w3c:quantity>");
            BigDecimal price = BigDecimal.ZERO;
            try {
                var productResp = catalogServiceClient.getProductById(item.getProductId());
                if (productResp.getBody() != null && productResp.getBody().getPrice() != null) {
                    price = productResp.getBody().getPrice();
                }
            } catch (Exception e) {
                logger.warn("Could not fetch price for productId {}: {}", item.getProductId(), e.getMessage());
            }
            sb.append("<w3c:price>").append(price).append("</w3c:price>");
            sb.append("</w3c:OrderItem>");
        }
        sb.append("</w3c:items>");
        sb.append("</w3c:request>");
        sb.append("</w3c:generate_invoice>");
        return sb.toString();
    }

    private String extractPdfBase64(String xml) {
        String openTag = "<pdfBase64>";
        String closeTag = "</pdfBase64>";
        int start = xml.indexOf(openTag);
        int end = xml.indexOf(closeTag);
        if (start >= 0 && end > start) {
            return xml.substring(start + openTag.length(), end);
        }
        openTag = "<tns:pdfBase64>";
        closeTag = "</tns:pdfBase64>";
        start = xml.indexOf(openTag);
        end = xml.indexOf(closeTag);
        if (start >= 0 && end > start) {
            return xml.substring(start + openTag.length(), end);
        }
        return "";
    }
}