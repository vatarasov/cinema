package ru.vtarasov.cinema.avro.generator;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderFinishedSchemaGenerator extends FileSchemaGenerator {
    public OrderFinishedSchemaGenerator(@Value("${avro.schema.generate.path}") String schemaPath,
                                        @Value("${avro.schema.order-finished.name}")String schemaName) {
        super(schemaPath, schemaName);
    }

    @Override
    public Schema buildSchema() {
        return SchemaBuilder.record("OrderFinishedDto")
                .namespace("generated.ru.vtarasov.cinema.avro.dto")
                .fields()
                .name("state")
                .type()
                .stringType()
                .noDefault()
                .endRecord();
    }
}
