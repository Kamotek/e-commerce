package com.bffservice.interfaces.rest;

import com.bffservice.application.command.model.CatalogServiceCreateProductCommand;
import com.bffservice.application.command.model.UpdateProductCommand;
import com.bffservice.domain.model.PagedResult;
import com.bffservice.domain.model.Product;
import com.bffservice.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "catalog-service", url = "${catalog.service.url}", configuration = FeignConfig.class)
public interface CatalogServiceClient {

    @PostMapping("/catalog/products")
    ResponseEntity<UUID> createProduct(@RequestBody CatalogServiceCreateProductCommand cmd);

    @PostMapping("/catalog/products/batch")
    ResponseEntity<Void> createProductsBatch(@RequestBody List<CatalogServiceCreateProductCommand> cmds);

    @GetMapping("/catalog/products")
    ResponseEntity<List<Product>> getAllProducts();

    @GetMapping("/catalog/products/featured")
    ResponseEntity<List<Product>> getAllProductsFeatured();

    @GetMapping("/catalog/products/{id}")
    ResponseEntity<Product> getProductById(@PathVariable("id") UUID id);

    @PutMapping("/catalog/products/{id}")
    ResponseEntity<Void> updateProduct(@PathVariable("id") UUID id,
                                       @RequestBody UpdateProductCommand cmd);

    @DeleteMapping("/catalog/products/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable("id") UUID id);

    @GetMapping("/catalog/products/filter")
    ResponseEntity<PagedResult<Product>> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Boolean availableOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, name = "sort") String sort
    );
}