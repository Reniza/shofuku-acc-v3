package com.shofuku.accsystem.action.disbursement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.DisbursementManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.domain.disbursements.CashPayment;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.SASConstants;

public class SearchDisbursementAction extends ActionSupport implements Preparable{

	private static final long serialVersionUID = 1L;

	Map actionSession;
	UserAccount user;

	SupplierManager supplierManager;
	DisbursementManager disbursementManager;

	public void prepare() throws Exception {
		
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		supplierManager 		= (SupplierManager) 	actionSession.get("supplierManager");
		disbursementManager 	= (DisbursementManager) actionSession.get("disbursementManager");
		
	}
	private String subModule;
	private String moduleParameter;
	private String moduleParameterValue;
	
	private String clicked;
	List disbursementList;
	
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
						disbursementList = disbursementManager.listDisbursementsByParameterLike(
										PettyCash.class, moduleParameter,
										moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						disbursementList = disbursementManager.listAlphabeticalAscByParameter(PettyCash.class, "pcVoucherNumber",session);
						moduleParameterValue="all";
						
					} else {
						disbursementList = disbursementManager.listDisbursementsByParameter(PettyCash.class,
										moduleParameter, moduleParameterValue,session);
					}
						if (disbursementList == null || disbursementList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
						
					return "pettyCash";
				} else if (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("BB")) {
					
					if (getModuleParameter().equalsIgnoreCase("payee")) {
							disbursementList = disbursementManager.listDisbursementsByParameterLike(
											CashPayment.class, moduleParameter,
											moduleParameterValue,session);
						} else if (moduleParameter.equalsIgnoreCase("ALL")) {
							disbursementList = disbursementManager.listAlphabeticalAscByParameter(CashPayment.class, "cashVoucherNumber",session);
							moduleParameterValue="all";
							
						}else {
							disbursementList = disbursementManager.listDisbursementsByParameter(CashPayment.class,
											moduleParameter, moduleParameterValue,session);
						}
							if (disbursementList == null || disbursementList.size()==0) {
								addActionMessage(SASConstants.NO_LIST);
							}
						
					return "cashPayment";
				} else if (null != getModuleParameter()&& getSubModule().equalsIgnoreCase("CC")){
					
					if (getModuleParameter().equalsIgnoreCase("payee") || getModuleParameter().equalsIgnoreCase("checkNo")) {
						disbursementList = disbursementManager.listDisbursementsByParameterLike(
										CheckPayments.class, moduleParameter,
										moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						disbursementList = disbursementManager.listDisbursementsCheckWithoutInvoice(session);
						moduleParameterValue="all";
						
					}else {
						disbursementList = disbursementManager.listDisbursementsByParameter(CheckPayments.class,
										moduleParameter, moduleParameterValue,session);
					}
						if ( disbursementList == null || disbursementList.size()==0) {
							addActionMessage(SASConstants.NO_LIST);
						}
					
					return "checkPayment";
				} else {

					if (getModuleParameter().equalsIgnoreCase("payee")) {
						disbursementList = disbursementManager.listDisbursementsByParameterLike(
										CheckPayments.class, moduleParameter,
										moduleParameterValue,session);
					}else if (moduleParameter.equalsIgnoreCase("ALL")) {
						disbursementList = disbursementManager.listDisbursementsWithInvoice(session);
						moduleParameterValue="all";
						
					} else if (moduleParameter.equals("invoiceNo")) {
						disbursementList = disbursementManager.listDisbursementsByParameter(CheckPayments.class,
								"invoice.supplierInvoiceNo", moduleParameterValue,session);
					
					}else if (getModuleParameter().equalsIgnoreCase("checkNo")) {
						disbursementList = disbursementManager.listDisbursementsByParameterLike(
								CheckPayments.class, moduleParameter,
								moduleParameterValue,session);
					}else {
						disbursementList = disbursementManager.listDisbursementsByParameter(CheckPayments.class,
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
