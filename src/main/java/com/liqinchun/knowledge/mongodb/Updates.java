package com.liqinchun.knowledge.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class Updates {
    private static ObjectMapper objectMapper;

    static {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        objectMapper = builder.build();
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static Update from(Object t, Predicate<Object> f) {

        System.out.println(f.and(f).test(t));
        return new Update();
    }

    public static Update from(Update t, UpdateOperation f) {

        f.update(t, "aaa");
        return t;
    }

    public static void main(String[] args) {
        from(null, Objects::isNull);
        UpdateOperation updateOperation = new UpdateOperation() {
            @Override
            public void update(Update update, String prefix) {
                update.set("aad", "aad");
            }
        };
        Update update = from(new Update(), updateOperation);
        System.out.println(update);
    }


}
