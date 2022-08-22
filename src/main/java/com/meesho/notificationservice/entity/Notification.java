package com.meesho.notificationservice.entity;

import com.meesho.notificationservice.utils.constants.AppConstants;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = AppConstants.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @Column(name="id")
    private String id;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private String status;

    @Column(name = "failure_code")
    private int failureCode;

    @Column(name = "failure_comment")
    private String failureComment;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt = new Date();


    public Notification(String phoneNumber, String message){
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
}