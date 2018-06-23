package com.ftd.io.batch;

import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Availability {
    
    private String siteId;
    private String fulfillmentChannelId;
    private String dayOfWeek;
    private String deliveryZipCode;
    private ArrayList<FulfillmentOption> fulfillmentOptions;


}
