package com.meesho.notificationservice.services.kafka;

import org.springframework.stereotype.Service;

public interface ProducerService {
    void sendId(String id) throws Exception;
}
