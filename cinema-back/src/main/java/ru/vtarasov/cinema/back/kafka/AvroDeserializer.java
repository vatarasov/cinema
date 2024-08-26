package ru.vtarasov.cinema.back.kafka;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

public class AvroDeserializer implements Deserializer<Serializable> {
    @Override
    public Serializable deserialize(String topic, byte[] data) {
        return SerializationUtils.deserialize(data);
    }
}
