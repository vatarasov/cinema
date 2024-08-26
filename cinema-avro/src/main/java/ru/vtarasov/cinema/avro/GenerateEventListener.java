package ru.vtarasov.cinema.avro;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.vtarasov.cinema.avro.generator.SchemaGenerator;

@Service
public class GenerateEventListener {
    private final String schemaPath;
    private final List<SchemaGenerator> generators;

    public GenerateEventListener(@Value("${avro.schema.generate.path}") String schemaPath,
                                 List<SchemaGenerator> generators) {
        this.schemaPath = schemaPath;
        this.generators = generators;
    }

    @EventListener
    public void generate(ContextRefreshedEvent event) throws Exception {
        cleanUp(schemaPath);
        generators.forEach(SchemaGenerator::generate);
    }

    private void cleanUp(String schemaPath) throws IOException {
        FileUtils.cleanDirectory(new File(schemaPath));
    }
}
