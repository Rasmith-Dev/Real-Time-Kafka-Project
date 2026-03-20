# Spring Boot Kafka Real-Time Wikimedia Project

This is a Spring Boot project that demonstrates real-time data streaming using Apache Kafka. The project fetches live Wikimedia recent change events and processes them through a Kafka-based producer-consumer architecture, ultimately storing the data in a database.

## 🏗️ Architecture Overview

The project consists of two main microservices (modules) orchestrated by a parent Maven project:

1. **Wikimedia Producer**:
    - Connects to the [Wikimedia Recent Change Stream](https://stream.wikimedia.org/v2/stream/recentchange) using `EventSource` (Server-Sent Events).
    - Reads the stream in real-time.
    - Publishes the event data to a Kafka topic named `wikimedia_recentchange`.

2. **Wikimedia Consumer**:
    - Listens to the `wikimedia_recentchange` Kafka topic.
    - Consumes the incoming JSON event data.
    - Stores the raw event data into an H2 database using Spring Data JPA.

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot 4.0.3**
- **Apache Kafka** (Local instance)
- **Spring Kafka**
- **Spring Data JPA**
- **H2 Database** (In-memory)
- **Lombok**
- **OkHttp** & **LaunchDarkly EventSource** (for SSE streaming)

## 📋 Prerequisites

Before running the project, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.6+**
- **Apache Kafka** installed and running locally

## ⚙️ Configuration

### Kafka Setup

By default, the applications expect Kafka to be running at `localhost:9092`.

Kafka 4.x uses **KRaft mode** (no Zookeeper required):

1. Format the storage (first time only):
```bash
   kafka-storage.sh format -t $(kafka-storage.sh random-uuid) -c config/kraft/server.properties
```

2. Start Kafka Broker:
```bash
   kafka-server-start.sh config/kraft/server.properties
```

### Database

The consumer module uses an in-memory **H2 database**. Database inserts can be verified via application logs — every insert appears as:
```
Hibernate: insert into wikimedia_recentchange (wiki_event_data,id) values (?,default)
```

## 🚀 How to Run

### 1. Build the project
```bash
mvn clean install
```

### 2. Run the Consumer
```bash
cd kafka-consumer-wikimedia
mvn spring-boot:run
```

The consumer will start listening on port `8081`.

### 3. Run the Producer
```bash
cd kafka-producer-wikimedia
mvn spring-boot:run
```

The producer will start fetching events from Wikimedia and publishing them to Kafka.

### 4. Verify messages in Kafka (optional)
```bash
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic wikimedia_recentchange --from-beginning
```

## 📦 Project Structure
```
.
├── kafka-consumer-wikimedia      # Kafka Consumer Module
│   ├── src/main/java             # Consumer & JPA Repository logic
│   └── src/main/resources        # H2 & Consumer configuration
├── kafka-producer-wikimedia      # Kafka Producer Module
│   ├── src/main/java             # SSE Handler & Kafka Producer logic
│   └── src/main/resources        # Producer configuration
└── pom.xml                       # Parent Maven POM
```

## 📝 Notes

- The producer is configured to run for **10 minutes** by default (`TimeUnit.MINUTES.sleep(10)` in `WikimediaChangesProducer.java`).
- The `wikimedia_recentchange` topic is automatically created by the producer if it does not exist.
- A `User-Agent` header is required when connecting to the Wikimedia stream — without it the server returns a `403 Forbidden` error.
- Both the producer and consumer must be running simultaneously for the full pipeline to work.