package ru.otus.productflux.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoClientConfig {

    private final String mongodbConnectionString;

    public MongoClientConfig(
        @Value("${spring.data.mongodb.uri}") String uri
    ) {
        mongodbConnectionString = uri;
    }

    @Bean
    MongoClient mongoClient() {
        CodecRegistry codecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        return MongoClients.create(MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(mongodbConnectionString))
            .codecRegistry(codecRegistry)
            .build());
    }
}
