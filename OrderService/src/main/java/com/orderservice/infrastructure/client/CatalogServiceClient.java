package com.orderservice.infrastructure.client;


import com.orderservice.application.command.model.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "catalog-service", url = "${catalog.service.url}")
public interface CatalogServiceClient {

    @GetMapping("/catalog/products/{id}")
    ProductDTO getProductById(@PathVariable("id") UUID id);
}