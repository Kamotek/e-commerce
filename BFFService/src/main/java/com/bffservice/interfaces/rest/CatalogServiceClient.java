package com.bffservice.interfaces.rest;

import com.bffservice.application.command.model.CreateProductCommand;
import com.bffservice.application.command.model.UpdateProductCommand;
import com.bffservice.domain.model.Product;
import com.bffservice.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "catalog-service", url = "${catalog.service.url}", configuration = FeignConfig.class)
public interface CatalogServiceClient {

    @PostMapping("/catalog/products")
    ResponseEntity<UUID> createProduct(@RequestBody CreateProductCommand cmd);

    @GetMapping("/catalog/products")
    ResponseEntity<List<Product>> getAllProducts();

    @GetMapping("/catalog/products/featured")
    ResponseEntity<List<Product>> getAllProductsFeatured();

    @GetMapping("/catalog/products/{id}")
    ResponseEntity<Product> getProductById(@PathVariable("id") UUID id);

    @PutMapping("/catalog/products/{id}")
    ResponseEntity<Void> updateProduct(@PathVariable("id") UUID id,
                                       @RequestBody UpdateProductCommand cmd);
}
