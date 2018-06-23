package com.ftd.io.batch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

import org.springframework.batch.item.ItemProcessor;

public class AvailabilityProcessor implements ItemProcessor<CSVRecord, Availability> {

	@Override
	public Availability process(CSVRecord csvRecord) throws Exception {
    	Availability avail = new Availability();
    	avail.setSiteId("FTD");
    	avail.setFulfillmentChannelId(csvRecord.getFulfillmentChannelId());
    	avail.setDeliveryZipCode(csvRecord.getDeliveryZipCode());
    	avail.setDayOfWeek(csvRecord.getDayOfWeek());
    	FulfillmentOption fo1 = new FulfillmentOption(csvRecord.getFloristSameDayDelivery(), toHHMMSS(csvRecord.getFloristSameDayDeliveryCutoffTime()));
    	FulfillmentOption fo2 = new FulfillmentOption(csvRecord.getFloristFutureDayDelivery(), toHHMMSS(csvRecord.getFloristFutureDayDeliveryCutoffTime()));
    	ArrayList<FulfillmentOption> options = new ArrayList<FulfillmentOption>();
    	options.add(fo1);
    	options.add(fo2);
    	avail.setFulfillmentOptions(options);
    	
    return avail;
	}
	
	private String toHHMMSS(String time) throws ParseException
	{
        DateFormat sdf = new SimpleDateFormat("HHmm");
        LocalTime lt= sdf.parse(time).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
		return lt.toString()+":00";
	}

}
