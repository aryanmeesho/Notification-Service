package com.meesho.notificationservice.services.elasticsearch;

import com.meesho.notificationservice.entity.ElasticSearchModal;
import com.meesho.notificationservice.entity.requests.SmsRequestBody;
import com.meesho.notificationservice.entity.responses.SmsResponseBody;
import org.springframework.data.domain.Page;

public interface ElasticSearch {

    public void createSmsIndex(ElasticSearchModal elasticSearchModal) throws Exception;
    public SmsResponseBody findSmsContainsText(SmsRequestBody smsRequestBody, int pageNo) throws Exception;

    public Page<ElasticSearchModal> findAll();
    public void deleteId(String id);

    public SmsResponseBody findBetweenTime(SmsRequestBody smsRequestBody, int pageNo) throws Exception;

}
