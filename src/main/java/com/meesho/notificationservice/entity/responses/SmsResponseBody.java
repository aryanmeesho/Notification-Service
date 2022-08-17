package com.meesho.notificationservice.entity.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meesho.notificationservice.entity.ElasticSearchModal;
import com.meesho.notificationservice.entity.Notification;
import com.meesho.notificationservice.services.elasticsearch.ElasticSearch;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SmsResponseBody {
    @JsonProperty("data")
    private List<ElasticSearchModal> data;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("error")
    private String error;

    public SmsResponseBody() {
    }

    public SmsResponseBody(List<ElasticSearchModal> data, String comments,String error) {
        this.data = data;
        this.comments = comments;
        this.error=error;
    }
}