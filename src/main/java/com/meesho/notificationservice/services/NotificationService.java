package com.meesho.notificationservice.services;
import com.meesho.notificationservice.entity.BlacklistNumber;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.entity.requests.SmsRequest;
import com.meesho.notificationservice.entity.responses.NotificationSuccessResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NotificationService {
    public List<Notification> findAll();

    public NotificationSuccessResponse save(SmsRequest smsRequestBody) throws Exception;

    public void blacklist(String number);

    public void whitelist(String number);

    public Set getAllBlacklistedNumbers();

    public boolean checkIfExist(String number);

    public Optional<Notification> getById(String requestId);

    public void thirdPartySmsHandler(Notification notification);
}
