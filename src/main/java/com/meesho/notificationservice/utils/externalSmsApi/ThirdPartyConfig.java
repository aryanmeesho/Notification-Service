package com.meesho.notificationservice.utils.externalSmsApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.notificationservice.data.request.ExternalSmsRequest;
import com.meesho.notificationservice.exceptions.Response;
import com.meesho.notificationservice.services.kafka.ConsumerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ThirdPartyConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyConfig.class);
    @Autowired
    SmsBuilder smsBuilder;

    @Value("${api.url}")
    private String url;

    @Autowired
    public RestTemplate restTemplate;

    public String thirdPartyApiCall(String id, String phoneNumber, String message) {

        ExternalSmsRequest smsRequest = smsBuilder.buildSmsRequest(id,phoneNumber,message);

        try{
            String response = restTemplate.postForObject(url,smsRequest,String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);

            JsonNode responseNode = rootNode.path("response");
            JsonNode transId =      responseNode.path("transid");

            if(transId.toString().length()>0){
                return mapper.treeToValue(responseNode, Response.class).toString();
            }else{
                return responseNode.get(0).toString();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return "Third Party API Failed";
    }

}