package com.meesho.notificationservice.config.ElasticSearch;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    Logger log = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Value("${ELASTIC_HOST_AND_PORT}")
    private String elasticHostAndPort;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration =
                ClientConfiguration.builder()
                        .connectedTo(elasticHostAndPort)
                        .build();

        log.info("Elastic Service Configured Successfully");
        return RestClients.create(clientConfiguration).rest();
    }

}