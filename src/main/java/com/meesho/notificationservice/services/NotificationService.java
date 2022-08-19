package com.meesho.notificationservice.services;
import com.meesho.notificationservice.entity.BlacklistNumber;
import com.meesho.notificationservice.entity.Notification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NotificationService {
    public List<Notification> findAll();

    public void save(Notification theNotification);

    public void blacklist(String number);

    public void whitelist(String number);

    public Set getAllBlacklistedNumbers();

    boolean checkIfExist(String number);

    public Optional<Notification> getById(String requestId);
}
