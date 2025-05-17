package com.catalogservice.application.query.handler;

import com.catalogservice.domain.model.Product;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.infrastructure.messaging.producer.CatalogReadPublisher;
import org.springframework.stereotype.Service;

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
        return Optional.ofNullable(productRepository.findById(id)
                .map(p -> {
                    readPublisher.publishReadEvent(p.getId());
                    return p;
                }).orElse(null));
    }

    public List<Product> handleFindAllFeatured() {
        List<Product> products = productRepository.findAllFeatured();
        products.forEach(p -> readPublisher.publishReadEvent(p.getId()));
        return products;
    }
}
