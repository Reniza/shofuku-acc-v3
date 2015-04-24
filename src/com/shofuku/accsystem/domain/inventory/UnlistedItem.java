package com.shofuku.accsystem.domain.inventory;

import java.io.Serializable;

public class UnlistedItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -42697499413427457L;

	private int id;
	private String itemCode;
	private String description;	
	private String uom;
	private String template;
	private String classification;
	
	
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
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
