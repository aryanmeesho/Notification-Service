package com.meesho.notificationservice.data.request;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ExternalSmsRequest {
    @Autowired
    private String deliveryChannel;

    @Autowired
    private Channels channels;

    @Autowired
    private List<Destination> destination;

    public ExternalSmsRequest(String deliveryChannel, Channels channels, List<Destination> destination) {
        this.deliveryChannel = deliveryChannel;
        this.channels = channels;
        this.destination = destination;
    }

    public String getDeliveryChannel() {
        return deliveryChannel;
    }

    public void setDeliveryChannel(String deliveryChannel) {
        this.deliveryChannel = deliveryChannel;
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
