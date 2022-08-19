package com.meesho.notificationservice.entity.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSuccessResponse {
    private String request_id;
    private String comment;
}
