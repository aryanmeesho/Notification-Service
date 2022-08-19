package com.meesho.notificationservice.entity.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThirdPartyApiResponse {
    private String code;
    private String description;
    private String transid;
}