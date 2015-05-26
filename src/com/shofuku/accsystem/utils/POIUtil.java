package com.shofuku.accsystem.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shofuku.accsystem.controllers.BaseController;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.customers.Customer;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.customers.CustomerStockLevel;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.disbursements.CashPayment;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.financials.AccountEntryProfile;
import com.shofuku.accsystem.domain.inventory.FPTS;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Ingredient;
import com.shofuku.accsystem.domain.inventory.OfficeSupplies;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.RequisitionForm;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.inventory.Utensils;
import com.shofuku.accsystem.domain.receipts.CashCheckReceipts;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.Supplier;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.domain.suppliers.SupplierPurchaseOrder;


//import com.sun.org.apache.bcel.internal.generic.NEW;

@SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked" })
public class POIUtil {
	protected final Logger logger = LoggerFactory.getLogger(POIUtil.class);
	
	Map<String,Object> actionSession;
	BaseController manager;
	private void initializeController() {
		transactionManager = (TransactionManager) actionSession.get("transactionManager");
		inventoryManager = (InventoryManager) actionSession.get("inventoryManager");
	}

	TransactionManager transactionManager;
	InventoryManager inventoryManager;
	
	PurchaseOrderDetailHelper podetailHelper = new PurchaseOrderDetailHelper(actionSession);
	DateFormatHelper dfh = new DateFormatHelper();
	
	
	HSSFCellStyle itemStyle;
	HSSFCellStyle lastItemStyle;
	HSSFCellStyle itemSoldTotalAmountStyle;
	
	String maxDate;
	String minDate;
	
	//START 2013 - PHASE 3 : PROJECT 1: MARK
	
	//END 2013 - PHASE 3 : PROJECT 1: MARK  
	
	Session session = getSession();
	private Session getSession() {
				return HibernateUtil.getSessionFactory().getCurrentSession();
	}
		
			
	public POIUtil(Map<String, Object> actionSession) {
		this.actionSession = actionSession;
	}


	private HSSFWorkbook getWorkbook(String fileName) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(fileName);
		POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
		return wb;
	}

	public Set<PurchaseOrderDetails> readOrderingForm(String customerType, String priceType,
			String fileName, String orderType, Session session) {
		Set<PurchaseOrderDetails> orderDetailSet = new HashSet<PurchaseOrderDetails>();

		try {

			HSSFWorkbook workBook = getWorkbook(fileName);
			// TODO: create iteration if order contains multiple sheets
			for (int x = 0; x < 3; x++) {
				try {
					HSSFSheet hssfSheet = workBook.getSheetAt(x);
					populateOrderDetail(customerType, priceType, orderDetailSet, hssfSheet,
							SASConstants.ORDERING_FORM_FIRST_ROW,
							SASConstants.ORDERING_FORM_FIRST_COLUMN, session);

					populateOrderDetail(customerType, priceType, orderDetailSet, hssfSheet,
							SASConstants.ORDERING_FORM_FIRST_ROW,
							SASConstants.ORDERING_FORM_SECOND_COLUMN, session);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("POIUtil readOrderingForm() : " + e.toString());
		}
		return orderDetailSet;

	}
	
	public Customer readCustomerStockLevelForm(Customer customer,String fileName, Session session) {
		
		try {

			HSSFWorkbook workBook = getWorkbook(fileName);
			// TODO: create iteration if order contains multiple sheets
			for (int x = 0; x < 3; x++) {
				try {
					HSSFSheet hssfSheet = workBook.getSheetAt(x);
					populateCustomerStockLevel(customer,hssfSheet,SASConstants.ORDERING_FORM_FIRST_ROW,
							SASConstants.ORDERING_FORM_FIRST_COLUMN, session);
					
					populateCustomerStockLevel(customer,hssfSheet,SASConstants.ORDERING_FORM_FIRST_ROW,
							SASConstants.ORDERING_FORM_SECOND_COLUMN, session);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("POIUtil readOrderingForm() : " + e.toString());
		}
		return customer;

	}
	

	private Transaction getCurrentTransaction(Session session) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
		} catch (RuntimeException runtimeExecption) {
			tx = session.getTransaction();
		}
		return tx;
	}


	private void populateOrderDetail(String customerType,String priceType,
			Set<PurchaseOrderDetails> orderDetailList, HSSFSheet hssfSheet,
			int itemSectionIndex, int column, Session session) throws Exception {

		initializeController();
		HSSFRow hssfRow = hssfSheet.getRow(itemSectionIndex);
		String group = "";
		
		//retrieve max rows as defined in record count table
		RecordCountHelper rch = new RecordCountHelper(actionSession);
		int maxRows = rch.getOrderingTemplateMaxRows();
		
		while (itemSectionIndex < maxRows) {
			try {
				if (!(null == hssfRow.getCell(column + 0, Row.CREATE_NULL_AS_BLANK))
						&& ("CODE".equalsIgnoreCase(hssfRow.getCell(column + 0,
								Row.CREATE_NULL_AS_BLANK).getStringCellValue()))) {
					group = (hssfRow.getCell(1 + column + 0,
							Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				}
			}catch(NullPointerException npe) {
				logger.debug("POIUtil populateOrderDetail() : " + npe.toString());
				break;
			}
			try {
				while (!(null == hssfRow.getCell(column + 0,
						Row.CREATE_NULL_AS_BLANK))
						&& !("CODE".equalsIgnoreCase(hssfRow.getCell(column + 0,Row.CREATE_NULL_AS_BLANK).getStringCellValue()))
						
						&& (
							(!("".equalsIgnoreCase(hssfRow.getCell(column + 0,Row.CREATE_NULL_AS_BLANK).getStringCellValue())))
							|| (
								("".equalsIgnoreCase(hssfRow.getCell(column + 0,Row.CREATE_NULL_AS_BLANK).getStringCellValue())) 
								&& SASConstants.UNLISTED_ITEMS.equalsIgnoreCase(group)
							   )
						   )
						
						
						) 
				
					{
					PurchaseOrderDetails purchaseOrderDetails = new PurchaseOrderDetails();
					String itemCode = hssfRow.getCell(column + 0,
							Row.CREATE_NULL_AS_BLANK).getStringCellValue();
					String description = hssfRow.getCell(column + 1,
							Row.CREATE_NULL_AS_BLANK).getStringCellValue();
					String uom = hssfRow.getCell(column + 2,
							Row.CREATE_NULL_AS_BLANK).getStringCellValue();
					double quantity = 0.0;
					try {
					quantity = hssfRow.getCell(column + 5,
							Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
					}catch(IllegalStateException ise) {
						quantity = 0.0;
					}
					if (quantity > 0) {
						purchaseOrderDetails.setItemCode(itemCode == null ? ""
								: itemCode);
						purchaseOrderDetails
								.setDescription(description == null ? ""
										: description);
						purchaseOrderDetails.setUnitOfMeasurement(uom == null ? ""
								: uom);
						purchaseOrderDetails.setQuantity(quantity);
						if(group.equalsIgnoreCase(SASConstants.UNLISTED_ITEMS)){
							purchaseOrderDetails
							.setInFinishedGoods(true);
							purchaseOrderDetails
							.setAmount(0.0);
						}else {
							purchaseOrderDetails.setUnitCost(inventoryManager
									.getItemPricingByItemCodeAndParameter(session,
											itemCode, customerType, priceType));
							purchaseOrderDetails
							.setInFinishedGoods(purchaseOrderDetails
									.getUnitCost() > 0 ? true : false);
							purchaseOrderDetails
							.setAmount(purchaseOrderDetails.getQuantity()
									* purchaseOrderDetails.getUnitCost());
						}
	
						purchaseOrderDetails.setGroup(group);
						orderDetailList.add(purchaseOrderDetails);
					}
					hssfRow = hssfSheet.getRow(++itemSectionIndex);
				}
			}catch(NullPointerException npe) {
				//when reached the end of rows but is less than max limit
				break;
			}
			if (itemSectionIndex < hssfSheet.getLastRowNum()) {
				itemSectionIndex++;
				hssfRow = hssfSheet.getRow(itemSectionIndex) == null ? hssfSheet
						.createRow(itemSectionIndex) : hssfSheet
						.getRow(itemSectionIndex);
			} else {
				break;
			}

		}

	}
	
	
	private void populateCustomerStockLevel(Customer customer, HSSFSheet hssfSheet,
			int itemSectionIndex, int column, Session session) throws Exception {

		HSSFRow hssfRow = hssfSheet.getRow(itemSectionIndex);
		String group = "";
		
		Map customerStockLevelMap;
		
		if(customer.getCustomerStockLevelMap()==null) {
			customerStockLevelMap= new HashMap();
		}else {
			customerStockLevelMap = customer.getCustomerStockLevelMap();
		}
		
		
		//retrieve max rows as defined in record count table
		RecordCountHelper rch = new RecordCountHelper(actionSession);
		int maxRows = rch.getCustomerStockLevelTemplateMaxRows();
		
		while (itemSectionIndex < maxRows) {
			if (!(null == hssfRow.getCell(column + 0, Row.CREATE_NULL_AS_BLANK))
					&& ("CODE".equalsIgnoreCase(hssfRow.getCell(column + 0,
							Row.CREATE_NULL_AS_BLANK).getStringCellValue()))) {
				group = (hssfRow.getCell(1 + column + 0,
						Row.CREATE_NULL_AS_BLANK).getStringCellValue());
			}
			try {
				while (!(null == hssfRow.getCell(column + 0,
						Row.CREATE_NULL_AS_BLANK))
						&& !("CODE".equalsIgnoreCase(hssfRow.getCell(column + 0,
								Row.CREATE_NULL_AS_BLANK).getStringCellValue()))
						&& !("".equalsIgnoreCase(hssfRow.getCell(column + 0,
								Row.CREATE_NULL_AS_BLANK).getStringCellValue()))) {
					
					CustomerStockLevel csl = new CustomerStockLevel();
					double stockLevel = 0.0;
					String itemCode= hssfRow.getCell(column + 0,
							Row.CREATE_NULL_AS_BLANK).getStringCellValue();
					
					try {
						stockLevel = hssfRow.getCell(column + 4,
								Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
						}catch(IllegalStateException ise) {
							stockLevel = 0.0;
					}
					
					
					
					if (stockLevel > 0) {
						customerStockLevelMap.put(itemCode, new CustomerStockLevel(stockLevel,itemCode));
					}else {
						if(customerStockLevelMap.get(itemCode)!=null) {
							customerStockLevelMap.put(itemCode, new CustomerStockLevel(0,""));
						}
					}
					hssfRow = hssfSheet.getRow(++itemSectionIndex);
				}
			}catch(NullPointerException npe) {
				//when reached the end of rows but is less than max limit
				break;
			}
			if (itemSectionIndex < hssfSheet.getLastRowNum()) {
				itemSectionIndex++;
				hssfRow = hssfSheet.getRow(itemSectionIndex) == null ? hssfSheet
						.createRow(itemSectionIndex) : hssfSheet
						.getRow(itemSectionIndex);
			} else {
				break;
			}

		}
		customer.setCustomerStockLevelMap(customerStockLevelMap);
	}

	public Object exportReport(String reportName, String action) {
		List objList = null;
		try {
			HSSFWorkbook wb = getWorkbook(reportName);
			HSSFSheet sheet = wb.getSheetAt(0);

			// write(sheet);

			// if needed iterate through the template
			iterateThroughTemplate(sheet);

		} catch (Exception e) {
			logger.debug("POIUtil readOrderingForm() : " + e.toString());
		}
		return objList;
	}

	private void iterateThroughTemplate(HSSFSheet sheet) {

		// Iterate over each row in the sheet
		Iterator rows = sheet.rowIterator();
		while (rows.hasNext()) {

			// Report Date value

			HSSFRow row = (HSSFRow) rows.next();
			System.out.println("Row #" + row.getRowNum());

			HSSFCell valueColumn = row.getCell(2);
			try {
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				valueColumn.setCellValue(dateFormat.format(calendar.getTime()));
			} catch (Exception e) {
			}

			// Iterate over each cell in the row and print out the cell's
			// content
			Iterator cells = row.cellIterator();
			while (cells.hasNext()) {
				HSSFCell cell = (HSSFCell) cells.next();
				System.out.println("Cell #" + cell.getCellNum());
				switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_NUMERIC:
					System.out.println(cell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_STRING:
					System.out.println(cell.getStringCellValue());
					break;
				default:
					System.out.println("unsuported cell type");
					break;
				}
			}

		}
	}

	private void printReport(HSSFSheet sheet) {
		// This is for printing
	}

	private void exportReport(HSSFSheet sheet) {
		// TODO Auto-generated method stub
		// this is where we export to excel
	}

	private HashMap<String, String> rowTitles;

	private String getTemplateFilePath(String reportName) {

		String templateDirectory = SASConstants.templateDirectory;
		String templateFilePath = templateDirectory + reportName
				+ SASConstants.POITemplateExtension;
		return templateFilePath;
	}

	// SUMMARY GENERATION START

	public HSSFWorkbook generateSummary(ServletContext servletContext,
			String subModule, List list) throws IOException {

		String filePath = servletContext
				.getRealPath(SASConstants.REPORT_SUMMARY_TEMPLATE_PATH
						+ subModule + SASConstants.REPORT_TYPE_SUMMARY
						+ SASConstants.REPORT_SUMMARY_TEMPLATE_TYPE);

		FileInputStream fileInputStream = new FileInputStream(filePath);
		POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);

		populateSummary(wb, list, subModule);

		return wb;
	}

	private void populateSummary(HSSFWorkbook wb, List list, String subModule) {
		if (subModule.equals("Supplier")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForSupplier(wb, list, subModule);
		} else if (subModule.equals("SupplierPurchaseOrder")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER_PO,
					SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_COL_START);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER_PO_ORDERS,
					SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START);
			populateSummaryForSupplier(wb, list, subModule);
		} else if (subModule.equals("ReceivingReport")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER_PO,
					SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_COL_START);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER_PO_ORDERS,
					SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START);
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER_RR,
					SASConstants.SUMMARY_TEMPLATE_SUPPLIER_RECEIVING_COL_START);
			populateSummaryForSupplier(wb, list, subModule);
		} else if (subModule.equals("SupplierInvoice")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER_PO,
					SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_COL_START);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER_PO_ORDERS,
					SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START);
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER_RR,
					SASConstants.SUMMARY_TEMPLATE_SUPPLIER_RECEIVING_COL_START);
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER_INV,
					SASConstants.SUMMARY_TEMPLATE_SUPPLIER_INVOICE_COL_START);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_SUPPLIER_INV_ORDERS,
					SASConstants.SUMMARY_TEMPLATE_SUPPLIER_INVOICE_PURCHASE_DETAILS_COL_START);
			populateSummaryForSupplier(wb, list, subModule);
		} else if (subModule.equals("Customer")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForCustomer(wb, list, subModule);
		} else if (subModule.equals("CustomerPurchaseOrder")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER_PO,
					SASConstants.SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_COL_START);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER_PO_ORDERS,
					SASConstants.SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START);
			populateSummaryForCustomer(wb, list, subModule);
		} else if (subModule.equals("DeliveryReceipt")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER_PO,
					SASConstants.SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_COL_START);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER_PO_ORDERS,
					SASConstants.SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START);
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER_RR,
					SASConstants.SUMMARY_TEMPLATE_CUSTOMER_DELIVERY_COL_START);
			populateSummaryForCustomer(wb, list, subModule);
		} else if (subModule.equals("CustomerSalesInvoice")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER_PO,
					SASConstants.SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_COL_START);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER_PO_ORDERS,
					SASConstants.SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START);
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER_RR,
					SASConstants.SUMMARY_TEMPLATE_CUSTOMER_DELIVERY_COL_START);
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER_INV,
					SASConstants.SUMMARY_TEMPLATE_CUSTOMER_INVOICE_COL_START);
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CUSTOMER_INV_ORDERS,
					SASConstants.SUMMARY_TEMPLATE_CUSTOMER_INVOICE_PURCHASE_DETAILS_COL_START);
			populateSummaryForCustomer(wb, list, subModule);
		} else if (subModule.equals("ItemSoldToCustomers")) {
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_ITEMS_SOLD_TO_CUSTOMERS,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForCustomer(wb, list, subModule);
		}else if (subModule.equals("ItemPurchasedFromSupplier")) {
			setSummaryHeaders(
					wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_ITEMS_PURCHASED_FROM_SUPPLIER,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForSupplier(wb, list, subModule);
		}else if (subModule.equals("StatementOfAccount")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_STATEMENT_OF_ACCOUNT,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForCustomer(wb, list, subModule);
		} else if (subModule.equals("PettyCash")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_PETTY_CASH,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForDisbursements(wb, list, subModule);
		} else if (subModule.equals("CashPayment")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CASH_PAYMENT,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForDisbursements(wb, list, subModule);
		} else if (subModule.equals("CheckPayment")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CHECK_PAYMENT,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForDisbursements(wb, list, subModule);
		} else if (subModule.equals("InvoiceCheckVoucher")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CHECK_PAYMENT,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForDisbursements(wb, list, subModule);
		} else if (subModule.equals("CashReceipts")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_CASH_RECEIPTS,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForReceipts(wb, list, subModule);
		} else if (subModule.equals("ORSales")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_OR_SALES,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForReceipts(wb, list, subModule);
		} else if (subModule.equals("OROthers")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_OR_OTHERS,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForReceipts(wb, list, subModule);
		} else if (subModule.equals("RawMaterials")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_RAW_MATERIALS,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForInventory(wb, list, subModule);

		} else if (subModule.equals("TradedItems")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_TRADED_ITEMS,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForInventory(wb, list, subModule);

		}else if (subModule.equals("Utensils")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_UTENSILS,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForInventory(wb, list, subModule);

		}else if (subModule.equals("OfficeSupplies")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_OFFICE_SUPPLIES,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForInventory(wb, list, subModule);

		} else if (subModule.equals("FinishedGoods")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_FINISHED_GOODS,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForInventory(wb, list, subModule);
		}else if (subModule.equals("FinishedProductTransferSlip")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_FPTS,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_FPTS,
					SASConstants.SUMMARY_TEMPLATE_FPTS_ORDER_COL_START);
			populateSummaryForInventory(wb, list, subModule);

		}else if (subModule.equals("OrderRequisition")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_RF,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_RF,
					SASConstants.SUMMARY_TEMPLATE_ORF_ORDER_COL_START);
			populateSummaryForInventory(wb, list, subModule);

		}else if (subModule.equals("ReturnSlip")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_RETURN_SLIP,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_RETURN_SLIP,
					SASConstants.SUMMARY_TEMPLATE_RS_ORDER_COL_START);
			populateSummaryForInventory(wb, list, subModule);

		}else if (subModule.equals("AccountEntryProfile")) {
			setSummaryHeaders(wb,
					SASConstants.SUMMARY_TEMPLATE_HEADER_ACCOUNT_ENTRY_SUMMARY,
					SASConstants.SUMMARY_TEMPLATE_FIRST_COL);
			populateSummaryForSupplier(wb, list, subModule);
		} 

	}

	private void setSummaryHeaders(HSSFWorkbook wb, String headerLabel,
			int columnStart) {
		HSSFSheet sheet = wb.getSheetAt(0);

		HSSFRow rowCompanyHeader = sheet.getRow(0);
		HSSFCell cellCompanyHeader = rowCompanyHeader.getCell(columnStart);
		cellCompanyHeader
				.setCellValue(SASConstants.SUMMARY_TEMPLATE_COMPANY_HEADER);

		HSSFRow rowHeader = sheet.getRow(1);
		HSSFCell cellHeader = rowHeader.getCell(columnStart);
		cellHeader.setCellValue(headerLabel);

		DateFormatHelper dfh = new DateFormatHelper();
		HSSFRow rowDate = sheet.getRow(2);
		HSSFCell cellDate = rowDate.getCell(columnStart);
		if (maxDate != null && !maxDate.equalsIgnoreCase("") && minDate != null
				&& !minDate.equalsIgnoreCase(""))
			cellDate.setCellValue(SASConstants.FROM_PERIOD + minDate
					+ SASConstants.TO + maxDate);
		else
			cellDate.setCellValue(dfh.getDateToday());
	}

	private void populateSummaryForCustomer(HSSFWorkbook wb, List list,
			String subModule) {
		HSSFSheet sheet = wb.getSheetAt(0);
		int currentRow = 0;
		try {
			lastItemStyle = wb.createCellStyle();
			itemSoldTotalAmountStyle = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setBoldweight((short) 1);
			itemSoldTotalAmountStyle.setFont(font);
			itemSoldTotalAmountStyle.setBorderBottom(CellStyle.BORDER_THICK);
			itemSoldTotalAmountStyle.setBorderTop(CellStyle.BORDER_THICK);
			lastItemStyle.cloneStyleFrom(sheet.getRow(5).getCell(0)
					.getCellStyle());
			lastItemStyle.setBorderBottom(HSSFBorderFormatting.BORDER_DASHED);

		} catch (Exception e) {
		}
		String previousCustomer = "";
		double totalAmount = 0;
		for (int counter = 0; counter < list.size(); counter++) {
			int maxRow = 0;
			HSSFRow row = sheet.getRow(currentRow
					+ SASConstants.SUMMARY_TEMPLATE_ROW_START) == null ? sheet
					.createRow(currentRow
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START) : sheet
					.getRow(currentRow
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START);

			if (subModule.equals("Customer")) {
				Customer customer = (Customer) list.get(counter);
				putCustomerValues(sheet, row, customer);
				maxRow += 1;
				putItemSeparator(
						sheet,
						row.getRowNum() + maxRow,
						SASConstants.SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_COL_START - 1);
			} else if (subModule.equals("CustomerPurchaseOrder")) {
				CustomerPurchaseOrder customerPurchaseOrder = (CustomerPurchaseOrder) list
						.get(counter);
				putCustomerValues(sheet, row,
						customerPurchaseOrder.getCustomer());
				putCustomerPurchaseOrderValues(sheet, row,
						customerPurchaseOrder);
				maxRow += getMaxRow(null == customerPurchaseOrder
						.getPurchaseOrderDetails() ? 0 : customerPurchaseOrder
						.getPurchaseOrderDetails().size());
				putItemSeparator(
						sheet,
						row.getRowNum() + maxRow,
						SASConstants.SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START + 5);

			} else if (subModule.equals("DeliveryReceipt")) {
				DeliveryReceipt deliveryReceipt = (DeliveryReceipt) list
						.get(counter);
				putCustomerValues(sheet, row, deliveryReceipt
						.getCustomerPurchaseOrder().getCustomer());
				putCustomerPurchaseOrderValues(sheet, row,
						deliveryReceipt.getCustomerPurchaseOrder());
				putDeliveryReceiptValues(sheet, row, deliveryReceipt);
				maxRow += getMaxRow(
						null == deliveryReceipt.getCustomerPurchaseOrder()
								.getPurchaseOrderDetails() ? 0
								: deliveryReceipt.getCustomerPurchaseOrder()
										.getPurchaseOrderDetails().size(),
						null == deliveryReceipt.getPurchaseOrderDetails() ? 0
								: deliveryReceipt.getPurchaseOrderDetails()
										.size());
				putItemSeparator(
						sheet,
						row.getRowNum() + maxRow,
						SASConstants.SUMMARY_TEMPLATE_CUSTOMER_INVOICE_COL_START - 2);

			} else if (subModule.equals("CustomerSalesInvoice")) {
				CustomerSalesInvoice customerSalesInvoice = (CustomerSalesInvoice) list
						.get(counter);
				putCustomerValues(sheet, row, customerSalesInvoice
						.getDeliveryReceipt().getCustomerPurchaseOrder()
						.getCustomer());
				putCustomerPurchaseOrderValues(sheet, row, customerSalesInvoice
						.getDeliveryReceipt().getCustomerPurchaseOrder());
				putDeliveryReceiptValues(sheet, row,
						customerSalesInvoice.getDeliveryReceipt());
				putCustomerSalesInvoiceValues(sheet, row, customerSalesInvoice);
				maxRow += getMaxRow(
						null == customerSalesInvoice.getDeliveryReceipt()
								.getCustomerPurchaseOrder()
								.getPurchaseOrderDetails() ? 0
								: customerSalesInvoice.getDeliveryReceipt()
										.getCustomerPurchaseOrder()
										.getPurchaseOrderDetails().size(),
						null == customerSalesInvoice.getDeliveryReceipt()
								.getPurchaseOrderDetails() ? 0
								: customerSalesInvoice.getDeliveryReceipt()
										.getPurchaseOrderDetails().size(),
						null == customerSalesInvoice.getPurchaseOrderDetails() ? 0
								: customerSalesInvoice
										.getPurchaseOrderDetails().size());
				putItemSeparator(
						sheet,
						row.getRowNum() + maxRow,
						SASConstants.SUMMARY_TEMPLATE_CUSTOMER_INVOICE_PURCHASE_DETAILS_COL_START + 5);

			} else if (subModule.equals("ItemSoldToCustomers")) {
				CustomerSalesInvoice customerInvoice = (CustomerSalesInvoice) list
						.get(counter);
				putItemsSoldToCustomerValues(sheet, row, customerInvoice);
				maxRow += getMaxRow(null == customerInvoice
						.getPurchaseOrderDetails() ? 0 : customerInvoice
						.getPurchaseOrderDetails().size() + 2);

			} else if (subModule.equals("StatementOfAccount")) {
				CustomerSalesInvoice customerInvoice = (CustomerSalesInvoice) list
						.get(counter);
				if (previousCustomer.equalsIgnoreCase(customerInvoice
						.getSoldTo())) {
					putStatementOfAccountValues(sheet, row, customerInvoice);
					totalAmount = totalAmount
							+ customerInvoice.getTotalAmount();
					previousCustomer = customerInvoice.getSoldTo();
				} else if (previousCustomer.equalsIgnoreCase("")) {
					putStatementOfAccountValues(sheet, row, customerInvoice);
					totalAmount = customerInvoice.getTotalAmount();
					previousCustomer = customerInvoice.getSoldTo();
				} else {
					// write total row
					putStatementOfAccountTotal(sheet, row, totalAmount);
					totalAmount = 0;
					previousCustomer = "";
					counter -= 1;
				}
				if (counter + 1 == list.size()) {
					row = sheet.getRow(currentRow + 1
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START) == null ? sheet
							.createRow(currentRow + 1
									+ SASConstants.SUMMARY_TEMPLATE_ROW_START)
							: sheet.getRow(currentRow + 1
									+ SASConstants.SUMMARY_TEMPLATE_ROW_START);
					putStatementOfAccountTotal(sheet, row, totalAmount);
					totalAmount = 0;
					previousCustomer = "";
				}
				maxRow += 1;
			}
			currentRow += maxRow;
		}
	}

	private void putStatementOfAccountTotal(HSSFSheet sheet, HSSFRow row,
			double totalAmount) {
		// TODO Auto-generated method stub
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL + 3;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue("TOTAL AMOUNT: ");
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(itemSoldTotalAmountStyle);
		cell.setCellValue(parseNullDouble(totalAmount));
	}

	private void putStatementOfAccountValues(HSSFSheet sheet, HSSFRow row,
			CustomerSalesInvoice invoice) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(invoice.getDeliveryReceipt()
				.getCustomerPurchaseOrder().getCustomer().getCustomerNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(invoice.getSoldTo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(invoice
				.getCustomerInvoiceDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(invoice.getCustomerInvoiceNo()));
		//START: add DR NO
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(invoice.getDeliveryReceipt().getDeliveryReceiptNo()));
		//END
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(invoice.getTotalAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
	}

	private void putItemsSoldToCustomerValues(HSSFSheet sheet, HSSFRow row,
			CustomerSalesInvoice invoice) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;

		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(invoice.getDeliveryReceipt()
				.getCustomerPurchaseOrder().getCustomer().getCustomerNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(invoice.getDeliveryReceipt()
				.getCustomerPurchaseOrder().getCustomer().getCustomerName()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		Map originalMap = convertPurchaseOrderDetailsToMap(invoice
				.getPurchaseOrderDetails());
		Map sortedMap = new TreeMap(originalMap);

		List mapKeys = new ArrayList(sortedMap.keySet());
		List mapValues = new ArrayList(sortedMap.values());
		TreeSet sortedSet = new TreeSet(mapKeys);
		Object[] sortedArray = sortedSet.toArray();
		int size = sortedArray.length;

		double totalAmount = 0;
		int lastCol = 0;
		int detailsColumnStart = 2;
		for (int i = 0; i < size; i++) {
			col = detailsColumnStart;
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			PurchaseOrderDetails poDetails = (PurchaseOrderDetails) originalMap
					.get(sortedArray[i]);
			cell.setCellValue(parseNullString(poDetails.getItemCode()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(poDetails.getQuantity()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(poDetails.getUnitOfMeasurement()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(poDetails.getAmount()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(poDetails.getDescription()));
			row = sheet.getRow(row.getRowNum() + 1) == null ? sheet
					.createRow(row.getRowNum() + 1) : sheet.getRow(row
					.getRowNum() + 1);
			totalAmount += poDetails.getAmount();
		}
		cell = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue("TOTAL AMOUNT: ");
		cell = row.getCell(5, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(itemSoldTotalAmountStyle);
		cell.setCellValue(parseNullDouble(totalAmount));
		row = sheet.getRow(row.getRowNum() + 1) == null ? sheet.createRow(row
				.getRowNum() + 1) : sheet.getRow(row.getRowNum() + 1);
		putItemSeparator(sheet, row.getRowNum(), 6);
	}
	
	private void putItemPurchasedFromSupplierValues(HSSFSheet sheet, HSSFRow row,
			ReceivingReport rr) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;

		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rr.getSupplierPurchaseOrder().getSupplier().getSupplierId()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rr.getSupplierPurchaseOrder().getSupplier().getSupplierName()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		Map originalMap = convertPurchaseOrderDetailsToMap(rr
				.getPurchaseOrderDetails());
		Map sortedMap = new TreeMap(originalMap);

		List mapKeys = new ArrayList(sortedMap.keySet());
		List mapValues = new ArrayList(sortedMap.values());
		TreeSet sortedSet = new TreeSet(mapKeys);
		Object[] sortedArray = sortedSet.toArray();
		int size = sortedArray.length;

		double totalAmount = 0;
		int lastCol = 0;
		int detailsColumnStart = 2;
		for (int i = 0; i < size; i++) {
			col = detailsColumnStart;
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			PurchaseOrderDetails poDetails = (PurchaseOrderDetails) originalMap
					.get(sortedArray[i]);
			cell.setCellValue(parseNullString(poDetails.getItemCode()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(poDetails.getQuantity()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(poDetails.getUnitOfMeasurement()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(poDetails.getAmount()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(poDetails.getDescription()));
			row = sheet.getRow(row.getRowNum() + 1) == null ? sheet
					.createRow(row.getRowNum() + 1) : sheet.getRow(row
					.getRowNum() + 1);
			totalAmount += poDetails.getAmount();
		}
		cell = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue("TOTAL AMOUNT: ");
		cell = row.getCell(5, Row.CREATE_NULL_AS_BLANK);
		cell.setCellStyle(itemSoldTotalAmountStyle);
		cell.setCellValue(parseNullDouble(totalAmount));
		row = sheet.getRow(row.getRowNum() + 1) == null ? sheet.createRow(row
				.getRowNum() + 1) : sheet.getRow(row.getRowNum() + 1);
		putItemSeparator(sheet, row.getRowNum(), 6);
	}

	private void putCustomerValues(HSSFSheet sheet, HSSFRow row,
			Customer customer) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;

		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getCustomerNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getCustomerName()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getContactName()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getContactTitle()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getCustomerType()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getBillingAddress()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getPhoneNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getFaxNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getMobileNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getEmailAddress()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customer.getWebsite()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

	}

	private void putCustomerPurchaseOrderValues(HSSFSheet sheet, HSSFRow row,
			CustomerPurchaseOrder customerPurchaseOrder) {

		int col = SASConstants.SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_COL_START;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(customerPurchaseOrder.getCustomer()
				.getCustomerNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customerPurchaseOrder
				.getCustomerPurchaseOrderId()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(dfh.parseDateToString(customerPurchaseOrder
				.getPurchaseOrderDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(customerPurchaseOrder
				.getDateOfDelivery()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(customerPurchaseOrder
				.getPaymentDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(customerPurchaseOrder
				.getPaymentTerm()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customerPurchaseOrder.getOrderedBy()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(customerPurchaseOrder
				.getTotalAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		putPurchaseOrderDetails(
				sheet,
				row,
				customerPurchaseOrder.getPurchaseOrderDetails(),
				SASConstants.SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START);
	}

	private void putDeliveryReceiptValues(HSSFSheet sheet, HSSFRow row,
			DeliveryReceipt deliveryReceipt) {
		PurchaseOrderDetailHelper podetailHelper = new PurchaseOrderDetailHelper(actionSession);
		podetailHelper.generatePODetailsListFromSet(deliveryReceipt.getPurchaseOrderDetails());
		podetailHelper.generateCommaDelimitedValues();
		int col = SASConstants.SUMMARY_TEMPLATE_CUSTOMER_DELIVERY_COL_START;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(deliveryReceipt
				.getCustomerPurchaseOrder().getCustomerPurchaseOrderId()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		
		cell.setCellValue(parseNullString(deliveryReceipt
				.getDeliveryReceiptNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		
		cell.setCellValue(dfh.parseDateToString(deliveryReceipt
				.getDeliveryReceiptDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		
		String tempRS = "";
		String delimeter="";
		for (ReturnSlip rs: deliveryReceipt.getReturnSlipList()){
			cell.setCellValue(parseNullString(rs.getReturnSlipNo()));
			tempRS = tempRS + delimeter + rs.getReturnSlipNo();
			delimeter=",";
		}
		cell.setCellValue(parseNullString(tempRS));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		
		cell.setCellValue(dfh.parseDateToString(deliveryReceipt
				.getShippingDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		
		cell.setCellValue(dfh.parseDateToString(deliveryReceipt.getDueDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(deliveryReceipt.getShippingMethod()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//START PHASE 3 AZ
				cell.setCellValue(podetailHelper.getTotalNonVattableAmount());
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(podetailHelper.getTotalVattableAmount());
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(podetailHelper.getTotalVatAmount());
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				//END PHASE 3 AZ
		cell.setCellValue(parseNullString(deliveryReceipt.getRemarks()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		putPurchaseOrderDetails(
				sheet,
				row,
				deliveryReceipt.getPurchaseOrderDetails(),
				SASConstants.SUMMARY_TEMPLATE_CUSTOMER_DELIVERY_PURCHASE_DETAILS_COL_START);
	}

	private void putCustomerSalesInvoiceValues(HSSFSheet sheet, HSSFRow row,
			CustomerSalesInvoice customerSalesInvoice) {
		podetailHelper.generatePODetailsListFromSet(customerSalesInvoice.getPurchaseOrderDetails());
		podetailHelper.generateCommaDelimitedValues();
		int col = SASConstants.SUMMARY_TEMPLATE_CUSTOMER_INVOICE_COL_START;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(customerSalesInvoice
				.getDeliveryReceipt().getDeliveryReceiptNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customerSalesInvoice
				.getCustomerInvoiceNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(customerSalesInvoice
				.getCustomerInvoiceDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customerSalesInvoice.getSoldTo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customerSalesInvoice.getAddress()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customerSalesInvoice.getBusStyle()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(customerSalesInvoice.getTin()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(customerSalesInvoice.getTotalSales()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//START PHASE 3 AZ
		cell.setCellValue(podetailHelper.getTotalNonVattableAmount());
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(podetailHelper.getTotalVattableAmount());
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(podetailHelper.getTotalVatAmount());
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//END PHASE 3 AZ

		putPurchaseOrderDetails(
				sheet,
				row,
				customerSalesInvoice.getPurchaseOrderDetails(),
				SASConstants.SUMMARY_TEMPLATE_CUSTOMER_INVOICE_PURCHASE_DETAILS_COL_START);

	}

	private void populateSummaryForSupplier(HSSFWorkbook wb, List list,
			String subModule) {
		HSSFSheet sheet = wb.getSheetAt(0);
		int currentRow = 0;
		lastItemStyle = wb.createCellStyle();
		itemSoldTotalAmountStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBoldweight((short) 1);
		itemSoldTotalAmountStyle.setFont(font);
		itemSoldTotalAmountStyle.setBorderBottom(CellStyle.BORDER_THICK);
		itemSoldTotalAmountStyle.setBorderTop(CellStyle.BORDER_THICK);
		
		try{
			lastItemStyle.cloneStyleFrom(sheet.getRow(5).getCell(0).getCellStyle());
		}catch(NullPointerException npe){
			//dont clone anything
		}
		lastItemStyle.setBorderBottom(HSSFBorderFormatting.BORDER_DASHED);
		for (int counter = 0; counter < list.size(); counter++) {
			int maxRow = 0;
			HSSFRow row = sheet.getRow(currentRow
					+ SASConstants.SUMMARY_TEMPLATE_ROW_START) == null ? sheet
					.createRow(currentRow
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START) : sheet
					.getRow(currentRow
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START);

			if (subModule.equals("Supplier")) {
				Supplier supplier = (Supplier) list.get(counter);
				putSupplierValues(sheet, row, supplier);
				maxRow += 1;
				putItemSeparator(
						sheet,
						row.getRowNum() + maxRow,
						SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_COL_START - 1);
			}else if (subModule.equals("AccountEntryProfile")) {
				AccountEntryProfile aep = (AccountEntryProfile) list.get(counter);
				putAccountEntryProfileValues(sheet, row, aep);
				maxRow += 1;
				putItemSeparator(
						sheet,
						row.getRowNum() + maxRow,
						SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_COL_START - 1);
			}else if (subModule.equals("SupplierPurchaseOrder")) {
				SupplierPurchaseOrder supplierPurchaseOrder = (SupplierPurchaseOrder) list
						.get(counter);
				putSupplierValues(sheet, row,
						supplierPurchaseOrder.getSupplier());
				putSupplierPurchaseOrderValues(sheet, row,
						supplierPurchaseOrder);
				maxRow += getMaxRow(null == supplierPurchaseOrder
						.getPurchaseOrderDetails() ? 0 : supplierPurchaseOrder
						.getPurchaseOrderDetails().size());
				putItemSeparator(
						sheet,
						row.getRowNum() + maxRow,
						SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START + 5);

			} else if (subModule.equals("ReceivingReport")) {
				ReceivingReport receivingReport = (ReceivingReport) list
						.get(counter);
				putSupplierValues(sheet, row, receivingReport
						.getSupplierPurchaseOrder().getSupplier());
				putSupplierPurchaseOrderValues(sheet, row,
						receivingReport.getSupplierPurchaseOrder());
				putReceivingReportValues(sheet, row, receivingReport);
				maxRow += getMaxRow(
						null == receivingReport.getSupplierPurchaseOrder()
								.getPurchaseOrderDetails() ? 0
								: receivingReport.getSupplierPurchaseOrder()
										.getPurchaseOrderDetails().size(),
						null == receivingReport.getPurchaseOrderDetails() ? 0
								: receivingReport.getPurchaseOrderDetails()
										.size());
				putItemSeparator(
						sheet,
						row.getRowNum() + maxRow,
						SASConstants.SUMMARY_TEMPLATE_SUPPLIER_INVOICE_COL_START - 1);

			} else if (subModule.equals("SupplierInvoice")) {
				SupplierInvoice supplierInvoice = (SupplierInvoice) list
						.get(counter);
				putSupplierValues(sheet, row, supplierInvoice
						.getReceivingReport().getSupplierPurchaseOrder()
						.getSupplier());
				putSupplierPurchaseOrderValues(sheet, row, supplierInvoice
						.getReceivingReport().getSupplierPurchaseOrder());
				putReceivingReportValues(sheet, row,
						supplierInvoice.getReceivingReport());
				putSupplierSalesInvoiceValues(sheet, row, supplierInvoice);
				maxRow += getMaxRow(null == supplierInvoice
						.getReceivingReport().getSupplierPurchaseOrder()
						.getPurchaseOrderDetails() ? 0 : supplierInvoice
						.getReceivingReport().getSupplierPurchaseOrder()
						.getPurchaseOrderDetails().size(),
						null == supplierInvoice.getReceivingReport()
								.getPurchaseOrderDetails() ? 0
								: supplierInvoice.getReceivingReport()
										.getPurchaseOrderDetails().size(),
						null == supplierInvoice.getPurchaseOrderDetails() ? 0
								: supplierInvoice.getPurchaseOrderDetails()
										.size());
				putItemSeparator(
						sheet,
						row.getRowNum() + maxRow,
						SASConstants.SUMMARY_TEMPLATE_SUPPLIER_INVOICE_PURCHASE_DETAILS_COL_START + 5);

			}else if (subModule.equals("ItemPurchasedFromSupplier")) {
				ReceivingReport rr = (ReceivingReport) list
						.get(counter);
				putItemPurchasedFromSupplierValues(sheet, row, rr);
				maxRow += getMaxRow(null == rr
						.getPurchaseOrderDetails() ? 0 : rr
						.getPurchaseOrderDetails().size() + 2);

			}
			currentRow += maxRow;

		}
	}

	private void populateSummaryForInventory(HSSFWorkbook wb, List list,
			String subModule) {

		HSSFSheet sheet = wb.getSheetAt(0);
		int currentRow = 0;
		lastItemStyle = wb.createCellStyle();
		lastItemStyle.cloneStyleFrom(sheet.getRow(5).getCell(0).getCellStyle());
		lastItemStyle.setBorderBottom(HSSFBorderFormatting.BORDER_DASHED);
		try {
			for (int counter = 0; counter < list.size(); counter++) {
				int maxRow = 0;
				HSSFRow row = sheet.getRow(currentRow
						+ SASConstants.SUMMARY_TEMPLATE_ROW_START) == null ? sheet
						.createRow(currentRow
								+ SASConstants.SUMMARY_TEMPLATE_ROW_START)
						: sheet.getRow(currentRow
								+ SASConstants.SUMMARY_TEMPLATE_ROW_START);

				if (subModule.equals("RawMaterials")) {
					RawMaterial rawMat = (RawMaterial) list.get(counter);
					putRawMaterialValues(sheet, row, rawMat);
					maxRow += 1;
				} else if (subModule.equals("TradedItems")) {
					TradedItem tradedItems = (TradedItem) list.get(counter);
					putTradedItemValues(sheet, row, tradedItems);
					maxRow += 1;
				} else if (subModule.equals("Utensils")) {
					Utensils utensils = (Utensils) list.get(counter);
					putUtensilsValues(sheet, row, utensils);
					maxRow += 1;
				} else if (subModule.equals("OfficeSupplies")) {
					OfficeSupplies officeSupplies= (OfficeSupplies) list.get(counter);
					putOfficeSuppliesValues(sheet, row, officeSupplies);
					maxRow += 1;
				} else if (subModule.equals("FinishedGoods")) {
					FinishedGood finishedGood = (FinishedGood) list
							.get(counter);
					putFinishedProductValues(sheet, row, finishedGood);

					maxRow += getMaxRow(null == finishedGood.getIngredients() ? 0
							: finishedGood.getIngredients().size());
					putItemSeparator(
							sheet,
							row.getRowNum() + maxRow,
							SASConstants.SUMMARY_TEMPLATE_HEADER_FINISHED_GOODS_INGREDIENTS_COL_START + 5);
				}else if (subModule.equals("FinishedProductTransferSlip")) {
					FPTS fpts = (FPTS) list.get(counter);
					putFPTSValues(sheet, row, fpts);
					maxRow += 1;
				} else if (subModule.equals("OrderRequisition")) {
					RequisitionForm rf = (RequisitionForm) list.get(counter);
					putOrderRequisitionValues(sheet, row, rf);
					maxRow += 1;
				} else if (subModule.equals("ReturnSlip")) {
					ReturnSlip rs = (ReturnSlip) list.get(counter);
					putReturnSlipValues(sheet, row, rs);
					maxRow += 1;
				} 
				currentRow += maxRow;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void putReturnSlipValues(HSSFSheet sheet, HSSFRow row, ReturnSlip rs) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(rs.getReturnSlipNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(rs.getReturnDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rs.getReturnSlipReferenceOrderNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			
		cell.setCellValue(parseNullString(rs.getReturnSlipFrom()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rs.getReturnSlipTo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		
	
		cell.setCellValue(parseNullString(rs.getApprovedBy()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rs.getPreparedBy()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		

		putPurchaseOrderDetails(
				sheet,
				row,
				rs.getPurchaseOrderDetails(),
				SASConstants.SUMMARY_TEMPLATE_RETURN_SLIP_PURCHASE_DETAILS_COL_START);
		
	}

	private void putFPTSValues(HSSFSheet sheet, HSSFRow row, FPTS fpts) {
		
			int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
			HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

			cell.setCellValue(parseNullString(fpts.getFptsNo()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(fpts.getFptsFrom()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(fpts.getFptsTo()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			
			cell.setCellValue(dfh.parseDateToString(fpts.getTransactionDate()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(fpts.getTransferredBy()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(dfh.parseDateToString(fpts.getReceivedDate()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(fpts.getReceivedBy()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(dfh.parseDateToString(fpts.getApprovedDate()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(fpts.getRequisitionForm().getRequisitionNo()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		
			String tempRS = "";
			String delimeter="";
			for (ReturnSlip rs: fpts.getReturnSlipList()){
				cell.setCellValue(parseNullString(rs.getReturnSlipNo()));
				tempRS = tempRS + delimeter + rs.getReturnSlipNo();
				delimeter=",";
			}
			cell.setCellValue(parseNullString(tempRS));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			
			
			putPurchaseOrderDetails(
					sheet,
					row,
					fpts.getPurchaseOrderDetailsTransferred(),
					SASConstants.SUMMARY_TEMPLATE_FPTS_PURCHASE_DETAILS_TRANSFERRED_COL_START);
			
			putPurchaseOrderDetails(
					sheet,
					row,
					fpts.getPurchaseOrderDetailsReceived(),
					SASConstants.SUMMARY_TEMPLATE_FPTS_PURCHASE_DETAILS_RECEIVED_COL_START);
		}
		
	private void putOrderRequisitionValues(HSSFSheet sheet, HSSFRow row, RequisitionForm rf) {
		
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(rf.getRequisitionNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(rf.getRequisitionDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rf.getRequisitionTo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rf.getRequisitionBy()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rf.getRequisitionApprovedBy()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rf.getRequistionReceivedBy()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		
		String tempRS = "";
		String delimeter="";
		for (ReturnSlip rs: rf.getReturnSlipList()){
			cell.setCellValue(parseNullString(rs.getReturnSlipNo()));
			tempRS = tempRS + delimeter + rs.getReturnSlipNo();
			delimeter=",";
		}
		cell.setCellValue(parseNullString(tempRS));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		

		putPurchaseOrderDetails(
				sheet,
				row,
				rf.getPurchaseOrderDetailsOrdered(),
				SASConstants.SUMMARY_TEMPLATE_RF_PURCHASE_DETAILS_ORDERED_COL_START);
		/*putPurchaseOrderDetails(
				sheet,
				row,
				rf.getPurchaseOrderDetailsReceived(),
				SASConstants.SUMMARY_TEMPLATE_RF_PURCHASE_DETAILS_RECEIVED_COL_START);*/
	}

	private void putIngredients(HSSFSheet sheet, HSSFRow row,
			Set<Ingredient> ingredients, int col) {
		Iterator iterator = ingredients.iterator();

		while (iterator.hasNext()) {
			int ingredientsColumnStart = col;
			HSSFCell cell = row.getCell(ingredientsColumnStart++,
					Row.CREATE_NULL_AS_BLANK);
			Ingredient ingredient = (Ingredient) iterator.next();
			cell.setCellValue(parseNullString(ingredient.getProductCode()));
			cell = row.getCell(ingredientsColumnStart++,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(ingredient.getQuantity()));
			cell = row.getCell(ingredientsColumnStart++,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(ingredient.getDescription()));
			cell = row.getCell(ingredientsColumnStart++,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(ingredient.getUnitOfMeasurement()));
			cell = row.getCell(ingredientsColumnStart++,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue((parseNullDouble(ingredient
					.getStandardPricePerUnit())));
			cell = row.getCell(ingredientsColumnStart++,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(ingredient
					.getActualPricePerUnit()));
			cell = row.getCell(ingredientsColumnStart++,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(ingredient
					.getTransferPricePerUnit()));
			cell = row.getCell(ingredientsColumnStart++,
					Row.CREATE_NULL_AS_BLANK);
			row = sheet.getRow(row.getRowNum() + 1) == null ? sheet
					.createRow(row.getRowNum() + 1) : sheet.getRow(row
					.getRowNum() + 1);
		}
	}

	private void putFinishedProductValues(HSSFSheet sheet, HSSFRow row,
			FinishedGood finishedGood) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(finishedGood.getProductCode()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(finishedGood.getDescription()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(finishedGood.getUnitOfMeasurement()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(finishedGood.getYields()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(finishedGood.getMarkUp()));
		// TODO: remove this after prices have been inserted

		if (finishedGood.getItemPricing() == null) {
			col += 6;
		} else {

			// Additional price type
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(finishedGood.getItemPricing()
					.getCompanyOwnedStandardPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(finishedGood.getItemPricing()
					.getCompanyOwnedActualPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(finishedGood.getItemPricing()
					.getCompanyOwnedTransferPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

			cell.setCellValue(parseNullDouble(finishedGood.getItemPricing()
					.getFranchiseStandardPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(finishedGood.getItemPricing()
					.getFranchiseActualPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(finishedGood.getItemPricing()
					.getFranchiseTransferPricePerUnit()));
			// TODO: REVISE PRICE DISPLAY TO GET IT FROM ITEM PRICING
			/*
			 * cell.setCellValue(parseNullDouble(finishedGood.
			 * getStandardPricePerUnit ())); cell =
			 * row.getCell(col++,Row.CREATE_NULL_AS_BLANK); cell.setCellValue
			 * (parseNullDouble(finishedGood.getActualPricePerUnit())); cell =
			 * row.getCell(col++,Row.CREATE_NULL_AS_BLANK);
			 */
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(finishedGood
					.getStandardTotalCost()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(finishedGood.getActualTotalCost()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(finishedGood
					.getTransferTotalCost()));
		}

		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(finishedGood.getQuantityIn()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(finishedGood.getQuantityOut()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(finishedGood.getWarehouse().getQuantityPerRecord()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(finishedGood.getWarehouse().getQuantityPerPhysicalCount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		putIngredients(
				sheet,
				row,
				finishedGood.getIngredients(),
				SASConstants.SUMMARY_TEMPLATE_HEADER_FINISHED_GOODS_INGREDIENTS_COL_START);
	}

	private void putRawMaterialValues(HSSFSheet sheet, HSSFRow row,
			RawMaterial rawMat) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;

		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(rawMat.getItemCode()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rawMat.getDescription()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(rawMat.getUnitOfMeasurement()));

		// TODO: remove this after prices have been inserted

		if (rawMat.getItemPricing() == null) {
			col += 6;
		} else {

			// Additional price type
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(rawMat.getItemPricing()
					.getCompanyOwnedStandardPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(rawMat.getItemPricing()
					.getCompanyOwnedActualPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(rawMat.getItemPricing()
					.getCompanyOwnedTransferPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

			cell.setCellValue(parseNullDouble(rawMat.getItemPricing()
					.getFranchiseStandardPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(rawMat.getItemPricing()
					.getFranchiseActualPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(rawMat.getItemPricing()
					.getFranchiseTransferPricePerUnit()));

		}

		// TODO: REVISE PRICE DISPLAY TO GET IT FROM ITEM PRICING
		/*
		 * cell.setCellValue(parseNullDouble(rawMat.getStandardPricePerUnit()));
		 * cell = row.getCell(col++,Row.CREATE_NULL_AS_BLANK);
		 * cell.setCellValue(parseNullDouble(rawMat.getActualPricePerUnit()));
		 * cell = row.getCell(col++,Row.CREATE_NULL_AS_BLANK);
		 */
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(rawMat.getQuantityIn()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(rawMat.getQuantityOut()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(rawMat.getWarehouse().getQuantityPerRecord()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(rawMat.getWarehouse().getQuantityPerPhysicalCount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

	}

	private void putTradedItemValues(HSSFSheet sheet, HSSFRow row,
			TradedItem tradedItems) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;

		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(tradedItems.getItemCode()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(tradedItems.getDescription()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(tradedItems.getUnitOfMeasurement()));

		// TODO: remove this after prices have been inserted

		if (tradedItems.getItemPricing() == null) {
			col += 6;
		} else {

			// Additional price type
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(tradedItems.getItemPricing()
					.getCompanyOwnedStandardPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(tradedItems.getItemPricing()
					.getCompanyOwnedActualPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(tradedItems.getItemPricing()
					.getCompanyOwnedTransferPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

			cell.setCellValue(parseNullDouble(tradedItems.getItemPricing()
					.getFranchiseStandardPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(tradedItems.getItemPricing()
					.getFranchiseActualPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(tradedItems.getItemPricing()
					.getFranchiseTransferPricePerUnit()));

		}

		// TODO: REVISE PRICE DISPLAY TO GET IT FROM ITEM PRICING
		/*
		 * cell.setCellValue(parseNullDouble(tradedItems.getStandardPricePerUnit(
		 * ))); cell = row.getCell(col++,Row.CREATE_NULL_AS_BLANK);
		 * cell.setCellValue
		 * (parseNullDouble(tradedItems.getActualPricePerUnit())); cell =
		 * row.getCell(col++,Row.CREATE_NULL_AS_BLANK);
		 */
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(tradedItems.getQuantityIn()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(tradedItems.getQuantityOut()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(tradedItems.getWarehouse().getQuantityPerRecord()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(tradedItems.getWarehouse()
				.getQuantityPerPhysicalCount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

	}
	
	private void putUtensilsValues(HSSFSheet sheet, HSSFRow row,
			Utensils utensils) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(utensils.getItemCode()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(utensils.getDescription()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(utensils.getUnitOfMeasurement()));

		// TODO: remove this after prices have been inserted
		if (utensils.getItemPricing() == null) {
			col += 6;
		} else {
			// Additional price type
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(utensils.getItemPricing()
					.getCompanyOwnedStandardPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(utensils.getItemPricing()
					.getCompanyOwnedActualPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(utensils.getItemPricing()
					.getCompanyOwnedTransferPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(utensils.getItemPricing()
					.getFranchiseStandardPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(utensils.getItemPricing()
					.getFranchiseActualPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(utensils.getItemPricing()
					.getFranchiseTransferPricePerUnit()));
		}
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(utensils.getQuantityIn()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(utensils.getQuantityOut()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(utensils.getWarehouse().getQuantityPerRecord()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(utensils.getWarehouse()
				.getQuantityPerPhysicalCount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
	}
	
	private void putOfficeSuppliesValues(HSSFSheet sheet, HSSFRow row,
			OfficeSupplies officeSupplies) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;

		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(officeSupplies.getItemCode()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(officeSupplies.getDescription()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(officeSupplies.getUnitOfMeasurement()));

		// TODO: remove this after prices have been inserted
		if (officeSupplies.getItemPricing() == null) {
			col += 6;
		} else {
			// Additional price type
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(officeSupplies.getItemPricing()
					.getCompanyOwnedStandardPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(officeSupplies.getItemPricing()
					.getCompanyOwnedActualPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(officeSupplies.getItemPricing()
					.getCompanyOwnedTransferPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(officeSupplies.getItemPricing()
					.getFranchiseStandardPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(officeSupplies.getItemPricing()
					.getFranchiseActualPricePerUnit()));
			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(officeSupplies.getItemPricing()
					.getFranchiseTransferPricePerUnit()));
		}
		
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(officeSupplies.getQuantityIn()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(officeSupplies.getQuantityOut()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(officeSupplies.getWarehouse().getQuantityPerRecord()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(officeSupplies.getWarehouse()
				.getQuantityPerPhysicalCount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

	}

	private void populateSummaryForReceipts(HSSFWorkbook wb, List list,
			String subModule) {

		HSSFSheet sheet = wb.getSheetAt(0);
		int currentRow = 0;
		lastItemStyle = wb.createCellStyle();
		lastItemStyle.cloneStyleFrom(sheet.getRow(5).getCell(0).getCellStyle());
		lastItemStyle.setBorderBottom(HSSFBorderFormatting.BORDER_DASHED);
		for (int counter = 0; counter < list.size(); counter++) {
			int maxRow = 0;
			HSSFRow row = sheet.getRow(currentRow
					+ SASConstants.SUMMARY_TEMPLATE_ROW_START) == null ? sheet
					.createRow(currentRow
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START) : sheet
					.getRow(currentRow
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START);

			if (subModule.equals("CashReceipts")) {
				CashCheckReceipts cashReceipts = (CashCheckReceipts) list
						.get(counter);
				putCashCheckReceiptsValues(sheet, row, cashReceipts);
			} else if (subModule.equals("ORSales")) {
				ORSales orSales = (ORSales) list.get(counter);
				putORSalesValues(sheet, row, orSales);
			} else if (subModule.equals("OROthers")) {
				OROthers orOthers = (OROthers) list.get(counter);
				putOROthersValues(sheet, row, orOthers);
			}
			currentRow += 1;
		}
	}

	private void putORSalesValues(HSSFSheet sheet, HSSFRow row, ORSales orSales) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(orSales.getOrNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(orSales.getOrDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orSales.getReceivedFrom()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orSales.getAddress()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orSales.getBusStyle()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orSales.getTin()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orSales.getVatDetails().getOrNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orSales.getSalesInvoiceNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(orSales.getTheAmountOf()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orSales.getInFullPartialPaymentOf()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(orSales.getAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//START P3 AZ
		cell.setCellValue(parseNullDouble(orSales.getVatDetails().getVattableAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(orSales.getVatDetails().getVatAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//END
		cell.setCellValue(parseNullDouble(orSales.getCash()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(orSales.getCheck()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(orSales.getTotal()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orSales.getBankCheckNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orSales.getAmountInWords()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
	}

	private void putOROthersValues(HSSFSheet sheet, HSSFRow row,
			OROthers orOthers) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(orOthers.getOrNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(orOthers.getOrDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orOthers.getReceivedFrom()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orOthers.getAddress()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orOthers.getBusStyle()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orOthers.getTin()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orOthers.getVatDetails().getOrNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orOthers.getSalesInvoiceNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(orOthers.getTheAmountOf()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orOthers.getInFullPartialPaymentOf()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(orOthers.getAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//START P3 AZ
		cell.setCellValue(parseNullDouble(orOthers.getVatDetails().getVattableAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(orOthers.getVatDetails().getVatAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//END
		cell.setCellValue(parseNullDouble(orOthers.getCash()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(orOthers.getCheck()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(orOthers.getTotal()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orOthers.getBankCheckNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(orOthers.getAmountInWords()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
	}

	private void putCashCheckReceiptsValues(HSSFSheet sheet, HSSFRow row,
			CashCheckReceipts cashChecks) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(cashChecks.getCashReceiptNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(cashChecks.getCashReceiptDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(cashChecks.getPayee()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(cashChecks.getParticulars()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//START P3 AZ 
		cell.setCellValue(parseNullString(cashChecks.getVatDetails().getTinNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(cashChecks.getVatDetails().getAddress()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(cashChecks.getVatDetails().getOrNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//
		cell.setCellValue(parseNullDouble(cashChecks.getAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		
		//START P3 AZ 
				cell.setCellValue(parseNullDouble(cashChecks.getVatDetails().getVattableAmount()));
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(parseNullDouble(cashChecks.getVatDetails().getVattableAmount()));
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				//
		cell.setCellValue(parseNullString(cashChecks.getBankName()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(cashChecks.getBankAccountNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(cashChecks.getCheckNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(cashChecks.getCheckRemarks()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

	}

	private void populateSummaryForDisbursements(HSSFWorkbook wb, List list,
			String subModule) {
		HSSFSheet sheet = wb.getSheetAt(0);
		int currentRow = 0;
		lastItemStyle = wb.createCellStyle();
		lastItemStyle.cloneStyleFrom(sheet.getRow(5).getCell(0).getCellStyle());
		lastItemStyle.setBorderBottom(HSSFBorderFormatting.BORDER_DASHED);
		double pettyCashTotalAmount = 0;
		boolean isPettyCashByRef = false;

		for (int counter = 0; counter < list.size(); counter++) {
			int maxRow = 0;
			HSSFRow row = sheet.getRow(currentRow
					+ SASConstants.SUMMARY_TEMPLATE_ROW_START) == null ? sheet
					.createRow(currentRow
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START) : sheet
					.getRow(currentRow
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START);

			if (subModule.equals("PettyCash")) {
				PettyCash pettyCash = (PettyCash) list.get(counter);
				if (counter == 0)
					isPettyCashByRef = ((PettyCash) (list.get(0))).isByRef();
				putPettyCashValues(sheet, row, pettyCash);
				pettyCashTotalAmount += pettyCash.getAmount();
			} else if (subModule.equals("CashPayment")) {
				CashPayment cashPayment = (CashPayment) list.get(counter);
				putCashPaymentValues(sheet, row, cashPayment);
			} else if (subModule.equals("CheckPayment")) {
				CheckPayments checkPayment = (CheckPayments) list.get(counter);
				putCheckPaymentValues(sheet, row, checkPayment);
			} else if (subModule.equals("InvoiceCheckVoucher")) {
				CheckPayments checkPayment = (CheckPayments) list.get(counter);
				putInvoiceCheckVoucherValues(sheet, row, checkPayment);
			}
			currentRow += 1;

		}
		if (subModule.equals("PettyCash")) {
			HSSFRow row = sheet.getRow(currentRow
					+ SASConstants.SUMMARY_TEMPLATE_ROW_START) == null ? sheet
					.createRow(currentRow
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START + 1)
					: sheet.getRow(currentRow
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START + 1);

			putPettyCashTotal(sheet, row, pettyCashTotalAmount,
					isPettyCashByRef);
		}
	}

	private void putPettyCashTotal(HSSFSheet sheet, HSSFRow row,
			double pettyCashTotalAmount, boolean isPettyCashByRef) {
		int col = SASConstants.SUMMARY_TEMPLATE_PETTY_CASH_TOTAL_AMOUNT_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(SASConstants.SUMMARY_TEMPLATE_PETTY_CASH_TOTAL_AMOUNT_LABEL);
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(pettyCashTotalAmount));

		if (isPettyCashByRef) {
			row = sheet.getRow(row.getRowNum()
					+ SASConstants.SUMMARY_TEMPLATE_ROW_START) == null ? sheet
					.createRow(row.getRowNum()
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START) : sheet
					.getRow(row.getRowNum()
							+ SASConstants.SUMMARY_TEMPLATE_ROW_START);

			col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;

			cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

			cell.setCellValue(SASConstants.SUMMARY_TEMPLATE_PETTY_CASH_RELEASED_BY);
			cell = row.getCell(col + 3, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(SASConstants.SUMMARY_TEMPLATE_PETTY_CASH_APPROVED_BY);
		}

	}

	private void putCheckPaymentValues(HSSFSheet sheet, HSSFRow row,
			CheckPayments checkPayment) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(checkPayment.getCheckVoucherNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(checkPayment
				.getCheckVoucherDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getPayee()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getDescription()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getParticulars()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getVatDetails().getAddress()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getVatDetails().getTinNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(checkPayment.getAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(checkPayment.getVatDetails().getVattableAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(checkPayment.getVatDetails().getVatAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getVatDetails().getOrNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getBankName()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getBankAccountNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getCheckNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(checkPayment.getChequeDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getIsPrinted()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getIsEncashed()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

	}

	private void putInvoiceCheckVoucherValues(HSSFSheet sheet, HSSFRow row,
			CheckPayments checkPayment) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(checkPayment.getCheckVoucherNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(checkPayment
				.getCheckVoucherDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getPayee()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getDescription()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getParticulars()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(checkPayment.getAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getInvoice()
				.getSupplierInvoiceNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(checkPayment.getDebitAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getCreditTitle()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(checkPayment.getCreditAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getBankName()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getCheckNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(checkPayment.getBankAccountNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

	}

	private void putCashPaymentValues(HSSFSheet sheet, HSSFRow row,
			CashPayment cashPayment) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(cashPayment.getCashVoucherNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(cashPayment
				.getCashVoucherDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(cashPayment.getPayee()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(cashPayment.getDescription()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(cashPayment.getParticulars()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//START P3 AZ
				cell.setCellValue(parseNullString(cashPayment.getVatDetails().getOrNo()));
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(parseNullString(cashPayment.getVatDetails().getTinNumber()));
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(parseNullString(cashPayment.getVatDetails().getAddress()));
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(parseNullDouble(cashPayment.getAmount()));
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(parseNullDouble(cashPayment.getVatDetails().getVattableAmount()));
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(parseNullDouble(cashPayment.getVatDetails().getVatAmount()));
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				//END P3 AZ

	}

	private void putPettyCashValues(HSSFSheet sheet, HSSFRow row,
			PettyCash pettyCash) {
		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(pettyCash.getPcVoucherNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(pettyCash.getPcVoucherDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(pettyCash.getPayee()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(pettyCash.getDescription()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(pettyCash.getParticulars()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//START P3 AZ
		cell.setCellValue(parseNullString(pettyCash.getVatDetails().getOrNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(pettyCash.getVatDetails().getTinNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(pettyCash.getVatDetails().getAddress()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(pettyCash.getAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(pettyCash.getVatDetails().getVattableAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(pettyCash.getVatDetails().getVatAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//END P3 AZ
		

	}

	private int getMaxRow(int... maxRows) {
		int maxRow = 0;
		for (int i = 0; i < maxRows.length; i++) {
			if (maxRow < maxRows[i]) {
				maxRow = maxRows[i];
			}
		}
		if (maxRow == 0) {
			maxRow = 1;
		}
		return maxRow;
	}

	private void putItemSeparator(HSSFSheet sheet, int rowNum, int lastColumn) {
		// HSSFRow
		// row = sheet.getRow(rowNum-1);
		// for(int x = 0;x<=lastColumn;x++) {
		// HSSFCell cell = row.getCell(x,Row.CREATE_NULL_AS_BLANK);
		// cell.setCellStyle(lastItemStyle);
		// }

	}

	private void putSupplierValues(HSSFSheet sheet, HSSFRow row,
			Supplier supplier) {

		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;

		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getSupplierId()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getSupplierName()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getSupplierStatus()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getContactName()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getContactTitle()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getCompanyAddress()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getPhoneNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getFaxNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getMobileNumber()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getEmailAddress()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplier.getWebsite()));
	}
	
	private void putAccountEntryProfileValues(HSSFSheet sheet, HSSFRow row,
			AccountEntryProfile aep) {

		int col = SASConstants.SUMMARY_TEMPLATE_FIRST_COL;

		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(aep.getParentCode()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(aep.getAccountCode()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(aep.getName()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(aep.getReportType()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(aep.getClassification()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(aep.getReportAction()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(aep.getHierarchy()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(aep.getIsActive()));
	
	}


	private void putSupplierPurchaseOrderValues(HSSFSheet sheet, HSSFRow row,
			SupplierPurchaseOrder supplierPurchaseOrder) {

		int col = SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_COL_START;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(supplierPurchaseOrder.getSupplier()
				.getSupplierId()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplierPurchaseOrder
				.getSupplierPurchaseOrderId()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(supplierPurchaseOrder
				.getPurchaseOrderDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(supplierPurchaseOrder
				.getTotalAmount()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		// cell.setCellValue(dfh.parseDateToString(supplierPurchaseOrder.getPaymentDate()));
		// cell = row.getCell(col++,Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(supplierPurchaseOrder
				.getDateOfDelivery()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplierPurchaseOrder.getOrderedBy()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplierPurchaseOrder.getApprovedBy()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		putPurchaseOrderDetails(
				sheet,
				row,
				supplierPurchaseOrder.getPurchaseOrderDetails(),
				SASConstants.SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START);

	}

	private void putReceivingReportValues(HSSFSheet sheet, HSSFRow row,
			ReceivingReport receivingReport) {

		podetailHelper.generatePODetailsListFromSet(receivingReport.getPurchaseOrderDetails());
		podetailHelper.generateCommaDelimitedValues();
		int col = SASConstants.SUMMARY_TEMPLATE_SUPPLIER_RECEIVING_COL_START;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(receivingReport
				.getSupplierPurchaseOrder().getSupplierPurchaseOrderId()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(receivingReport
				.getReceivingReportNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(receivingReport
				.getReceivingReportDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		
		String tempRS = "";
		String delimeter="";
		for (ReturnSlip rs: receivingReport.getReturnSlipList()){
			cell.setCellValue(parseNullString(rs.getReturnSlipNo()));
			tempRS = tempRS + delimeter + rs.getReturnSlipNo();
			delimeter=",";
		}
		cell.setCellValue(parseNullString(tempRS));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				
		cell.setCellValue(dfh.parseDateToString(receivingReport
				.getReceivingReportPaymentDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(receivingReport.getTotalAmount());
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//START PHASE 3 AZ
		cell.setCellValue(podetailHelper.getTotalNonVattableAmount());
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(podetailHelper.getTotalVattableAmount());
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(podetailHelper.getTotalVatAmount());
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//END PHASE 3 AZ
		cell.setCellValue(parseNullString(receivingReport.getRemarks()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		putPurchaseOrderDetails(
				sheet,
				row,
				receivingReport.getPurchaseOrderDetails(),
				SASConstants.SUMMARY_TEMPLATE_SUPPLIER_RECEIVING_PURCHASE_DETAILS_COL_START);
	}

	private void putSupplierSalesInvoiceValues(HSSFSheet sheet, HSSFRow row,
			SupplierInvoice supplierInvoice) {
		podetailHelper.generatePODetailsListFromSet(supplierInvoice.getPurchaseOrderDetails());
		podetailHelper.generateCommaDelimitedValues();
		int col = SASConstants.SUMMARY_TEMPLATE_SUPPLIER_INVOICE_COL_START;
		HSSFCell cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		cell.setCellValue(parseNullString(supplierInvoice.getReceivingReport()
				.getReceivingReportNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplierInvoice
				.getSupplierInvoiceNo()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(supplierInvoice
				.getSupplierInvoiceDate()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(supplierInvoice.getRemarks()));
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(supplierInvoice.getDebit1Amount());
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
		//START PHASE 3 AZ
				cell.setCellValue(podetailHelper.getTotalNonVattableAmount());
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(podetailHelper.getTotalVattableAmount());
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(podetailHelper.getTotalVatAmount());
				cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);
				//END PHASE 3 AZ
		
		cell.setCellValue(supplierInvoice.getPreparedBy());
		cell = row.getCell(col++, Row.CREATE_NULL_AS_BLANK);

		putPurchaseOrderDetails(
				sheet,
				row,
				supplierInvoice.getPurchaseOrderDetails(),
				SASConstants.SUMMARY_TEMPLATE_SUPPLIER_INVOICE_PURCHASE_DETAILS_COL_START);
	}

	private Map<String, PurchaseOrderDetails> convertPurchaseOrderDetailsToMap(
			Set set) {
		Iterator itr = set.iterator();
		Map<String, PurchaseOrderDetails> tempMap = new HashMap();
		try {
			int nullCtr= 0;
			while (itr.hasNext()) {
				PurchaseOrderDetails podetails = (PurchaseOrderDetails) itr
						.next();
				String nullFiller = "Unlisted Item -";
				
				if(null==podetails.getItemCode() || "".equalsIgnoreCase(podetails.getItemCode()) || " ".equalsIgnoreCase(podetails.getItemCode())) {
					nullFiller=nullFiller+nullCtr++;
				}
				tempMap.put(nullFiller+podetails.getItemCode(), podetails);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tempMap;

	}

	private void putPurchaseOrderDetails(HSSFSheet sheet, HSSFRow row,
			Set<PurchaseOrderDetails> purchaseOrderDetails, int col) {
		Map originalMap = convertPurchaseOrderDetailsToMap(purchaseOrderDetails);
		Map sortedMap = new TreeMap(originalMap);

		List mapKeys = new ArrayList(sortedMap.keySet());
		List mapValues = new ArrayList(sortedMap.values());
		TreeSet sortedSet = new TreeSet(mapKeys);
		Object[] sortedArray = sortedSet.toArray();
		int size = sortedArray.length;

		for (int i = 0; i < size; i++) {
			int detailsColumnStart = col;
			HSSFCell cell = row.getCell(detailsColumnStart++,
					Row.CREATE_NULL_AS_BLANK);
			PurchaseOrderDetails poDetails = (PurchaseOrderDetails) originalMap
					.get(sortedArray[i]);
			cell.setCellValue(parseNullString(poDetails.getItemCode()));
			cell = row.getCell(detailsColumnStart++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(poDetails.getDescription()));
			cell = row.getCell(detailsColumnStart++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(poDetails.getQuantity()));
			cell = row.getCell(detailsColumnStart++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(poDetails.getUnitOfMeasurement()));
			cell = row.getCell(detailsColumnStart++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(poDetails.getUnitCost()));
			cell = row.getCell(detailsColumnStart++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(poDetails.getAmount()));
			cell = row.getCell(detailsColumnStart++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(poDetails.getVattableAmount()));
			cell = row.getCell(detailsColumnStart++, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(poDetails.getVatAmount()));
			cell = row.getCell(detailsColumnStart++, Row.CREATE_NULL_AS_BLANK);
			row = sheet.getRow(row.getRowNum() + 1) == null ? sheet
					.createRow(row.getRowNum() + 1) : sheet.getRow(row
					.getRowNum() + 1);
		}
	}

	private String parseNullString(String value) {
		return null == value ? "" : value;
	}

	private String parseNullDouble(Double value) {
		DecimalFormat df = new DecimalFormat("###,###,###.00");
		return null == value ? "0.0" : df.format(value);
	}

	public HSSFWorkbook printReceipts(String subModule, Map receiptMap,
			ServletContext servletContext) throws IOException {
		String filePath = servletContext
				.getRealPath(SASConstants.REPORT_SUMMARY_TEMPLATE_PATH
						+ SASConstants.RECEIPTTEMPLATE
						+ SASConstants.REPORT_SUMMARY_TEMPLATE_TYPE);

		FileInputStream fileInputStream;
		fileInputStream = new FileInputStream(filePath);
		POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);

		populateReceipt(wb, receiptMap, subModule);
		return wb;
	}

	private void populateReceipt(HSSFWorkbook wb, Map receiptMap,
			String subModule) {
		HSSFSheet sheet = wb.getSheetAt(0);
		int currentRow = 0;

		HSSFRow row = null;
		HSSFCell cell = null;
		row = sheet.getRow(SASConstants.ReceiptORNoRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptORNoRowMapping) : sheet
				.getRow(SASConstants.ReceiptORNoRowMapping);
		cell = row.getCell(SASConstants.ReceiptORNoColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString((String) receiptMap.get("orNumber")));

		row = sheet.getRow(SASConstants.ReceiptORDateRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptORDateRowMapping) : sheet
				.getRow(SASConstants.ReceiptORDateRowMapping);
		cell = row.getCell(SASConstants.ReceiptORDateColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString((Timestamp) receiptMap
				.get("orDate")));

		row = sheet.getRow(SASConstants.ReceiptReceivedFromRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptReceivedFromRowMapping) : sheet
				.getRow(SASConstants.ReceiptReceivedFromRowMapping);
		cell = row.getCell(SASConstants.ReceiptReceivedFromColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString((String) receiptMap
				.get("receivedFrom")));

		row = sheet.getRow(SASConstants.ReceiptAddressRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptAddressRowMapping) : sheet
				.getRow(SASConstants.ReceiptAddressRowMapping);
		cell = row.getCell(SASConstants.ReceiptAddressColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString((String) receiptMap.get("address")));

		row = sheet.getRow(SASConstants.ReceiptBusStyleRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptBusStyleRowMapping) : sheet
				.getRow(SASConstants.ReceiptBusStyleRowMapping);
		cell = row.getCell(SASConstants.ReceiptBusStyleColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString((String) receiptMap.get("busStyle")));

		row = sheet.getRow(SASConstants.ReceiptTinRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptTinRowMapping) : sheet
				.getRow(SASConstants.ReceiptTinRowMapping);
		cell = row.getCell(SASConstants.ReceiptTinColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString((String) receiptMap.get("tin")));

		row = sheet.getRow(SASConstants.ReceiptAmountInWordsRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptAmountInWordsRowMapping) : sheet
				.getRow(SASConstants.ReceiptAmountInWordsRowMapping);
		cell = row.getCell(SASConstants.ReceiptAmountInWordsColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString((String) receiptMap
				.get("amountInWords")));

		row = sheet.getRow(SASConstants.ReceiptAmountOfRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptAmountOfRowMapping) : sheet
				.getRow(SASConstants.ReceiptAmountOfRowMapping);
		cell = row.getCell(SASConstants.ReceiptAmountOfColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble((Double) receiptMap
				.get("theAmountOf")));

		row = sheet.getRow(SASConstants.ReceiptInPartialFullAmountOfRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptInPartialFullAmountOfRowMapping)
				: sheet.getRow(SASConstants.ReceiptInPartialFullAmountOfRowMapping);
		cell = row.getCell(SASConstants.ReceiptInPartialFullAmountOfColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString((String) receiptMap
				.get("inFullPartialPaymentOf")));

		row = sheet.getRow(SASConstants.ReceiptAmountRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptAmountRowMapping) : sheet
				.getRow(SASConstants.ReceiptAmountRowMapping);
		cell = row.getCell(SASConstants.ReceiptAmountColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble((Double) receiptMap.get("amount")));

		row = sheet.getRow(SASConstants.ReceiptSalesInvoiceNoRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptSalesInvoiceNoRowMapping)
				: sheet.getRow(SASConstants.ReceiptSalesInvoiceNoRowMapping);
		cell = row.getCell(SASConstants.ReceiptSalesInvoiceNoColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString((String) receiptMap
				.get("salesInvoiceNumber")));

		row = sheet.getRow(SASConstants.ReceiptSalesInvoiceAmountRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptSalesInvoiceAmountRowMapping)
				: sheet.getRow(SASConstants.ReceiptSalesInvoiceAmountRowMapping);
		cell = row.getCell(SASConstants.ReceiptSalesInvoiceAmountColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble((Double) receiptMap.get("amount")));

		row = sheet.getRow(SASConstants.ReceiptCashRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptCashRowMapping) : sheet
				.getRow(SASConstants.ReceiptCashRowMapping);
		cell = row.getCell(SASConstants.ReceiptCashColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble((Double) receiptMap.get("cash")));

		row = sheet.getRow(SASConstants.ReceiptCheckRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptCheckRowMapping) : sheet
				.getRow(SASConstants.ReceiptCheckRowMapping);
		cell = row.getCell(SASConstants.ReceiptCheckColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble((Double) receiptMap.get("check")));

		row = sheet.getRow(SASConstants.ReceiptCheckNoRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptCheckNoRowMapping) : sheet
				.getRow(SASConstants.ReceiptCheckNoRowMapping);
		cell = row.getCell(SASConstants.ReceiptCheckNoColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString((String) receiptMap
				.get("bankCheckNo")));

		row = sheet.getRow(SASConstants.ReceiptCashCheckTotalAmountRowMapping) == null ? sheet
				.createRow(SASConstants.ReceiptCashCheckTotalAmountRowMapping)
				: sheet.getRow(SASConstants.ReceiptCashCheckTotalAmountRowMapping);
		cell = row.getCell(SASConstants.ReceiptCashCheckTotalAmountColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble((Double) receiptMap.get("total")));

	}

	public HSSFWorkbook printCheckPayments(String subModule, CheckPayments chp,
			ServletContext servletContext) throws IOException {
		String filePath = servletContext
				.getRealPath(SASConstants.REPORT_SUMMARY_TEMPLATE_PATH
						+ "checkVoucher_checkPayment_template"
						+ SASConstants.REPORT_SUMMARY_TEMPLATE_TYPE);

		FileInputStream fileInputStream;
		fileInputStream = new FileInputStream(filePath);
		POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);

		printCheque(wb, chp, subModule);
		printCheckVoucher(wb, chp, subModule);
		// if(subModule.equalsIgnoreCase("supplierCheckVoucher")) {
		// printCheckVoucher(wb, chp, subModule);
		// }else{
		// wb.removeSheetAt(1);
		// }

		return wb;
	}

	private void printCheque(HSSFWorkbook wb, CheckPayments chp,
			String subModule) {
		HSSFSheet sheet = wb.getSheetAt(0);
		int currentRow = 0;

		HSSFRow row = null;
		HSSFCell cell = null;
		// set constants
		row = sheet.getRow(SASConstants.DisbursementCheckPaymentDateRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckPaymentDateRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckPaymentDateRowMapping);
		cell = row.getCell(SASConstants.DisbursementCheckPaymentDateColMapping,
				Row.CREATE_NULL_AS_BLANK);
		if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
			cell.setCellValue(dfh.parseDateToString(chp.getDueDate()));
		} else {
			cell.setCellValue(dfh.parseDateToString(chp.getCheckVoucherDate()));
		}
		
		
		row = sheet
				.getRow(SASConstants.DisbursementCheckPaymentPayeeRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckPaymentPayeeRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckPaymentPayeeRowMapping);
		cell = row.getCell(
				SASConstants.DisbursementCheckPaymentPayeeColMapping,
				Row.CREATE_NULL_AS_BLANK);
		//
		// if(subModule.equalsIgnoreCase("supplierCheckVoucher")) {
		// cell.setCellValue(parseNullString(chp.getInvoice().getReceivingReport().getSupplierPurchaseOrder().getSupplier().getSupplierName()));
		// }else {
		//
		cell.setCellValue(parseNullString(chp.getPayee()));
		// }

		row = sheet
				.getRow(SASConstants.DisbursementCheckPaymentAmountInWordsRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckPaymentAmountInWordsRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckPaymentAmountInWordsRowMapping);
		cell = row.getCell(
				SASConstants.DisbursementCheckPaymentAmountInwordsColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(chp.getAmountInWords()));

		row = sheet
				.getRow(SASConstants.DisbursementCheckPaymentBankNoRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckPaymentBankNoRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckPaymentBankNoRowMapping);
		cell = row.getCell(
				SASConstants.DisbursementCheckPaymentBankNoColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(chp.getCheckNo()));

		row = sheet
				.getRow(SASConstants.DisbursementCheckPaymentAmountRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckPaymentAmountRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckPaymentAmountRowMapping);
		cell = row.getCell(
				SASConstants.DisbursementCheckPaymentAmountColMapping,
				Row.CREATE_NULL_AS_BLANK);
		
		 if(subModule.equalsIgnoreCase("supplierCheckVoucher")) {
		 cell.setCellValue(parseNullDouble(chp.getAmountToPay()));
		 }else {
		
		cell.setCellValue(parseNullDouble(chp.getAmount()));
		}

	}

	private void printCheckVoucher(HSSFWorkbook wb, CheckPayments chp,
			String subModule) {
		
		initializeController();
		
		HSSFSheet sheet = wb.getSheetAt(1);
		int currentRow = 0;

		HSSFRow row = null;
		HSSFCell cell = null;
		// set constants
		
		row = sheet.getRow(SASConstants.DisbursementCheckVoucherNoRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherNoRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherNoRowMapping);
		cell = row.getCell(SASConstants.DisbursementCheckVoucherNoColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(chp.getCheckVoucherNumber()));
		
		
		row = sheet.getRow(SASConstants.DisbursementCheckVoucherDateRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherDateRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherDateRowMapping);
		;
		cell = row.getCell(SASConstants.DisbursementCheckVoucherDateColMapping,
				Row.CREATE_NULL_AS_BLANK);
		//if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
		//	cell.setCellValue(dfh.parseDateToString(chp.getCheckVoucherDate()));
		//} else {
			cell.setCellValue(dfh.parseDateToString(chp.getCheckVoucherDate()));
		//}
		row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherPayeeRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherPayeeRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherPayeeRowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherPayeeColMapping,
				Row.CREATE_NULL_AS_BLANK);
		if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
			cell.setCellValue(parseNullString(chp.getInvoice()
					.getReceivingReport().getSupplierPurchaseOrder()
					.getSupplier().getSupplierName()));
		} else {
			cell.setCellValue(parseNullString(chp.getPayee()));
		}
		row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherAmountInWordsRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherAmountInWordsRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherAmountInWordsRowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherAmountInWordColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(chp.getAmountInWords()));

		row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherBankNoRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherBankNoRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherBankNoRowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherBankNoColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(chp.getCheckNo()));

		row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherAmountRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherAmountRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherAmountRowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherAmountColMapping,
				Row.CREATE_NULL_AS_BLANK);
		
		
		// if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
		// cell.setCellValue(parseNullDouble(chp.getInvoice().getCredit1Amount()));
		// }else {
		if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
		cell.setCellValue(parseNullDouble(chp.getAmountToPay()));
		}
		else{
			cell.setCellValue(parseNullDouble(chp.getAmount()));
		}
		// }
		
		row = sheet.getRow(SASConstants.DisbursementCheckVoucherBankNameRowMapping);
				 cell =	 row.getCell(SASConstants.DisbursementCheckVoucherPaymentFromColMapping);
				 cell.setCellValue(parseNullString(chp.getBankName()));
		
		row = sheet.getRow(SASConstants.DisbursementCheckVoucherPaymentFromRowMapping);
			cell = row.getCell(SASConstants.DisbursementCheckVoucherPaymentFromColMapping);
			cell.setCellValue(parseNullString(chp.getCheckNo()));
			
		row = sheet.getRow(SASConstants.DisbursementCheckVoucherPaymentFromRowMapping + 1);
			cell = row.getCell(SASConstants.DisbursementCheckVoucherPaymentFromColMapping);
			cell.setCellValue(dfh.parseDateToString(chp.getChequeDate()));
		
		row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherTotalPurchasesRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherTotalPurchasesRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherTotalPurchasesRowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherTotalPurchasesColMapping,
				Row.CREATE_NULL_AS_BLANK);
		if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
			cell.setCellValue("Total Purchases : "
					+ parseNullDouble(chp.getInvoice()
							.getPurchaseOrderDetailsTotalAmount()));
		} else {
			cell.setCellValue(parseNullString(""));
		}

		/*row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherDebitRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherDebitRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherDebitRowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherDebitColMapping,
				Row.CREATE_NULL_AS_BLANK);
		if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
			cell.setCellValue(parseNullDouble(chp.getInvoice()
					.getDebit1Amount()));
		} else {
			cell.setCellValue(parseNullDouble(chp.getDebitAmount()));
		}*/
		row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherVatAmountRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherVatAmountRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherVatAmountRowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherVatAmountColMapping,
				Row.CREATE_NULL_AS_BLANK);
		//if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
			cell.setCellValue(parseNullDouble(chp.getVatDetails().getVatAmount()));
		//} else {
			//cell.setCellValue(parseNullDouble(chp.getVatAmount()));
		//}
		
		row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherAmountToVatRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherAmountToVatRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherAmountToVatRowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherAmountToVatColMapping,
				Row.CREATE_NULL_AS_BLANK);
//		if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {

			cell.setCellValue(parseNullDouble(chp.getVatDetails().getVattableAmount()));
					
	//	} else {
	//		cell.setCellValue(parseNullDouble(chp.getFinalAmount()));
	//	}

		row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherReferenceRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherReferenceRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherReferenceRowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherReferenceColMapping,
				Row.CREATE_NULL_AS_BLANK);
		if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
			cell.setCellValue("INV# " + parseNullString(chp.getInvoice()
					.getSupplierInvoiceNo() + " / " + dfh.parseDateToString(chp.getInvoice().getSupplierInvoiceDate())
					+ " / RR# :" + chp.getInvoice().getReceivingReport().getReceivingReportNo()) 
					+ " / " + "Invoice Due Date: " + dfh.parseDateToString(chp.getInvoice().getReceivingReport().getReceivingReportPaymentDate()));
			
		} else {
			cell.setCellValue(parseNullString(chp.getVatDetails().getOrNo()));
		}
		row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherAmount2RowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherAmount2RowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherAmount2RowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherAmount2ColMapping,
				Row.CREATE_NULL_AS_BLANK);
	//	if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
			double tempAmount = chp.getVatDetails().getVattableAmount() + chp.getVatDetails().getVatAmount();
			double nonVatableAmount = 0;
			nonVatableAmount = chp.getAmount() - tempAmount;
			cell.setCellValue(parseNullDouble(nonVatableAmount));
	//	} else {
	//		cell.setCellValue(parseNullDouble(chp.getCreditAmount()));
	//	}
		
		//update check voucher amount for multiple checks
		if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
			
		row = sheet
				.getRow(SASConstants.DisbursementCheckVoucherTotalPurchasesRowMapping) == null ? sheet
				.createRow(SASConstants.DisbursementCheckVoucherTotalPurchasesRowMapping)
				: sheet.getRow(SASConstants.DisbursementCheckVoucherTotalPurchasesRowMapping);
		;
		cell = row.getCell(
				SASConstants.DisbursementCheckVoucherPaymentTermsColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString("Payment Terms : " + chp.getInvoice().getReceivingReport().getSupplierPurchaseOrder().getSupplier().getPaymentTerm()));
			
			row = sheet
					.getRow(SASConstants.DisbursementCheckVoucherAmountPaidLabelRowMapping) == null ? sheet
					.createRow(SASConstants.DisbursementCheckVoucherAmountPaidLabelRowMapping)
					: sheet.getRow(SASConstants.DisbursementCheckVoucherAmountPaidLabelRowMapping);
			;
			cell = row.getCell(
					SASConstants.DisbursementCheckVoucherAmountPaidLabelColMapping,
					Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(parseNullString("Amount Paid : " + parseNullDouble(chp.getAmountToPay())));
		
		}
		// row = sheet.getRow(SASConstants.);
		// cell = row.getCell(SASConstants.);
		// cell.setCellValue(parseNullString(chp.getCheckNo()));
		
		if (subModule.equalsIgnoreCase("supplierCheckVoucher")) {
			
			row = sheet
					.getRow(SASConstants.DisbursementCheckVoucherCreditRowMapping) == null ? sheet
					.createRow(SASConstants.DisbursementCheckVoucherCreditRowMapping)
					: sheet.getRow(SASConstants.DisbursementCheckVoucherCreditRowMapping);
			;
			cell = row.getCell(
					SASConstants.DisbursementCheckVoucherCreditColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(chp.getInvoice().getDebit1Amount()));
		}else {
			row = sheet
					.getRow(SASConstants.DisbursementCheckVoucherCreditRowMapping) == null ? sheet
					.createRow(SASConstants.DisbursementCheckVoucherCreditRowMapping)
					: sheet.getRow(SASConstants.DisbursementCheckVoucherCreditRowMapping);
			;
			cell = row.getCell(
					SASConstants.DisbursementCheckVoucherCreditColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(chp.getAmount()));
		}
		
		// START: replacement of values for accounting entries title	
			//START Phase 3 - Azhee
			List tempList = transactionManager.listTransactionByParameterLike(com.shofuku.accsystem.domain.financials.Transaction.class,
					"transactionReferenceNumber", chp.getCheckVoucherNumber(), session);
			Iterator itrTempList = tempList.iterator();
			List transactionList = new ArrayList<com.shofuku.accsystem.domain.financials.Transaction>(); 
			
			while(itrTempList.hasNext()) {
				com.shofuku.accsystem.domain.financials.Transaction transactions = (com.shofuku.accsystem.domain.financials.Transaction)itrTempList.next();
				if(transactions.getIsInUse().equalsIgnoreCase(SASConstants.TRANSACTION_IN_USE)) {
					transactionList.add(transactions);
				}
			}
			chp.setTransactions(transactionList);
		//END Phase 3 - Azhee
			
			Iterator itrAE = chp.getTransactions().iterator();
			int x=0;	
			x =SASConstants.DisbursementCheckVoucherAccountTitleRowMapping;
			while(itrAE.hasNext()) {
				 com.shofuku.accsystem.domain.financials.Transaction transaction = (com.shofuku.accsystem.domain.financials.Transaction) itrAE.next();
					
					row = sheet.getRow(x) == null ? sheet.createRow(x) : sheet
						.getRow(x++);
					cell = row.getCell(SASConstants.DisbursementCheckVoucherAccountTitleColMapping,	Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(parseNullString(transaction.getAccountEntry().getName()));
					
					if(transaction.getAccountEntry().getAccountingRules().getDisbursementCheckVoucher().equalsIgnoreCase("DEBIT")) {
						cell = row.getCell(9, Row.CREATE_NULL_AS_BLANK);
					}else {
						cell = row.getCell(10, Row.CREATE_NULL_AS_BLANK);				
					}
					cell.setCellValue(parseNullDouble(transaction.getAmount()));
					
				}
			//END Phase 3 - Azhee
		/*	
			Iterator itr = chp.getInvoice().getPurchaseOrderDetails()
					.iterator();
			int x = 0;
					//SASConstants.DisbursementCheckVoucherAccountTitleRowMapping;
			int numberOfOrderDetails = chp.getInvoice()
					.getPurchaseOrderDetails().size();
			if (numberOfOrderDetails < 6) {
				wb.removeSheetAt(2);
			}
			int newSheetStartingRow = 1;
			
			while (itr.hasNext()) {
				PurchaseOrderDetails invoiceDetails = (PurchaseOrderDetails) itr
						.next();
				String value = invoiceDetails.getDescription() + " "
						+ invoiceDetails.getQuantity() + " "
						+ invoiceDetails.getUnitOfMeasurement() + " @ P"
						+ invoiceDetails.getUnitCost();
				
				if (numberOfOrderDetails < 6) {
					x =SASConstants.DisbursementCheckVoucherAccountTitleRowMapping;
					row = sheet.getRow(x) == null ? sheet.createRow(x) : sheet
						.getRow(x++);
					cell = row
						.getCell(
								SASConstants.DisbursementCheckVoucherAccountTitleColMapping,
								Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(parseNullString(value));
					
				} else {
					sheet = wb.getSheetAt(2);
					row = sheet.getRow(newSheetStartingRow) == null ? sheet
							.createRow(newSheetStartingRow) : sheet
							.getRow(newSheetStartingRow);
					cell = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(parseNullString(value));
					cell = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(parseNullDouble(invoiceDetails
							.getAmount()));
					cell = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(parseNullDouble(invoiceDetails
							.getAmount()));
					newSheetStartingRow++;
				}
			} */
			
		/*
		} else {
			row = sheet
					.getRow(SASConstants.DisbursementCheckVoucherAccountTitleRowMapping) == null ? sheet
					.createRow(SASConstants.DisbursementCheckVoucherAccountTitleRowMapping)
					: sheet.getRow(SASConstants.DisbursementCheckVoucherAccountTitleRowMapping);
			;
			cell = row
					.getCell(
							SASConstants.DisbursementCheckVoucherAccountTitleColMapping,
							Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(chp.getParticulars()));
		*/	
		
		
		
	}

	public HSSFWorkbook printCustomerInvoice(String subModule,
			CustomerSalesInvoice csi, ServletContext servletContext)
			throws IOException {
		String filePath = servletContext
				.getRealPath(SASConstants.REPORT_SUMMARY_TEMPLATE_PATH
						+ "customerSalesInvoice1"
						+ SASConstants.REPORT_SUMMARY_TEMPLATE_TYPE);

		FileInputStream fileInputStream;
		fileInputStream = new FileInputStream(filePath);
		POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);

		printCustomerInvoice(wb, csi, subModule);

		return wb;
	}

	private void printCustomerInvoice(HSSFWorkbook wb,
			CustomerSalesInvoice csi, String subModule) {
		HSSFSheet sheet = wb.getSheetAt(0);
		int currentRow = 0;

		HSSFRow row = null;
		HSSFCell cell = null;

		// set constants
		row = sheet.getRow(SASConstants.CustomerSalesInvoiceDateRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerSalesInvoiceDateRowMapping)
				: sheet.getRow(SASConstants.CustomerSalesInvoiceDateRowMapping);
		cell = row.getCell(SASConstants.CustomerSalesInvoiceDateColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(csi.getCustomerInvoiceDate()));

		row = sheet.getRow(SASConstants.CustomerSalesInvoiceDRNoRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerSalesInvoiceDateRowMapping)
				: sheet.getRow(SASConstants.CustomerSalesInvoiceDateRowMapping);
		cell = row.getCell(SASConstants.CustomerSalesInvoiceDRNoColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(csi.getDeliveryReceipt()
				.getDeliveryReceiptNo()));

		row = sheet.getRow(SASConstants.CustomerSalesInvoiceSoldToRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerSalesInvoiceSoldToRowMapping)
				: sheet.getRow(SASConstants.CustomerSalesInvoiceSoldToRowMapping);
		cell = row.getCell(SASConstants.CustomerSalesInvoiceSoldToColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(csi.getSoldTo()));

		row = sheet.getRow(SASConstants.CustomerSalesInvoiceAddressRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerSalesInvoiceAddressRowMapping)
				: sheet.getRow(SASConstants.CustomerSalesInvoiceAddressRowMapping);
		cell = row.getCell(SASConstants.CustomerSalesInvoiceAddressColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(csi.getAddress()));

		// row =
		// sheet.getRow(SASConstants.CustomerSalesInvoiceBusStyleRowMapping)==null?sheet.createRow(SASConstants.CustomerSalesInvoiceBusStyleRowMapping):sheet.getRow(SASConstants.CustomerSalesInvoiceBusStyleRowMapping);
		// cell =
		// row.getCell(SASConstants.CustomerSalesInvoiceBusStyleColMapping,Row.CREATE_NULL_AS_BLANK);
		// cell.setCellValue(parseNullString(csi.getBusStyle()));
		//
		// row =
		// sheet.getRow(SASConstants.CustomerSalesInvoiceTinRowMapping)==null?sheet.createRow(SASConstants.CustomerSalesInvoiceTinRowMapping):sheet.getRow(SASConstants.CustomerSalesInvoiceTinRowMapping);
		// cell =
		// row.getCell(SASConstants.CustomerSalesInvoiceTinColMapping,Row.CREATE_NULL_AS_BLANK);
		// cell.setCellValue(parseNullString(csi.getTin()));
		//
		row = sheet
				.getRow(SASConstants.CustomerSalesInvoiceTotalSalesRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerSalesInvoiceTotalSalesRowMapping)
				: sheet.getRow(SASConstants.CustomerSalesInvoiceTotalSalesRowMapping);
		cell = row.getCell(
				SASConstants.CustomerSalesInvoiceTotalSalesColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(csi.getTotalSales()));

		row = sheet.getRow(SASConstants.CustomerSalesInvoiceVatRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerSalesInvoiceVatRowMapping)
				: sheet.getRow(SASConstants.CustomerSalesInvoiceVatRowMapping);
		cell = row.getCell(SASConstants.CustomerSalesInvoiceVatColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(csi.getVatDetails().getVatAmount()));

		row = sheet
				.getRow(SASConstants.CustomerSalesInvoiceTotalAmountRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerSalesInvoiceTotalAmountRowMapping)
				: sheet.getRow(SASConstants.CustomerSalesInvoiceTotalAmountRowMapping);
		cell = row.getCell(
				SASConstants.CustomerSalesInvoiceTotalAmountColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(csi.getVatDetails().getVattableAmount()));

		int x = SASConstants.CustomerSalesInvoiceQuantityRowMapping;
		HSSFCellStyle style = wb.createCellStyle();
		// HSSFCellStyle style2 = wb.createCellStyle();
		// style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		int numberOfOrderDetails = csi.getPurchaseOrderDetails().size();
		
			if (numberOfOrderDetails < 26) {
				wb.removeSheetAt(2);
			}
		
		//
		// Sorting PODetails using podetailsHelper
		podetailHelper.setPurchaseOrderDetailsSet(csi
				.getPurchaseOrderDetails());
		sortListsAlphabetically(podetailHelper);
		// getting PODetails as sorted list
		List sortedPurchaseOrderDetails = new ArrayList();
		sortedPurchaseOrderDetails = podetailHelper
				.generatePODetailsListFromSet(podetailHelper
						.getPurchaseOrderDetailsSet());
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
		style2.setBorderLeft(HSSFCellStyle.BORDER_DOTTED);
		style2.setBorderRight(HSSFCellStyle.BORDER_DOTTED);
		style2.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
		
		HSSFCellStyle styleAlignRight = wb.createCellStyle();
		styleAlignRight.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
		styleAlignRight.setBorderLeft(HSSFCellStyle.BORDER_DOTTED);
		styleAlignRight.setBorderRight(HSSFCellStyle.BORDER_DOTTED);
		styleAlignRight.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
		styleAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		
		Iterator itr = sortedPurchaseOrderDetails.iterator();
		
		while (itr.hasNext()) {
			if (x >= 41 && sheet.equals(wb.getSheetAt(0))) {
				sheet = wb.getSheetAt(2);
				x = 1;
				int newNumberOfOrderDetails = numberOfOrderDetails - 25;
				newNumberOfOrderDetails = newNumberOfOrderDetails + 1;
			
					row = sheet.getRow(newNumberOfOrderDetails) == null ? sheet
							.createRow(newNumberOfOrderDetails) : sheet
							.getRow(newNumberOfOrderDetails);
					cell = row.getCell(
							SASConstants.CustomerSalesInvoiceAmountColMapping,
							Row.CREATE_NULL_AS_BLANK);
					cell.setCellStyle(style2);
					cell.setCellValue(parseNullDouble(csi.getTotalSales()));

					row = sheet.getRow(newNumberOfOrderDetails) == null ? sheet
							.createRow(newNumberOfOrderDetails) : sheet
							.getRow(newNumberOfOrderDetails);
					cell = row.getCell(SASConstants.CUSTOMER_INVOICE_TOTAL_SALES,
							Row.CREATE_NULL_AS_BLANK);
					cell.setCellStyle(styleAlignRight);
					cell.setCellValue(parseNullString("TOTAL SALES PHP"));
				}
		
			
			
			/*
			 * else { wb.getActiveSheetIndex(); wb.removeSheetAt(0); }
			 */

			PurchaseOrderDetails invoiceDetails = (PurchaseOrderDetails) itr
					.next();

			// if(numberOfOrderDetails>25){
			// row =
			// sheet.getRow(newSheetStartingRow)==null?sheet.createRow(newSheetStartingRow):sheet.getRow(newSheetStartingRow);
			// HSSFCell cellqty = row.getCell(0,Row.CREATE_NULL_AS_BLANK);
			// cellqty.setCellStyle(style);
			// cellqty.setCellValue(parseNullDouble(invoiceDetails.getQuantity()));
			// cell = row.getCell(1,Row.CREATE_NULL_AS_BLANK);
			// cell.setCellValue(parseNullString(invoiceDetails.getUnitOfMeasurement()));
			// cell = row.getCell(2,Row.CREATE_NULL_AS_BLANK);
			// cell.setCellValue(parseNullString(invoiceDetails.getDescription()));
			// cell = row.getCell(5,Row.CREATE_NULL_AS_BLANK);
			// cell.setCellValue(parseNullDouble(invoiceDetails.getUnitCost()));
			// cell = row.getCell(6,Row.CREATE_NULL_AS_BLANK);
			// cell.setCellValue(parseNullDouble(invoiceDetails.getAmount()));
			// newSheetStartingRow++;
			// }else{
			row = sheet.getRow(x) == null ? sheet.createRow(x) : sheet
					.getRow(x);
			HSSFCell cellqty = row.getCell(
					SASConstants.CustomerSalesInvoiceQuantityColMapping,
					Row.CREATE_NULL_AS_BLANK);
			
			if (x==1) {
				style2 = cellqty.getCellStyle();
			}
			
			// cellqty.setCellStyle(style);
			cellqty.setCellStyle(style2);
			cellqty.setCellValue(parseNullDouble(invoiceDetails.getQuantity()));
			cell = row.getCell(SASConstants.CustomerSalesInvoiceUnitColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellStyle(style2);
			cell.setCellValue(parseNullString(invoiceDetails
					.getUnitOfMeasurement()));
			cell = row.getCell(
					SASConstants.CustomerSalesInvoiceItemCodeColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellStyle(style2);
			cell.setCellValue(parseNullString(invoiceDetails.getItemCode()));
			cell = row.getCell(
					SASConstants.CustomerSalesInvoiceDescriptionColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellStyle(style2);
			cell.setCellValue(parseNullString(invoiceDetails.getDescription()));
			cell = row.getCell(
					SASConstants.CustomerSalesInvoiceUnitPriceColMapping,
					Row.CREATE_NULL_AS_BLANK);
			
			cell.setCellStyle(styleAlignRight);
						cell.setCellValue(parseNullDouble(invoiceDetails.getUnitCost()));
			cell = row.getCell(
					SASConstants.CustomerSalesInvoiceAmountColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellStyle(styleAlignRight);
			
			cell.setCellValue(parseNullDouble(invoiceDetails.getAmount()));
			
			x++;

		}

		printBIRCustomerInvoice(wb, csi, subModule);

	}

	private void printBIRCustomerInvoice(HSSFWorkbook wb,
			CustomerSalesInvoice csi, String subModule) {

		HSSFSheet sheet = wb.getSheetAt(1);
		int currentRow = 0;

		HSSFRow row = null;
		HSSFCell cell = null;

		// set constants
		row = sheet.getRow(SASConstants.CustomerBIRSalesInvoiceDateRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerBIRSalesInvoiceDateRowMapping)
				: sheet.getRow(SASConstants.CustomerBIRSalesInvoiceDateRowMapping);
		cell = row.getCell(SASConstants.CustomerBIRSalesInvoiceDateColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(csi.getCustomerInvoiceDate()));

		// row =
		// sheet.getRow(SASConstants.CustomerSalesInvoiceDRNoRowMapping)==null?sheet.createRow(SASConstants.CustomerSalesInvoiceDateRowMapping):sheet.getRow(SASConstants.CustomerSalesInvoiceDateRowMapping);
		// cell =
		// row.getCell(SASConstants.CustomerSalesInvoiceDRNoColMapping,Row.CREATE_NULL_AS_BLANK);
		// cell.setCellValue(parseNullString(csi.getDeliveryReceipt().getDeliveryReceiptNo()));
		//

		row = sheet
				.getRow(SASConstants.CustomerBIRSalesInvoiceSoldToRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerBIRSalesInvoiceSoldToRowMapping)
				: sheet.getRow(SASConstants.CustomerBIRSalesInvoiceSoldToRowMapping);
		cell = row.getCell(
				SASConstants.CustomerBIRSalesInvoiceSoldToColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(csi.getSoldTo()));

		row = sheet
				.getRow(SASConstants.CustomerBIRSalesInvoiceAddressRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerBIRSalesInvoiceAddressRowMapping)
				: sheet.getRow(SASConstants.CustomerBIRSalesInvoiceAddressRowMapping);
		cell = row.getCell(
				SASConstants.CustomerBIRSalesInvoiceAddressColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(csi.getAddress()));

		row = sheet
				.getRow(SASConstants.CustomerBIRSalesInvoiceBusStyleRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerBIRSalesInvoiceBusStyleRowMapping)
				: sheet.getRow(SASConstants.CustomerBIRSalesInvoiceBusStyleRowMapping);
		cell = row.getCell(
				SASConstants.CustomerBIRSalesInvoiceBusStyleColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(csi.getBusStyle()));

		row = sheet.getRow(SASConstants.CustomerBIRSalesInvoiceTinRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerBIRSalesInvoiceTinRowMapping)
				: sheet.getRow(SASConstants.CustomerBIRSalesInvoiceTinRowMapping);
		cell = row.getCell(SASConstants.CustomerBIRSalesInvoiceTinColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(csi.getTin()));

		row = sheet
				.getRow(SASConstants.CustomerBIRSalesInvoiceTotalSalesRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerBIRSalesInvoiceTotalSalesRowMapping)
				: sheet.getRow(SASConstants.CustomerBIRSalesInvoiceTotalSalesRowMapping);
		cell = row.getCell(
				SASConstants.CustomerBIRSalesInvoiceTotalSalesColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(csi.getTotalSales()));

		row = sheet.getRow(SASConstants.CustomerBIRSalesInvoiceVatRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerBIRSalesInvoiceVatRowMapping)
				: sheet.getRow(SASConstants.CustomerBIRSalesInvoiceVatRowMapping);
		cell = row.getCell(SASConstants.CustomerBIRSalesInvoiceVatColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(csi.getVat()));

		row = sheet
				.getRow(SASConstants.CustomerBIRSalesInvoiceTotalAmountRowMapping) == null ? sheet
				.createRow(SASConstants.CustomerBIRSalesInvoiceTotalAmountRowMapping)
				: sheet.getRow(SASConstants.CustomerBIRSalesInvoiceTotalAmountRowMapping);
		cell = row.getCell(
				SASConstants.CustomerBIRSalesInvoiceTotalAmountColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullDouble(csi.getTotalAmount()));

	}

	private PurchaseOrderDetailHelper sortListsAlphabetically(
			PurchaseOrderDetailHelper poDetailsHelper) {

		List purchaseOrderDetailsList = poDetailsHelper
				.generatePODetailsListFromSet(poDetailsHelper
						.getPurchaseOrderDetailsSet());
		List sortedPurchaseOrderDetailsList = new ArrayList();

		HashMap<String, PurchaseOrderDetails> map = new HashMap<String, PurchaseOrderDetails>();
		Set<PurchaseOrderDetails> sortedMap = new HashSet<PurchaseOrderDetails>();
		List itemCodeList = new ArrayList();
		List unlistedItemsList= new ArrayList();
		HashMap<String,PurchaseOrderDetails> unlistedItemsMap = new HashMap<String,PurchaseOrderDetails>();
		
		
		try {
			Iterator<PurchaseOrderDetails> itr = purchaseOrderDetailsList
					.iterator();
			while (itr.hasNext()) {
				PurchaseOrderDetails podetails = (PurchaseOrderDetails) itr
						.next();
				if(podetails.getItemCode().trim().equalsIgnoreCase("")) {
					unlistedItemsMap.put(SASConstants.NOT_APPLICABLE,podetails);
					// YOU LEFT HERE PROBLEM: PODETAILS CANT SHOW FOR SAME ITEM CODES WHICH IS BLANK FOR UNLISTED ITEMS
					unlistedItemsList.add(podetails);
					
				}else {
					map.put(podetails.getItemCode(),podetails);
					itemCodeList.add(podetails.getItemCode());
				}
			}

			Collections.sort(itemCodeList);

			Iterator<String> iteratorSorted = itemCodeList.iterator();
			while (iteratorSorted.hasNext()) {
				String code = (String) iteratorSorted.next();
				sortedPurchaseOrderDetailsList.add(map.get(code));
				sortedMap.add(map.get(code));
			}
			
			//include unlisted items
			Iterator unlistedItemsMapItr = unlistedItemsList.iterator();
			while(unlistedItemsMapItr.hasNext()) {
				PurchaseOrderDetails podetails = (PurchaseOrderDetails)unlistedItemsMapItr.next();
				sortedMap.add(podetails);
			}
			sortedPurchaseOrderDetailsList.addAll(unlistedItemsList);
			//end include unlisted items

			
			poDetailsHelper.setPurchaseOrderDetailsSet(sortedMap);
			poDetailsHelper
					.setPurchaseOrderDetailsList(sortedPurchaseOrderDetailsList);
		} catch (NullPointerException nfe) {
			nfe.printStackTrace();
		}

		return poDetailsHelper;

	}

	private void printSupplierPurchaseOrder(HSSFWorkbook wb,
			SupplierPurchaseOrder spo, String subModule) {
		HSSFSheet sheet = wb.getSheetAt(0);
		int currentRow = 0;

		HSSFRow row = null;
		HSSFCell cell = null;

		// set constants
		row = sheet.getRow(SASConstants.SupplierPurchaseOrderDateRowMapping) == null ? sheet
				.createRow(SASConstants.SupplierPurchaseOrderDateRowMapping)
				: sheet.getRow(SASConstants.SupplierPurchaseOrderDateRowMapping);
		cell = row.getCell(SASConstants.SupplierPurchaseOrderDateColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(spo.getPurchaseOrderDate()));

		row = sheet.getRow(SASConstants.SupplierPurchaseOrderNoRowMapping) == null ? sheet
				.createRow(SASConstants.SupplierPurchaseOrderNoRowMapping)
				: sheet.getRow(SASConstants.SupplierPurchaseOrderNoRowMapping);
		cell = row.getCell(SASConstants.SupplierPurchaseOrderNoColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(spo.getSupplierPurchaseOrderId()));

		row = sheet
				.getRow(SASConstants.SupplierPurchaseOrderDeliveryDateRowMapping) == null ? sheet
				.createRow(SASConstants.SupplierPurchaseOrderDeliveryDateRowMapping)
				: sheet.getRow(SASConstants.SupplierPurchaseOrderDeliveryDateRowMapping);
		cell = row.getCell(
				SASConstants.SupplierPurchaseOrderDeliveryDateColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(dfh.parseDateToString(spo.getDateOfDelivery()));

		row = sheet.getRow(SASConstants.SupplierPurchaseOrderAddressRowMapping) == null ? sheet
				.createRow(SASConstants.SupplierPurchaseOrderAddressRowMapping)
				: sheet.getRow(SASConstants.SupplierPurchaseOrderAddressRowMapping);
		cell = row.getCell(SASConstants.SupplierPurchaseOrderAddressColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(spo.getSupplier().getCompanyAddress()));

		row = sheet
				.getRow(SASConstants.SupplierPurchaseOrderSupplierNameRowMapping) == null ? sheet
				.createRow(SASConstants.SupplierPurchaseOrderSupplierNameRowMapping)
				: sheet.getRow(SASConstants.SupplierPurchaseOrderSupplierNameRowMapping);
		cell = row.getCell(
				SASConstants.SupplierPurchaseOrderSupplierNameColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(spo.getSupplier().getSupplierName()));

		row = sheet
				.getRow(SASConstants.SupplierPurchaseOrderContactNameRowMapping) == null ? sheet
				.createRow(SASConstants.SupplierPurchaseOrderContactNameRowMapping)
				: sheet.getRow(SASConstants.SupplierPurchaseOrderContactNameRowMapping);
		cell = row.getCell(
				SASConstants.SupplierPurchaseOrderContactNameColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(spo.getSupplier().getContactName()));

		row = sheet
				.getRow(SASConstants.SupplierPurchaseOrderPaymentTermsRowMapping) == null ? sheet
				.createRow(SASConstants.SupplierPurchaseOrderPaymentTermsRowMapping)
				: sheet.getRow(SASConstants.SupplierPurchaseOrderPaymentTermsRowMapping);
		cell = row.getCell(
				SASConstants.SupplierPurchaseOrderPaymentTermsColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(spo.getSupplier().getPaymentTerm()));

		row = sheet.getRow(SASConstants.SupplierPurchaseOrderFaxNoRowMapping) == null ? sheet
				.createRow(SASConstants.SupplierPurchaseOrderFaxNoRowMapping)
				: sheet.getRow(SASConstants.SupplierPurchaseOrderFaxNoRowMapping);
		cell = row.getCell(SASConstants.SupplierPurchaseOrderFaxNoColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(spo.getSupplier().getFaxNumber()));

		row = sheet.getRow(SASConstants.SupplierPurchaseOrderTotalAmountRowMapping) == null ? sheet
				.createRow(SASConstants.SupplierPurchaseOrderTotalAmountRowMapping)
				: sheet.getRow(SASConstants.SupplierPurchaseOrderTotalAmountRowMapping);
		cell = row.getCell(SASConstants.SupplierPurchaseOrderTotalAmountColMapping,
				Row.CREATE_NULL_AS_BLANK);
		cell.setCellValue(parseNullString(parseNullDouble(spo.getTotalAmount())));
		
		
		int x = SASConstants.SupplierPurchaseOrderQuantityRowMapping;
		HSSFCellStyle style = wb.createCellStyle();
		// HSSFCellStyle style2 = wb.createCellStyle();
		// style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		int numberOfOrderDetails = spo.getPurchaseOrderDetails().size();
		
		if (numberOfOrderDetails < 5) {
			wb.removeSheetAt(1);
		}

		// Sorting PODetails using podetailsHelper
		podetailHelper.setPurchaseOrderDetailsSet(spo
				.getPurchaseOrderDetails());
		sortListsAlphabetically(podetailHelper);
		// getting PODetails as sorted list
		List sortedPurchaseOrderDetails = new ArrayList();
		sortedPurchaseOrderDetails = podetailHelper
				.generatePODetailsListFromSet(podetailHelper
						.getPurchaseOrderDetailsSet());

		Iterator itr = sortedPurchaseOrderDetails.iterator();
		
		if (numberOfOrderDetails > 5) {
			sheet = wb.getSheetAt(1);
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			x = 1;
//			int newNumberOfOrderDetails = numberOfOrderDetails - 5;
//			newNumberOfOrderDetails = newNumberOfOrderDetails + 1;
//
//			row = sheet.getRow(newNumberOfOrderDetails) == null ? sheet
//					.createRow(newNumberOfOrderDetails) : sheet
//					.getRow(newNumberOfOrderDetails);
//			cell = row.getCell(
//					SASConstants.SupplierPurchaseOrderTotalAmountColMapping,
//					Row.CREATE_NULL_AS_BLANK);
//			cell.setCellValue(parseNullDouble(spo.getTotalAmount()));
//
//			// row =
			// sheet.getRow(newNumberOfOrderDetails)==null?sheet.createRow(newNumberOfOrderDetails):sheet.getRow(newNumberOfOrderDetails);
			// cell =
			// row.getCell(SASConstants.CUSTOMER_INVOICE_TOTAL_SALES,Row.CREATE_NULL_AS_BLANK);
			// cell.setCellValue(parseNullString("TOTAL PHP"));
			//
		}
//		else {
//			sheet = wb.getSheetAt(0);
//		}

		while (itr.hasNext()) {
				
			
			PurchaseOrderDetails supplierPruchaseOrderDetails = (PurchaseOrderDetails) itr
					.next();

			row = sheet.getRow(x) == null ? sheet.createRow(x) : sheet
					.getRow(x);
			HSSFCell cellqty = row.getCell(
					SASConstants.SupplierPurchaseOrderQuantityColMapping,
					Row.CREATE_NULL_AS_BLANK);
			// cellqty.setCellStyle(style);
			cellqty.setCellValue(parseNullDouble(supplierPruchaseOrderDetails
					.getQuantity()));
			cell = row.getCell(
					SASConstants.SupplierPurchaseOrderUnitColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(supplierPruchaseOrderDetails
					.getUnitOfMeasurement()));
			cell = row.getCell(
					SASConstants.SupplierPurchaseOrderItemCodeColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(supplierPruchaseOrderDetails
					.getItemCode()));
			cell = row.getCell(
					SASConstants.SupplierPurchaseOrderDescriptionColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullString(supplierPruchaseOrderDetails
					.getDescription()));
			cell = row.getCell(
					SASConstants.SupplierPurchaseOrderUnitPriceColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(supplierPruchaseOrderDetails
					.getUnitCost()));
			cell = row.getCell(
					SASConstants.SupplierPurchaseOrderAmountColMapping,
					Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue(parseNullDouble(supplierPruchaseOrderDetails
					.getAmount()));
			
			x++;

		}

	}

	public HSSFWorkbook printSupplierPurchaseOrderInExcel(String subModule,
			SupplierPurchaseOrder spo, ServletContext servletContext)
			throws IOException {
		String filePath = servletContext
				.getRealPath(SASConstants.REPORT_SUMMARY_TEMPLATE_PATH
						+ "supplierPurchaseOrder"
						+ SASConstants.REPORT_SUMMARY_TEMPLATE_TYPE);

		FileInputStream fileInputStream;
		fileInputStream = new FileInputStream(filePath);
		POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);

		printSupplierPurchaseOrder(wb, spo, subModule);

		return wb;
	}

	public String getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	public String getMinDate() {
		return minDate;
	}

	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	// SUMMARY GENERATION END

	// GENERATE ORDERING FORM TEMPLATE

	public void putCellValue(HSSFCell cell, String value) {
		cell.setCellValue(value);
	}
	
	public void putCellValue(HSSFCell cell, double value) {
		cell.setCellValue(value);
	}

	public HSSFColor prepareCellColor(HSSFWorkbook wb, int r, int g, int b) {
		HSSFPalette palette = wb.getCustomPalette();
		HSSFColor myColor = palette.findSimilarColor(r, g, b);
		return myColor;
	}

	public void putCellStyle(HSSFCell cell, HSSFCellStyle style) {

	}

	public HSSFCell getCurrentCell(HSSFRow row, int cellNum) {
		HSSFCell cell = row.getCell(cellNum);
		if (cell == null) {
			row.createCell(cellNum);
			return row.getCell(cellNum);
		}
		return cell;
	}

	public HSSFRow getRow(HSSFSheet sheet, int rownum) {
		HSSFRow row = sheet.getRow(rownum);
		if (row == null) {
			sheet.createRow(rownum);
			return sheet.getRow(rownum);
		}
		return row;
	}

}
