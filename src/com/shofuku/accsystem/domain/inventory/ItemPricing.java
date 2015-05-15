package com.shofuku.accsystem.domain.inventory;

public class ItemPricing {
	
	private int pricingId;
	private String itemCode;
	private String itemType;
	private double franchiseStandardPricePerUnit;
	private double franchiseActualPricePerUnit;
	private double franchiseTransferPricePerUnit;
	private double companyOwnedStandardPricePerUnit;
	private double companyOwnedActualPricePerUnit;
	private double companyOwnedTransferPricePerUnit;
	
	public ItemPricing(int pricingId, String itemCode, String itemType,
			double franchiseStandardPricePerUnit,
			double franchiseActualPricePerUnit,
			double franchiseTransferPricePerUnit,
			double companyOwnedStandardPricePerUnit,
			double companyOwnedActualPricePerUnit,
			double companyOwnedTransferPricePerUnit) {
		super();
		this.pricingId = pricingId;
		this.itemCode = itemCode;
		this.itemType = itemType;
		this.franchiseStandardPricePerUnit = franchiseStandardPricePerUnit;
		this.franchiseActualPricePerUnit = franchiseActualPricePerUnit;
		this.franchiseTransferPricePerUnit = franchiseTransferPricePerUnit;
		this.companyOwnedStandardPricePerUnit = companyOwnedStandardPricePerUnit;
		this.companyOwnedActualPricePerUnit = companyOwnedActualPricePerUnit;
		this.companyOwnedTransferPricePerUnit = companyOwnedTransferPricePerUnit;
	}

	public ItemPricing() {
	}

	public int getPricingId() {
		return pricingId;
	}

	public void setPricingId(int pricingId) {
		this.pricingId = pricingId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public double getFranchiseStandardPricePerUnit() {
		return franchiseStandardPricePerUnit;
	}

	public void setFranchiseStandardPricePerUnit(
			double franchiseStandardPricePerUnit) {
		this.franchiseStandardPricePerUnit = franchiseStandardPricePerUnit;
	}

	public double getFranchiseActualPricePerUnit() {
		return franchiseActualPricePerUnit;
	}

	public void setFranchiseActualPricePerUnit(double franchiseActualPricePerUnit) {
		this.franchiseActualPricePerUnit = franchiseActualPricePerUnit;
	}

	public double getFranchiseTransferPricePerUnit() {
		return franchiseTransferPricePerUnit;
	}

	public void setFranchiseTransferPricePerUnit(
			double franchiseTransferPricePerUnit) {
		this.franchiseTransferPricePerUnit = franchiseTransferPricePerUnit;
	}

	public double getCompanyOwnedStandardPricePerUnit() {
		return companyOwnedStandardPricePerUnit;
	}

	public void setCompanyOwnedStandardPricePerUnit(
			double companyOwnedStandardPricePerUnit) {
		this.companyOwnedStandardPricePerUnit = companyOwnedStandardPricePerUnit;
	}

	public double getCompanyOwnedActualPricePerUnit() {
		return companyOwnedActualPricePerUnit;
	}

	public void setCompanyOwnedActualPricePerUnit(
			double companyOwnedActualPricePerUnit) {
		this.companyOwnedActualPricePerUnit = companyOwnedActualPricePerUnit;
	}

	public double getCompanyOwnedTransferPricePerUnit() {
		return companyOwnedTransferPricePerUnit;
	}

	public void setCompanyOwnedTransferPricePerUnit(
			double companyOwnedTransferPricePerUnit) {
		this.companyOwnedTransferPricePerUnit = companyOwnedTransferPricePerUnit;
	}
	
}
