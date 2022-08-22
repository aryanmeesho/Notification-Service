package com.meesho.notificationservice.utils.constants;

import org.springframework.beans.factory.annotation.Value;

public class AppConstants {

    // Kafka Consumer Constants
    public static final String TOPIC_NAME = "notification.send_sms";
    public static final String GROUP_ID = "group1";

    // Elastic Search Constants
    public static final String SMS_INDEX = "sms_requests4";
    public static final int DEFAULT_PAGE_SIZE = 5;

    // SQL Constants
    public static final String TABLE_NAME = "sms_requests";

}
