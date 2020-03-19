package api.streams.model;

import java.time.LocalDateTime;

public class SalesOrder {
	//OrderDate,Region,Rep,Item,Units,Unit Cost
	private final LocalDateTime orderDate;
	private final String region;
	private final String rep;
	private final String item;
	private final Integer units;
	private final Double unitCost;
	public SalesOrder(LocalDateTime orderDate, String region, String rep, String item, Integer units, Double unitCost) {
		super();
		this.orderDate = orderDate;
		this.region = region;
		this.rep = rep;
		this.item = item;
		this.units = units;
		this.unitCost = unitCost;
	}
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	public String getRegion() {
		return region;
	}
	public String getRep() {
		return rep;
	}
	public String getItem() {
		return item;
	}
	public Integer getUnits() {
		return units;
	}
	public Double getUnitCost() {
		return unitCost;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((orderDate == null) ? 0 : orderDate.hashCode());
		result = prime * result + ((rep == null) ? 0 : rep.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalesOrder other = (SalesOrder) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (!orderDate.equals(other.orderDate))
			return false;
		if (rep == null) {
			if (other.rep != null)
				return false;
		} else if (!rep.equals(other.rep))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SalesOrder [orderDate=" + orderDate + ", region=" + region + ", rep=" + rep + ", item=" + item
				+ ", units=" + units + ", unitCost=" + unitCost + "]";
	}
}
