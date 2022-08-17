package com.meesho.notificationservice.repositories;

import com.meesho.notificationservice.entity.ElasticSearchModal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface ESRepository extends ElasticsearchRepository<ElasticSearchModal, String> {

}
