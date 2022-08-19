package com.meesho.notificationservice.entity;

import com.meesho.notificationservice.utils.constants.AppConstants;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = AppConstants.SMS_INDEX)
public class ElasticSearchModal implements Serializable {

    @Id
    private String id;
    private String phoneNumber;
    private String message;
    private String status;
    private int failureCode;
    private String failureComment;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis, name = "createdAt")
    private Date createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis, name = "updatedAt")
    private Date updatedAt;
}
