package com.meesho.notificationservice.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    private static final Logger log = LogManager.getLogger(RestTemplateConfig.class);

    @Bean
    public RestTemplate restTemplate() {

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout(3000);
        httpRequestFactory.setConnectTimeout(3000);

        log.info("Rest Template has been initialised!");

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        return restTemplate;
    }
}
