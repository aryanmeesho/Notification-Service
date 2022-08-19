package com.meesho.notificationservice.exceptions;

import com.meesho.notificationservice.entity.enums.ErrorCodes;

public class NotFoundException extends RuntimeException{
    private final ErrorCodes errorCode;

    public NotFoundException(String message, ErrorCodes errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getCode() {
        return this.errorCode;
    }
}
