package com.meesho.notificationservice.data.request;

import org.springframework.beans.factory.annotation.Autowired;

public class Channels {

    @Autowired
    private Text sms;

    public Text getSms() {
        return sms;
    }

    public Channels(Text sms) {
        this.sms = sms;
    }

    public void setSms(Text sms) {
        this.sms = sms;
    }
}
