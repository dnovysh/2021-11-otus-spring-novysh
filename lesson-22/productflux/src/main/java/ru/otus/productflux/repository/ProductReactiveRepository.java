package ru.otus.productflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.productflux.model.Product;

public interface ProductReactiveRepository extends ReactiveMongoRepository<Product, String> {

    Flux<Product> findAllByOrderBySku();

    Flux<Product> findAllByNameContainsIgnoreCaseOrderBySku(String name);

    Flux<Product> findAllByCategoryNameContainsIgnoreCaseOrderBySku(String categoryName);

    Flux<Product> findAllByNameContainsIgnoreCaseAndCategoryNameContainsIgnoreCaseOrderBySku(
        String name, String categoryName
    );
}
