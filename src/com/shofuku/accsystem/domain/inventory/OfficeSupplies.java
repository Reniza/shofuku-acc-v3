package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;

import com.shofuku.accsystem.domain.financials.Vat;

public class OfficeSupplies implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4542115324013111702L;
	
	private String itemCode;
	private String classification;
	private String subClassification;
	private String template;
	private String stockStatusDay;
	private String description;
	private String unitOfMeasurement;
	private String isVattable;
	private double quantityIn;
	private double quantityOut;
	private ItemPricing itemPricing;
	private Vat vatDetails;
	private Warehouse warehouse;
	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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
	public String getStockStatusDay() {
		return stockStatusDay;
	}
	public void setStockStatusDay(String stockStatusDay) {
		this.stockStatusDay = stockStatusDay;
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
	public String getIsVattable() {
		return isVattable;
	}
	public void setIsVattable(String isVattable) {
		this.isVattable = isVattable;
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
	public ItemPricing getItemPricing() {
		return itemPricing;
	}
	public void setItemPricing(ItemPricing itemPricing) {
		this.itemPricing = itemPricing;
	}
	public Vat getVatDetails() {
		return vatDetails;
	}
	public void setVatDetails(Vat vatDetails) {
		this.vatDetails = vatDetails;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	
}
