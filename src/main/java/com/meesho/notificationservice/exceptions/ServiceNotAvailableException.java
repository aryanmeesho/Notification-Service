package com.meesho.notificationservice.exceptions;
import com.meesho.notificationservice.entity.enums.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ServiceNotAvailableException extends RuntimeException{

    private final ErrorCodes errorCode;

    public ServiceNotAvailableException(String message, ErrorCodes errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getCode() {
        return this.errorCode;
    }
}
