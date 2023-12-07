package com.lakesail.flink.samples.basic;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonSchema<T> implements SerializationSchema<T>, DeserializationSchema<T> {
    private static final long serialVersionUID = 1L;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Class<T> type;

    public JsonSchema(Class<T> type) {
        this.type = type;
    }

    @Override
    public byte[] serialize(T value) {
        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws IOException {
        return objectMapper.readValue(bytes, this.type);
    }

    @Override
    public boolean isEndOfStream(T value) {
        return false;
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return TypeInformation.of(this.type);
    }
}
