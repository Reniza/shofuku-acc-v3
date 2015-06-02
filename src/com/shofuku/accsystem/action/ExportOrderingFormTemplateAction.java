package com.shofuku.accsystem.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.DisbursementManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerStockLevel;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Item;
import com.shofuku.accsystem.domain.inventory.OfficeSupplies;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.inventory.UnlistedItem;
import com.shofuku.accsystem.domain.inventory.Utensils;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.AccountEntryProfileUtil;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.POIUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.SASConstants;

public class ExportOrderingFormTemplateAction extends ActionSupport implements Preparable {

	private static final long serialVersionUID = -8557620697888911805L;
	
	Map actionSession;
	UserAccount user;

	InventoryUtil inventoryUtil;
	AccountEntryProfileUtil accountEntryUtil;
	POIUtil poiUtil;
	
	SupplierManager supplierManager;
	CustomerManager customerManager;
	InventoryManager inventoryManager; 
	AccountEntryManager accountEntryManager;
	TransactionManager transactionManager;
	LookupManager lookupManager;
	DisbursementManager disbursementManager;

	PurchaseOrderDetails orderDetails;
	PurchaseOrderDetailHelper poDetailsHelperToCompare;
	PurchaseOrderDetailHelper poDetailsGrouped;
	PurchaseOrderDetailHelper poDetailsHelper;
	PurchaseOrderDetailHelper poDetailsHelperDraft;

	// add other managers for other modules Manager()
	
	public void prepare() throws Exception {
		
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		inventoryUtil = new InventoryUtil(actionSession);
		accountEntryUtil = new AccountEntryProfileUtil(actionSession);
		poiUtil = new POIUtil(actionSession);
		
		supplierManager 		= (SupplierManager) 	actionSession.get("supplierManager");
		customerManager 		= (CustomerManager) 	actionSession.get("customerManager");
		inventoryManager 		= (InventoryManager) 	actionSession.get("inventoryManager"); 
		accountEntryManager		= (AccountEntryManager) actionSession.get("accountEntryManager");
		transactionManager 		= (TransactionManager) 	actionSession.get("transactionManager");
		lookupManager 			= (LookupManager) 		actionSession.get("lookupManager");
		disbursementManager 	= (DisbursementManager) actionSession.get("disbursementManager");
		
		if(poDetailsHelper==null) {
			poDetailsHelper = new PurchaseOrderDetailHelper(actionSession);
		}else {
			poDetailsHelper.setActionSession(actionSession);
		}
		if(poDetailsHelperToCompare==null) {
			poDetailsHelperToCompare = new PurchaseOrderDetailHelper(actionSession);
		}else {
			poDetailsHelperToCompare.setActionSession(actionSession);
		}
		if(poDetailsHelperDraft==null) {
			poDetailsHelperDraft = new PurchaseOrderDetailHelper(actionSession);
		}else {
			poDetailsHelperDraft.setActionSession(actionSession);
		}
		if(poDetailsGrouped==null) {
			poDetailsGrouped = new PurchaseOrderDetailHelper(actionSession);
		}else {
			poDetailsGrouped.setActionSession(actionSession);
		}
		
	}
	
	InputStream excelStream;
	String contentDisposition;
	
	String orderingFormType;
	Customer customer;
	List customerNoList;
	
	HashMap<String,HashMap<String,ArrayList<Item>>> itemMap = new HashMap<String,HashMap<String,ArrayList<Item>>>(); 
	
	private static final Logger logger = Logger
			.getLogger(ExportOrderingFormTemplateAction.class);
	
	@Override
	public String execute() throws Exception {

		Session session = getSession();
		
		//GET INVENTORY LIST PER CLASSIFICATION (WET/DRY/OFFICE)
		itemMap = getAllItemList(session);
		
		//GENERATE FILE
		excelStream = generateCurrentOrderingFormTemplate(); 
//		logger.error("THIS IS A TEST LOG");
		
		return SUCCESS;
	}

	public String listCustomer(){
		Session session = getSession();
		customerNoList = customerManager.listAllCustomerNo(session);
		
		return "listCustomer";
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap<String,HashMap<String,ArrayList<Item>>> getAllItemList(Session session) {
		Iterator iterator = null;
		customerNoList = customerManager.listAllCustomerNo(session);
		//START: 2015 - PHASE 3a - stock level per customer
		Map customerSpecificStockLevel = new HashMap<>();
		//temp value: CMJCC01
		if (orderingFormType.equalsIgnoreCase("S")){
			List result = inventoryManager.listByParameter(Customer.class, "customerNo",customer.getCustomerNo(),session);
			customer =(Customer) result.get(0);
			customerSpecificStockLevel = customer.getCustomerStockLevelMap();
			//END: 2015 - PHASE 3a - stock level per customer
		}
		
		
		List<RawMaterial> rawMatList =inventoryManager.listInventoryAlphabeticalAscByParameter(RawMaterial.class, "subClassification",session);
		List<TradedItem> tradedItemList =inventoryManager.listInventoryAlphabeticalAscByParameter(TradedItem.class, "subClassification",session);
		List<Utensils> utensilsList =inventoryManager.listInventoryAlphabeticalAscByParameter(Utensils.class, "subClassification",session);
		List<OfficeSupplies> ofcSupList =inventoryManager.listInventoryAlphabeticalAscByParameter(OfficeSupplies.class, "subClassification",session);
		List<FinishedGood> finList = inventoryManager.listInventoryAlphabeticalAscByParameter(FinishedGood.class, "subClassification", session);
		List<UnlistedItem> ulList = inventoryManager.listAlphabeticalAscByParameter(UnlistedItem.class, "description", session); 
		
		HashMap<String,ArrayList<Item>> subClassMap = new HashMap<String,ArrayList<Item>>();
		
		ArrayList<Item> tempList = new ArrayList<Item>();
		
		iterator = rawMatList.iterator();
		while(iterator.hasNext()) {
			RawMaterial rawMaterial = (RawMaterial) iterator.next();
			if (rawMaterial.getClassification() == null ||
					(!rawMaterial.getTemplate().equalsIgnoreCase(orderingFormType)
					&& (!rawMaterial.getTemplate().equalsIgnoreCase("B")  
						|| rawMaterial.getTemplate().equalsIgnoreCase("N"))))  {
//				System.out.println(rawMaterial.getDescription()+" : not included");
			} else {

				if (itemMap.get(rawMaterial.getClassification()) == null) {
					tempList = new ArrayList<Item>();
					subClassMap = new HashMap<String, ArrayList<Item>>();
				} else {
					subClassMap = itemMap.get(rawMaterial.getClassification());

					if (subClassMap.get(rawMaterial.getSubClassification()) == null) {
						tempList = new ArrayList<Item>();
					} else {
						tempList = (ArrayList<Item>) subClassMap
								.get(rawMaterial.getSubClassification());
					}
				}
				
				//START: 2015 - PHASE 3a - stock level per customer
				CustomerStockLevel csl = (CustomerStockLevel)customerSpecificStockLevel.get(rawMaterial.getItemCode()); 
				double tempStockLevelValue=0;
				if(csl==null) {
				}else {
				tempStockLevelValue=csl.getStockLevel();
				}
				//END: 2015 - PHASE 3a - stock level per customer
				
				//START: 2013 - PHASE 3 : PROJECT 4: MARK
				tempList.add(new Item(rawMaterial.getItemCode(), rawMaterial.getDescription(), rawMaterial.getUnitOfMeasurement(),rawMaterial.getClassification(),
						rawMaterial.getSubClassification(),rawMaterial.getIsVattable(),tempStockLevelValue));
				//END: 2013 - PHASE 3 : PROJECT 4: MARK
				subClassMap.put(rawMaterial.getSubClassification(), tempList);
				itemMap.put(rawMaterial.getClassification(), subClassMap);
			}
		}
		
		iterator = tradedItemList.iterator();
		while(iterator.hasNext()) {
			TradedItem tradedItem = (TradedItem) iterator.next();
			if(tradedItem.getItemCode().equalsIgnoreCase("FD01")) {
//				System.out.println("FOUND");
			}	
			if  (tradedItem.getClassification() == null ||
					(!tradedItem.getTemplate().equalsIgnoreCase(orderingFormType)
					&& (!tradedItem.getTemplate().equalsIgnoreCase("B")  
						|| tradedItem.getTemplate().equalsIgnoreCase("N"))))  {
//				System.out.println(tradedItem.getDescription()+" : not included");
			} else {

				if (itemMap.get(tradedItem.getClassification()) == null) {
					tempList = new ArrayList<Item>();
					subClassMap = new HashMap<String, ArrayList<Item>>();
				} else {
					subClassMap = itemMap.get(tradedItem.getClassification());

					if (subClassMap.get(tradedItem.getSubClassification()) == null) {
						tempList = new ArrayList<Item>();
					} else {
						tempList = (ArrayList<Item>) subClassMap
								.get(tradedItem.getSubClassification());
					}
				}
				
				//START: 2015 - PHASE 3a - stock level per customer
				CustomerStockLevel csl = (CustomerStockLevel)customerSpecificStockLevel.get(tradedItem.getItemCode()); 
				double tempStockLevelValue=0;
				if(csl==null) {
				}else {
				tempStockLevelValue=csl.getStockLevel();
				}
				//END: 2015 - PHASE 3a - stock level per customer
				
				//START: 2013 - PHASE 3 : PROJECT 4: MARK
				tempList.add(new Item(tradedItem.getItemCode(), tradedItem.getDescription(), tradedItem.getUnitOfMeasurement(),
						tradedItem.getClassification(), tradedItem.getSubClassification(),tradedItem.getIsVattable(),tempStockLevelValue));
				//END: 2013 - PHASE 3 : PROJECT 4: MARK
				subClassMap.put(tradedItem.getSubClassification(), tempList);
				itemMap.put(tradedItem.getClassification(), subClassMap);
			}
		}
		
		iterator = utensilsList.iterator();
		while(iterator.hasNext()) {
			Utensils utensils = (Utensils) iterator.next();
			if(utensils.getItemCode().equalsIgnoreCase("FD01")) {
//				System.out.println("FOUND");
			}	
			if  (utensils.getClassification() == null ||
					(!utensils.getTemplate().equalsIgnoreCase(orderingFormType)
					&& (!utensils.getTemplate().equalsIgnoreCase("B")  
						|| utensils.getTemplate().equalsIgnoreCase("N"))))  {
//				System.out.println(utensils.getDescription()+" : not included");
			} else {

				if (itemMap.get(utensils.getClassification()) == null) {
					tempList = new ArrayList<Item>();
					subClassMap = new HashMap<String, ArrayList<Item>>();
				} else {
					subClassMap = itemMap.get(utensils.getClassification());

					if (subClassMap.get(utensils.getSubClassification()) == null) {
						tempList = new ArrayList<Item>();
					} else {
						tempList = (ArrayList<Item>) subClassMap
								.get(utensils.getSubClassification());
					}
				}
				
				//START: 2015 - PHASE 3a - stock level per customer
				CustomerStockLevel csl = (CustomerStockLevel)customerSpecificStockLevel.get(utensils.getItemCode()); 
				double tempStockLevelValue=0;
				if(csl==null) {
				}else {
				tempStockLevelValue=csl.getStockLevel();
				}
				//END: 2015 - PHASE 3a - stock level per customer
				
				//START: 2013 - PHASE 3 : PROJECT 4: MARK
				tempList.add(new Item(utensils.getItemCode(), utensils.getDescription(), utensils.getUnitOfMeasurement(),
						utensils.getClassification(), utensils.getSubClassification(),utensils.getIsVattable(),tempStockLevelValue));
				//END: 2013 - PHASE 3 : PROJECT 4: MARK
				subClassMap.put(utensils.getSubClassification(), tempList);
				itemMap.put(utensils.getClassification(), subClassMap);
			}
		}
		
		iterator = ofcSupList.iterator();
		while(iterator.hasNext()) {
			OfficeSupplies ofcSup = (OfficeSupplies) iterator.next();
			if(ofcSup.getItemCode().equalsIgnoreCase("FD01")) {
//				System.out.println("FOUND");
			}	
			if  (ofcSup.getClassification() == null ||
					(!ofcSup.getTemplate().equalsIgnoreCase(orderingFormType)
					&& (!ofcSup.getTemplate().equalsIgnoreCase("B")  
						|| ofcSup.getTemplate().equalsIgnoreCase("N"))))  {
//				System.out.println(ofcSup.getDescription()+" : not included");
			} else {

				if (itemMap.get(ofcSup.getClassification()) == null) {
					tempList = new ArrayList<Item>();
					subClassMap = new HashMap<String, ArrayList<Item>>();
				} else {
					subClassMap = itemMap.get(ofcSup.getClassification());

					if (subClassMap.get(ofcSup.getSubClassification()) == null) {
						tempList = new ArrayList<Item>();
					} else {
						tempList = (ArrayList<Item>) subClassMap
								.get(ofcSup.getSubClassification());
					}
				}
				
				//START: 2015 - PHASE 3a - stock level per customer
				CustomerStockLevel csl = (CustomerStockLevel)customerSpecificStockLevel.get(ofcSup.getItemCode()); 
				double tempStockLevelValue=0;
				if(csl==null) {
				}else {
				tempStockLevelValue=csl.getStockLevel();
				}
				//END: 2015 - PHASE 3a - stock level per customer
				
				//START: 2013 - PHASE 3 : PROJECT 4: MARK
				tempList.add(new Item(ofcSup.getItemCode(), ofcSup.getDescription(), ofcSup.getUnitOfMeasurement(),
						ofcSup.getClassification(), ofcSup.getSubClassification(),ofcSup.getIsVattable(),tempStockLevelValue));
				//END: 2013 - PHASE 3 : PROJECT 4: MARK
				subClassMap.put(ofcSup.getSubClassification(), tempList);
				itemMap.put(ofcSup.getClassification(), subClassMap);
			}
		}
		
		tempList = new ArrayList<Item>();
		iterator = finList.iterator();
		while(iterator.hasNext()){
			FinishedGood finGood = (FinishedGood) iterator.next();
			if(finGood.getClassification() == null ||
					(!finGood.getTemplate().equalsIgnoreCase(orderingFormType)
					&& (!finGood.getTemplate().equalsIgnoreCase("B")  
						|| finGood.getTemplate().equalsIgnoreCase("N"))))  {
//				System.out.println(finGood.getDescription()+" : not included");	
			}else {
				if(itemMap.get(finGood.getClassification())==null) {
					tempList = new ArrayList<Item>();
					subClassMap = new HashMap<String,ArrayList<Item>>();
				}else {
					subClassMap = itemMap.get(finGood.getClassification());
					
					if(subClassMap.get(finGood.getSubClassification())==null) {
						tempList = new ArrayList<Item>();
					}else {
						tempList = (ArrayList<Item>) subClassMap.get(finGood.getSubClassification());
					}
				}
				
				//START: 2015 - PHASE 3a - stock level per customer
				CustomerStockLevel csl = (CustomerStockLevel)customerSpecificStockLevel.get(finGood.getProductCode()); 
				double tempStockLevelValue=0;
				if(csl==null) {
				}else {
				tempStockLevelValue=csl.getStockLevel();
				}
				//END: 2015 - PHASE 3a - stock level per customer
				
				
				//START: 2013 - PHASE 3 : PROJECT 4: MARK
				tempList.add(new Item(finGood.getProductCode(), finGood.getDescription(), finGood.getUnitOfMeasurement(),
						finGood.getClassification(),finGood.getSubClassification(),finGood.getIsVattable(),tempStockLevelValue));
				//END: 2013 - PHASE 3 : PROJECT 4: MARK
				subClassMap.put(finGood.getSubClassification(), tempList);
				itemMap.put(finGood.getClassification(),subClassMap);
			}
		}
		
		iterator = ulList.iterator();
		while(iterator.hasNext()) {
			UnlistedItem unlistedItem = (UnlistedItem) iterator.next();
			if(unlistedItem.getClassification() == null) {
//				System.out.println(unlistedItem.getDescription()+" : not included");	
			}else {
				if (itemMap.get(unlistedItem.getClassification()) == null) {
					tempList = new ArrayList<Item>();
					subClassMap = new HashMap<String, ArrayList<Item>>();
				} else {
					subClassMap = itemMap.get(unlistedItem.getClassification());

					if (subClassMap.get(SASConstants.UNLISTED_ITEMS) == null) {
						tempList = new ArrayList<Item>();
					} else {
						tempList = (ArrayList<Item>) subClassMap
								.get(SASConstants.UNLISTED_ITEMS);
					}
				}
				//START: 2015 - PHASE 3a - stock level per customer
			
				double tempStockLevelValue=0;
				
				tempList.add(new Item(unlistedItem.getItemCode(), unlistedItem.getDescription(), unlistedItem.getUom(),unlistedItem.getClassification(), SASConstants.UNLISTED_ITEMS,"N",tempStockLevelValue));
				subClassMap.put(SASConstants.UNLISTED_ITEMS, tempList);
				itemMap.put(unlistedItem.getClassification(), subClassMap);
				//END: 2015 - PHASE 3a - stock level per customer
			}
		}
		
		return itemMap;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
	}
	
	private InputStream generateCurrentOrderingFormTemplate() {
		InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.ORDERING_FORM_ROOT);
		POIFSFileSystem fsFileSystem = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			fsFileSystem = new POIFSFileSystem(pdInputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
			populateWorkSheets(wb);
			wb.write(baos);

			
		} catch (IOException e) {
			logger.error("GENERATE CURRENT ORDERING FORM TEMPLATE EXCEPTION: "+ e.toString());
			e.printStackTrace();
			return null;
		}
		return new ByteArrayInputStream(baos.toByteArray());
	}
	HSSFCellStyle subCategoryCellStyle ;
	HSSFCellStyle itemListStyle;
	HSSFCellStyle itemListStyleUnLockedWithBorder;
	HSSFCellStyle itemListStyleUnLockedWithBottomBorder;
	HSSFCellStyle headerListStyle;
	HSSFCellStyle noStyle;
	
	@SuppressWarnings("rawtypes")
	private void populateWorkSheets(HSSFWorkbook wb) {
		int sheetCtr=0;
		int currentRow = 9;
		int currentColumn = 1;
		
		int rowLimit = 0;
		
		//for unlistedItemPointer
		int lastRow=0;
		
		HSSFSheet sheet = wb.getSheetAt(sheetCtr);
		sheet.protectSheet("makoy");
		
		//subcategory style
		HSSFRow row = sheet.getRow(1);
		HSSFCell cell = row.getCell(15);
		subCategoryCellStyle = cell.getCellStyle();
		subCategoryCellStyle.setLocked(true);
		
		//headeritem style
		row = sheet.getRow(2);
		cell = row.getCell(15);
		headerListStyle = cell.getCellStyle();
		headerListStyle.setLocked(true);
		
		//normal item style
		row = sheet.getRow(3);
		cell = row.getCell(15);
		itemListStyle = cell.getCellStyle();
		itemListStyle.setLocked(true);
		
		//no style
		row = sheet.getRow(4);
		cell = row.getCell(15);
		noStyle = cell.getCellStyle();
		noStyle.setLocked(true);
		
		
		
		//normal item style (not Locked)
		itemListStyleUnLockedWithBorder = wb.createCellStyle();
		itemListStyleUnLockedWithBorder.setLocked(false);
		itemListStyleUnLockedWithBorder.setBorderBottom(CellStyle.BORDER_THIN);
		itemListStyleUnLockedWithBorder.setBorderLeft(CellStyle.BORDER_THIN);
		itemListStyleUnLockedWithBorder.setBorderRight(CellStyle.BORDER_THIN);
		itemListStyleUnLockedWithBorder.setBorderTop(CellStyle.BORDER_THIN);
		
		//normal item style (not Locked)
		itemListStyleUnLockedWithBottomBorder = wb.createCellStyle();
		itemListStyleUnLockedWithBottomBorder.setLocked(false);
		itemListStyleUnLockedWithBottomBorder.setBorderBottom(CellStyle.BORDER_THIN);
		itemListStyleUnLockedWithBottomBorder.setBorderLeft(CellStyle.BORDER_NONE);
		itemListStyleUnLockedWithBottomBorder.setBorderRight(CellStyle.BORDER_NONE);
		itemListStyleUnLockedWithBottomBorder.setBorderTop(CellStyle.BORDER_NONE);
		
		//write customer name
		row = sheet.getRow(2);
		cell = row.getCell(9);
		cell.setCellStyle(itemListStyleUnLockedWithBottomBorder);
		poiUtil.putCellValue(cell, customer.getCustomerNo());
		
		row = sheet.getRow(3);
		cell = row.getCell(9);
		cell.setCellStyle(itemListStyleUnLockedWithBottomBorder);
		poiUtil.putCellValue(cell, "<BLANK>");
		
		row = sheet.getRow(4);
		cell = row.getCell(9);
		cell.setCellStyle(itemListStyleUnLockedWithBottomBorder);
		poiUtil.putCellValue(cell, "<BLANK>");
		
		row = sheet.getRow(5);
		cell = row.getCell(9);
		cell.setCellStyle(itemListStyleUnLockedWithBottomBorder);
		poiUtil.putCellValue(cell, "<BLANK>");
		
		row = sheet.getRow(6);
		cell = row.getCell(9);
		cell.setCellStyle(itemListStyleUnLockedWithBottomBorder);
		poiUtil.putCellValue(cell, "<BLANK>");
		
		
		
		for(HashMap<String, ArrayList<Item>> subClassessMap : itemMap.values()) {
			//iterate sheets
			sheet = wb.getSheetAt(sheetCtr++);
			rowLimit = getRowLimit(subClassessMap);
			String sheetName = sheet.getSheetName();
//			System.out.println("NOW PROCESSING SHEET: "+ sheetName);
			ArrayList<Item> unlistedItemList= new ArrayList<Item>();
			for(ArrayList<Item> itemArrayListPerSubClass : subClassessMap.values()) {
				//create sub class header
				//TODO:addNullCheck
				String subClassDesc = ((Item)itemArrayListPerSubClass.get(0)).getSubClassification();
				if(subClassDesc.equalsIgnoreCase(SASConstants.UNLISTED_ITEMS)) {
					unlistedItemList.addAll(itemArrayListPerSubClass);
					continue;
				}
				createSubClassHeader(sheet,subClassDesc,currentRow,currentColumn);
				currentRow+=2;
				Iterator iterator = itemArrayListPerSubClass.iterator();
				while(iterator.hasNext()) {
					Item tempItem = (Item)iterator.next();
					insertItem(sheet,currentRow++,currentColumn,tempItem);
				}
				
				lastRow=currentRow;
				if(currentRow>rowLimit/2) {
					currentColumn++;
					
					currentRow = 9;
				}
			}
			
			//write unlisted items
			currentRow=lastRow++;
			currentColumn = 2;
			
			createSubClassHeader(sheet,SASConstants.UNLISTED_ITEMS,currentRow,currentColumn);
			currentRow+=2;
			Iterator iterator = unlistedItemList.iterator();
			while(iterator.hasNext()) {
				Item tempItem = (Item)iterator.next();
				//to force write on 2nd column
				insertItem(sheet,currentRow++,currentColumn,tempItem);
			}
			
			
			currentRow = 9;
			currentColumn = 1;
			
		}
		
		
		//delete style template columns
		sheet = wb.getSheetAt(0);
		
		//subcategory style
		row = sheet.getRow(1);
		cell = row.createCell(15);
		cell.setCellValue("");
		cell.setCellStyle(noStyle);
		
		//normal item style
		row = sheet.getRow(2);
		cell = row.createCell(15);
		cell.setCellValue("");
		cell.setCellStyle(noStyle);
		
		//headeritem style
		row = sheet.getRow(3);
		cell = row.createCell(15);		
		cell.setCellValue("");
		cell.setCellStyle(noStyle);
		
		//no style
		row = sheet.getRow(4);
		cell = row.getCell(15);
		cell.setCellValue("");
		cell.setCellStyle(noStyle);
		
	}
	private int getRowLimit(HashMap<String, ArrayList<Item>> subClassessMap) {
		int ctr =0;
		for(ArrayList<Item> itemArrayListPerSubClass : subClassessMap.values()) {
			//To consider rows for writing subclass headers					
			ctr+=2;
			Iterator iterator = itemArrayListPerSubClass.iterator();
			while(iterator.hasNext()) {
				Item tempItem = (Item)iterator.next();
				ctr++;
			}
		}
		return ctr;
	}

	private void insertItem(HSSFSheet sheet, int currentRow, int currentColumn,
			Item tempItem) {
		// TODO Auto-generated method stub
		
		if(currentColumn==1) {
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, tempItem.getItemCode());
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, tempItem.getDescription());
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, tempItem.getUnitOfMeasurement());
			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, "");
			cell = poiUtil.getCurrentCell(row,4);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, tempItem.getCustomerStockLevel());
			cell = poiUtil.getCurrentCell(row,5);
			cell.setCellStyle(itemListStyleUnLockedWithBorder);
			poiUtil.putCellValue(cell, "");
			
		}else if (currentColumn ==2) {
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,7);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, tempItem.getItemCode());
			cell = poiUtil.getCurrentCell(row,8);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, tempItem.getDescription());
			cell = poiUtil.getCurrentCell(row,9);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, tempItem.getUnitOfMeasurement());
			cell = poiUtil.getCurrentCell(row,10);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, "");
			cell = poiUtil.getCurrentCell(row,11);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, tempItem.getCustomerStockLevel());
			cell = poiUtil.getCurrentCell(row,12);
			cell.setCellStyle(itemListStyleUnLockedWithBorder);
			poiUtil.putCellValue(cell, "");
		}
		
	}

	//current row = current row being written on
	//current column = either 1 or 2
	@SuppressWarnings("deprecation")
	private void createSubClassHeader(HSSFSheet sheet,String subClass, int currentRow,int currentColumn) {

		// TODO CHANGE HARD CODED TO CONSTANTS
		// try to apply color
		/*
		//apply green
		HSSFCellStyle style = poiUtil.getCurrentCell(row,1).getCellStyle();
		
		HSSFColor lightGreen = poiUtil.prepareCellColor(sheet.getWorkbook(), 205,255,204);
		 * HSSFColor darkYellow = poiUtil.prepareCellColor(sheet.getWorkbook(), 255,255,0);
		 * HSSFColor lightYellow = poiUtil.prepareCellColor(sheet.getWorkbook(), 255,255,153); 
			style.setFillForegroundColor(lightGreen.getIndex());
		 */
		 
		HSSFRow row=poiUtil.getRow(sheet, currentRow);
		if(currentColumn==1) {
			HSSFCell cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "Inventory");
			cell = poiUtil.getCurrentCell(row,4);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "Stock");
			cell = poiUtil.getCurrentCell(row,5);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "Store");
			
			row=poiUtil.getRow(sheet, ++currentRow);
			cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "CODE");
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(subCategoryCellStyle);
			poiUtil.putCellValue(cell, subClass);
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "UOM");
			
			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "Physical Count");
			cell = poiUtil.getCurrentCell(row,4);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "Level");
			cell = poiUtil.getCurrentCell(row,5);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "ORDER");
			
		}else if (currentColumn ==2) {
			HSSFCell cell = poiUtil.getCurrentCell(row,10);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "QTY");
			cell = poiUtil.getCurrentCell(row,11);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "Stock");
			cell = poiUtil.getCurrentCell(row,12);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "Store");
			
			row=poiUtil.getRow(sheet, ++currentRow);
			cell = poiUtil.getCurrentCell(row,7);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "CODE");
			cell = poiUtil.getCurrentCell(row,8);
			cell.setCellStyle(subCategoryCellStyle);
			poiUtil.putCellValue(cell, subClass);
			cell = poiUtil.getCurrentCell(row,9);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "UOM");
			cell = poiUtil.getCurrentCell(row,10);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "Physical Count");
			cell = poiUtil.getCurrentCell(row,11);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "Level");
			cell = poiUtil.getCurrentCell(row,12);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "ORDER");
		}
		
		
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getContentDisposition() {
		
		return "filename=\""+ SASConstants.ORDERING_FORM_FILENAME +"\"";
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public String getOrderingFormType() {
		return orderingFormType;
	}

	public void setOrderingFormType(String orderingFormType) {
		this.orderingFormType = orderingFormType;
	}

	public List getCustomerNoList() {
		return customerNoList;
	}

	public void setCustomerNoList(List customerNoList) {
		this.customerNoList = customerNoList;
	}
	
}
