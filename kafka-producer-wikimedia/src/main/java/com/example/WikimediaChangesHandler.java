package com.example;

import com.launchdarkly.eventsource.MessageEvent;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class WikimediaChangesHandler implements BackgroundEventHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(WikimediaChangesHandler.class);

    private KafkaTemplate<String, String> kafkaTemplate;
    private String topic;

    public WikimediaChangesHandler(KafkaTemplate<String, String> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void onOpen() throws Exception {
        LOGGER.info("Stream opened");
    }

    @Override
    public void onClosed() throws Exception {
        LOGGER.info("Stream closed");
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) throws Exception {
        LOGGER.info("Event Data -> {}", messageEvent.getData());
        kafkaTemplate.send(topic, messageEvent.getData());
    }

    @Override
    public void onComment(String comment) throws Exception {
    }

    @Override
    public void onError(Throwable t) {
        LOGGER.error("Error receiving stream", t);
    }
}