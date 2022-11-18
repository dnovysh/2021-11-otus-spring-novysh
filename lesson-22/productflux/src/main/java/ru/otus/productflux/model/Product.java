package ru.otus.productflux.model;

import lombok.*;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.springframework.data.mongodb.core.mapping.Document;

import static ru.otus.productflux.ProductfluxApplication.PRODUCTS_COLLECTION_NAME;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = PRODUCTS_COLLECTION_NAME)
public class Product {

    @BsonId
    @BsonRepresentation(value = BsonType.OBJECT_ID)
    private String id;

    private String sku;

    private String name;

    private String description;

    private Price price;

    private String imageUrl;

    private Integer unitsInStock;

    private Category category;
}
