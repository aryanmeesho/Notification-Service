package com.meesho.notificationservice.services.elasticsearch;

import com.meesho.notificationservice.entity.ElasticSearchModal;
import com.meesho.notificationservice.entity.enums.ErrorCodes;
import com.meesho.notificationservice.entity.requests.ElasticSearchRequest;
import com.meesho.notificationservice.entity.responses.ElasticSearchResponse;
import com.meesho.notificationservice.exceptions.InvalidRequestException;
import com.meesho.notificationservice.repositories.ESRepository;
import com.meesho.notificationservice.utils.constants.AppConstants;
import com.meesho.notificationservice.utils.validators.PhoneNumberValidator;
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
    public Page<ElasticSearchModal> findAll() throws Exception {
        try {
            return (Page<ElasticSearchModal>) esRepository.findAll();
        }
        catch (Exception exc){
            throw new Exception("findAll query Execution Failed in ESServiceImpl, Error : " + exc.getMessage());
        }
    }

    @Override
    public void deleteId(String id) throws Exception {
        logger.info("Deleting by ID : ", id);
        try {
            esRepository.deleteById(id);
        }
        catch (Exception exc){
            throw new Exception("deleteId Query Execution Failed in ESServiceImpl, Error : " + exc.getMessage());
        }
    }

    @Override
    public void createSmsIndex(ElasticSearchModal elasticSearchModal) throws Exception {
            logger.info("Creating Elastic Search SMS");
            try {
                esRepository.save(elasticSearchModal);
            }
            catch (Exception exc){
                logger.error("Elastic Search Common Error : " + exc.getMessage());
            }
    }

    @Override
    public ElasticSearchResponse findSmsContainsText(ElasticSearchRequest smsRequestBody, int pageNo) throws Exception {

        String searchText = smsRequestBody.getSearchText();

        if(searchText.isEmpty() == true){
            throw new InvalidRequestException("Please Provide the text", ErrorCodes.BAD_REQUEST_ERROR);
        }
        // Building Query
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("message", searchText + "*");

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .withPageable(PageRequest.of(pageNo, DEFAULT_PAGE_SIZE))
                .build();

        // Executing Query
        try {
            SearchHits<ElasticSearchModal> smsRecordSearchHits = elasticsearchOperations
                    .search(searchQuery, ElasticSearchModal.class, IndexCoordinates.of(SMS_INDEX));

            return ElasticSearchResponse.builder().data(smsRecordSearchHits.stream()
                    .map(SearchHit::getContent).collect(Collectors.toList())).build();
        }
        catch (Exception exc){
            throw new Exception("findSmsContainsText Query Execution Failed in ESServiceImpl, Error : " + exc.getMessage());
        }
    }

    @Override
    public ElasticSearchResponse findBetweenTime(ElasticSearchRequest smsRequestBody, int pageNo) throws Exception {

        Date lStartTime = smsRequestBody.getStartTime();
        Date lEndTime = smsRequestBody.getEndTime();

        logger.info("lStartTime : " + lStartTime);
        logger.info("lEndTime : " + lEndTime);

        LocalDateTime startTime = lStartTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endTime = lEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();


        // Error Handling
        if (startTime.isAfter(endTime)) {
            throw new InvalidRequestException("Start time must be before than End Time", ErrorCodes.BAD_REQUEST_ERROR);
        }
        else if(PhoneNumberValidator.phoneNumberNullCheck(smsRequestBody.getPhoneNumber()) == true){
            throw new InvalidRequestException("Please Provide the Phone Number", ErrorCodes.BAD_REQUEST_ERROR);
        }
        else if(PhoneNumberValidator.isValidNumber(smsRequestBody.getPhoneNumber()) == false ){
            throw new InvalidRequestException("The Format of Phone Number must be : +91XXXXXXXXXX", ErrorCodes.BAD_REQUEST_ERROR);
        }


        // Building Query
        Criteria criteria1 = new Criteria ("phoneNumber").matches(smsRequestBody.getPhoneNumber());

        Criteria criteria2 = new Criteria("createdAt")
                .greaterThan(lStartTime)
                .lessThan(lEndTime).and(criteria1);


        Query searchQuery = new CriteriaQuery(criteria2);
        searchQuery.setPageable(PageRequest.of(pageNo, DEFAULT_PAGE_SIZE));


        // Executing the query
        try {
            SearchHits<ElasticSearchModal> smsRecordSearchHits = elasticsearchOperations
                    .search(searchQuery, ElasticSearchModal.class, IndexCoordinates.of(SMS_INDEX));

            return ElasticSearchResponse.builder().data(smsRecordSearchHits.stream()
                    .map(SearchHit::getContent).collect(Collectors.toList())).build();
        }
        catch (Exception exc){
            throw new Exception("FindBetweenTime Query Execution Failed in ESServiceImpl, Error : " + exc.getMessage());
        }

    }

}
