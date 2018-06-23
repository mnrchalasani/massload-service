package com.ftd.io.batch;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FulfillmentOption {
    private String name;
    private String cutOffTime;
}
