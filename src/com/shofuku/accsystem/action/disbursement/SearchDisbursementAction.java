package com.shofuku.accsystem.action.disbursement;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;
import com.shofuku.accsystem.controllers.DisbursementManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.domain.disbursements.CashPayment;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.receipts.CashCheckReceipts;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierPurchaseOrder;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class SearchDisbursementAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String subModule;
	private String moduleParameter;
	private String moduleParameterValue;
	
	private String clicked;
	List disbursementList;
	SupplierManager supManager = new SupplierManager();
	DisbursementManager manager = new DisbursementManager();

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	public String execute() throws Exception{
		Session session = getSession();
		disbursementList = new ArrayList();
		try {
			if (getClicked().equals("true")) {
				

				if (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("AA")) {
					
					if (getModuleParameter().equalsIgnoreCase("payee")) {
						disbursementList = manager.listDisbursementsByParameterLike(
										PettyCash.class, moduleParameter,
										moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						disbursementList = supManager.listAlphabeticalAscByParameter(PettyCash.class, "pcVoucherNumber",session);
						moduleParameterValue="all";
						
					} else {
						disbursementList = manager.listDisbursementsByParameter(PettyCash.class,
										moduleParameter, moduleParameterValue,session);
					}
						if (disbursementList == null || disbursementList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
						
					return "pettyCash";
				} else if (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("BB")) {
					
					if (getModuleParameter().equalsIgnoreCase("payee")) {
							disbursementList = manager.listDisbursementsByParameterLike(
											CashPayment.class, moduleParameter,
											moduleParameterValue,session);
						} else if (moduleParameter.equalsIgnoreCase("ALL")) {
							disbursementList = supManager.listAlphabeticalAscByParameter(CashPayment.class, "cashVoucherNumber",session);
							moduleParameterValue="all";
							
						}else {
							disbursementList = manager.listDisbursementsByParameter(CashPayment.class,
											moduleParameter, moduleParameterValue,session);
						}
							if (disbursementList == null || disbursementList.size()==0) {
								addActionMessage(SASConstants.NO_LIST);
							}
						
					return "cashPayment";
				} else if (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("CC")){
					
					if (getModuleParameter().equalsIgnoreCase("payee") || getModuleParameter().equalsIgnoreCase("checkNo")) {
						disbursementList = manager.listDisbursementsByParameterLike(
										CheckPayments.class, moduleParameter,
										moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						disbursementList = manager.listDisbursementsCheckWithoutInvoice(session);
						moduleParameterValue="all";
						
					}else {
						disbursementList = manager.listDisbursementsByParameter(CheckPayments.class,
										moduleParameter, moduleParameterValue,session);
					}
						if ( disbursementList == null || disbursementList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
					
					return "checkPayment";
				} else {

					if (getModuleParameter().equalsIgnoreCase("payee")) {
						disbursementList = manager.listDisbursementsByParameterLike(
										CheckPayments.class, moduleParameter,
										moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						disbursementList = manager.listDisbursementsWithInvoice(session);
						moduleParameterValue="all";
						
					} else if (moduleParameter.equals("invoiceNo")) {
						disbursementList = manager.listDisbursementsByParameter(CheckPayments.class,
								"invoice.supplierInvoiceNo", moduleParameterValue,session);
					
					}else if (getModuleParameter().equalsIgnoreCase("checkNo")) {
						disbursementList = manager.listDisbursementsByParameterLike(
								CheckPayments.class, moduleParameter,
								moduleParameterValue,session);
					}else {
						disbursementList = manager.listDisbursementsByParameter(CheckPayments.class,
										moduleParameter, moduleParameterValue,session);
					}
						if (disbursementList == null || disbursementList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
					
					return "checkVoucher";
					}
			}
		moduleParameterValue="";
		return "populateList";
		
		} catch (RuntimeException re) {
			if (getSubModule().equals("AA")){
				return "pettyCash";
			}else if (getSubModule().equals("BB")){
				return "cashPayment";
			}else if (getSubModule().equals("CC")){
				return "checkPayment";
			}else {
				return "checkVoucher";
			}
		}finally {
			if(session.isOpen()){
				session.close();
				session.getSessionFactory().close();
			}
		}

	}
	
	public String getClicked() {
		return clicked;
	}

	public void setClicked(String clicked) {
		this.clicked = clicked;
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

	public List getDisbursementList() {
		return disbursementList;
	}

	public void setDisbursementList(List disbursementList) {
		this.disbursementList = disbursementList;
	}


}
