package ru.vtarasov.cinema.front.kafka;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

public class AvroSerializer implements Serializer<Serializable> {
    @Override
    public byte[] serialize(String topic, Serializable data) {
        return SerializationUtils.serialize(data);
    }
}
