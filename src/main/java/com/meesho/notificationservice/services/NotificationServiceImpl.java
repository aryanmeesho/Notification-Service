package com.meesho.notificationservice.services;

import com.meesho.notificationservice.entity.BlacklistNumber;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.repositories.BlacklistRepository;
import com.meesho.notificationservice.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.List;
import java.util.Set;

@Service
public class NotificationServiceImpl implements NotificationService {
    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${redis.set}")
    private String KEY;

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public void save(Notification theNotificationRequest) {
        notificationRepository.save(theNotificationRequest);
    }

    @Override
    public void blacklist(String number) {
        logger.info("add to blacklist cache : " + number);
        redisTemplate.opsForSet().add(KEY, number);
        BlacklistNumber phoneNumber = new BlacklistNumber(number);
        blacklistRepository.save(phoneNumber);
    }

    @Override
    public void whitelist(String number) {
        logger.info("remove to blacklist cache : " + number);
        redisTemplate.opsForSet().remove(KEY,number);
        blacklistRepository.whitelistNumber(number);
    }

    @Override
    public Set getAllBlacklistedNumbers() {
        logger.info("getAllBlacklistedNumbers");
        return redisTemplate.opsForSet().members(KEY) ;
    }

    @Override
    public boolean checkIfExist(String number) {
        logger.info("checkIfExist, number = {}",number);
            return redisTemplate.opsForSet().isMember(KEY, number);
    }

}
