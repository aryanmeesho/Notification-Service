package com.meesho.notificationservice.services.elasticsearch;

import com.meesho.notificationservice.entity.ElasticSearchModal;
import com.meesho.notificationservice.entity.requests.SmsRequestBody;
import com.meesho.notificationservice.entity.responses.SmsResponseBody;
import com.meesho.notificationservice.exceptions.Response;
import com.meesho.notificationservice.repositories.ESRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
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
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.query.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElasticSearchServiceImpl implements  ElasticSearch {
    Logger logger = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);
    public static final int DEFAULT_PAGE_SIZE = 5;

    @Autowired
    public ESRepository esRepository;

    private static final String SMS_INDEX = "sms_requests1";
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<ElasticSearchModal> findAll(){ return (Page<ElasticSearchModal>) esRepository.findAll(); }

    @Override
    public void deleteId(String id){ esRepository.deleteAll();}
    @Override
    public void createSmsIndex(ElasticSearchModal elasticSearchModal) throws Exception{


        logger.info("creating SMS Index");

        try {
            esRepository.save(elasticSearchModal);
        }
        catch (Exception exc){
            logger.info("Exc : " + exc);
        }

        logger.info("SMS Index Created Successfully");
    }

    @Override
    public SmsResponseBody findSmsContainsText(SmsRequestBody smsRequestBody, int pageNo) throws Exception {

        String searchText = smsRequestBody.getSearchText();
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("message", searchText + "*");

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .withPageable(PageRequest.of(pageNo, DEFAULT_PAGE_SIZE))
                .build();

        SearchHits<ElasticSearchModal> smsRecordSearchHits = elasticsearchOperations
                .search(searchQuery, ElasticSearchModal.class, IndexCoordinates.of(SMS_INDEX));

        return SmsResponseBody.builder().data(smsRecordSearchHits.stream()
                .map(SearchHit::getContent).collect(Collectors.toList())).build();
    }

    @Override
    public SmsResponseBody findBetweenTime(SmsRequestBody smsRequestBody, int pageNo) throws Exception {

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


        logger.info("criteria is : " + criteria);

        Query searchQuery = new CriteriaQuery(criteria);
        searchQuery.setPageable(PageRequest.of(pageNo, DEFAULT_PAGE_SIZE));

        logger.info("searchQuery is : " + searchQuery);

            SearchHits<ElasticSearchModal> smsRecordSearchHits = elasticsearchOperations
                    .search(searchQuery, ElasticSearchModal.class, IndexCoordinates.of(SMS_INDEX));

        logger.info("smsRecordSearchHits is : " + smsRecordSearchHits);

        if (smsRecordSearchHits.getTotalHits() <= pageNo * DEFAULT_PAGE_SIZE) {
            throw new Exception("No more results to show");
        }

        return SmsResponseBody.builder().data(smsRecordSearchHits.stream()
                .map(SearchHit::getContent).collect(Collectors.toList())).build();
    }

}
