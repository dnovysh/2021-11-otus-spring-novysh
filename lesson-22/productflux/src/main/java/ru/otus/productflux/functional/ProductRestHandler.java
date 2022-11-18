package ru.otus.productflux.functional;

import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.productflux.model.Price;
import ru.otus.productflux.model.Product;
import ru.otus.productflux.repository.ProductReactiveRepository;

import java.util.Collections;
import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class ProductRestHandler {

    private final ProductReactiveRepository productRepository;

    private final Mono<ServerResponse> responseNotFound = ServerResponse.notFound().build();

    private final Mono<ServerResponse> responseBadRequest = ServerResponse.badRequest().build();

    public ProductRestHandler(ProductReactiveRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<ServerResponse> getProductById(ServerRequest request) {
        String id = request.pathVariable("id");
        return productRepository.findById(id)
            .flatMap(product ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromValue(product))
            ).switchIfEmpty(responseNotFound);
    }

    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        val optionalName = request.queryParam("name");
        val optionalCategoryName = request.queryParam("categoryName");

        if (optionalName.isPresent() && optionalCategoryName.isPresent()) {
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productRepository.findAllByNameContainsIgnoreCaseAndCategoryNameContainsIgnoreCaseOrderBySku(
                    optionalName.get(), optionalCategoryName.get()
                ), Product.class);
        }

        if (optionalName.isPresent()) {
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productRepository.findAllByNameContainsIgnoreCaseOrderBySku(
                    optionalName.get()
                ), Product.class);
        }

        //noinspection OptionalIsPresent
        if (optionalCategoryName.isPresent()) {
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productRepository.findAllByCategoryNameContainsIgnoreCaseOrderBySku(
                    optionalCategoryName.get()
                ), Product.class);
        }

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(productRepository.findAllByOrderBySku(), Product.class);
    }

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        Mono<Product> unsavedProduct = request.bodyToMono(Product.class);
        return unsavedProduct
            .flatMap(product -> productRepository.save(product)
                .flatMap(savedProduct -> ServerResponse.accepted()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromValue(savedProduct)))
            ).switchIfEmpty(responseBadRequest);
    }

    public Mono<ServerResponse> updateProductPrice(ServerRequest request) {
        Mono<Price> price$ = request.bodyToMono(Price.class);
        val id = request.pathVariable("id");

        Mono<Product> updatedProduct$ = price$.flatMap(price ->
            productRepository.findById(id)
                .flatMap(productForUpdate -> {
                    productForUpdate.setPrice(price);
                    return productRepository.save(productForUpdate);
                })
        );

        return updatedProduct$.flatMap(product ->
            ServerResponse.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(product))
        ).switchIfEmpty(responseNotFound);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Void> deleted = productRepository.deleteById(id);
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(deleted, Void.class);
    }

    public Mono<ServerResponse> index(final ServerRequest request) {
        final Map<String, Object> model =
            Collections.singletonMap("products", productRepository.findAllByOrderBySku());
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("index", model);
    }
}
