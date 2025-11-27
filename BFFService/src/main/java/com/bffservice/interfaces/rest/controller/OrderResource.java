package com.bffservice.interfaces.rest.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderResource {
    public String id;
    public String userId;
    public Instant orderDate;
    public String shippingStreet;
    public String shippingCity;
    public String shippingPostalCode;
    public String shippingCountry;
    public List<Item> items;
    public String paymentStatus;
    public boolean finished;
    public BigDecimal totalAmount;
    public List<Link> links;

    public static class Item {
        public String productId;
        public int quantity;
    }

    public static class Link {
        public String rel;
        public String href;
        public String method;

        public Link(String rel, String href, String method) {
            this.rel = rel;
            this.href = href;
            this.method = method;
        }
    }
}