package com.meesho.notificationservice.services;

import com.meesho.notificationservice.entity.BlacklistNumber;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.repositories.BlacklistRepository;
import com.meesho.notificationservice.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private BlacklistRepository blacklistRepository;

    @Autowired
    public NotificationServiceImpl( NotificationRepository theNotificationRepository, BlacklistRepository theBlacklistRepository){
        notificationRepository = theNotificationRepository;
        blacklistRepository = theBlacklistRepository;
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public void save(Notification theNotificationRequest) {
        notificationRepository.save(theNotificationRequest);
    }

    @Override
    public void blacklist(BlacklistNumber number) {blacklistRepository.save(number); }

    @Override
    public void whitelist(String number) {blacklistRepository.whitelistNumber(number);}

    @Override
    public List<BlacklistNumber> getAllBlacklistedNumbers() {return blacklistRepository.findAll();}

}
