package com.meesho.notificationservice.entity.responses;

import com.meesho.notificationservice.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByIdSuccessResponse {
    private Notification data;
}
