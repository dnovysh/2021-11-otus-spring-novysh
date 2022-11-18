package ru.otus.productflux.functional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ProductRestRouter {

    @Bean
    public RouterFunction<ServerResponse> routeProduct(ProductRestHandler productRestHandler) {
        return RouterFunctions
            .route(GET("/func/products"), productRestHandler::getAllProducts)
            .andRoute(GET("/func/products/{id}"), productRestHandler::getProductById)
            .andRoute(POST("/func/products"), productRestHandler::addProduct)
            .andRoute(PUT("func/products/{id}/price"), productRestHandler::updateProductPrice)
            .andRoute(DELETE("/func/products/{id}"), productRestHandler::deleteProduct)
            .andRoute(GET("/"), productRestHandler::index);
    }
}
