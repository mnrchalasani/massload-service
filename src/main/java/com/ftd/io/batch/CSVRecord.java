package com.ftd.io.batch;

import lombok.Data;

@Data
public class CSVRecord {
	private String fulfillmentChannelId, dayOfWeek, productId, deliveryZipCode, floristSameDayDelivery, floristSameDayDeliveryCutoffTime, floristFutureDayDelivery, floristFutureDayDeliveryCutoffTime;

}
