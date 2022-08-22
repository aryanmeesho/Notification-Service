package com.meesho.notificationservice.controllers;

import com.meesho.notificationservice.data.BlacklistNumbers;
import com.meesho.notificationservice.entity.requests.SmsRequest;
import com.meesho.notificationservice.exceptions.InvalidRequestException;
import com.meesho.notificationservice.utils.Converters;
import com.meesho.notificationservice.entity.ElasticSearchModal;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.entity.requests.ElasticSearchRequest;
import com.meesho.notificationservice.entity.responses.*;
import com.meesho.notificationservice.entity.responses.NotificationErrorResponse;
import com.meesho.notificationservice.entity.responses.NotificationSuccessResponse;
import com.meesho.notificationservice.services.NotificationService;
import com.meesho.notificationservice.services.elasticsearch.ElasticSearchService;
import com.meesho.notificationservice.services.kafka.ProducerService;
import com.meesho.notificationservice.utils.validators.PhoneNumberValidator;
import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/v1")
public class NotificationRestController {

    private static final Logger log = LoggerFactory.getLogger(NotificationRestController.class);
    private NotificationService notificationService;
    private ProducerService producerService;
    private ElasticSearchService elasticSearchService;

    @Autowired
    public NotificationRestController(ElasticSearchService theElasticSearchService, NotificationService theNotificationService, ProducerService theProducerService) {
        notificationService = theNotificationService;
        producerService = theProducerService;
        elasticSearchService = theElasticSearchService;
    }

    @GetMapping("/notifications")
    public List<Notification> findAll() {
            return notificationService.findAll();
    }

    @PostMapping("/sms/send")
    public ResponseEntity<Object> addNotificatonRequest(@RequestBody SmsRequest theSmsRequest) {
        try {
            return new ResponseEntity<>(notificationService.save(theSmsRequest), HttpStatus.OK);
        } catch (InvalidRequestException exc) {
            return new ResponseEntity<>(new NotificationErrorResponse("INVALID_REQUEST", exc.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception exc){
            return new ResponseEntity<>(new NotificationErrorResponse("SMS SERVICE DOWN", exc.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/sms/{request_id}")
    public ResponseEntity<Object> getNotificationInfo(@PathVariable("request_id") String requestId){
        try {
            Notification theNotification = notificationService.getById(requestId).get();
            return new ResponseEntity<>(theNotification, HttpStatus.OK);
        }
        catch (Exception exc){
            NotificationErrorResponse error = new NotificationErrorResponse("INVALID_REQUEST", "Request ID Not Found");
            return new ResponseEntity<>(new GetByIdFailureResponse(error), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/blacklist")
    public ResponseEntity<Object> addBlacklist(@RequestBody BlacklistNumbers theBlacklistNumbers){

        List<String> numbers = theBlacklistNumbers.getPhoneNumbers();

        for (String number : numbers) {
            notificationService.blacklist(number);
        }

        return new ResponseEntity<>(new BlacklistSuccessResponse("Successfully blacklisted"), HttpStatus.OK);
    }

    @DeleteMapping("/blacklist")
    public  ResponseEntity<Object> removeBlacklist(@RequestBody BlacklistNumbers theBlacklistNumbers){

        List<String> numbers = theBlacklistNumbers.getPhoneNumbers();

        for (String number : numbers) {
            notificationService.whitelist(number);
        }
        return new ResponseEntity<>(new BlacklistSuccessResponse("Successfully whitelisted"), HttpStatus.OK);

    }


    @GetMapping("/blacklist")
    public ResponseEntity<Object> getBlacklistedNumbers(){
        Set result = notificationService.getAllBlacklistedNumbers();
        return new ResponseEntity<>(new GetAllBlacklistSuccessResponse(result), HttpStatus.OK);
    }

    @GetMapping("/getAllES")
    public Page<ElasticSearchModal> getAllElasticSearchModals() throws Exception {
        return elasticSearchService.findAll();
    }

    @GetMapping("/searchbytext/{page}")
        public ElasticSearchResponse getByText(@PathVariable("page") int page , @RequestBody ElasticSearchRequest smsRequestBody) throws Exception {
          return elasticSearchService.findSmsContainsText(smsRequestBody, page);
    }

    @DeleteMapping("/deleteES/{id}")
    public void deleteByID(@PathVariable("id") String id) throws Exception {
        elasticSearchService.deleteId(id);
    }

    @GetMapping("/searchbytime/{page}")
    public ResponseEntity<ElasticSearchResponse> searchBetween(@PathVariable("page") int pageNo, @RequestBody ElasticSearchRequest smsRequestBody) {
        try {
            return ResponseEntity.ok(elasticSearchService.findBetweenTime(smsRequestBody, pageNo));
        } catch (Exception e) {
            log.error("Invalid Request");
            return ResponseEntity.badRequest().body(ElasticSearchResponse.builder().error(e.getMessage()).build());
        }
    }

}


