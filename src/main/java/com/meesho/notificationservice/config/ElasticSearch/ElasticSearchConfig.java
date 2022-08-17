package com.meesho.notificationservice.config.ElasticSearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);
    @Value("${ELASTIC_HOST_AND_PORT}")
    private String elasticHostAndPort;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration =
                ClientConfiguration.builder()
                        .connectedTo(elasticHostAndPort)
                        .build();

        logger.info("Elastic Search connected successfully ");
        return RestClients.create(clientConfiguration).rest();
    }
}