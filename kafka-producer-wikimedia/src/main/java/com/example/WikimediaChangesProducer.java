package com.example;

import com.launchdarkly.eventsource.ConnectStrategy;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import okhttp3.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
public class WikimediaChangesProducer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(WikimediaChangesProducer.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {

        String topic = "wikimedia_recentchange";
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        BackgroundEventHandler eventHandler =
                new WikimediaChangesHandler(kafkaTemplate, topic);

        // v4.x API: build EventSource.Builder using ConnectStrategy with headers
        Headers headers = new Headers.Builder()
                .add("User-Agent", "kafka-wikimedia-producer/1.0 (learning-project)")
                .build();

        EventSource.Builder eventSourceBuilder = new EventSource.Builder(
                ConnectStrategy.http(URI.create(url))
                        .headers(headers)
        );

        BackgroundEventSource eventSource = new BackgroundEventSource.Builder(
                eventHandler, eventSourceBuilder
        ).build();

        eventSource.start();

        TimeUnit.MINUTES.sleep(10);
    }
}