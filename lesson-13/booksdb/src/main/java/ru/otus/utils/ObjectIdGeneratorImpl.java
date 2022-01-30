package ru.otus.utils;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class ObjectIdGeneratorImpl implements ObjectIdGenerator {
    @Override
    public String getObjectId() {
        return ObjectId.get().toString();
    }
}
