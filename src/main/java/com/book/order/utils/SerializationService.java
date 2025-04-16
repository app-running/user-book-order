package com.book.order.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class SerializationService {
    private final ObjectMapper mapper;

    public SerializationService(final ObjectMapper mapper) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.writerWithDefaultPrettyPrinter();
        this.mapper = mapper;
    }

    @SneakyThrows
    public <T> T convertToObject(final String json, Class<T> clazz) {
        return mapper.readValue(json, clazz);
    }
}
