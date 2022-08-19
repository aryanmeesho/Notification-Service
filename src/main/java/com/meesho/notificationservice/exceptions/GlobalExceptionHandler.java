package com.meesho.notificationservice.exceptions;

import com.meesho.notificationservice.entity.enums.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{

    // To Handle "Service Not Available" Exceptions
    @ExceptionHandler(ServiceNotAvailableException.class)
    public final ResponseEntity<Object> handleServiceNotAvailableException(ServiceNotAvailableException exc, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),exc.getCode().getCode(),exc.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // To Handle "Invalid Request" Exceptions
    @ExceptionHandler(InvalidRequestException.class)
    public final ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException exc, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),exc.getCode().getCode(),exc.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // To Handle "Not Found" Exceptions
    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(NotFoundException exc, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),exc.getCode().getCode(),exc.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    // To Handle All The Remaining Exceptions
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception exc, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ErrorCodes.BAD_REQUEST_ERROR.getCode(),exc.getMessage(),
                request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
