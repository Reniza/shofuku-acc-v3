package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Item  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6945630721070450867L;
	private int id;
	private String itemCode;
	private String description;
	private String unitOfMeasurement;
	private String classification;
	private String subClassification;
	private String itemType;
	private String isVattable;
	private String isShared;
	private String isActive;
	private String location;
	
	private double customerStockLevel;

	private Warehouse warehouse;
	private Set<Warehouse> warehouses = 
			new HashSet<Warehouse>(0);
	
	public Item() {
		
	}
	
	public Item(String itemCode, String description, String unitOfMeasurement,
			String classification, String subClassification,String isVattable) {
		this.itemCode = itemCode;
		this.description = description;
		this.unitOfMeasurement = unitOfMeasurement;
		this.classification = classification;
		this.subClassification = subClassification;
		this.isVattable = isVattable;
	}

	public Item(String itemCode, String description, String unitOfMeasurement,
			String classification, String subClassification,String isVattable,double customerStockLevel) {
		this.itemCode = itemCode;
		this.description = description;
		this.unitOfMeasurement = unitOfMeasurement;
		this.classification = classification;
		this.subClassification = subClassification;
		this.isVattable = isVattable;
		this.customerStockLevel = customerStockLevel;
		
	}

	/*
	 * getters setters
	 */
	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}
	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getSubClassification() {
		return subClassification;
	}
	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	public String getIsVattable() {
		return isVattable;
	}

	public void setIsVattable(String isVattable) {
		this.isVattable = isVattable;
	}
	//END: 2013 - PHASE 3 : PROJECT 4: MARK
	//START: 2015 - PHASE 3a - stock level per customer
	public double getCustomerStockLevel() {
		return customerStockLevel;
	}
	public void setCustomerStockLevel(double customerStockLevel) {
		this.customerStockLevel = customerStockLevel;
	}
	//END: 2015 - PHASE 3a - stock level per customer

	public Set<Warehouse> getWarehouses() {
		return warehouses;
	}

	public void setWarehouses(Set<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIsShared() {
		return isShared;
	}

	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
