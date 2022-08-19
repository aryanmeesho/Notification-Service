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

    // Third Party API Constants
    public static final String KEY = "93ceffda-5941-11ea-9da9-025282c394f2";
    public static final String URL = "https://api.imiconnect.in/resources/v1/messaging";

    // Auth Constants

    public static final String Auth_KEY = "23582437590817";

}
