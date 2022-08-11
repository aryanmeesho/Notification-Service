package com.meesho.notificationservice.services.kafka;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.repositories.NotificationRepository;
import com.meesho.notificationservice.utils.constants.AppConstants;
import com.meesho.notificationservice.utils.externalSmsApi.ThirdPartyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;

@Service
public class ConsumerServiceImpl implements  ConsumerService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerServiceImpl.class);

    private NotificationRepository notificationRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${redis.set}")
    private String KEY;

    @Autowired
    public ConsumerServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Autowired
    private ThirdPartyConfig thirdPartyConfig;


    @KafkaListener(topics = AppConstants.TOPIC_NAME, groupId = AppConstants.GROUP_ID)
    @Override
    public void setId(String id) {
        LOGGER.info(String.format("Message received -> %s", id));

        // step1: find notification using ID
        int requestId = Integer.valueOf(id);
        Notification notification = notificationRepository.findAll().get(requestId-1);

        //step2: check whether the phone no. is blacklisted or not using redis
        if(redisTemplate.opsForSet().isMember(KEY, notification.getPhoneNumber()) == true){
             LOGGER.info("IT IS BLACKLISTED");
           //  return new ResponseEntity<>("The Phone Number is blacklisted", HttpStatus.OK);
        }


        //step3: send message on phone no. using 3rd party API
        try{
            String response = thirdPartyConfig.thirdPartyApiCall(String.valueOf(notification.getId()),
                    notification.getPhoneNumber(), notification.getMessage());

            LOGGER.info(response);

        }catch(Exception ex){
            LOGGER.error(ex.getMessage());
            LOGGER.info("Can't make it");
        }

    }





}
