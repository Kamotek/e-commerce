package com.bffservice.interfaces.rest.controller;

import com.bffservice.application.command.model.CatalogServiceCreateProductCommand;
import com.bffservice.application.command.model.CreateProductCommand;
import com.bffservice.application.command.model.UpdateProductCommand;
import com.bffservice.domain.model.PagedResult;
import com.bffservice.domain.model.Product;
import com.bffservice.interfaces.rest.CatalogServiceClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/catalog/products")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogServiceClient catalogClient;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<UUID> createProduct(
            @Valid @RequestBody CreateProductCommand cmd
    ) {
        try {
            CatalogServiceCreateProductCommand feignCmd = transformToFeignCommand(cmd);
            ResponseEntity<UUID> resp = catalogClient.createProduct(feignCmd);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(resp.getBody())
                    .toUri();
            return ResponseEntity.created(location).body(resp.getBody());

        } catch (JsonProcessingException e) {
            log.error("Error serializing product data to JSON", e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> createProductsBatch(
            @Valid @RequestBody List<CreateProductCommand> cmds
    ) {
        try {
            List<CatalogServiceCreateProductCommand> feignCmds = new ArrayList<>();
            for (CreateProductCommand cmd : cmds) {
                feignCmds.add(transformToFeignCommand(cmd));
            }
            ResponseEntity<Void> resp = catalogClient.createProductsBatch(feignCmds);
            return ResponseEntity.status(resp.getStatusCode()).build();

        } catch (JsonProcessingException e) {
            log.error("Error serializing batch product data to JSON", e);
            return ResponseEntity.status(500).build();
        }
    }


    private CatalogServiceCreateProductCommand transformToFeignCommand(CreateProductCommand cmd) throws JsonProcessingException {
        String imageUrlsJson = objectMapper.writeValueAsString(cmd.getImageUrls() != null ? cmd.getImageUrls() : Collections.emptyList());
        String specificationsJson = objectMapper.writeValueAsString(cmd.getSpecifications() != null ? cmd.getSpecifications() : Collections.emptyMap());

        return CatalogServiceCreateProductCommand.builder()
                .name(cmd.getName())
                .description(cmd.getDescription())
                .price(cmd.getPrice())
                .originalPrice(cmd.getOriginalPrice())
                .category(cmd.getCategory())
                .inventory(cmd.getInventory())
                .brand(cmd.getBrand())
                .badge(cmd.getBadge())
                .imageUrls(imageUrlsJson)
                .specifications(specificationsJson)
                .build();
    }


    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        ResponseEntity<List<Product>> resp = catalogClient.getAllProducts();
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Product>> getFeaturedProducts() {
        ResponseEntity<List<Product>> resp = catalogClient.getAllProductsFeatured();
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(
            @PathVariable UUID id
    ) {
        ResponseEntity<Product> resp = catalogClient.getProductById(id);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductCommand cmd
    ) {
        cmd.setId(id);
        ResponseEntity<Void> resp = catalogClient.updateProduct(id, cmd);
        return ResponseEntity.status(resp.getStatusCode()).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        ResponseEntity<Void> resp = catalogClient.deleteProduct(id);
        return ResponseEntity.status(resp.getStatusCode()).build();
    }

    @GetMapping("/filter")
    public ResponseEntity<PagedResult<Product>> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Boolean availableOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort
    ) {
        ResponseEntity<PagedResult<Product>> resp = catalogClient.filterProducts(
                category,
                maxPrice,
                brand,
                availableOnly,
                page,
                size,
                sort
        );
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }
}