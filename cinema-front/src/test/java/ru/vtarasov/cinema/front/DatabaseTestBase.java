package ru.vtarasov.cinema.front;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureEmbeddedDatabase
@ActiveProfiles("test")
public abstract class DatabaseTestBase {
}
