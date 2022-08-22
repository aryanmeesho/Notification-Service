package com.meesho.notificationservice.services.kafka;
import com.meesho.notificationservice.entity.ElasticSearchModal;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.entity.enums.ErrorCodes;
import com.meesho.notificationservice.exceptions.InvalidRequestException;
import com.meesho.notificationservice.exceptions.NotFoundException;
import com.meesho.notificationservice.repositories.NotificationRepository;
import com.meesho.notificationservice.services.NotificationService;
import com.meesho.notificationservice.services.elasticsearch.ElasticSearchService;
import com.meesho.notificationservice.utils.Converters;
import com.meesho.notificationservice.utils.constants.AppConstants;
import com.meesho.notificationservice.utils.externalSmsApi.ThirdPartyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class ConsumerServiceImpl implements  ConsumerService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerServiceImpl.class);

    private NotificationRepository notificationRepository;
    private ElasticSearchService elasticSearchService;

    private Converters converter;

    private NotificationService notificationService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${redis.set}")
    private String KEY;

    @Autowired
    public ConsumerServiceImpl(NotificationService theNotificationService, ElasticSearchService theElasticSearchService, Converters theConverter, NotificationRepository theNotificationRepository) {
        notificationRepository = theNotificationRepository;
        converter = theConverter;
        elasticSearchService = theElasticSearchService;
        notificationService = theNotificationService;
    }

    @Autowired
    private ThirdPartyConfig thirdPartyConfig;

    @KafkaListener(topics = AppConstants.TOPIC_NAME, groupId = AppConstants.GROUP_ID)
    @Override
    public void setId(String id) throws Exception {
        LOGGER.info(String.format("Message received -> %s", id));

        // step1: find notification using ID
        String requestId = id;
        Notification notification = notificationRepository.findById(requestId).get();;


        // step 2: send message on phone no. using 3rd party API
        //  notificationService.thirdPartySmsHandler(notification);


        // step 3: add notification to ElasticSearchIndex
        ElasticSearchModal elasticSearchModal = converter.notiToES(notification);
        elasticSearchService.createSmsIndex(elasticSearchModal);
    }





}
