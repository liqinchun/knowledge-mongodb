package com.liqinchun.knowledge.mongodb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EmbeddedObjectSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ObjectCodec codec = jsonGenerator.getCodec();
        jsonGenerator.setCodec(null);
        jsonGenerator.writeObject(o);
        jsonGenerator.setCodec(codec);
    }
}
