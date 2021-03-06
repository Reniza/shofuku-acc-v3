package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;

public class OfficeSupplies  extends Item  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4542115324013111702L;
	
	private String template;
	private String stockStatusDay;
	private double quantityIn;
	private double quantityOut;
	private ItemPricing itemPricing;
	
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
	
}
