package com.catalogservice.application.command.handler;

import com.catalogservice.application.command.model.DeleteProductCommand;
import com.catalogservice.domain.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DeleteProductCommandHandler {
    private final ProductRepository productRepository;

    public void handle(DeleteProductCommand command) {
        productRepository.deleteProductById(command.getId());
    }
}
