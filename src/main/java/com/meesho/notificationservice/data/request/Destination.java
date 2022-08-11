package com.meesho.notificationservice.data.request;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Destination {

    @Autowired
    private List<String> msisdn;

    @Autowired
    private String correlationId;

    public Destination(List<String> msisdn, String correlationId) {
        this.msisdn = msisdn;
        this.correlationId = correlationId;
    }

    public List<String> getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(List<String> msisdn) {
        this.msisdn = msisdn;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
