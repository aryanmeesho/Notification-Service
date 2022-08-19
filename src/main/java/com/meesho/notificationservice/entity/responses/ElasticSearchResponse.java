package com.meesho.notificationservice.entity.responses;

import com.meesho.notificationservice.entity.ElasticSearchModal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticSearchResponse {

    private List<ElasticSearchModal> data;
    private String comments;
    private String error;
}