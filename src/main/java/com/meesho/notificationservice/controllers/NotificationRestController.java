package com.meesho.notificationservice.controllers;

import com.meesho.notificationservice.data.BlacklistNumbers;
import com.meesho.notificationservice.data.Converters;
import com.meesho.notificationservice.entity.ElasticSearchModal;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.entity.requests.SmsRequestBody;
import com.meesho.notificationservice.entity.responses.SmsResponseBody;
import com.meesho.notificationservice.exceptions.NotificationErrorResponse;
import com.meesho.notificationservice.exceptions.NotificationSuccessResponse;
import com.meesho.notificationservice.services.NotificationService;
import com.meesho.notificationservice.services.elasticsearch.ElasticSearchServiceImpl;
import com.meesho.notificationservice.services.kafka.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1")
public class NotificationRestController {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationRestController.class);
    private NotificationService notificationService;
    private ProducerService producerService;

    private Converters converter;

    private ElasticSearchServiceImpl elasticSearchService;
    @Autowired
    public NotificationRestController(Converters theConverter, ElasticSearchServiceImpl theElasticSearchService, NotificationService theNotificationService, ProducerService theProducerService) {
        notificationService = theNotificationService;
        producerService = theProducerService;
        converter = theConverter;
        elasticSearchService = theElasticSearchService;
    }

    @GetMapping("/notifications")
    public List<Notification> findAll() {
        List<Notification> result = notificationService.findAll();
        System.out.println(result);
        return result;
    }

    @PostMapping("/sms/send")
    public ResponseEntity<Object> addNotificatonRequest(@RequestBody Notification theNotification) throws Exception {


          if(theNotification.getPhoneNumber() == null || theNotification.getPhoneNumber().length() == 0){
              NotificationErrorResponse error = new NotificationErrorResponse("INVALID_REQUEST", "Phone Number is mandatory");
              return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
          }

          // add notification to db
          notificationService.save(theNotification);
          int requestId = theNotification.getId();

          // send id to kafka producer
          producerService.sendId(String.valueOf(requestId));

          // add notification to ES
          ElasticSearchModal elasticSearchModal = converter.notiToES(theNotification);
          elasticSearchService.createSmsIndex(elasticSearchModal);

          NotificationSuccessResponse data = new NotificationSuccessResponse(requestId, "Successfully Sent");
          return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/sms/{request_id}")
    public ResponseEntity<Object> getNotificationInfo(@PathVariable("request_id") int requestId){
        Notification theNotification = new Notification();

        try {
             theNotification = notificationService.findAll().get(requestId - 1);
        }
        catch (Exception exc){
            NotificationErrorResponse error = new NotificationErrorResponse("INVALID_REQUEST", "Request ID Not Found");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

           return new ResponseEntity<>(theNotification, HttpStatus.OK);
    }


    @PostMapping("/blacklist")
    public ResponseEntity<String> addBlacklist(@RequestBody BlacklistNumbers theBlacklistNumbers){

        List<String> numbers = theBlacklistNumbers.getPhoneNumbers();

        for (String number : numbers) {
            notificationService.blacklist(number);
            System.out.println(number);
        }

        return new ResponseEntity<>("Successfully blacklisted", HttpStatus.OK);
    }

    @DeleteMapping("/blacklist")
    public  ResponseEntity<String> removeBlacklist(@RequestBody BlacklistNumbers theBlacklistNumbers){
        // unblock these numbers
        List<String> numbers = theBlacklistNumbers.getPhoneNumbers();

        for (String number : numbers) {
            notificationService.whitelist(number);
        }
        return new ResponseEntity<>("Successfully whitelisted", HttpStatus.OK);

    }


    @GetMapping("/blacklist")
    public Set getBlacklistedNumbers(){
       // List<String> blacklistNumbers = new ArrayList<>();

        Set result = notificationService.getAllBlacklistedNumbers();
//        for(BlacklistNumber number: result){
//              blacklistNumbers.add(number.getPhoneNumber());
//        }
        return result;
    }

    @GetMapping("/getAllES")
    public Page<ElasticSearchModal> getAllElasticSearchModals(){
        return elasticSearchService.findAll();
    }

    @GetMapping("/searchbytext/{page}")
        public SmsResponseBody getByText(@PathVariable("page") int page ,@RequestBody SmsRequestBody smsRequestBody) throws Exception {
          return elasticSearchService.findSmsContainsText(smsRequestBody, page);
    }

    @DeleteMapping("/deleteES/{id}")
    public void deleteByID(@PathVariable("id") String id){
        elasticSearchService.deleteId(id);
    }

    @GetMapping("/searchbytime/{page}")
    public ResponseEntity<SmsResponseBody> searchBetween(@PathVariable("page") int pageNo, @RequestBody SmsRequestBody smsRequestBody) {
        try {
            return ResponseEntity.ok(elasticSearchService.findBetweenTime(smsRequestBody, pageNo));
        } catch (Exception e) {
            LOG.error("Invalid Request");
            return ResponseEntity.badRequest().body(SmsResponseBody.builder().error(e.getMessage()).build());
        }
    }

}


