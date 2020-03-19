package api.streams;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import api.streams.dto.SalesOrderDTO;
import api.streams.interfaces.DisplayLogic;
import api.streams.interfaces.FilterLogic;
import api.streams.interfaces.MultiplyLogic;
import api.streams.model.SalesOrder;

public class StreamMultiplexer {
	
	private static final String[] DATASET = "1-6-18,East,Jones,Pencil,95, 1.99 >>1-23-18,Central,Kivell,Binder,50, 19.99 >>2-9-18,Central,Jardine,Pencil,36, 4.99>>2-26-18,Central,Gill,Pen,27, 19.99 >>3-15-18,West,Sorvino,Pencil,56, 2.99>>4-1-18,East,Jones,Binder,60, 4.99 >>4-18-18,Central,Andrews,Pencil,75, 1.99>>5-5-18,Central,Jardine,Pencil,90, 4.99>>5-22-18,West,Thompson,Pencil,32, 1.99>>6-8-18,East,Jones,Binder,60, 8.99 >>6-25-18,Central,Morgan,Pencil,90, 4.99 >>7-12-18,East,Howard,Binder,29, 1.99 >>7-29-18,East,Parent,Binder,81, 19.99>>8-15-18,East,Jones,Pencil,35, 4.99>>9-1-18,Central,Smith,Desk,2, 125.00>>9-18-18,East,Jones,Pen Set,16, 15.99 >>10-5-18,Central,Morgan,Binder,28, 8.99>>10-22-18,East,Jones,Pen,64, 8.99>>11-8-18,East,Parent,Pen,15, 19.99>>11-25-18,Central,Kivell,Pen Set,96, 4.99>>12-12-18,Central,Smith,Pencil,67, 1.29>>12-29-18,East,Parent,Pen Set,74, 15.99>>1-15-19,Central,Gill,Binder,46, 8.99>>2-1-19,Central,Smith,Binder,87, 15.00>>2-18-19,East,Jones,Binder,4, 4.99>>3-7-19,West,Sorvino,Binder,7, 19.99>>3-24-19,Central,Jardine,Pen Set,50, 4.99>>4-10-19,Central,Andrews,Pencil,66, 1.99>>4-27-19,East,Howard,Pen,96, 4.99>>5-14-19,Central,Gill,Pencil,53, 1.29>>5-31-19,Central,Gill,Binder,80, 8.99>>6-17-19,Central,Kivell,Desk,5, 125.00>>7-4-19,East,Jones,Pen Set,62, 4.99>>7-21-19,Central,Morgan,Pen Set,55, 12.49>>8-7-19,Central,Kivell,Pen Set,42, 23.95>>8-24-19,West,Sorvino,Desk,3, 275.00>>9-10-19,Central,Gill,Pencil,7, 1.29>>9-27-19,West,Sorvino,Pen,76, 1.99>>10-14-19,West,Thompson,Binder,57, 19.99>>10-31-19,Central,Andrews,Pencil,14, 1.29>>11-17-19,Central,Jardine,Binder,11, 4.99>>12-4-19,Central,Jardine,Binder,94, 19.99>>12-21-19,Central,Andrews,Binder,28, 4.99".split(">>");
	public static final String DATETIMEFORMAT = "M-d-yy";
	
	//example method with functional interfaces as parameter
	private static <T extends Number> void findMaxFrom(Stream<T> data, FilterLogic<T> filterLogic, MultiplyLogic<T> multiplyLogic, DisplayLogic<T> displayLogic) {
		data.filter(filterLogic::isValid).map(multiplyLogic::multiply).forEach(displayLogic::display);
	}
	
	public static void main(String[] args) {
		
		//method reference
		//building list of orders
		//System.out.println(Arrays.deepToString(DATASET));
		List<SalesOrder> salesOrders = Arrays.stream(DATASET)
										.parallel()
										.map(SalesOrderDTO::convertToObject).collect(Collectors.toList());
		
		System.out.println(Arrays.deepToString(salesOrders.toArray()));
		
		//Collectors.toM
		//highest sale based on region and value and date
		Map<String, Double> regionBasedValues = salesOrders.parallelStream().collect(Collectors.toMap(t->t.getRegion(), t->(t.getUnitCost()*t.getUnits()), (a1,a2)->(a1>a2?a1:a2)));
		regionBasedValues.keySet().forEach(t->System.out.println("Region: "+t+" sale:"+new BigDecimal(regionBasedValues.get(t)).setScale(2, RoundingMode.HALF_UP).doubleValue()));
		
		//total order value for a certain month
		LocalDateTime FINAL_DATE_TIME = LocalDateTime.of(2018, 3, 31, 23, 00);
		System.out.println(
			"Total order value: "+
			salesOrders.parallelStream()
			.filter(t -> (t.getOrderDate().compareTo(FINAL_DATE_TIME)<=0 && t.getOrderDate().compareTo(FINAL_DATE_TIME.minusDays(30))>=0))
			.mapToDouble(t->t.getUnitCost()*t.getUnits())
			.sum()
		);
		
		//Data for every day total value for a period of time
		Map<LocalDateTime, Double> valueBasedOn = salesOrders.parallelStream().collect(Collectors.toMap(t->t.getOrderDate(), t->t.getUnitCost()*t.getUnits(), (d1,d2)->d1+d2));
		
		valueBasedOn.keySet().stream()
			.map(ldt->
				("Sale for date: "+ldt.toLocalDate().format(DateTimeFormatter.ofPattern(DATETIMEFORMAT))+" is:"
						+Math.floor(valueBasedOn.get(ldt)))
				)
			.forEach(System.out::println);
		
		
		//calculating how much money accumulated for decimal ceiling
		LocalDate minDate = Collections.min(salesOrders, (a1, a2)->(a1.getOrderDate().compareTo(a2.getOrderDate()))).getOrderDate().toLocalDate();
		LocalDate maxDate = Collections.max(salesOrders, (a1, a2)->(a1.getOrderDate().compareTo(a2.getOrderDate()))).getOrderDate().toLocalDate();
		
		long mdays = maxDate.toEpochDay() - minDate.toEpochDay();		
		int days = Period.between(minDate, maxDate).getMonths();
		
		Double theySell = salesOrders.stream().mapToDouble(t->t.getUnits()*t.getUnitCost()).sum();
		Double theyTook = salesOrders.stream().mapToDouble(t->t.getUnitCost()*t.getUnits()).map(t->Math.ceil(t)-t).sum();
		System.out.println("For "+days+"+months or "+mdays+"days to be precise, From: "+minDate+" they earned: "+theySell+" & the took extra: "+theyTook);
		
		//Example of own functional interfaces
		StreamMultiplexer.findMaxFrom(IntStream.range(15, 200).boxed(), t->t%5==0 ,t->t*10, System.out::println);
		
		//file streams	
		/**
		Queue<Path> filePaths = new LinkedList<>();
		try {
			Files.newDirectoryStream(Paths.get("D:\\movies")).forEach(t->filePaths.add(t));
			while(!filePaths.isEmpty()) {
				Path path = filePaths.poll();
				if(Files.isDirectory(path)) {
					Files.list(path).forEach(t->filePaths.add(t));
				} else {
					System.out.println("File name: "+path.toAbsolutePath().toString());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		**/
	}
}
