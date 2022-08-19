package com.meesho.notificationservice.services.elasticsearch;

import com.meesho.notificationservice.entity.ElasticSearchModal;
import com.meesho.notificationservice.entity.requests.ElasticSearchRequest;
import com.meesho.notificationservice.entity.responses.ElasticSearchResponse;
import com.meesho.notificationservice.repositories.ESRepository;
import com.meesho.notificationservice.utils.constants.AppConstants;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.query.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class ElasticSearchServiceServiceImpl implements ElasticSearchService {
    Logger logger = LoggerFactory.getLogger(ElasticSearchServiceServiceImpl.class);
    public static final int DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;

    @Autowired
    public ESRepository esRepository;

    private static final String SMS_INDEX = AppConstants.SMS_INDEX;

    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticSearchServiceServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public Page<ElasticSearchModal> findAll(){ return (Page<ElasticSearchModal>) esRepository.findAll(); }

    @Override
    public void deleteId(String id){ esRepository.deleteAll();}
    @Override
    public void createSmsIndex(ElasticSearchModal elasticSearchModal) {
            logger.info("Creating Elastic Search SMS");
            try {
                ElasticSearchModal response = esRepository.save(elasticSearchModal);
            }
            catch (Exception exc){
                logger.info("Some error: " + exc);
            }
    }

    @Override
    public ElasticSearchResponse findSmsContainsText(ElasticSearchRequest smsRequestBody, int pageNo) throws Exception {

        String searchText = smsRequestBody.getSearchText();
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("message", searchText + "*");

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .withPageable(PageRequest.of(pageNo, DEFAULT_PAGE_SIZE))
                .build();

        SearchHits<ElasticSearchModal> smsRecordSearchHits = elasticsearchOperations
                .search(searchQuery, ElasticSearchModal.class, IndexCoordinates.of(SMS_INDEX));

        return ElasticSearchResponse.builder().data(smsRecordSearchHits.stream()
                .map(SearchHit::getContent).collect(Collectors.toList())).build();
    }

    @Override
    public ElasticSearchResponse findBetweenTime(ElasticSearchRequest smsRequestBody, int pageNo) throws Exception {

        Date lStartTime = smsRequestBody.getStartTime();
        Date lEndTime = smsRequestBody.getEndTime();

        logger.info("lStartTime : " + lStartTime);
        logger.info("lEndTime : " + lEndTime);

        LocalDateTime startTime = lStartTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endTime = lEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();


        if (startTime.isAfter(endTime)) {
            throw new Exception("Start Time has to be lesser than End Time");
        }

        Criteria criteria = new Criteria("createdAt")
                .greaterThan(lStartTime)
                .lessThan(lEndTime);

        Query searchQuery = new CriteriaQuery(criteria);
        searchQuery.setPageable(PageRequest.of(pageNo, DEFAULT_PAGE_SIZE));


        SearchHits<ElasticSearchModal> smsRecordSearchHits = elasticsearchOperations
                    .search(searchQuery, ElasticSearchModal.class, IndexCoordinates.of(SMS_INDEX));

//        if (smsRecordSearchHits.getTotalHits() <= pageNo * DEFAULT_PAGE_SIZE) {
//            throw new Exception("No more results to show");
//        }

        return ElasticSearchResponse.builder().data(smsRecordSearchHits.stream()
                .map(SearchHit::getContent).collect(Collectors.toList())).build();
    }

}
