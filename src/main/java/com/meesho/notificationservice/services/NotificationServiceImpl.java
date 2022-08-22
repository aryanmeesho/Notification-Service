package com.meesho.notificationservice.services;

import com.meesho.notificationservice.entity.BlacklistNumber;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.entity.enums.ErrorCodes;
import com.meesho.notificationservice.entity.requests.SmsRequest;
import com.meesho.notificationservice.entity.responses.NotificationSuccessResponse;
import com.meesho.notificationservice.exceptions.InvalidRequestException;
import com.meesho.notificationservice.exceptions.ServiceNotAvailableException;
import com.meesho.notificationservice.repositories.BlacklistRepository;
import com.meesho.notificationservice.repositories.NotificationRepository;
import com.meesho.notificationservice.services.kafka.ProducerService;
import com.meesho.notificationservice.utils.externalSmsApi.ThirdPartyConfig;
import com.meesho.notificationservice.utils.validators.PhoneNumberValidator;
import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationService {
    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private ThirdPartyConfig thirdPartyConfig;

    @Autowired
    private ProducerService kafkaProducerService;

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
    public NotificationSuccessResponse save(SmsRequest smsRequestBody) {

        Notification theNotification = new Notification(smsRequestBody.getPhoneNumber(), smsRequestBody.getMessage());

        // PhoneNumber NULL CHECK
        if(PhoneNumberValidator.phoneNumberNullCheck(theNotification.getPhoneNumber()) == true)
            throw new InvalidRequestException("The Phone Number can not be NULL", ErrorCodes.BAD_REQUEST_ERROR);

        // PhoneNumber Valid CHECK
        if (PhoneNumberValidator.isValidNumber(theNotification.getPhoneNumber()) == false)
            throw new InvalidRequestException("The Format of Phone Number must be : +91XXXXXXXXXX", ErrorCodes.BAD_REQUEST_ERROR);

        // Message NULL CHECK
        if(theNotification.getMessage().isEmpty() == true)
            throw new InvalidRequestException("empty message can not be send", ErrorCodes.BAD_REQUEST_ERROR);

        // Blacklist CHECK
        if(redisTemplate.opsForSet().isMember(KEY, theNotification.getPhoneNumber()) == true){
            logger.error("Notification Send Failed: The Number is in Blacklist");

            // updating the db
            theNotification.setStatus("FAILED");
            theNotification.setFailureComment("number is blacklisted");
            theNotification.setFailureCode(HttpStatus.FORBIDDEN.value());
            theNotification.setUpdatedAt(new Date());
            theNotification.setId(UUID.randomUUID().toString());
            notificationRepository.save(theNotification);

            throw new InvalidRequestException("Notification Send Failed: The Number is in Blacklist", ErrorCodes.BAD_REQUEST_ERROR);
        }

        // update theNotification
        theNotification.setId(UUID.randomUUID().toString());
        theNotification.setStatus("IN_PROCESS");

        // save in DB and sending to kafka
        logger.info("Saving Notification to dB");
        try {
            notificationRepository.save(theNotification);
            kafkaProducerService.sendId(theNotification.getId());
        }
        catch (Exception exc){
            throw new InvalidRequestException("Error in sending to kafka : " + exc, ErrorCodes.SERVICE_UNAVAILABLE_ERROR);
        }

        return new NotificationSuccessResponse(theNotification.getId(), "Successfully Sent");
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

    @Override
    public void thirdPartySmsHandler(Notification notification) {
        try{
            String response = thirdPartyConfig.thirdPartyApiCall(String.valueOf(notification.getId()),
                    notification.getPhoneNumber(), notification.getMessage());
            logger.info(response);
        }
        catch(Exception ex){
            logger.error(ex.getMessage());
        }
    }

}
