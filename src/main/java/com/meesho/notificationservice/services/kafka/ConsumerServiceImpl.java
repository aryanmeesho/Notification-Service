package com.meesho.notificationservice.services.kafka;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.repositories.NotificationRepository;
import com.meesho.notificationservice.utils.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl implements  ConsumerService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerServiceImpl.class);

    private NotificationRepository notificationRepository;

    @Autowired
    public ConsumerServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = AppConstants.TOPIC_NAME, groupId = AppConstants.GROUP_ID)
    @Override
    public void setId(String id) {
        LOGGER.info(String.format("Message received -> %s", id));

        // step1: find notification using ID
        int requestId = Integer.valueOf(id);
        Notification notification = notificationRepository.findAll().get(requestId);

        // TODO:: step2: check whether the phone no. is valid or not and blacklisted or not using redis

        // TODO:: step3: send message on phone no. using 3rd party API
    }





}
