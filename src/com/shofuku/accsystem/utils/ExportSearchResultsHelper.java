package com.shofuku.accsystem.utils;

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
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.Session;

import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.BaseController;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.domain.customers.CustomerPurchaseOrder;
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.customers.DeliveryReceipt;
import com.shofuku.accsystem.domain.disbursements.CashPayment;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.disbursements.PettyCash;
import com.shofuku.accsystem.domain.inventory.Item;
import com.shofuku.accsystem.domain.receipts.OROthers;
import com.shofuku.accsystem.domain.receipts.ORSales;
import com.shofuku.accsystem.domain.suppliers.ReceivingReport;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;
import com.shofuku.accsystem.domain.suppliers.SupplierPurchaseOrder;

public class ExportSearchResultsHelper{

	private static final long serialVersionUID = -8557620697888911805L;

	private static final Logger logger = Logger
			.getLogger(ExportSearchResultsHelper.class);
	
	Map<String,Object> actionSession;
	BaseController manager;
	
	InputStream excelStream;
	String contentDisposition;
	POIUtil poiUtil = new POIUtil(actionSession);
	
	
	
	public ExportSearchResultsHelper(Map<String, Object> actionSession) {
		this.actionSession = actionSession;
	}
	String searchType;
	
	List<Object> searchResults; 
	List supplierList;
	List<ReceivingReport> rrList;
	
	public List<ReceivingReport> getRrList() {
		return rrList;
	}

	public void setRrList(List<ReceivingReport> rrList) {
		this.rrList = rrList;
	}

	public InputStream generateExportedXls() throws Exception {

		try {
			//GENERATE FILE
			excelStream = generateReportFromResults(); 
		}catch(Exception e){
			logger.error("ExportSearchResultsAction.execute() : null results" + e.toString());
			return null;
		}
		
		return excelStream;
	}

	private void generateHeaders(HSSFWorkbook wb) throws Exception {

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow currentRow = sheet.getRow(0);
		
		if(searchType.equalsIgnoreCase("ReceivingReport")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "RECEIVING REPORT");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "SUPPLIER NAME");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "PO NUMBER");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "RECEIVING REPORT NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "RECEIVING REPORT DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "RR TOTAL AMOUNT");
			
		}else if(searchType.equalsIgnoreCase("SupplierPurchaseOrder")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "SUPPLIER PURCHASE ORDER");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "SUPPLIER NAME");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "PURCHASE ORDER NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "PURCHASE ORDER DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "PO TOTAL AMOUNT");
			
		}else if(searchType.equalsIgnoreCase("SupplierInvoice")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "SUPPLIER INVOICE");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "SUPPLIER NAME");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "RR NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "INVOICE NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "INVOICE DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "INVOICE TOTAL AMOUNT");
			
		}else if(searchType.equalsIgnoreCase("CustomerPurchaseOrder")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "CUSTOMER PURCHASE ORDER");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "CUSTOMER NAME");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "PO NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "PO DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "TOTAL AMOUNT");
			
		}else if(searchType.equalsIgnoreCase("DeliveryReceipt")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "DELIVERY RECEIPT");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "CUSTOMER NAME");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "DELIVERY RECEIPT NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "DELIVERY RECEIPT DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "PO NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "TOTAL AMOUNT");
			
		}else if(searchType.equalsIgnoreCase("CustomerSalesInvoice")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "CUSTOMER INVOICE");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "CUSTOMER NAME");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "INVOICE NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "INVOICE DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "DR NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "TOTAL AMOUNT");
			
		}else if(searchType.equalsIgnoreCase("PettyCash")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "PETTY CASH");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "PAYEE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "VOUCHER NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "VOUCHER DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "TOTAL AMOUNT");
			
		}else if(searchType.equalsIgnoreCase("CashPayment")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "CASH PAYMENT");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "PAYEE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "VOUCHER NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "VOUCHER DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "TOTAL AMOUNT");
			
		}else if(searchType.equalsIgnoreCase("CheckPayment")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "CHECK PAYMENT");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "PAYEE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "VOUCHER NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "VOUCHER DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "INVOICE NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "TOTAL AMOUNT");
			
		}else if(searchType.equalsIgnoreCase("ORSales")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "OR SALES");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "RECEIVED FROM");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "OR NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "OR DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "TOTAL AMOUNT");
			
		}else if(searchType.equalsIgnoreCase("OROthers")){
			
			if(searchResults.size()<0) {
				throw new Exception();
			}
			
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "OR OTHERS");
			
			//last digit is based on how many columns the results have
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(1);
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "RECEIVED FROM");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "OR NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "OR DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "TOTAL AMOUNT");
			
		}
		
		
	}

	private InputStream generateReportFromResults() throws Exception {
		InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.SEARCH_RESULTS_TEMPLATE_ROOT);
		POIFSFileSystem fsFileSystem = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			fsFileSystem = new POIFSFileSystem(pdInputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
			populateWorkSheets(wb);
			wb.write(baos);

			
		} catch (IOException e) {
			logger.error("generateReportFromResults() : "+ e.toString());
			e.printStackTrace();
			return null;
		}
		return new ByteArrayInputStream(baos.toByteArray());
	}
	HSSFCellStyle titleStyle ;
	HSSFCellStyle itemListStyle;
	HSSFCellStyle headerListStyle;
	HSSFCellStyle noStyle;
	
	@SuppressWarnings("rawtypes")
	private void populateWorkSheets(HSSFWorkbook wb) throws Exception {
		int sheetCtr=0;
		int currentRow = 3;
		int currentColumn = 1;
		
		HSSFSheet sheet = wb.getSheetAt(sheetCtr);
		
		//Title style
		HSSFRow row = sheet.getRow(0);
		HSSFCell cell = row.getCell(12);
		titleStyle = cell.getCellStyle();
		
		//headeritem style
		row = sheet.getRow(1);
		cell = row.getCell(12);
		headerListStyle = cell.getCellStyle();
		
		//normal item style
		row = sheet.getRow(2);
		cell = row.getCell(12);
		itemListStyle = cell.getCellStyle();
		
		//no style
		row = sheet.getRow(3);
		cell = row.getCell(12);
		noStyle = cell.getCellStyle();
		
		//Generate Headers
		generateHeaders(wb);
		
		
		
		Iterator resultsIterator = searchResults.iterator();
		while(resultsIterator.hasNext()) {
			Object object = resultsIterator.next();
			insertItem(sheet,currentRow++,currentColumn,object);
		}
		
		
		//delete style template columns
		sheet = wb.getSheetAt(0);
		
		//subcategory style
		row = sheet.getRow(0);
		cell = row.createCell(12);
		cell.setCellValue("");
		cell.setCellStyle(noStyle);
		
		//normal item style
		row = sheet.getRow(1);
		cell = row.createCell(12);
		cell.setCellValue("");
		cell.setCellStyle(noStyle);
		
		//headeritem style
		row = sheet.getRow(2);
		cell = row.createCell(12);		
		cell.setCellValue("");
		cell.setCellStyle(noStyle);
		
		//no style
		row = sheet.getRow(3);
		cell = row.getCell(12);
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
			Object object) {
		
		if(searchType.equalsIgnoreCase("ReceivingReport")){
			//Cast object to the result type
			ReceivingReport rr = (ReceivingReport)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, rr.getReceivingReportNo());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, rr.getReceivingReportDate().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, rr.getSupplierPurchaseOrder().getSupplier().getSupplierName());
			

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, String.valueOf(rr.getSupplierPurchaseOrder().getSupplierPurchaseOrderId()));
			
			cell = poiUtil.getCurrentCell(row,4);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, String.valueOf(rr.getTotalAmount()));
			
			for(int x=0; x<=5;x++) {
				sheet.autoSizeColumn(x);
			}
			
		}else if(searchType.equalsIgnoreCase("SupplierPurchaseOrder")){
			//Cast object to the result type
			SupplierPurchaseOrder spo = (SupplierPurchaseOrder)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, spo.getSupplier().getSupplierName());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, spo.getSupplierPurchaseOrderId().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, spo.getPurchaseOrderDate().toString());

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, String.valueOf(spo.getTotalAmount()));
			
			for(int x=0; x<=5;x++) {
				sheet.autoSizeColumn(x);
			}
			
		}else if(searchType.equalsIgnoreCase("SupplierInvoice")){
			//Cast object to the result type
			SupplierInvoice sinv = (SupplierInvoice)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, sinv.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getSupplierName());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, sinv.getReceivingReport().getReceivingReportNo().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, sinv.getSupplierInvoiceNo().toString());

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, String.valueOf(sinv.getSupplierInvoiceDate()));
			
			cell = poiUtil.getCurrentCell(row,4);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, String.valueOf(sinv.getDebit1Amount()));
			
			for(int x=0; x<=5;x++) {
				sheet.autoSizeColumn(x);
			}
			
		}else if(searchType.equalsIgnoreCase("CustomerPurchaseOrder")){
			//Cast object to the result type
			CustomerPurchaseOrder cpo = (CustomerPurchaseOrder)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, cpo.getCustomer().getCustomerName());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, cpo.getCustomerPurchaseOrderId().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, cpo.getPurchaseOrderDate().toString());

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, String.valueOf(cpo.getTotalAmount()));
			
			for(int x=0; x<=5;x++) {
				sheet.autoSizeColumn(x);
			}
			
		}else if(searchType.equalsIgnoreCase("DeliveryReceipt")){
			//Cast object to the result type
			DeliveryReceipt dr = (DeliveryReceipt)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, dr.getCustomerPurchaseOrder().getCustomer().getCustomerName());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, dr.getDeliveryReceiptNo().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, dr.getDeliveryReceiptDate().toString());

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, dr.getCustomerPurchaseOrder().getCustomerPurchaseOrderId().toString());

			cell = poiUtil.getCurrentCell(row,4);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, String.valueOf(dr.getTotalAmount()));
			
			for(int x=0; x<=5;x++) {
				sheet.autoSizeColumn(x);
			}
			
		}else if(searchType.equalsIgnoreCase("CustomerSalesInvoice")){
			//Cast object to the result type
			CustomerSalesInvoice cinv = (CustomerSalesInvoice)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, cinv.getDeliveryReceipt().getCustomerPurchaseOrder().getCustomer().getCustomerName());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, cinv.getCustomerInvoiceNo().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, cinv.getCustomerInvoiceDate().toString());

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, cinv.getDeliveryReceipt().getDeliveryReceiptNo().toString());

			cell = poiUtil.getCurrentCell(row,4);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, String.valueOf(cinv.getTotalAmount()));
			
			for(int x=0; x<=5;x++) {
				sheet.autoSizeColumn(x);
			}
			
		}else if(searchType.equalsIgnoreCase("PettyCash")){
			//Cast object to the result type
			PettyCash pc = (PettyCash)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, pc.getPayee());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, pc.getPcVoucherNumber().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, pc.getPcVoucherDate().toString());

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, Double.valueOf(pc.getAmount()).toString());

			for(int x=0; x<=5;x++) {
				sheet.autoSizeColumn(x);
			}
			
		}else if(searchType.equalsIgnoreCase("CashPayment")){
			//Cast object to the result type
			CashPayment cp = (CashPayment)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, cp.getPayee());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, cp.getCashVoucherNumber().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, cp.getCashVoucherDate().toString());

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, Double.valueOf(cp.getAmount()).toString());

			for(int x=0; x<=5;x++) {
				sheet.autoSizeColumn(x);
			}
			
		}else if(searchType.equalsIgnoreCase("CheckPayment")){
			//Cast object to the result type
			CheckPayments chp = (CheckPayments)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, chp.getPayee());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, chp.getCheckVoucherNumber().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, chp.getCheckVoucherDate().toString());

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			if (chp.getInvoice() != null) {
				poiUtil.putCellValue(cell, chp.getInvoice().getSupplierInvoiceNo().toString());
			}else {
				poiUtil.putCellValue(cell, "");
			}

			cell = poiUtil.getCurrentCell(row,4);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, Double.valueOf(chp.getAmount()).toString());

			for(int x=0; x<=5;x++) {
				sheet.autoSizeColumn(x);
			}
		
		}else if(searchType.equalsIgnoreCase("ORSales")){
			//Cast object to the result type
			ORSales orSales = (ORSales)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, orSales.getReceivedFrom());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, orSales.getOrNumber().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, orSales.getOrDate().toString());

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, Double.valueOf(orSales.getAmount()).toString());

			for(int x=0; x<=4;x++) {
				sheet.autoSizeColumn(x);
			}
		
		}else if(searchType.equalsIgnoreCase("OROthers")){
			//Cast object to the result type
			OROthers orOthers = (OROthers)object;
			
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, orOthers.getReceivedFrom());
		
			cell = poiUtil.getCurrentCell(row,1);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, orOthers.getOrNumber().toString());
			
			cell = poiUtil.getCurrentCell(row,2);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, orOthers.getOrDate().toString());

			cell = poiUtil.getCurrentCell(row,3);
			cell.setCellStyle(itemListStyle);
			poiUtil.putCellValue(cell, Double.valueOf(orOthers.getAmount()).toString());

			for(int x=0; x<=4;x++) {
				sheet.autoSizeColumn(x);
			}
		
		}
	}


	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getContentDisposition() {
		
		return "filename=\""+ searchType +"-searchResults\"";
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	public List<Object> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<Object> searchResults) {
		this.searchResults = searchResults;
	}
	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public List getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(List supplierList) {
		this.supplierList = supplierList;
	}
}
