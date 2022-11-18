package ru.otus.productflux.config;

import com.mongodb.reactivestreams.client.MongoClient;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongockConfig {

    private final String databaseName;

    public MongockConfig(@Value("${spring.data.mongodb.database}") String databaseName) {
        this.databaseName = databaseName;
    }

    @Bean
    public MongockInitializingBeanRunner getBuilder(MongoClient reactiveMongoClient,
                                                    ApplicationContext context) {
        return MongockSpringboot.builder()
            .setDriver(MongoReactiveDriver.withDefaultLock(reactiveMongoClient, databaseName))
            .addMigrationScanPackage("ru.otus.productflux.migration")
            .setSpringContext(context)
            .setTransactionEnabled(false)
            .buildInitializingBeanRunner();
    }
}
