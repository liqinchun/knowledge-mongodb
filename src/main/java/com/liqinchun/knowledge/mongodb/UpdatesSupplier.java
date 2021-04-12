package com.liqinchun.knowledge.mongodb;

import org.springframework.data.mongodb.core.query.Update;

@FunctionalInterface
public interface UpdatesSupplier {

    public void apply(Update update, String key, Object value);
}
