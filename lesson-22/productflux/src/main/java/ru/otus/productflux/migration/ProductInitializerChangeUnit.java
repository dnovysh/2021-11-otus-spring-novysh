package ru.otus.productflux.migration;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.api.annotations.*;
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync;
import io.mongock.driver.mongodb.reactive.util.SubscriberSync;
import lombok.val;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.productflux.model.Category;
import ru.otus.productflux.model.Price;
import ru.otus.productflux.model.Product;

import java.math.BigDecimal;
import java.util.List;

import static ru.otus.productflux.ProductfluxApplication.PRODUCTS_COLLECTION_NAME;

@ChangeUnit(id = "product-initializer", order = "001", author = "dnovysh")
public class ProductInitializerChangeUnit {

    private static final Logger logger = LoggerFactory.getLogger(ProductInitializerChangeUnit.class);

    @BeforeExecution
    public void beforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.createCollection(PRODUCTS_COLLECTION_NAME).subscribe(subscriber);
        subscriber.await();
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection(PRODUCTS_COLLECTION_NAME).drop().subscribe(subscriber);
        subscriber.await();
    }

    @Execution
    public void execution(ClientSession clientSession, MongoDatabase mongoDatabase) {
        SubscriberSync<InsertManyResult> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection(PRODUCTS_COLLECTION_NAME, Product.class)
            .insertMany(clientSession, getProductList())
            .subscribe(subscriber);

        InsertManyResult result = subscriber.getFirst();
        logger.info("ProductInitializerChangeUnit.execution wasAcknowledged: {}", result.wasAcknowledged());
        result.getInsertedIds()
            .forEach((key, value) -> logger.info("update id[{}] : {}", key, value));
    }


    @RollbackExecution
    public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
        SubscriberSync<DeleteResult> subscriber = new MongoSubscriberSync<>();

        mongoDatabase
            .getCollection(PRODUCTS_COLLECTION_NAME, Product.class)
            .deleteMany(clientSession, new Document())
            .subscribe(subscriber);
        DeleteResult result = subscriber.getFirst();
        logger.info("ProductInitializerChangeLog.rollbackExecution wasAcknowledged: {}", result.wasAcknowledged());
        logger.info("ProductInitializerChangeLog.rollbackExecution deleted count: {}", result.getDeletedCount());
    }

    private List<Product> getProductList() {
        val books = new Category(1, "Books");
        val coffeeMugs = new Category(2, "Coffee Mugs");
        val mousePads = new Category(3, "Mouse Pads");

        return List.of(
            Product.builder()
                .sku("BOOK-TECH-1001")
                .name("Become a Guru in JavaScript")
                .description("Learn JavaScript at your own pace...")
                .price(new Price(BigDecimal.valueOf(20.99), "USD"))
                .imageUrl("assets/images/products/books/book-luv2code-1001.png")
                .unitsInStock(100)
                .category(books)
                .build(),
            Product.builder()
                .sku("BOOK-TECH-1002")
                .name("Exploring Vue.js")
                .description("Learn Vue.js at your own pace.")
                .price(new Price(BigDecimal.valueOf(14.99), "USD"))
                .imageUrl("assets/images/products/books/book-luv2code-1002.png")
                .unitsInStock(70)
                .category(books)
                .build(),
            Product.builder()
                .sku("BOOK-TECH-1003")
                .name("Advanced Techniques in Big Data")
                .description("Learn Big Data at your own pace.")
                .price(new Price(BigDecimal.valueOf(13.99), "USD"))
                .imageUrl("assets/images/products/books/book-luv2code-1003.png")
                .unitsInStock(50)
                .category(books)
                .build(),
            Product.builder()
                .sku("COFFEEMUG-1001")
                .name("Coffee Mug - Cherokee")
                .description("Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design.")
                .price(new Price(BigDecimal.valueOf(18.99), "USD"))
                .imageUrl("assets/images/products/coffeemugs/coffeemug-luv2code-1001.png")
                .unitsInStock(35)
                .category(coffeeMugs)
                .build(),
            Product.builder()
                .sku("COFFEEMUG-1002")
                .name("Coffee Mug - Sweeper")
                .description("Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design.")
                .price(new Price(BigDecimal.valueOf(18.99), "USD"))
                .imageUrl("assets/images/products/coffeemugs/coffeemug-luv2code-1002.png")
                .unitsInStock(25)
                .category(coffeeMugs)
                .build(),
            Product.builder()
                .sku("COFFEEMUG-1003")
                .name("Coffee Mug - Aspire")
                .description("Do you love mathematics? If so, then you need this elegant coffee mug with an amazing fractal design.")
                .price(new Price(BigDecimal.valueOf(18.99), "USD"))
                .imageUrl("assets/images/products/coffeemugs/coffeemug-luv2code-1003.png")
                .unitsInStock(60)
                .category(coffeeMugs)
                .build(),
            Product.builder()
                .sku("MOUSEPAD-1001")
                .name("Mouse Pad - Cherokee")
                .description("Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal.")
                .price(new Price(BigDecimal.valueOf(17.99), "USD"))
                .imageUrl("assets/images/products/mousepads/mousepad-luv2code-1001.png")
                .unitsInStock(100)
                .category(mousePads)
                .build(),
            Product.builder()
                .sku("MOUSEPAD-1002")
                .name("Mouse Pad - Sweeper")
                .description("Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal.")
                .price(new Price(BigDecimal.valueOf(17.99), "USD"))
                .imageUrl("assets/images/products/mousepads/mousepad-luv2code-1002.png")
                .unitsInStock(100)
                .category(mousePads)
                .build(),
            Product.builder()
                .sku("MOUSEPAD-1003")
                .name("Mouse Pad - Aspire")
                .description("Fractal images are amazing! You can now own a mouse pad with a unique and amazing fractal.")
                .price(new Price(BigDecimal.valueOf(17.99), "USD"))
                .imageUrl("assets/images/products/mousepads/mousepad-luv2code-1003.png")
                .unitsInStock(100)
                .category(mousePads)
                .build()
        );
    }
}
