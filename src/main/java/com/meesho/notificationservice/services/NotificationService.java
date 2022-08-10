package com.meesho.notificationservice.services;

import com.meesho.notificationservice.entity.BlacklistNumber;
import com.meesho.notificationservice.entity.Notification;

import java.util.List;

public interface NotificationService {
    public List<Notification> findAll();

    public void save(Notification theNotification);

    public void blacklist(BlacklistNumber number);

    public void whitelist(String number);

    public List<BlacklistNumber>  getAllBlacklistedNumbers();
}
