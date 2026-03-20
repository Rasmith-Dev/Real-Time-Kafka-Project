package com.example;

import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

@Service
public class KafkaDatabaseConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaDatabaseConsumer.class);

    private final WikimediaDataRepository repository;

    public KafkaDatabaseConsumer(WikimediaDataRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "wikimedia_recentchange", groupId = "myGroup")
    public void consume(String eventMessage) {
        logger.info(String.format("Event message Received -> %s", eventMessage));

        WikimediaData wikimediaData = new WikimediaData();
        wikimediaData.setWikiEventData(eventMessage);
        repository.save(wikimediaData);
    }
}
