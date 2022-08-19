package com.meesho.notificationservice.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalSmsRequest {
    private String deliverychannel;
    private Channels channels;
    private List<Destination> destination;
}
