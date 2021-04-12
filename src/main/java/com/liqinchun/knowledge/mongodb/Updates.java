package com.liqinchun.knowledge.mongodb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;

public class Updates {

    private static final UpdatesSupplier DEFAULT_UPDATES_SUPPLIER = Update::set;
    private UpdatesSupplier supplier = DEFAULT_UPDATES_SUPPLIER;
    private ObjectMapper objectMapper;
    private static final Updates INSTANCE = new Updates();

    private Updates() {
        var builder = new Jackson2ObjectMapperBuilder();
        var module = new SimpleModule("ObjectIdM");
        var objectSerializer = new EmbeddedObjectSerializer();
        module.addSerializer(Date.class, objectSerializer);
        module.addSerializer(ObjectId.class, objectSerializer);
        module.addSerializer(ZoneId.class, objectSerializer);
        module.addSerializer(Instant.class, objectSerializer);
        objectMapper =
                builder.modules(module)
                        .serializationInclusion(JsonInclude.Include.ALWAYS)
                        .featuresToEnable(SerializationFeature.INDENT_OUTPUT)
                        .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .build();
    }

    public static Updates newInstance() {
        return new Updates();
    }

    public static Updates getInstance() {
        return INSTANCE;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public Updates setObjectMapper(ObjectMapper mapper) {
        objectMapper = mapper;
        return this;
    }

    public Updates setUpdatesSupplier(UpdatesSupplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public Updates includeNull(boolean includeNull) {
        if (includeNull) {
            this.objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        } else {
            this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        return this;
    }

    public Update convertBeanToUpdate(Object bean, String... exclueds) {
        var map = objectMapper.convertValue(bean, Document.class);
        return convertMapToUpdate(null, map, exclueds);
    }

    public Update convertBeanToUpdate(String prefix, Object bean, String... exclueds) {
        var document = objectMapper.convertValue(bean, Document.class);
        return convertMapToUpdate(prefix, document, exclueds);
    }

    public Update convertMapToUpdate(Map<String, Object> object, String... excludes) {
        return convertMapToUpdate(null, object, excludes);
    }

    public Update convertMapToUpdate(String prefix, Map<String, Object> object, String... exclude) {
        if (object == null || object.size() == 0) {
            return Update.update("updatedAt", Instant.now());
        }
        Update update = new Update();
        convertMapToUpdate(update, prefix, object, exclude);
        if (CollectionUtils.isEmpty(update.getUpdateObject())) {
            update.set("updatedAt", Instant.now());
        }
        return update;
    }

    public void convertMapToUpdate(
            Update update, String prefix, Map<String, Object> object, String... excludes) {
        List<String> excludeList = Arrays.asList(excludes);
        for (var entry : object.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            String fullKey = (prefix == null ? key : prefix + "." + key);
            if (excludeList.contains(fullKey)) {
                continue;
            }
            if (value instanceof Map) {
                //noinspection unchecked
                convertMapToUpdate(update, fullKey, (Map<String, Object>) value, excludes);
            } else {
                supplier.apply(update, fullKey, value);
            }
        }
    }

    public static Update from(Object bean, String... excludes) {
        return from(null, bean, excludes);
    }

    public static Update from(String prefix, Object bean, String... excludes) {
        return getInstance().convertBeanToUpdate(prefix, bean, excludes);
    }

    public Updates setSupplier(UpdatesSupplier supplier) {
        this.supplier = supplier;
        return this;
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

        Update update = from(new Update(), (Update::inc));
        System.out.println(update);
    }
}
