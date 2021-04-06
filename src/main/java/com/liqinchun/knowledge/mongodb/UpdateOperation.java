package com.liqinchun.knowledge.mongodb;

import org.springframework.data.mongodb.core.query.Update;

import java.util.function.Predicate;

@FunctionalInterface
public interface UpdateOperation {
    void update(Update update, String prefix);
}
