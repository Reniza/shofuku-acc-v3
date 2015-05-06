package com.shofuku.accsystem.utils;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.inventory.Item;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;

public class ImportOfflineOrdersUtil {
	protected final Logger logger = LoggerFactory.getLogger(ImportOfflineOrdersUtil.class);
	
	CustomerManager customerManager = new CustomerManager();
	InventoryManager inventoryManager = new InventoryManager();
	
	RecordCountHelper rch = new RecordCountHelper();
	DateFormatHelper dfh = new DateFormatHelper();
	
	Session session = getSession();
	private Session getSession() {
				return HibernateUtil.getSessionFactory().getCurrentSession();
	}
		
	private String importType;
	private	String lastCellRead= "";
	
	
	private Map<String, Item> getItemMap(ArrayList<Item> itemList ){
		Iterator itemItr = itemList.iterator();
		HashMap itemMap = new HashMap();
		while(itemItr.hasNext()) {
			Item item = (Item) itemItr.next();
			itemMap.put(item.getItemCode(), item);
		}
		return itemMap; 
	}
	
	public void readImportFile(String fileName,Session session) {
		
		Set<PurchaseOrderDetails> orderDetailSet = new HashSet<PurchaseOrderDetails>();
		ArrayList<Item> itemList = inventoryManager.getAllItemList(session);

		try {

			HSSFWorkbook workBook = getWorkbook(fileName);
			int numberOfSheets =  workBook.getNumberOfSheets();
			/*
			 * RULES FOR PODETAILS
			 * SUPPLIER = "C","standard";
			 * CUSTOMER
			 * IF CC then getCustomerType(),"standard"
			 * ELSE getCustomerType(),"transfer",
			 */
			for (int x = 0; x < numberOfSheets; x++) {
				try {
					HSSFSheet hssfSheet = workBook.getSheetAt(x);
					
					//skip headers
					int currentRowPointer=1;
					HSSFRow hssfRow = null;
					HSSFCell cell = null;
					if (importType.equalsIgnoreCase(SASConstants.SUPPLIER)) {
						
					}else if (importType.equalsIgnoreCase(SASConstants.CUSTOMER)) {
						//create customer
						Customer customer = new Customer();
						hssfRow = hssfSheet.getRow(currentRowPointer);
						cell = hssfRow.getCell(SASConstants.IMPORT_COLUMN_CUSTOMER_NO, Row.CREATE_NULL_AS_BLANK);
						customer.setCustomerNo(cell.getStringCellValue());
						customer = customerManager.loadCustomer(customer.getCustomerNo());
						
						//create customerPO
						CustomerPurchaseOrder cpo = new CustomerPurchaseOrder();
						cpo.setCustomer(customer);
						cpo.setCustomerPurchaseOrderId(rch.getPrefix(SASConstants.CUSTOMERPO,SASConstants.CUSTOMERPO_PREFIX));
						cell = hssfRow.getCell(SASConstants.IMPORT_COLUMN_PO_DATE);
						cpo.setPurchaseOrderDate(dfh.dynamicParseWordedDateToTimestamp(hssfRow.getCell(SASConstants.IMPORT_COLUMN_PO_DATE).getDateCellValue()));
						cpo.setDateOfDelivery(dfh.dynamicParseWordedDateToTimestamp(hssfRow.getCell(SASConstants.IMPORT_COLUMN_PO_DELIVERY_DATE).getDateCellValue()));
						cpo.setPaymentDate(dfh.dynamicParseWordedDateToTimestamp(hssfRow.getCell(SASConstants.IMPORT_COLUMN_PO_PAYMENT_DATE).getDateCellValue()));
						cpo.setPaymentTerm(hssfRow.getCell(SASConstants.IMPORT_COLUMN_PO_PAYMENT_TERMS, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
						
						//set cpo details
						
						/*
						 * RULES FOR PODETAILS
						 * SUPPLIER = "C","standard";
						 * CUSTOMER
						 * IF CC then getCustomerType(),"standard"
						 * ELSE getCustomerType(),"transfer",
						 */

						//set cpo purchase order details
						PurchaseOrderDetailHelper poDtlHelper = new PurchaseOrderDetailHelper();
						if(customer.getCustomerType().equalsIgnoreCase("CC")) {
							poDtlHelper.generatePODetailsSet(populateOrderDetail(getItemMap(itemList),customer.getCustomerType() , "standard", SASConstants.IMPORT_COLUMN_PO_ITEM_CODE, hssfSheet, session));
						}else {
							poDtlHelper.generatePODetailsSet(populateOrderDetail(getItemMap(itemList),customer.getCustomerType() , "transfer", SASConstants.IMPORT_COLUMN_PO_ITEM_CODE, hssfSheet, session));
						}
						poDtlHelper.persistNewSetElements(session);
						cpo.setPurchaseOrderDetails(poDtlHelper.generatePODetailsSet());
						customerManager.addCustomerObject(cpo, session);
						//create DR
							//create ape
							/*
							 * populateOrderDetail(customerType, priceType, orderDetailSet, hssfSheet,
							SASConstants.ORDERING_FORM_FIRST_ROW,
							SASConstants.ORDERING_FORM_SECOND_COLUMN, session);
							 */
					}
					

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("POIUtil readOrderingForm() : " + e.toString());
		}
	}
	
	
	private  Set<PurchaseOrderDetails> populateOrderDetail(Map<String, Item> itemMap, String orderType,String priceType,
			int column,HSSFSheet hssfSheet, Session session) throws Exception {
		
		Set<PurchaseOrderDetails> orderDetailSet = new HashSet<PurchaseOrderDetails>();
		/*
		 * RULES FOR PODETAILS
		 * SUPPLIER = "C","standard";
		 * CUSTOMER
		 * IF CC then getCustomerType(),"standard"
		 * ELSE getCustomerType(),"transfer",
		 */
		boolean hasItemsLeft=true;
		int rowNum=1;
		try {
			HSSFRow hssfRow =  hssfSheet.getRow(rowNum);
			
			while (hasItemsLeft){
				PurchaseOrderDetails purchaseOrderDetail = new PurchaseOrderDetails();
				
				lastCellRead="row: "+rowNum+" and column: "+ column;
				String itemCode = hssfRow.getCell(column).getStringCellValue();
					
				lastCellRead="row: "+rowNum+" and column: "+ column+1;
				String description= hssfRow.getCell(column + 1,Row.CREATE_NULL_AS_BLANK).getStringCellValue();

				boolean isUnlisted= false;
				// get item Details from general item list
				Item item = itemMap.get(itemCode);
				// if blank then it is unlisted, get description
				if(item == null) {
					//if description is blank then end of item list
					if(description.equalsIgnoreCase("")) {
						hasItemsLeft = false;
						continue;
					}else {
						isUnlisted=true;
					}
				}

				double quantity = 0.0;
				try {
				quantity = hssfRow.getCell(column + 2,
						Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
				}catch(IllegalStateException ise) {
					quantity = 0.0;
				}
				if (quantity > 0) {
					
					if(!isUnlisted) {
						purchaseOrderDetail.setItemCode(item.getItemCode());
						purchaseOrderDetail.setDescription(item.getDescription());
						purchaseOrderDetail.setUnitOfMeasurement(item.getUnitOfMeasurement());
						purchaseOrderDetail.setQuantity(quantity);
						purchaseOrderDetail.setUnitCost(inventoryManager.getItemPricingByItemCodeAndParameter(session,itemCode, orderType, priceType));
						purchaseOrderDetail.setInFinishedGoods(purchaseOrderDetail.getUnitCost() > 0 ? true : false);
						purchaseOrderDetail.setAmount(purchaseOrderDetail.getQuantity() * purchaseOrderDetail.getUnitCost());
					}else {
						purchaseOrderDetail.setInFinishedGoods(true);
						purchaseOrderDetail.setAmount(0.0);							
					}
					orderDetailSet.add(purchaseOrderDetail);
				}
				hssfRow = hssfSheet.getRow(++rowNum);
				if(hssfRow==null) {
					hasItemsLeft=false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.debug("ImportOfflineOrdersUtil populateOrderDetail() : " +"Last Cell Processed is: "+lastCellRead + e.toString());	
			
		}
		
	return orderDetailSet;
	}


	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}
	
	private HSSFWorkbook getWorkbook(String fileName) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(fileName);
		POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
		return wb;
	}
	
}
