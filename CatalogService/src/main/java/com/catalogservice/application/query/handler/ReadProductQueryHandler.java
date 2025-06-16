package com.catalogservice.application.query.handler;

import com.catalogservice.domain.model.Product;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.infrastructure.messaging.producer.CatalogReadPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReadProductQueryHandler {
    private final ProductRepository productRepository;
    private final CatalogReadPublisher readPublisher;

    public ReadProductQueryHandler(ProductRepository productRepository,
                                   CatalogReadPublisher readPublisher) {
        this.productRepository = productRepository;
        this.readPublisher = readPublisher;
    }

    public List<Product> handleFindAll() {
        List<Product> products = productRepository.findAll();
        products.forEach(p -> readPublisher.publishReadEvent(p.getId()));
        return products;
    }

    public Optional<Product> handleFindById(UUID id) {
        return Optional.ofNullable(
                productRepository.findById(id)
                        .map(p -> {
                            readPublisher.publishReadEvent(p.getId());
                            return p;
                        }).orElse(null)
        );
    }

    public List<Product> handleFindAllFeatured() {
        List<Product> products = productRepository.findAllFeatured();
        products.forEach(p -> readPublisher.publishReadEvent(p.getId()));
        return products;
    }

    public Page<Product> handleFindByFilters(
            String category,
            BigDecimal maxPrice,
            String brand,
            Boolean availableOnly,
            int page,
            int size,
            String sort
    ) {
        Sort.Order order = Sort.Order.by("id");
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            if (parts.length == 2) {
                order = new Sort.Order(
                        getDirection(parts[1]),
                        parts[0]
                );
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        boolean avail = Boolean.TRUE.equals(availableOnly);

        Page<Product> productPage = productRepository.findByFilters(
                category,
                maxPrice,
                brand,
                avail,
                pageable
        );

        productPage.getContent().forEach(p -> readPublisher.publishReadEvent(p.getId()));
        return productPage;
    }

    private Sort.Direction getDirection(String direction) {
        if ("desc".equalsIgnoreCase(direction)) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}
