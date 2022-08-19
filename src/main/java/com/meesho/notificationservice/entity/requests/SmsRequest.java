package com.meesho.notificationservice.entity.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequest {
    public String phoneNumber;
    public String message;
}
