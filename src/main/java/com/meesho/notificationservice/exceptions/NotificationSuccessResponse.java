package com.meesho.notificationservice.exceptions;

public class NotificationSuccessResponse {

    private int request_id;
    private String comment;

    public NotificationSuccessResponse(){

    }

    public NotificationSuccessResponse(int request_id, String comment) {
        this.request_id = request_id;
        this.comment = comment;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
