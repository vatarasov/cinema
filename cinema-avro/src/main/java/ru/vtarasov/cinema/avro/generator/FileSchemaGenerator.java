package ru.vtarasov.cinema.avro.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;

@Slf4j
public abstract class FileSchemaGenerator implements SchemaGenerator {
    private final String schemaPath;
    private final String schemaName;

    protected FileSchemaGenerator(String schemaPath,
                                  String schemaName) {
        this.schemaPath = schemaPath;
        this.schemaName = schemaName;
    }

    @Override
    public final void generate() {
        Schema schema = buildSchema();
        saveFile(schema);
    }

    protected abstract Schema buildSchema();

    private void saveFile(Schema schema) {
        Path path = Path.of(schemaPath, schemaName + ".avsc");
        try {
            Files.writeString(path, schema.toString(true));
        } catch (IOException e) {
            log.error("Couldn't write schema: {}", path);
        }
    }
}
