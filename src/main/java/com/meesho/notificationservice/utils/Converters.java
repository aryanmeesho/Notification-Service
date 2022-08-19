package com.meesho.notificationservice.utils;

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
        elasticSearchModal.setId(theNotification.getId());
        elasticSearchModal.setMessage(theNotification.getMessage());
        elasticSearchModal.setPhoneNumber(theNotification.getPhoneNumber());
        elasticSearchModal.setCreatedAt(theNotification.getCreatedAt());
        elasticSearchModal.setFailureCode(theNotification.getFailureCode());
        elasticSearchModal.setFailureComment(theNotification.getFailureComment());
        elasticSearchModal.setUpdatedAt(theNotification.getUpdatedAt());
        elasticSearchModal.setStatus(theNotification.getStatus());
        return elasticSearchModal;
    }
}
