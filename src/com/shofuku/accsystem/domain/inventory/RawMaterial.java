package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;
public class RawMaterial extends Item implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8066939164504106918L;
	private String template;
	private String stockStatusDay;
	private double quantityIn;
	private double quantityOut;
	
	private ItemPricing itemPricing;
	
	
	public RawMaterial(){}
	
	public RawMaterial(String itemCode,String description,String unitOfMeasurement,ItemPricing itemPricing) {
		super.setItemCode(itemCode);
		super.setDescription(description);
		super.setUnitOfMeasurement(unitOfMeasurement);
		this.itemPricing = itemPricing;
	}
	
	public RawMaterial(String itemCode, String classification,
			String subClassification , String template, String description,
			String unitOfMeasurement, ItemPricing itemPricing) {
		super.setItemCode(itemCode);
		super.setClassification(classification);
		super.setSubClassification(subClassification);
		this.template = template;
		super.setDescription(description);
		super.setUnitOfMeasurement(unitOfMeasurement);
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

	public ItemPricing getItemPricing() {
		return itemPricing;
	}
	public void setItemPricing(ItemPricing itemPricing) {
		this.itemPricing = itemPricing;
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

}
