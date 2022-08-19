package com.meesho.notificationservice.exceptions;

import com.meesho.notificationservice.entity.enums.ErrorCodes;

public class InvalidRequestException extends RuntimeException{
    private final ErrorCodes errorCode;

    public InvalidRequestException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getCode() {
        return this.errorCode;
    }
}
