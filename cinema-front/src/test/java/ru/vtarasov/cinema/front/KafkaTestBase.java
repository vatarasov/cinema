package ru.vtarasov.cinema.front;


import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.kafka.KafkaContainer;

public abstract class KafkaTestBase extends DatabaseTestBase {
    @Container
    public static KafkaContainer KAFKA = new KafkaContainer("apache/kafka:3.8.0")
            .withExposedPorts(9092)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(9092), new ExposedPort(9092)))
            ));
}
