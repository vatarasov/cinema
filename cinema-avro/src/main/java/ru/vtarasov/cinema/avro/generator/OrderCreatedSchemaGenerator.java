package ru.vtarasov.cinema.avro.generator;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedSchemaGenerator extends FileSchemaGenerator {
    public OrderCreatedSchemaGenerator(@Value("${avro.schema.generate.path}") String schemaPath,
                                       @Value("${avro.schema.order-creared.name}")String schemaName) {
        super(schemaPath, schemaName);
    }

    @Override
    public Schema buildSchema() {
        return SchemaBuilder.record("OrderEventDto")
                .namespace("generated.ru.vtarasov.cinema.avro.dto")
                .fields()
                .name("film")
                .type()
                .stringType()
                .noDefault()
                .name("price")
                .type()
                .intType()
                .noDefault()
                .endRecord();
    }
}
