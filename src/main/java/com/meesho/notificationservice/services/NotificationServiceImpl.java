package com.meesho.notificationservice.services;

import com.meesho.notificationservice.entity.BlacklistNumber;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.entity.enums.ErrorCodes;
import com.meesho.notificationservice.exceptions.InvalidRequestException;
import com.meesho.notificationservice.exceptions.ServiceNotAvailableException;
import com.meesho.notificationservice.repositories.BlacklistRepository;
import com.meesho.notificationservice.repositories.NotificationRepository;
import com.meesho.notificationservice.utils.validators.PhoneNumberValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
        logger.info("finding All Notifications from dB");
        try {
            return notificationRepository.findAll();
        }
        catch (Exception exc){
            throw new ServiceNotAvailableException("Couldn't get from DB, error : " + exc.getMessage(),
                    ErrorCodes.SERVICE_UNAVAILABLE_ERROR);
        }
    }

    @Override
    public void save(Notification theNotificationRequest) {

        if(PhoneNumberValidator.isValidNumber(theNotificationRequest.getPhoneNumber())) {
            logger.info("Saving Notification to dB");
            try {
                theNotificationRequest.setId(UUID.randomUUID().toString());
                notificationRepository.save(theNotificationRequest);
            }
            catch (Exception exc){
                throw new ServiceNotAvailableException("Couldn't save to DB, error : " + exc.getMessage(),
                        ErrorCodes.SERVICE_UNAVAILABLE_ERROR);
            }
        }
        else{
            throw new InvalidRequestException("The Format of Phone Number must be : +91XXXXXXXXXX", ErrorCodes.BAD_REQUEST_ERROR);
        }
    }

    @Override
    public Optional<Notification> getById(String requestId){
        try {
            return notificationRepository.findById(requestId);
        }
        catch (Exception exc){
            throw new ServiceNotAvailableException("Couldn't get from DB, error : " + exc.getMessage(),
                    ErrorCodes.SERVICE_UNAVAILABLE_ERROR);
        }
    }

    @Override
    public void blacklist(String number) {
        logger.info("add to blacklist cache : " + number);

        if(PhoneNumberValidator.phoneNumberNullCheck(number) == true ||
           PhoneNumberValidator.isValidNumber(number) == false){
            throw new InvalidRequestException("The Format of Phone Number must be : +91XXXXXXXXXX", ErrorCodes.BAD_REQUEST_ERROR);
        }

        try {
            redisTemplate.opsForSet().add(KEY, number);
            BlacklistNumber phoneNumber = new BlacklistNumber(number);
            blacklistRepository.save(phoneNumber);
            logger.info("Successfully Blacklisted");
        }
        catch (Exception exc){
            throw new ServiceNotAvailableException( "Couldn't add to cache, error : " + exc.getMessage(),
                    ErrorCodes.SERVICE_UNAVAILABLE_ERROR);
        }
    }

    @Override
    public void whitelist(String number) {
        logger.info("remove to blacklist cache : " + number);
        try {
            redisTemplate.opsForSet().remove(KEY, number);
            blacklistRepository.whitelistNumber(number);
            logger.info("Successfully Whitelisted");
        }
        catch (Exception exc){
            throw new ServiceNotAvailableException("Couldn't remove from cache, error : " + exc.getMessage(),
                    ErrorCodes.SERVICE_UNAVAILABLE_ERROR);
        }
    }

    @Override
    public Set getAllBlacklistedNumbers() {
        logger.info("getAllBlacklistedNumbers");
        try {
            return redisTemplate.opsForSet().members(KEY);
        }
        catch (Exception exc){
            throw new ServiceNotAvailableException("Couldn't get from cache, error : " + exc.getMessage(),
                    ErrorCodes.SERVICE_UNAVAILABLE_ERROR);
        }
    }

    @Override
    public boolean checkIfExist(String number) {
        logger.info("checkIfExist, number = {}",number);
        try {
            return redisTemplate.opsForSet().isMember(KEY, number);
        }
        catch (Exception exc){
            throw new ServiceNotAvailableException("Couldn't check from cache, error : " + exc.getMessage(),
                    ErrorCodes.SERVICE_UNAVAILABLE_ERROR);
        }
    }

}
