package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    // Получаем значение из переменной окружения, чтобы определить, это экземпляр для записи или чтения
    @Value("${app.instance.role}")
    private String instanceRole;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Product> getAllProducts() {
        if ("writer".equalsIgnoreCase(instanceRole)) {
            return Flux.error(new RuntimeException("This instance is write-only"));
        }
        return repository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Product> createProduct(@RequestBody Product product) {
        if (!"writer".equalsIgnoreCase(instanceRole)) {
            return Mono.error(new RuntimeException("This instance is read-only"));
        }
        return repository.save(product);
    }
}
