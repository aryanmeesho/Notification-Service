package com.meesho.notificationservice.services.auth;

import com.meesho.notificationservice.entity.enums.ErrorCodes;
import com.meesho.notificationservice.exceptions.InvalidRequestException;
import com.meesho.notificationservice.utils.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HeaderInterceptorService implements HandlerInterceptor {

    @Value("${AUTHENTICATION.SECRET.KEY}")
    private String Auth_KEY;

    Logger logger = LoggerFactory.getLogger(HeaderInterceptorService.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorization = request.getHeader("Authorization");

        if(authorization.contentEquals(Auth_KEY)){
            return  true;
        }

        throw new InvalidRequestException("Authorization token invalid", ErrorCodes.FORBIDDEN_ERROR);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       try {
           HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
       }
       catch (Exception exc){
           throw new Exception("postHandle in HandleInterceptorService Failed, Error : " + exc.getMessage());
       }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        }
        catch (Exception exc){
            throw new Exception("afterCompletion in HandleInterceptorService Failed, Error : " + exc.getMessage());
        }
    }
}