package com.meesho.notificationservice.services.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService{

    Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

    @Value("${kafka.topic}")
    private String TOPIC;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendId(String id) throws Exception {
        logger.info(String.format("id sent -> %s", id));

        try {
            kafkaTemplate.send(TOPIC, id);
        }
        catch (Exception exc){
            throw new Exception("Kafka Producer Failed, Error : " + exc.getMessage());
        }
    }

}
