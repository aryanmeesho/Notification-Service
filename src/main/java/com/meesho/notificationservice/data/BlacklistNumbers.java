package com.meesho.notificationservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistNumbers {
    private List<String> phoneNumbers;
}
