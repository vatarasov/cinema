### Cinema Front & Back Microservices

Project consists of three modules:
1. cinema-avro - AVRO serialization/deserialization models, schemes and Java-generators
2. cinema-front - producer of order creation, paid and cancelled events into Kafka broker
3. cinema-back - consumer of that events from Kafka broker and logger its into output stream

## Prerequisites
1. Installed Java 17
2. Installed docker and docker-compose or standalone PostgreSQL 14, Kafka and Zookeeper

## How to install project
1. Run ru.vtarasov.cinema.avro.AvroGenerationApplication to generate AVRO schemes into src/main/resources/application.properties#avro.schema.generate.path property value in IntelliJ Idea
2. mvnw install from cinema project

## How to run project
1. Run docker-compose up -d from cinema project or your standalone PostgreSQL 14, Kafka and Zookeeper
2. Run installed cinema-front-0.0.1-SNAPSHOT.jar - you can see produced recods into Kafka broker in the output stream
3. Run installed cinema-back-0.0.1-SNAPSHOT.jar - you can see consumed and handled recods from Kafka broker in the output stream
