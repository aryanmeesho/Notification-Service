package com.meesho.notificationservice.data;

import com.meesho.notificationservice.entity.ElasticSearchModal;
import com.meesho.notificationservice.entity.Notification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Converters {

    public Converters() {
    }

    public ElasticSearchModal notiToES(Notification theNotification){
        ElasticSearchModal elasticSearchModal = new ElasticSearchModal();
        elasticSearchModal.setId(String.valueOf(theNotification.getId()));
        elasticSearchModal.setMessage(theNotification.getMessage());
        elasticSearchModal.setPhoneNumber(theNotification.getPhoneNumber());
        elasticSearchModal.setCreatedAt(theNotification.getCreatedAt());
        elasticSearchModal.setFailureCode(theNotification.getFailureCode());
        elasticSearchModal.setFailureComment(theNotification.getFailureComment());
        return elasticSearchModal;
    }
}
