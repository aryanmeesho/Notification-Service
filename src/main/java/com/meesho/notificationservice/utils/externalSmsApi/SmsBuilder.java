package com.meesho.notificationservice.utils.externalSmsApi;
import com.meesho.notificationservice.data.request.Channels;
import com.meesho.notificationservice.data.request.Destination;
import com.meesho.notificationservice.data.request.ExternalSmsRequest;
import com.meesho.notificationservice.data.request.Text;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SmsBuilder {

    public ExternalSmsRequest buildSmsRequest(String id, String phoneNumber, String message){
        Text text = new Text(message);
        Channels channels = new Channels(text);

        List<String> msisdnList = new ArrayList<>();
        msisdnList.add(phoneNumber);

        List<Destination> destinationList = new ArrayList<>();
        Destination destination = new Destination(msisdnList, id);
        destinationList.add(destination);

        ExternalSmsRequest smsRequest = new ExternalSmsRequest("sms", channels, destinationList);
        return smsRequest ;
    }

}
