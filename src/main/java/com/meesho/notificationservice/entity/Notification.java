package com.meesho.notificationservice.entity;

import com.meesho.notificationservice.entity.enums.Status;
import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "sms_requests")
public class Notification {

    // Define Fields

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "message")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "failure_code")
    private int failureCode;

    @Column(name = "failure_comment")
    private String failureComment;

    // define Constructor
    public Notification(){

    }

    public Notification(String phoneNumber, String message){
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    // Define getters and setters


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(int failureCode) {
        this.failureCode = failureCode;
    }

    public String getFailureComment() {
        return failureComment;
    }

    public void setFailureComment(String failureComment) {
        this.failureComment = failureComment;
    }

    //Define tostring

    @Override
    public String toString() {
        return "Notification [id=" + id + ", phoneNumber=" + phoneNumber + ", message=" + message +
                ", status=" + status + ", failure_code=" + failureCode + ", failure_comment=" + failureComment + "]";
    }
}
