package com.meesho.notificationservice.utils.validators;

public class PhoneNumberValidator {

    public static boolean isValidNumber(String phoneNumber) {

        String requiredCountryCode = "+91";
        String inputCountryCode = phoneNumber.substring(0,3);
        int requiredLength = 13;

        if (phoneNumber.length() != requiredLength) {
            return false;
        } else if (inputCountryCode.equals(requiredCountryCode) == false) {
            return false;
        }
            return true;
    }

    public static boolean phoneNumberNullCheck(String phoneNumber){

        if(phoneNumber == null || phoneNumber.length() == 0){
            return true;
        }
        return false;
    }

}
