package com.meesho.notificationservice.data;

import java.util.List;

public class BlacklistNumbers {

    private List<String> phoneNumbers;

    public BlacklistNumbers() {
    }

    public BlacklistNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
