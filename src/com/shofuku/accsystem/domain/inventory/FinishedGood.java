package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Set;

public class FinishedGood implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2432354236113837419L;
	
	private String productCode;
	private String classification;
	private String subClassification;
	private String template;
	private String stockStatusDay;
	
	private String isShared;
	private Warehouse warehouse;
	
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
	private double standardTotalCost;
	private double actualTotalCost;
	private double transferTotalCost;
	private double yields;
	private double markUp;
	private Set<Ingredient> ingredients;
	private ItemPricing itemPricing;
	
	public ItemPricing getItemPricing() {
		return itemPricing;
	}
	
	public void setItemPricing(ItemPricing itemPricing) {
		this.itemPricing = itemPricing;
	}

	public double getTransferTotalCost() {
		return transferTotalCost;
	}

	public void setTransferTotalCost(double transferTotalCost) {
		this.transferTotalCost = transferTotalCost;
	}
	
	public double getMarkUp() {
		return markUp;
	}

	public void setMarkUp(double markUp) {
		this.markUp = markUp;
	}

	public double getYields() {
		return yields;
	}

	public void setYields(double yields) {
		this.yields = yields;
	}

	public double getStandardTotalCost() {
		return standardTotalCost;
	}

	public void setStandardTotalCost(double standardTotalCost) {
		this.standardTotalCost = standardTotalCost;
	}

	public double getActualTotalCost() {
		return actualTotalCost;
	}

	public void setActualTotalCost(double actualTotalCost) {
		this.actualTotalCost = actualTotalCost;
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

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
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

	public Set<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<Ingredient> ingredients) {
		this.ingredients = ingredients;
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
	public String getIsShared() {
		return isShared;
	}
	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
}
