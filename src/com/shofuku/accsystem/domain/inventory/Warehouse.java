package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;

public class Warehouse implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int id;
	
	private String locationCode;
	private String itemCode;
	private double quantityPerRecord;
	private double quantityPerPhysicalCount;
	
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public double getQuantityPerRecord() {
		return quantityPerRecord;
	}
	public void setQuantityPerRecord(double quantityPerRecord) {
		this.quantityPerRecord = quantityPerRecord;
	}
	public double getQuantityPerPhysicalCount() {
		return quantityPerPhysicalCount;
	}
	public void setQuantityPerPhysicalCount(double quantityPerPhysicalCount) {
		this.quantityPerPhysicalCount = quantityPerPhysicalCount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
