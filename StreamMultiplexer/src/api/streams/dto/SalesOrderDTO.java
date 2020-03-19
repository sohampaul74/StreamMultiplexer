package api.streams.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import api.streams.StreamMultiplexer;
import api.streams.model.SalesOrder;

public class SalesOrderDTO {
	public static SalesOrder convertToObject(String text) {
		//System.out.println(text);
		String[] tokens = text.split(",");
		LocalDateTime orderDate = LocalDateTime.of(LocalDate.parse(tokens[0], DateTimeFormatter.ofPattern(StreamMultiplexer.DATETIMEFORMAT)), LocalTime.of(10, 00));
		String region = tokens[1];
		String rep = tokens[2];
		String item = tokens[3];
		Integer units = Integer.parseInt(tokens[4]);
		Double cost = Double.parseDouble(tokens[5]);
		
		return new SalesOrder(orderDate, region, rep, item, units, cost);
	}
}
