package com.shofuku.accsystem.action.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.domain.inventory.FPTS;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Item;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.RequisitionForm;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.inventory.UnlistedItem;
import com.shofuku.accsystem.domain.inventory.Utensils;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class SearchInventoryAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1121251364643576096L;
	
	private String subModule;
	private String moduleParameter;
	private String moduleParameterValue;
	private String clicked;
	List inventoryList;
	RawMaterial rm;
	FinishedGood fg;
	TradedItem ti;
	Utensils u;
	UnlistedItem unl;
	FPTS fpts;
	RequisitionForm rf;
	List<Item> resultList = new ArrayList<Item>();
	

	InventoryManager manager=new InventoryManager();
	HashMap<String,HashMap<String,ArrayList<Item>>> itemMap = new HashMap<String,HashMap<String,ArrayList<Item>>>(); 
	String orderingFormType;
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception {
		Session session = getSession();
		try {
			if (getClicked().equals("true")) {

				if ((null != getModuleParameter()&& getSubModule().equalsIgnoreCase("rawMat")) 
						|| (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("tradedItems"))
						|| (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("utensil"))
						|| (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("unlistedItems"))) {
				
					if (getModuleParameter().equalsIgnoreCase("description")) {
						if (getSubModule().equalsIgnoreCase("rawMat")){
						inventoryList = manager.listInventoryByParameterLike(RawMaterial.class, moduleParameter, moduleParameterValue,session);
						}else if (getSubModule().equalsIgnoreCase("tradedItems")){
							inventoryList = manager.listInventoryByParameterLike(TradedItem.class, moduleParameter, moduleParameterValue,session);
						}else if (getSubModule().equalsIgnoreCase("utensils")){
							inventoryList = manager.listInventoryByParameterLike(Utensils.class, moduleParameter, moduleParameterValue,session);
						}else{
							inventoryList = manager.listInventoryByParameterLike(UnlistedItem.class, moduleParameter, moduleParameterValue,session);
						}
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						if (getSubModule().equalsIgnoreCase("rawMat")){
							inventoryList = manager.listAlphabeticalAscByParameter(RawMaterial.class, "itemCode",session);
						}else if (getSubModule().equalsIgnoreCase("tradedItems")){
							inventoryList = manager.listAlphabeticalAscByParameter(TradedItem.class, "itemCode",session);
						}else if (getSubModule().equalsIgnoreCase("utensils")){
							inventoryList = manager.listAlphabeticalAscByParameter(Utensils.class, "itemCode",session);
						}else{
							inventoryList = manager.listAlphabeticalAscByParameter(UnlistedItem.class, "itemCode",session);
						}
						moduleParameterValue="all";
					}else {
						if (getSubModule().equalsIgnoreCase("rawMat")){
						inventoryList = manager.listInventoryByParameter(RawMaterial.class,
										moduleParameter, moduleParameterValue,session);
						}else if (getSubModule().equalsIgnoreCase("utensils")){
							inventoryList = manager.listInventoryByParameter(Utensils.class,
									moduleParameter, moduleParameterValue,session);
						}else if (getSubModule().equalsIgnoreCase("unlisteditems")){
						inventoryList = manager.listInventoryByParameter(UnlistedItem.class,
								moduleParameter, moduleParameterValue,session);
						}else{
							inventoryList = manager.listInventoryByParameter(TradedItem.class,
									moduleParameter, moduleParameterValue,session);
						}
					}
						if (inventoryList == null || inventoryList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
					
					if (getSubModule().equalsIgnoreCase("rawMat")){
						return "rawMat";
					}else if (getSubModule().equalsIgnoreCase("utensils")){
						return "utensils";
					}else if (getSubModule().equalsIgnoreCase("unlistedItems")){
						return "unlistedItems";
					}else {
						return "tradedItems";
					}
				}  else if(null != getModuleParameter()&& getSubModule().equalsIgnoreCase("returnSlip")) {
					if (getModuleParameter().equalsIgnoreCase("returnSlipNo")) {
						inventoryList = manager.listInventoryByParameterLike(ReturnSlip.class, moduleParameter, moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("orderReferenceNo")) {
						inventoryList = manager.listInventoryByParameterLike(ReturnSlip.class, moduleParameter, moduleParameterValue,session);
					}else if(moduleParameter.equalsIgnoreCase("ALL")){
						inventoryList = manager.listAlphabeticalAscByParameter(ReturnSlip.class, "returnSlipNo",session);
						moduleParameterValue="all";
					}else {
						inventoryList = manager.listInventoryByParameter(FinishedGood.class,
										moduleParameter, moduleParameterValue,session);
					}
						if (inventoryList == null || inventoryList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
					return "returnSlip";
				} else if (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("fpts")){
				
					FPTS fpts = new FPTS();
					 if (moduleParameter.equalsIgnoreCase("ALL")) {
						inventoryList = manager.listAlphabeticalAscByParameter(FPTS.class, "fptsNo",session);
						moduleParameterValue="all";
					}else if (getModuleParameter().equalsIgnoreCase("fptsFrom") || getModuleParameter().equalsIgnoreCase("fptsTo")) {
						inventoryList = manager.listInventoryByParameterLike(FPTS.class, moduleParameter, moduleParameterValue,session);
					}else if (null != getModuleParameter() && moduleParameter.equalsIgnoreCase("requisitionNo")) {
						inventoryList = manager.searchFPTSByOrderRequisitionNo(FPTS.class, "requisitionForm.requisitionNo", moduleParameterValue, session);
					} else if (moduleParameter.endsWith("Date")) {
						inventoryList = manager.getInventoryElementsByDate(
								new DateFormatHelper()
										.parseStringToTime(moduleParameterValue),
								fpts.getClass().getName(), moduleParameter,session);
					}else {
						inventoryList = manager.listInventoryByParameter(FPTS.class,
										moduleParameter, moduleParameterValue,session);
					}
						if (inventoryList == null || inventoryList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
					return "fpts";
				}else if (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("rf")){
				
					RequisitionForm rf = new RequisitionForm();
					 if (moduleParameter.equalsIgnoreCase("ALL")) {
						inventoryList = manager.listAlphabeticalAscByParameter(RequisitionForm.class, "requisitionNo",session);
						moduleParameterValue="all";
					}else if (getModuleParameter().equalsIgnoreCase("requisitionTo") || getModuleParameter().equalsIgnoreCase("requisitionBy")) {
						inventoryList = manager.listInventoryByParameterLike(RequisitionForm.class, moduleParameter, moduleParameterValue,session);
					} else if (moduleParameter.endsWith("Date")) {
						inventoryList = manager.getInventoryElementsByDate(
								new DateFormatHelper()
										.parseStringToTime(moduleParameterValue),
								rf.getClass().getName(), moduleParameter,session);
					}else {
						inventoryList = manager.listInventoryByParameter(RequisitionForm.class,
										moduleParameter, moduleParameterValue,session);
					}
						if (inventoryList == null || inventoryList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
					return "rf";
				}  else if (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("finGood")) {
					if (getModuleParameter().equalsIgnoreCase("description")) {
						inventoryList = manager.listInventoryByParameterLike(FinishedGood.class, moduleParameter, moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						inventoryList = manager.listAlphabeticalAscByParameter(FinishedGood.class, "productCode",session);
						moduleParameterValue="all";
					}else {
						inventoryList = manager.listInventoryByParameter(FinishedGood.class,
										moduleParameter, moduleParameterValue,session);
					}
						if (inventoryList == null || inventoryList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
					return "finGood";
				}else{
					//select *  from RM, TI, FG where item code/product code = "module parameter value"
					//get from textbox
					List<Item> tempList = new ArrayList<Item>();
					tempList = manager.getAllItemList(session);
									
					//use this for display
					
							for(Item item: tempList){
								if(moduleParameter.equalsIgnoreCase("description")){
									if(item.getDescription().toUpperCase().contains(moduleParameterValue.toUpperCase())){
										resultList.add(item);
										
									}
								}else{
									if(item.getItemCode().equalsIgnoreCase(moduleParameterValue)){
										resultList.add(item);
									
									}
								}
							}
							if (resultList == null || resultList.size()==0) {
								addActionMessage(SASConstants.NO_LIST);
							}
				}
			}
			moduleParameterValue="";
		return "populateList";
		} catch (RuntimeException re) {
			if (getSubModule().equalsIgnoreCase("rawMat")) {
				return "rawMat";
			}else if (getSubModule().equalsIgnoreCase("tradedItems")) {
				return "tradedItems";
			}else if (getSubModule().equalsIgnoreCase("utensils")) {
				return "utensils";
			}else if (getSubModule().equalsIgnoreCase("unlistedItems")) {
				return "unlistedItems";
			}else if (getSubModule().equalsIgnoreCase("fpts")) {
				return "fpts";
			}else if (getSubModule().equalsIgnoreCase("rf")) {
				return "rf";
			}else if (getSubModule().equalsIgnoreCase("finGood")) {
				return "finGood";
			}else {
				return "populateList";
			}
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}
		
	}

	
	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	public String getModuleParameter() {
		return moduleParameter;
	}

	public void setModuleParameter(String moduleParameter) {
		this.moduleParameter = moduleParameter;
	}

	public String getModuleParameterValue() {
		return moduleParameterValue;
	}

	public void setModuleParameterValue(String moduleParameterValue) {
		this.moduleParameterValue = moduleParameterValue;
	}

	public String getClicked() {
		return clicked;
	}

	public void setClicked(String clicked) {
		this.clicked = clicked;
	}

	public List getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List inventoryList) {
		this.inventoryList = inventoryList;
	}

	public RawMaterial getRm() {
		return rm;
	}

	public void setRm(RawMaterial rm) {
		this.rm = rm;
	}

	public FinishedGood getFg() {
		return fg;
	}

	public void setFg(FinishedGood fg) {
		this.fg = fg;
	}
	public TradedItem getTi() {
		return ti;
	}
	public void setTi(TradedItem ti) {
		this.ti = ti;
	}
	public FPTS getFpts() {
		return fpts;
	}
	public void setFpts(FPTS fpts) {
		this.fpts = fpts;
	}
	public RequisitionForm getRf() {
		return rf;
	}
	public void setRf(RequisitionForm rf) {
		this.rf = rf;
	}
	public String getOrderingFormType() {
		return orderingFormType;
	}
	public void setOrderingFormType(String orderingFormType) {
		this.orderingFormType = orderingFormType;
	}
	public List<Item> getResultList() {
		return resultList;
	}
	public void setResultList(List<Item> resultList) {
		this.resultList = resultList;
	}
	public Utensils getU() {
		return u;
	}
	public void setU(Utensils u) {
		this.u = u;
	}
	public UnlistedItem getUnl() {
		return unl;
	}
	public void setUnl(UnlistedItem unl) {
		this.unl = unl;
	}
}
