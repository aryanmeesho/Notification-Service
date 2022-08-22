package com.meesho.notificationservice.services.elasticsearch;

import com.meesho.notificationservice.entity.ElasticSearchModal;
import com.meesho.notificationservice.entity.requests.ElasticSearchRequest;
import com.meesho.notificationservice.entity.responses.ElasticSearchResponse;
import org.springframework.data.domain.Page;

public interface ElasticSearchService {

    public void createSmsIndex(ElasticSearchModal elasticSearchModal) throws Exception;
    public ElasticSearchResponse findSmsContainsText(ElasticSearchRequest smsRequestBody, int pageNo) throws Exception;

    public Page<ElasticSearchModal> findAll() throws Exception;
    public void deleteId(String id) throws Exception;

    public ElasticSearchResponse findBetweenTime(ElasticSearchRequest smsRequestBody, int pageNo) throws Exception;

}
