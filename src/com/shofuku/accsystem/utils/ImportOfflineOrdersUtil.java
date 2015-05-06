package com.shofuku.accsystem.utils;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.inventory.Item;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.UnlistedItem;

public class ImportOfflineOrdersUtil {
	protected final Logger logger = LoggerFactory.getLogger(ImportOfflineOrdersUtil.class);
	
	CustomerManager customerManager = new CustomerManager();
	InventoryManager inventoryManager = new InventoryManager();
	
	RecordCountHelper rch = new RecordCountHelper();
	DateFormatHelper dfh = new DateFormatHelper();
	
	private List<String> errorString;
	
	private String importType;
	private	String lastCellRead= "";
	
	
	public void readImportFile(String fileName,Session session) {
		
		errorString = new ArrayList<String>();
		
		Set<PurchaseOrderDetails> orderDetailSet = new HashSet<PurchaseOrderDetails>();
		ArrayList<Item> itemList = inventoryManager.getAllItemList(session);
		
		/*
		 * RULES FOR PODETAILS
		 * SUPPLIER = "C","standard";
		 * CUSTOMER
		 * IF CC then getCustomerType(),"standard"
		 * ELSE getCustomerType(),"transfer",
		 */
		
		try {

			HSSFWorkbook workBook = getWorkbook(fileName);
			int numberOfSheets =  workBook.getNumberOfSheets();

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
						
						if(customer==null) {
							errorString.add("Customer Number "+customer.getCustomerNo()+" is not existing");
							continue;
						}
						
						//create customerPO
						CustomerPurchaseOrder cpo = new CustomerPurchaseOrder();
						cpo.setCustomer(customer);
						cpo.setCustomerPurchaseOrderId(rch.getPrefix(SASConstants.CUSTOMERPO,SASConstants.CUSTOMERPO_PREFIX));
						
						//read columns and log for errors
						
						try {
							cpo.setPurchaseOrderDate(dfh.dynamicParseWordedDateToTimestamp(hssfRow.getCell(SASConstants.IMPORT_COLUMN_PO_DATE).getDateCellValue()));
						} catch (Exception e) {errorString.add("purchase order Date on sheet "+x+" is invalid");						}
						
						try {
						cpo.setDateOfDelivery(dfh.dynamicParseWordedDateToTimestamp(hssfRow.getCell(SASConstants.IMPORT_COLUMN_PO_DELIVERY_DATE).getDateCellValue()));
						} catch (Exception e) {errorString.add("PO Delivery Date on sheet "+x+" is invalid");						}
						
						try {
						cpo.setPaymentDate(dfh.dynamicParseWordedDateToTimestamp(hssfRow.getCell(SASConstants.IMPORT_COLUMN_PO_PAYMENT_DATE).getDateCellValue()));
						} catch (Exception e) {errorString.add("PO Payment Date on sheet "+x+" is invalid");						}	
						
						try {
						cpo.setPaymentTerm(hssfRow.getCell(SASConstants.IMPORT_COLUMN_PO_PAYMENT_TERMS, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
						} catch (Exception e) {errorString.add("PO Payment term on sheet "+x+" is invalid");						}	
						
						// set cpo purchase order details
						// RULES FOR PODETAILS APPLIED HERE
						PurchaseOrderDetailHelper poDtlHelper = new PurchaseOrderDetailHelper();
						if(customer.getCustomerType().equalsIgnoreCase("CC")) {
							populateOrderDetail(getItemMap(itemList),customer.getCustomerType() , "standard", SASConstants.IMPORT_COLUMN_PO_ITEM_CODE, hssfSheet, session);
						}else {
							orderDetailSet = populateOrderDetail(getItemMap(itemList),customer.getCustomerType() , "transfer", SASConstants.IMPORT_COLUMN_PO_ITEM_CODE, hssfSheet, session);
						}
						poDtlHelper.generatePODetailsSet(orderDetailSet);
						poDtlHelper.generateCommaDelimitedValues();
						poDtlHelper.setOrderDate(cpo.getPurchaseOrderDate());
						
						cpo.setPurchaseOrderDetails(poDtlHelper.persistNewSetElements(session));
						boolean addResult =customerManager.addCustomerObject(cpo, session);
						if (addResult) {
							rch.updateCount(SASConstants.CUSTOMERPO, "add");
						}

						/*
						 * BEGIN - ADD Delivery Receipt
						 */
					}
					

				} catch (Exception e) {
					e.printStackTrace();
					errorString.add("Unknown error on sheet "+x+" unable to add order");
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
		boolean hasItemsLeft=true;
		int rowNum=1;
		try {
			HSSFRow hssfRow =  hssfSheet.getRow(rowNum);
			
			while (hasItemsLeft){
				try {
					PurchaseOrderDetails purchaseOrderDetail = new PurchaseOrderDetails();
					
					lastCellRead="row: "+rowNum+" and column: "+ column;
					String itemCode = hssfRow.getCell(column,Row.CREATE_NULL_AS_BLANK).getStringCellValue();
						
					lastCellRead="row: "+rowNum+" and column: "+ column+1;
					String description= hssfRow.getCell(column + 1,Row.CREATE_NULL_AS_BLANK).getStringCellValue();

					boolean isUnlisted= false;
					
					// get item Details from pre-loaded general itemMap
					Item item = itemMap.get(itemCode);
					
					// if blank then and has a description it is unlisted, get description
					if(item == null) {
						//if description is blank then end of item list
						if(description.equalsIgnoreCase("")) {
							if(!itemCode.equalsIgnoreCase("")) {
								errorString.add("Error on item :   "+ itemCode +" unable to add this item  (non existing)");
							}else {
								hasItemsLeft = false;
							}
							hssfRow = hssfSheet.getRow(++rowNum);
							if(hssfRow==null) {
								hasItemsLeft=false;
							}
							continue;
						}else {
							isUnlisted=true;
						}
					}

					double quantity = 0.0;
					try {
					quantity = hssfRow.getCell(column + 3,
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
							UnlistedItem unlistedItem = new UnlistedItem();
							try {
								unlistedItem = (UnlistedItem) inventoryManager.listInventoryByParameterLike(UnlistedItem.class, "description",
										description,session).get(0);
								purchaseOrderDetail.setInFinishedGoods(true);
								purchaseOrderDetail.setAmount(0.0);
							} catch (IndexOutOfBoundsException e) {
								errorString.add("Error on item with description "+ description +" unable to add this item  (non existing)");
								logger.debug("Unlisted Item Not Yet existing in database: " +" Cell Processed is: "+lastCellRead);	
								hssfRow = hssfSheet.getRow(++rowNum);
								if(hssfRow==null) {
									hasItemsLeft=false;
								}
								continue;
							}
								
						}
						orderDetailSet.add(purchaseOrderDetail);
					}
					hssfRow = hssfSheet.getRow(++rowNum);
					if(hssfRow==null) {
						hasItemsLeft=false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorString.add("Error on item number  "+ rowNum +" unable to add this item");
					logger.debug("ImportOfflineOrdersUtil populateOrderDetail() : " +"Last Cell Processed is: "+lastCellRead + e.toString());	
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			errorString.add("Unknown error on sheet "+ hssfSheet.getSheetName() +" unable to add order details");
			logger.debug("ImportOfflineOrdersUtil populateOrderDetail() : " +"Last Cell Processed is: "+lastCellRead + e.toString());	
			
		}
		
	return orderDetailSet;
	}

	private HSSFWorkbook getWorkbook(String fileName) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(fileName);
		POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
		return wb;
	}
	

	private Map<String, Item> getItemMap(ArrayList<Item> itemList ){
		Iterator itemItr = itemList.iterator();
		HashMap itemMap = new HashMap();
		while(itemItr.hasNext()) {
			Item item = (Item) itemItr.next();
			itemMap.put(item.getItemCode(), item);
		}
		return itemMap; 
	}

	
	//getter setters
	
	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}


	public List<String> getErrorString() {
		return errorString;
	}

	public void setErrorString(List<String> errorString) {
		this.errorString = errorString;
	}

}
