package com.shofuku.accsystem.domain.customers;

import java.io.Serializable;

public class CustomerStockLevel implements Serializable

{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6590898953277019557L;
	private int id;
	private double stockLevel;
	private String itemCode;

	public CustomerStockLevel() {
	}

	public CustomerStockLevel(double stockLevel, String itemCode) {
		this.stockLevel = stockLevel;
		this.itemCode= itemCode;
	}

	public double getStockLevel() {
		return stockLevel;
	}

	public void setStockLevel(double stockLevel) {
		this.stockLevel = stockLevel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

}
