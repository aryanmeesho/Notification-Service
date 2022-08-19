package com.meesho.notificationservice.entity.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByIdFailureResponse {
    private NotificationErrorResponse error;
}
