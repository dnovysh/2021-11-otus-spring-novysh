package ru.otus.productflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import ru.otus.productflux.repository.ProductReactiveRepository;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackageClasses = {ProductReactiveRepository.class})
public class ProductfluxApplication {

    public final static String PRODUCTS_COLLECTION_NAME = "products";

    public static void main(String[] args) {
        SpringApplication.run(ProductfluxApplication.class, args);
    }

}
