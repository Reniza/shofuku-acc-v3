package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;
public class RawMaterial implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8066939164504106918L;
	private String itemCode;
	private String classification;
	private String subClassification;
	private String template;
	private String stockStatusDay;
	
	public String getStockStatusDay() {
		return stockStatusDay;
	}
	public void setStockStatusDay(String stockStatusDay) {
		this.stockStatusDay = stockStatusDay;
	}
	private String description;
	private String unitOfMeasurement;
	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	private String isVattable;
	//END: 2013 - PHASE 3 : PROJECT 4: MARK
	private double quantityIn;
	private double quantityOut;
	private double quantityPerRecord;
	private double quantityPerPhysicalCount;
	private ItemPricing itemPricing;
	
	public RawMaterial(){
		
	}
	public RawMaterial(String itemCode,String description,String unitOfMeasurement,ItemPricing itemPricing) {
		this.itemCode = itemCode;
		this.description = description;
		this.unitOfMeasurement = unitOfMeasurement;
		this.itemPricing = itemPricing;
	}
	
	
	public RawMaterial(String itemCode, String classification,
			String subClassification , String template, String description,
			String unitOfMeasurement, ItemPricing itemPricing) {
		this.itemCode = itemCode;
		this.classification = classification;
		this.subClassification = subClassification;
		this.template = template;
		this.description = description;
		this.unitOfMeasurement = unitOfMeasurement;
		this.itemPricing = itemPricing;
	}
	
	public double getQuantityIn() {
		return quantityIn;
	}

	public void setQuantityIn(double quantityIn) {
		this.quantityIn = quantityIn;
	}

	public double getQuantityOut() {
		return quantityOut;
	}

	public void setQuantityOut(double quantityOut) {
		this.quantityOut = quantityOut;
	}

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

	public ItemPricing getItemPricing() {
		return itemPricing;
	}
	public void setItemPricing(ItemPricing itemPricing) {
		this.itemPricing = itemPricing;
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
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	//START: 2013 - PHASE 3 : PROJECT 4: MARK
	public String getIsVattable() {
		return isVattable;
	}
	public void setIsVattable(String isVattable) {
		this.isVattable = isVattable;
	}
	//END: 2013 - PHASE 3 : PROJECT 4: MARK
	
}
