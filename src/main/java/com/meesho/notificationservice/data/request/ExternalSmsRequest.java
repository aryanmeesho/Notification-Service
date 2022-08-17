package com.meesho.notificationservice.data.request;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ExternalSmsRequest {

    @Autowired
    private String deliverychannel;

    @Autowired
    private Channels channels;

    @Autowired
    private List<Destination> destination;

    public ExternalSmsRequest(String deliverychannel, Channels channels, List<Destination> destination) {
        this.deliverychannel = deliverychannel;
        this.channels = channels;
        this.destination = destination;
    }

    public String getDeliverychannel() {
        return deliverychannel;
    }

    public void setDeliverychannel(String deliverychannel) {
        this.deliverychannel = deliverychannel;
    }

    public Channels getChannels() {
        return channels;
    }

    public void setChannels(Channels channels) {
        this.channels = channels;
    }

    public List<Destination> getDestination() {
        return destination;
    }

    public void setDestination(List<Destination> destination) {
        this.destination = destination;
    }
}
