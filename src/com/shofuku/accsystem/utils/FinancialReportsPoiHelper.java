package com.shofuku.accsystem.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
import com.shofuku.accsystem.domain.customers.CustomerSalesInvoice;
import com.shofuku.accsystem.domain.disbursements.CheckPayments;
import com.shofuku.accsystem.domain.financials.JournalEntryProfile;
import com.shofuku.accsystem.domain.financials.Transaction;
import com.shofuku.accsystem.domain.financials.Vat;
import com.shofuku.accsystem.domain.inventory.Item;
import com.shofuku.accsystem.domain.suppliers.SupplierInvoice;

public class FinancialReportsPoiHelper{

	Map<String,Object> actionSession;
	BaseController manager;
	
	private void initializeController() {
		accountEntryManager = (AccountEntryManager) actionSession.get("accountEntryManager");
		financialsManager = (FinancialsManager) actionSession.get("financialsManager");
	}
	
	//Controllers
	FinancialsManager financialsManager;
	AccountEntryManager accountEntryManager;
	
	//poi variables
	
	POIFSFileSystem fsFileSystem = null;
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	HSSFCellStyle titleStyle ;
	HSSFCellStyle subTitleStyle ;
	HSSFCellStyle itemListStyle;
	HSSFCellStyle headerListStyle;
	HSSFCellStyle noStyle;
	
	//Map for supplier invoice on vat report
	Map checkVoucherMap;

	private static final Logger logger = Logger
			.getLogger(FinancialReportsPoiHelper.class);
	
	InputStream excelStream;
	String contentDisposition;
	POIUtil poiUtil = new POIUtil(actionSession);
	DateFormatHelper dfh= new DateFormatHelper();
	
	String reportType;
	
	//for ledger of accounts
	boolean isSupplier=false;
	

	
	
	public FinancialReportsPoiHelper(Map<String, Object> actionSession) {
		this.actionSession = actionSession;
	}



	public InputStream generateExportedXls() throws Exception {

		try {
			//GENERATE FILE
			//excelStream = generateReportFromResults(); 
		}catch(Exception e){
			logger.error("ExportSearchResultsAction.execute() : null results" + e.toString());
			return null;
		}
		
		return excelStream;
	}
	
	
	
	/* METHODS THAT WILL GENERATE DIFFERENT FINANCIAL REPORTS*/
	
	
	private void parseTemplate(HSSFSheet sheet,int currentRow,
			List trialBalanceEntries) {
		
		HashMap trialBalanceMap = convertTrialBalanceToMap(trialBalanceEntries);
		
		HSSFRow row=poiUtil.getRow(sheet, currentRow++);
		int currentColumnIndex = determineColumnStart();
		int startingColumnForVariables = currentColumnIndex;
		HSSFCell cell = poiUtil.getCurrentCell(row,currentColumnIndex++);
		
		
		String cellString = "";
		//this will be the value to print
				
		//loop through all the rows
		boolean eof=false;
		//loop through the columns until end
		while (!eof){
			cellString = cell.getStringCellValue();
			if(cellString==null || cellString.equalsIgnoreCase("")) {
				row=poiUtil.getRow(sheet, currentRow++);
				cell = poiUtil.getCurrentCell(row,startingColumnForVariables);
				currentColumnIndex = startingColumnForVariables+1;
				continue;
			}else if(cell.getStringCellValue().equalsIgnoreCase("{END}")) {
				deleteVariables(sheet, row.getRowNum(),currentColumnIndex-1,  currentColumnIndex);
				continue;
			}else if(cell.getStringCellValue().equalsIgnoreCase("{EOF}")) {
				eof=true;
				deleteVariables(sheet, row.getRowNum(),0,  currentColumnIndex);
				continue;
			}
			boolean hasVariablesRemaining = true;
			double total = 0;
			while(hasVariablesRemaining) {
				cellString = cell.getStringCellValue();
				String operation= "";
				int charPtr=0;
				char x = cellString.charAt(charPtr);
				while(charPtr<=cellString.length()) {
						operation = getVariable(cellString,charPtr++);
						String variable= "";
						if(operation.equalsIgnoreCase("+")) {
							variable = getVariable(cellString,charPtr+=2);
							total = add(total,variable,trialBalanceMap);break;
						}else if(operation.equalsIgnoreCase("-")) {
							variable = getVariable(cellString,charPtr+=2);
							total = subtract(total,variable,trialBalanceMap);break;
						}else if(operation.indexOf("parent")>-1) {
							variable = getVariable(cellString,charPtr+=8);
							total = addAllChildren(operation,total,variable,trialBalanceMap);break;
						}else if(operation.indexOf("var")>-1) {
							variable = getVariable(cellString,charPtr+=5);
							total = addExistingCellValue(operation,total,variable,sheet);break;
						}else {
							total = add(total,variable,trialBalanceMap);break;
						}
				}
				cell = poiUtil.getCurrentCell(row,currentColumnIndex++);
				if(cell!=null) {
					if(cell.getStringCellValue().equalsIgnoreCase("{END}")) {
						hasVariablesRemaining=false; 
					}
				}else {	
					hasVariablesRemaining=false;
				}
			}
			if(reportType.equalsIgnoreCase("04")) {
				writeTotal(total, sheet, row.getRowNum(), SASConstants.BALANCE_SHEET_VALUES_COLUMN);
				deleteVariables(sheet, row.getRowNum(),SASConstants.BALANCE_SHEET_VARIABLES_COLUMN_START,  currentColumnIndex);
			}else if(reportType.equalsIgnoreCase("03")) {
				writeTotal(total, sheet, row.getRowNum(), SASConstants.INCOME_STATEMENT_VALUES_COLUMN);
				deleteVariables(sheet, row.getRowNum(),SASConstants.INCOME_STATEMENT_VARIABLES_COLUMN_START,  currentColumnIndex);
			}else if(reportType.equalsIgnoreCase("05")) {
				writeTotal(total, sheet, row.getRowNum(), SASConstants.COGM_VALUES_COLUMN);
				deleteVariables(sheet, row.getRowNum(),SASConstants.COGM_VARIABLES_COLUMN_START,  currentColumnIndex);
			}else if(reportType.equalsIgnoreCase("06")) {
				writeTotal(total, sheet, row.getRowNum(), SASConstants.COGS_VALUES_COLUMN);
				deleteVariables(sheet, row.getRowNum(),SASConstants.COGS_VARIABLES_COLUMN_START,  currentColumnIndex);
			}else if(reportType.equalsIgnoreCase("07")) {
				writeTotal(total, sheet, row.getRowNum(), SASConstants.STOCKHOLDERS_EQUITY_VALUES_COLUMN);
				deleteVariables(sheet, row.getRowNum(),SASConstants.STOCKHOLDERS_EQUITY_VARIABLES_COLUMN_START,  currentColumnIndex);
			}
			currentColumnIndex= startingColumnForVariables;
			row=poiUtil.getRow(sheet, currentRow++);
			if(row!=null) {
				cell = poiUtil.getCurrentCell(row,currentColumnIndex++);
				if(cell!=null) {
					if(cell.getStringCellValue().equalsIgnoreCase("{EOF}")) {
						eof=true;
						deleteVariables(sheet, row.getRowNum(),0,  currentColumnIndex);
					}else {
						hasVariablesRemaining=true;
					}
				}
			}
		}
	}

	private double addExistingCellValue(String operation, double total,
			String variable, HSSFSheet sheet) {
		char action = operation.charAt(0);
		char cellRow = String.valueOf(variable.charAt(0)).toUpperCase().charAt(0);
		
		int colNum="ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(cellRow);
		String rowNumberStr= variable.substring(1, variable.length());
		int rowNum = Integer.valueOf(rowNumberStr)-1;
		
		HSSFRow row = sheet.getRow(rowNum);
		HSSFCell cell = poiUtil.getCurrentCell(row, colNum);
		
		if(action=='+') {
			//right now it handles a-z
			total = total + cell.getNumericCellValue();
		}else {
			total = total - cell.getNumericCellValue();
		}
		return total;
	}



	private void deleteVariables(HSSFSheet sheet, int rowNum,
			int startColumn,int lastColumnWithVariable) {
		int x = startColumn;
		while(x<lastColumnWithVariable) {
		// trial balance, vat report has subtitle has 5 rows
		HSSFRow	row = sheet.getRow(rowNum);
		HSSFCell	cell = row.getCell(x++);
		clearCell(cell);
		}
	}



	/*
	 * Determines where the variables start as per each report template
	 * */
	private int determineColumnStart() {
		if(reportType.equalsIgnoreCase("04")) {
			return SASConstants.BALANCE_SHEET_VARIABLES_COLUMN_START;
		}else if(reportType.equalsIgnoreCase("03")) {
			return SASConstants.INCOME_STATEMENT_VARIABLES_COLUMN_START;
		}else if(reportType.equalsIgnoreCase("05")) {
			return SASConstants.COGM_VARIABLES_COLUMN_START;
		}else if(reportType.equalsIgnoreCase("06")) {
			return SASConstants.COGS_VARIABLES_COLUMN_START;
		}else if(reportType.equalsIgnoreCase("07")) {
			return SASConstants.STOCKHOLDERS_EQUITY_VARIABLES_COLUMN_START;
		}
		else {
		}
		return 0;
	}



	private String getVariable(String wholeString,int currentPtr) {
		String variable="";
		char x = wholeString.charAt(++currentPtr);
		while(x!='}') {
			x = wholeString.charAt(currentPtr++);
			if(x!='}') {
				variable = variable + x;
			}else {
				break;
			}
		}
		return variable;
	}
	
	private double add(double total, String variable, HashMap trialBalanceMap) {
		TrialBalanceEntry entry= (TrialBalanceEntry) trialBalanceMap.get(variable); 
		double currentValue = 0;
		if(entry!=null) {
			currentValue = entry.getDrAmount() + entry.getCrAmount();
		}else {
		}
		return total + currentValue;
	}
	private double subtract(double total, String variable, HashMap trialBalanceMap) {
		TrialBalanceEntry entry= (TrialBalanceEntry) trialBalanceMap.get(variable); 
		double currentValue = 0;
		if(entry!=null) {
			currentValue = entry.getDrAmount() + entry.getCrAmount();
		}else {
		}
		return total - currentValue;		
	}
	private double addAllChildren(String operation, double total, String variable, HashMap trialBalanceMap) {
		List listOfTrialBalanceEntries = new ArrayList<TrialBalanceEntry>();
		listOfTrialBalanceEntries.addAll(trialBalanceMap.values());
		Iterator itr = listOfTrialBalanceEntries.iterator();
		while(itr.hasNext()) {
			TrialBalanceEntry entry = (TrialBalanceEntry) itr.next();
			double currentValue = 0;
			if(entry!=null) {
				if(variable.equalsIgnoreCase(entry.getAccountParentCode())) {
					currentValue = entry.getDrAmount() + entry.getCrAmount();
					if(operation.indexOf("+")>-1) {
						total = total + currentValue;
					}else if(operation.indexOf("-")>-1) {
						total = total - currentValue;
					}	
				}
			}else {
			}
		}
		return total;
	}

	private void writeTotal(double total,HSSFSheet sheet, int currentRow, int valueColumn) {
		HSSFRow row=poiUtil.getRow(sheet, currentRow);
		HSSFCell cell = poiUtil.getCurrentCell(row,valueColumn);
		cell.setCellStyle(cell.getCellStyle());
		poiUtil.putCellValue(cell, parseNullDouble(total));
		
		cell = poiUtil.getCurrentCell(row,valueColumn-1);
		cell.setCellStyle(noStyle);
		poiUtil.putCellValue(cell, parseNullString("Php"));
	}



	private double parseCellVariable(double total,String formula, HashMap trialBalanceEntries) {
		int charPtr=0;
		char x = formula.charAt(charPtr);
		String operation= "";
		while(charPtr<=formula.length()) {
			if(x=='{') {
				while(x!='}') {
					x = formula.charAt(charPtr++);
					operation = operation+x;
				}
				if(operation.equalsIgnoreCase("+")) {
					
				}
			}
		}
		return 0;
	}



	private HashMap convertTrialBalanceToMap(List trialBalanceEntries) {
		// TODO Auto-generated method stub
		Iterator itr = trialBalanceEntries.iterator();
		HashMap map = new HashMap<String, TrialBalanceEntry>();
		while(itr.hasNext()) {
			TrialBalanceEntry entry = (TrialBalanceEntry)itr.next();
			map.put(entry.getAccountCode(), entry);
		}
		return map;
	}



	public InputStream generateVatReport(String dateFrom,String dateTo,Session session) throws Exception {
		
		
		Date startDate;
		Date endDate;
		
		startDate = dfh.parseStringToTime(dateFrom);
		endDate = dfh.parseStringToTime(dateTo);
		
		initializeController();
		
		//get data
		List vatDetailsList = financialsManager.getVatDetailsBetweenDates(startDate, endDate, Vat.class.getName(), "orDate", session, "tinNumber");
		
				
		//TODO: make query for check vouchers		
		//List ledgerResultsCheckVouchers = financialsMgr.getSupplierInvoiceFromSupplierIDList(supplierList, session);
				
				
		//load template
		InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.VAT_REPORT_TEMPLATE);
		
		try {
			fsFileSystem = new POIFSFileSystem(pdInputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
			
			//load styles from pre styled cells
			loadStyles(wb);		
			
			//writer headers
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowPtr=0;
			HSSFRow currentRow = sheet.getRow(rowPtr++);
			int maxCols = 7;
			int startRow=0;
			int endRow=0;
			int startCol=0;

			//write title
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "Vat Report");
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write sub title
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			poiUtil.putCellValue(cell, "As of Dates: ");
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write date range
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			poiUtil.putCellValue(cell, startDate + " - " + endDate );
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			int cellPtr = 0;
			currentRow = sheet.getRow(rowPtr++);
			
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "INVOICE/OR DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "SUPPLIER/PARTICULARS");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "INVOICE/OR NO");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "ADDRESS");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "TIN");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "AMOUNT");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "VATABLE AMOUNT");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "VAT AMOUNT");
			
			
			/*
			 * get supplier invoice details for reference numbers pertaining to check vouchers
			 */
			checkVoucherMap = new HashMap();
			List checkVoucherList = financialsManager.listAlphabeticalAscByParameter(
					CheckPayments.class, "checkVoucherNumber",session);
			
			Iterator itr = checkVoucherList.iterator();
			if(checkVoucherList!=null) {
				while(itr.hasNext()) {
					CheckPayments checkVoucher = (CheckPayments) itr.next();
					if(checkVoucher.getInvoice().getSupplierInvoiceNo()!="")
					checkVoucherMap.put(checkVoucher.getCheckVoucherNumber(), checkVoucher.getInvoice().getSupplierInvoiceNo());
				}
			}
			
			//populate table
			insertItem(sheet, rowPtr++, 5, vatDetailsList);
			
			
			//delete cell styles
			deleteStyleCells(wb);

			//adjust column sizes for better presentation
			autosizeColumns(sheet, maxCols);
			
			wb.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public InputStream generateTrialBalanceReport(String dateFrom,String dateTo,Session session) throws Exception {
		
		Date startDate;
		Date endDate;
		
		startDate = dfh.parseStringToTime(dateFrom);
		endDate = dfh.parseStringToTime(dateTo);
		
		
		//get data
		List trialBalanceEntries = generateTrialBalanceEntries(startDate,endDate,session);
		
		//load template
		InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.TRIAL_BALANCE_REPORT_TEMPLATE);
		
		try {
			fsFileSystem = new POIFSFileSystem(pdInputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
			
			//load styles from pre styled cells
			loadStyles(wb);		
			
			
			//writer headers
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowPtr=0;
			HSSFRow currentRow = sheet.getRow(rowPtr++);
			int maxCols = 3;
			int startRow=0;
			int endRow=0;
			int startCol=0;
			
			//write title
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "Trial Balance");
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write sub title
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			poiUtil.putCellValue(cell, "As of: ");
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write date range
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			poiUtil.putCellValue(cell, startDate + " - " + endDate );
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			currentRow = sheet.getRow(rowPtr++);
			
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,startCol++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "ACCOUNT TITLE");
			
			cell = poiUtil.getCurrentCell(currentRow,startCol++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "ACCOUNT TYPE");
			
			cell = poiUtil.getCurrentCell(currentRow,startCol++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "DR");
			
			cell = poiUtil.getCurrentCell(currentRow,startCol++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "CR");
			
			
			//populate table
			insertItem(sheet, rowPtr++, 5, trialBalanceEntries);
			
			
			//delete cell styles
			deleteStyleCells(wb);

			//adjust column sizes for better presentation
			autosizeColumns(sheet, maxCols);
			
			wb.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}


	public InputStream generateLedgerAccountsReport(String dateFrom, String dateTo,List supplierList,List customerList, Session session) throws Exception {
		
		Date startDate;
		Date endDate;
		
		startDate = dfh.parseStringToTime(dateFrom);
		endDate = dfh.parseStringToTime(dateTo);
		
		initializeController();
		
		//get data
		//TODO: use date
		
		List ledgerResultsInvoices =null;
		if(supplierList!=null && supplierList.size() >0) {
			isSupplier=true;
			ledgerResultsInvoices = financialsManager.getSupplierInvoiceFromSupplierIDList(supplierList, session);			
		}else if(customerList!=null && customerList.size() >0) {
			ledgerResultsInvoices = financialsManager.getCustomerInvoiceFromCustomerIDList(customerList, session);
		}else {
			//selected none
			ledgerResultsInvoices = new ArrayList<>();
		}

		
		//TODO: make query for check vouchers		
		//List ledgerResultsCheckVouchers = financialsMgr.getSupplierInvoiceFromSupplierIDList(supplierList, session);
		
		
		//load template
		InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.LEDGER_REPORTS_TEMPLATE);
	
		
		try {
			fsFileSystem = new POIFSFileSystem(pdInputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
			
			//load styles from pre styled cells
			loadStyles(wb);		
			
			//writer headers
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowPtr=0;
			HSSFRow currentRow = sheet.getRow(rowPtr++);
			int maxCols = 5;
			int startRow=0;
			int endRow=0;
			int startCol=0;
			
			//write title
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			poiUtil.putCellValue(cell, "Ledger of Accounts");
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write date range
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			poiUtil.putCellValue(cell, startDate + " - " + endDate );
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			currentRow = sheet.getRow(startRow);
			
			int cellPtr = 0;
			currentRow = sheet.getRow(rowPtr++);
			
			//INSERT COLUMN HEADERS HERE
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "NAME");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "INVOICE NUMBER");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "INVOICE DATE");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "AMOUNT");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "VAT AMOUNT");
			
			cell = poiUtil.getCurrentCell(currentRow,cellPtr++);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "VATTABLE AMOUNT");
			
			//populate table
			insertItem(sheet, rowPtr, 5, ledgerResultsInvoices);
			
			
			//delete cell styles
			deleteStyleCells(wb);
			
			
			//adjust column sizes for better presentation
			autosizeColumns(sheet, maxCols);
			
			wb.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	/*
	 * CELL STYLE FORMATTING
	 * */
	
	private void applyStyleToMergedRegion(int startRow, int endRow,
			int startCol, int maxCols, HSSFWorkbook wb,HSSFCellStyle cellStyle ) {
		HSSFSheet sheet = wb.getSheetAt(0);
		
		for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
			HSSFRow currentRow = sheet.getRow(rowNum);
			if (currentRow == null) {
				sheet.createRow(rowNum);
				logger.trace("while check row " + rowNum + " was created");
			}
			for (int colNum = startCol; colNum <= maxCols; colNum++) {
				HSSFCell currentCell = currentRow.getCell(colNum);
				if (currentCell == null) {
					currentCell = currentRow.createCell(colNum);
					logger.trace("while check cell " + rowNum + ":" + colNum
							+ " was created");
				}
				currentCell.setCellStyle(cellStyle);
			}
		}
	}

	private void loadStyles(HSSFWorkbook wb) {
		int sheetCtr = 0;
		int currentRow = 0;
		int currentColumn = 12;
		HSSFSheet sheet = wb.getSheetAt(sheetCtr);
		
		// Title style
		HSSFRow row = sheet.getRow(currentRow++);
		HSSFCell cell = row.getCell(currentColumn);
		titleStyle = cell.getCellStyle();

		// for those reports with subtitle
		if (reportType.equalsIgnoreCase("02")
				|| reportType.equalsIgnoreCase("09")
				|| reportType.equalsIgnoreCase("01")
				|| reportType.equalsIgnoreCase("03")
				|| reportType.equalsIgnoreCase("04")
				|| reportType.equalsIgnoreCase("05")
				|| reportType.equalsIgnoreCase("06")
				|| reportType.equalsIgnoreCase("07")
				|| reportType.equalsIgnoreCase("08")){
			row = sheet.getRow(currentRow++);
			cell = row.getCell(currentColumn);
			subTitleStyle = cell.getCellStyle();
		}

		// headeritem style
		row = sheet.getRow(currentRow++);
		cell = row.getCell(currentColumn);
		headerListStyle = cell.getCellStyle();

		// normal item style
		row = sheet.getRow(currentRow++);
		cell = row.getCell(currentColumn);
		itemListStyle = cell.getCellStyle();

		// no style
		row = sheet.getRow(currentRow++);
		cell = row.getCell(currentColumn);
		noStyle = cell.getCellStyle();
	}

	private void deleteStyleCells(HSSFWorkbook wb) {
		//delete style template columns
		HSSFSheet sheet = wb.getSheetAt(0);
		
		//subcategory style
		HSSFRow row = sheet.getRow(0);
		HSSFCell cell = row.createCell(12);
		clearCell(cell);
		
		//normal item style
		row = sheet.getRow(1);
		cell = row.createCell(12);
		clearCell(cell);
		
		//headeritem style
		row = sheet.getRow(2);
		cell = row.createCell(12);		
		clearCell(cell);
		
		//no style
		row = sheet.getRow(3);
		cell = row.getCell(12);
		clearCell(cell);
		
		//trial balance, vat report has subtitle has 5 rows
		if(reportType.equalsIgnoreCase("02")||reportType.equalsIgnoreCase("09")|| reportType.equalsIgnoreCase("04")
				|| reportType.equalsIgnoreCase("03")|| reportType.equalsIgnoreCase("05")|| reportType.equalsIgnoreCase("06")
				|| reportType.equalsIgnoreCase("07")) {
			row = sheet.getRow(4);
			cell = row.getCell(12);
			clearCell(cell);
			
		}
	}
	
	
	
	private void clearCell(HSSFCell cell) {
		if(cell!=null) {
			cell.setCellValue("");
			cell.setCellStyle(noStyle);
		}
	}
	
	private void autosizeColumns(HSSFSheet sheet, int maxCols) {
		for(int x=0; x<=maxCols;x++) {
			sheet.autoSizeColumn(x);
		}
	}

	/*
	 * END CELL STYLE FORMATTING
	 * */
	
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

	
	/*
	 * DATA POPULATION
	 * */
	private void insertItem(HSSFSheet sheet, int currentRow, int currentColumnsUsed,
			Object object) {
		
		if(reportType.equalsIgnoreCase("01")){
			if(isSupplier) {
				//Cast object to the result type
				List invoiceList = (List)object;
				Iterator itr = invoiceList.iterator();
				HSSFRow row=poiUtil.getRow(sheet, currentRow);
				while(itr.hasNext()) {
					SupplierInvoice supInv = (SupplierInvoice )itr.next();
					
					
					HSSFCell cell = poiUtil.getCurrentCell(row,0);
					cell.setCellStyle(itemListStyle);
					poiUtil.putCellValue(cell,parseNullString(supInv.getReceivingReport().getSupplierPurchaseOrder().getSupplier().getSupplierName()));
					
					
					cell = poiUtil.getCurrentCell(row,1);
					cell.setCellStyle(itemListStyle);
					poiUtil.putCellValue(cell,parseNullString(supInv.getSupplierInvoiceNo()));
					
					cell = poiUtil.getCurrentCell(row,2);
					cell.setCellStyle(itemListStyle);
					cell.setCellValue(dfh.parseDateToString((Timestamp) supInv.getSupplierInvoiceDate()));
					
	
					cell = poiUtil.getCurrentCell(row,3);
					cell.setCellStyle(itemListStyle);
					poiUtil.putCellValue(cell, parseNullDouble(supInv.getDebit1Amount()));
					
					cell = poiUtil.getCurrentCell(row,4);
					cell.setCellStyle(itemListStyle);
					poiUtil.putCellValue(cell, parseNullDouble(supInv.getVatDetails().getVatAmount()));
					
					cell = poiUtil.getCurrentCell(row,5);
					cell.setCellStyle(itemListStyle);
					poiUtil.putCellValue(cell, parseNullDouble(supInv.getVatDetails().getVattableAmount()));
					
					row=sheet.createRow(row.getRowNum()+1);
				}
			}else {
				//Cast object to the result type
				List invoiceList = (List)object;
				Iterator itr = invoiceList.iterator();
				HSSFRow row=poiUtil.getRow(sheet, currentRow);
				while(itr.hasNext()) {
					CustomerSalesInvoice custInvoice = (CustomerSalesInvoice )itr.next();
					
					
					HSSFCell cell = poiUtil.getCurrentCell(row,0);
					cell.setCellStyle(itemListStyle);
					poiUtil.putCellValue(cell,parseNullString(custInvoice.getDeliveryReceipt().getCustomerPurchaseOrder().getCustomer().getContactName()));
					
					
					cell = poiUtil.getCurrentCell(row,1);
					cell.setCellStyle(itemListStyle);
					poiUtil.putCellValue(cell,parseNullString(custInvoice.getCustomerInvoiceNo()));
					
					cell = poiUtil.getCurrentCell(row,2);
					cell.setCellStyle(itemListStyle);
					cell.setCellValue(dfh.parseDateToString((Timestamp) custInvoice.getCustomerInvoiceDate()));
					
	
					cell = poiUtil.getCurrentCell(row,3);
					cell.setCellStyle(itemListStyle);
					poiUtil.putCellValue(cell, parseNullDouble(custInvoice.getTotalSales()));
					
					cell = poiUtil.getCurrentCell(row,4);
					cell.setCellStyle(itemListStyle);
					poiUtil.putCellValue(cell, parseNullDouble(custInvoice.getVatDetails().getVatAmount()));
					
					cell = poiUtil.getCurrentCell(row,5);
					cell.setCellStyle(itemListStyle);
					poiUtil.putCellValue(cell, parseNullDouble(custInvoice.getVatDetails().getVattableAmount()));
					
					row=sheet.createRow(row.getRowNum()+1);
				}
			}
		}else if(reportType.equalsIgnoreCase("08")){
			//Cast object to the result type
			List journalEntries = (List)object;
			Iterator itr = journalEntries.iterator();
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			
			//get modules available
			HashMap<String, ArrayList<JournalEntryProfile>> sortedEntries = new HashMap<String, ArrayList<JournalEntryProfile>>();
			while(itr.hasNext()) {
				JournalEntryProfile jep = (JournalEntryProfile )itr.next();
				if(sortedEntries.get(jep.getModule())==null) {
					ArrayList<JournalEntryProfile> list = new ArrayList<JournalEntryProfile>();
					list.add(jep);
					sortedEntries.put(jep.getModule(), list);
				}else {
					ArrayList<JournalEntryProfile> list = sortedEntries.get(jep.getModule());
					list.add(jep);
					sortedEntries.put(jep.getModule(), list);
				}
			}
			
			//get unique modules here and assign to modules
			List modules = new ArrayList<>();
			modules.add(sortedEntries.keySet());
			Iterator moduleItr = modules.iterator();
			
			
			
			while(itr.hasNext()) {
				String module = (String)itr.next();
				List journalEntriesPerModule = sortedEntries.get(module);
				Iterator entriesItr = journalEntriesPerModule.iterator();
				
				//write module name
				HSSFCell cell = poiUtil.getCurrentCell(row,0);
				poiUtil.putCellValue(cell, module);
				applyStyleToMergedRegion(currentRow,currentRow,0, 8,sheet.getWorkbook(),titleStyle);
				sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow++, 0, 8));
				
				while(entriesItr.hasNext()) {
					JournalEntryProfile jep = (JournalEntryProfile) entriesItr.next();
					
					row = sheet.getRow(currentRow);
					
					//if debit name
					cell = poiUtil.getCurrentCell(row,1);
					cell.setCellValue(jep.getEntryName());
					
					//if credit name
					cell = poiUtil.getCurrentCell(row,2);
					cell.setCellValue(jep.getEntryName());
					
					//if debit value
					cell = poiUtil.getCurrentCell(row,4);
					cell.setCellValue(jep.getAmount());
					
					//if credit value
					cell = poiUtil.getCurrentCell(row,6);
					cell.setCellValue(jep.getAmount());
					
					currentRow++;
				}
			}
			
		}else if(reportType.equalsIgnoreCase("09")){
			//Cast object to the result type
			List invoiceList = (List)object;
			Iterator itr = invoiceList.iterator();
			HSSFRow row=poiUtil.getRow(sheet, currentRow);
			
			while(itr.hasNext()) {
				Vat vatDetails = (Vat)itr.next();
				
				
				HSSFCell cell = poiUtil.getCurrentCell(row,0);
				cell.setCellStyle(itemListStyle);
				poiUtil.putCellValue(cell,dfh.parseDateToString(vatDetails.getOrDate()));
				
				cell = poiUtil.getCurrentCell(row,1);
				cell.setCellStyle(itemListStyle);
				poiUtil.putCellValue(cell,parseNullString(vatDetails.getPayee()));
				
				cell = poiUtil.getCurrentCell(row,2);
				cell.setCellStyle(itemListStyle);
				if(vatDetails.getVatReferenceNo().indexOf("CV-")>-1) {
					poiUtil.putCellValue(cell,parseNullString((String)checkVoucherMap.get(vatDetails.getVatReferenceNo())));
				}else {
					poiUtil.putCellValue(cell,parseNullString(vatDetails.getVatReferenceNo()));
				}
				
				cell = poiUtil.getCurrentCell(row,3);
				cell.setCellStyle(itemListStyle);
				poiUtil.putCellValue(cell,parseNullString(vatDetails.getAddress()));
				
				cell = poiUtil.getCurrentCell(row,4);
				cell.setCellStyle(itemListStyle);
				poiUtil.putCellValue(cell,parseNullString(vatDetails.getTinNumber()));
				
				cell = poiUtil.getCurrentCell(row,5);
				cell.setCellStyle(itemListStyle);
				poiUtil.putCellValue(cell,parseNullDouble(vatDetails.getAmount()));
				
				cell = poiUtil.getCurrentCell(row,6);
				cell.setCellStyle(itemListStyle);
				poiUtil.putCellValue(cell,parseNullDouble(vatDetails.getVattableAmount()));
				
				cell = poiUtil.getCurrentCell(row,7);
				cell.setCellStyle(itemListStyle);
				cell.setCellValue(parseNullDouble(vatDetails.getVatAmount()));
				
				row=sheet.createRow(row.getRowNum()+1);
			}
		} else if (reportType.equalsIgnoreCase("02")) {
			// Cast object to the result type
			List invoiceList = (List) object;
			Iterator itr = invoiceList.iterator();
			HSSFRow row = poiUtil.getRow(sheet, currentRow);
			double drTotal = 0;
			double crTotal = 0;
			while (itr.hasNext()) {
				TrialBalanceEntry entry = (TrialBalanceEntry) itr.next();

				HSSFCell cell = poiUtil.getCurrentCell(row, 0);
				cell.setCellStyle(itemListStyle);
				poiUtil.putCellValue(cell,parseNullString(entry.getAccountTitle()));

				cell = poiUtil.getCurrentCell(row, 1);
				cell.setCellStyle(itemListStyle);
				poiUtil.putCellValue(cell,parseNullString(entry.getAccountType()));

				cell = poiUtil.getCurrentCell(row, 2);
				cell.setCellStyle(itemListStyle);
				poiUtil.putCellValue(cell, parseNullDouble(entry.getDrAmount()));
				drTotal=drTotal+entry.getDrAmount();

				cell = poiUtil.getCurrentCell(row, 3);
				cell.setCellStyle(itemListStyle);
				poiUtil.putCellValue(cell, parseNullDouble(entry.getCrAmount()));
				crTotal=crTotal+entry.getCrAmount();

				row = sheet.createRow(row.getRowNum() + 1);
			}
			
			//write date range
			HSSFCell cell = poiUtil.getCurrentCell(row,0);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, "TOTALS: " );
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(row.getRowNum(),row.getRowNum(),0, 1,sheet.getWorkbook(),headerListStyle);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),row.getRowNum(), 0, 1));
			
			 cell = poiUtil.getCurrentCell(row, 2);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, parseNullDouble(drTotal));

			cell = poiUtil.getCurrentCell(row, 3);
			cell.setCellStyle(headerListStyle);
			poiUtil.putCellValue(cell, parseNullDouble(crTotal));
		}
		for(int x=0; x<=currentColumnsUsed;x++) {
			sheet.autoSizeColumn(x);
		}
	}
	
	public InputStream generateJournalEntries(String dateFrom, String dateTo,
			Session session) {
		
			Date startDate;
			Date endDate;
			
			initializeController();
			
			startDate = dfh.parseStringToTime(dateFrom);
			endDate = dfh.parseStringToTime(dateTo);
			
			//get data
			//0001
			//00001
			List journalEntries = new ArrayList();
			JournalEntryProfile jep = new JournalEntryProfile();
			jep = (JournalEntryProfile) accountEntryManager.listByParameter(JournalEntryProfile.class, "entryNo", "0001", session).get(0);
			journalEntries.add(jep);
			jep = (JournalEntryProfile) accountEntryManager.listByParameter(JournalEntryProfile.class, "entryNo", "00001", session).get(0);
			journalEntries.add(jep);
			
//			List journalEntries = generateTrialBalanceEntries(startDate,endDate,session);
					
			//load template
			InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.JOURNAL_ENTRIES_TEMPLATE);
			
			
			try {
				fsFileSystem = new POIFSFileSystem(pdInputStream);
				HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
				
				//load styles from pre styled cells
				loadStyles(wb);
				
				//writer headers
				HSSFSheet sheet = wb.getSheetAt(0);
				int rowPtr=0;
				HSSFRow currentRow = sheet.getRow(rowPtr++);
				int maxCols = 6;
				int startRow=0;
				int endRow=0;
				int startCol=0;
				
				//write title
				HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
				cell.setCellStyle(titleStyle);
				poiUtil.putCellValue(cell, "Journal Entries");
				//last digit is based on how many columns the results have
				applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,titleStyle);
				sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
				
				//write date range
				currentRow = sheet.getRow(rowPtr++);
				cell = poiUtil.getCurrentCell(currentRow,0);
				cell.setCellStyle(subTitleStyle);
				poiUtil.putCellValue(cell, "for Date range: "+ startDate + " - " + endDate );
				//last digit is based on how many columns the results have
				applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
				sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
				
				int cellPtr = 4;
				currentRow = sheet.getRow(rowPtr++);
				
				//INSERT COLUMN HEADERS HERE
				
				cell = poiUtil.getCurrentCell(currentRow,cellPtr+=2);
				cell.setCellStyle(headerListStyle);
				poiUtil.putCellValue(cell, "   DR    ");
				
				cell = poiUtil.getCurrentCell(currentRow,cellPtr);
				cell.setCellStyle(headerListStyle);
				poiUtil.putCellValue(cell, "   CR    ");
				
				//populate table
//				parseTemplate(sheet,rowPtr,journalEntries);
//				insertItem(sheet, rowPtr++, 5, journalEntries);
				
				//delete cell styles
				deleteStyleCells(wb);

				//adjust column sizes for better presentation
				autosizeColumns(sheet, maxCols);
				
				wb.write(baos);
				return new ByteArrayInputStream(baos.toByteArray());
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return null;
			
		}
	
	private List generateTrialBalanceEntries(Date startDate, Date endDate,
			Session session) {
		
		initializeController();
		
		List transactionsList = financialsManager.getActiveTransactionsBetweenDates(startDate, endDate, Transaction.class.getName(), "transactionDate", session, "transactionReferenceNumber");

		//generate map for transactions
		Map debitTransactionsMap = new HashMap<String,TrialBalanceEntry>();
		Map creditTransactionsMap = new HashMap<String,TrialBalanceEntry>();
		List accountList = new ArrayList();
		
		List trialBalanceEntries = new ArrayList();
		
		
		Iterator listItr = transactionsList.iterator();
		while(listItr.hasNext()) {
			Transaction transaction = (Transaction)listItr.next();
			TrialBalanceEntry entry = null;
			
			//check if existing on any of the maps
			if(debitTransactionsMap.get(transaction.getAccountEntry().getAccountCode())==null && creditTransactionsMap.get(transaction.getAccountEntry().getAccountCode())==null) {
				if(!accountList.contains(transaction.getAccountEntry().getAccountCode())) {
					accountList.add(transaction.getAccountEntry().getAccountCode());
				}
			}
			
			if(transaction.getTransactionAction().equalsIgnoreCase("DEBIT") && transaction.getIsInUse().equalsIgnoreCase("IN USE")  ) {
				if(debitTransactionsMap.get(transaction.getAccountEntry().getAccountCode())==null) {
					entry = new TrialBalanceEntry(transaction.getAccountEntry().getAccountCode(),transaction.getAccountEntry().getParentCode(),transaction.getAccountEntry().getName(), transaction.getAccountEntry().getClassification(), transaction.getAmount(), 0);
					debitTransactionsMap.put(transaction.getAccountEntry().getAccountCode(), entry);
				}else {
					entry = (TrialBalanceEntry) debitTransactionsMap.get(transaction.getAccountEntry().getAccountCode());
					double amountToAdd = transaction.getAmount() + entry.getDrAmount();
					entry.setDrAmount(amountToAdd);
					debitTransactionsMap.put(transaction.getAccountEntry().getAccountCode(), entry);
				}
			}else if(transaction.getTransactionAction().equalsIgnoreCase("CREDIT") && transaction.getIsInUse().equalsIgnoreCase("IN USE") ){
				if(creditTransactionsMap.get(transaction.getAccountEntry().getAccountCode())==null) {
					entry = new TrialBalanceEntry(transaction.getAccountEntry().getAccountCode(),transaction.getAccountEntry().getParentCode(),transaction.getAccountEntry().getName(), transaction.getAccountEntry().getClassification(), 0, transaction.getAmount());
					creditTransactionsMap.put(transaction.getAccountEntry().getAccountCode(), entry);
				}else {
					entry = (TrialBalanceEntry) creditTransactionsMap.get(transaction.getAccountEntry().getAccountCode());
					double amountToAdd = transaction.getAmount() + entry.getCrAmount();
					entry.setCrAmount(amountToAdd);
					creditTransactionsMap.put(transaction.getAccountEntry().getAccountCode(), entry);
				}
			}
			
			
		}
		
		listItr = accountList.iterator();
		while(listItr.hasNext()) {
			String code = (String) listItr.next();
			double debitAmount=0;
			double creditAmount=0;
			String title = "";
			String name = "";
			String classification="";
			TrialBalanceEntry entry=null;
			if(debitTransactionsMap.get(code)!=null) {
				entry = (TrialBalanceEntry)debitTransactionsMap.get(code);
				title = entry.getAccountTitle();
				classification = entry.getAccountType();
				debitAmount=entry.getDrAmount();
			}
			if(creditTransactionsMap.get(code)!=null) {				
				entry = (TrialBalanceEntry)creditTransactionsMap.get(code);
				title = entry.getAccountTitle();
				classification = entry.getAccountType();
				creditAmount=entry.getCrAmount();
				// remove debit from credit if it exist
				if(debitTransactionsMap.get(code)!=null) {
					TrialBalanceEntry debitEntry = (TrialBalanceEntry)debitTransactionsMap.get(code);
					String debittitle = debitEntry.getAccountTitle();
					String debitclassification = debitEntry.getAccountType();
					debitAmount=debitAmount - debitEntry.getDrAmount();
					
					//remove from credit amount
					creditAmount=creditAmount-debitEntry.getDrAmount();
					
				}
			}
			
			if(creditAmount==0 && debitAmount==0){}
			else{
				entry = new TrialBalanceEntry(entry.getAccountCode(),entry.getAccountParentCode(),title,classification, debitAmount, creditAmount);
				trialBalanceEntries.add(entry);
			}
		}
		return trialBalanceEntries;
	}

	
	/*
	 * END DATA POPULATION
	 * */
	
	private String parseNullString(String value) {
		return null == value ? "" : value;
	}

	private double parseNullDouble(Double value) {
		DecimalFormat df = new DecimalFormat("###,###,###.00");
		return null == value ? 0.0 : value;
	}

	
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	class TrialBalanceEntry {
		private String accountCode;
		private String accountParentCode;
		private String accountTitle;
		private String accountType;
		private double drAmount;
		private double crAmount;
		
		TrialBalanceEntry(String accountCode, String accountParentCode,String accountTitle,String accountType,double drAmount,double crAmount){
			this.accountCode = accountCode;
			this.accountParentCode = accountParentCode;
			this.accountTitle = accountTitle;
			if(accountType.equalsIgnoreCase("ASSET")) {
				this.accountType = "A";
			}else if(accountType.equalsIgnoreCase("LIABILITIES")) {
				this.accountType = "L";
			}else if(accountType.equalsIgnoreCase("SHAREHOLDERS EQUITY")) {
				this.accountType = "SE";
			}else if(accountType.equalsIgnoreCase("COST OF GOODS SOLD")) {
				this.accountType = "COGS";
			}else if(accountType.equalsIgnoreCase("EXPENSES")) {
				this.accountType = "E";
			}else {
				this.accountType = accountType;
			}
			
			this.drAmount = drAmount;
			this.crAmount = crAmount;
		}
		
		protected String getAccountCode(){return accountCode;}
		protected String getAccountParentCode(){return accountParentCode;}
		protected String getAccountTitle(){return accountTitle;}
		protected String getAccountType(){return accountType;}
		protected void setDrAmount(double drAmount){this.drAmount = drAmount;}
		protected void setCrAmount(double crAmount){this.crAmount = crAmount;}
		protected double getDrAmount(){return drAmount;}
		protected double getCrAmount(){return crAmount;}
		
	}

	public InputStream generateIncomeStatement(String dateFrom, String dateTo,
			Session session) {
		Date startDate;
		Date endDate;
		
		startDate = dfh.parseStringToTime(dateFrom);
		endDate = dfh.parseStringToTime(dateTo);
		
		//get data
		List trialBalanceEntries = generateTrialBalanceEntries(startDate,endDate,session);
				
		//load template
		InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.INCOME_STATEMENT_TEMPLATE);
		
		
		try {
			fsFileSystem = new POIFSFileSystem(pdInputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
			
			//load styles from pre styled cells
			loadStyles(wb);		
			
			//writer headers
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowPtr=0;
			HSSFRow currentRow = sheet.getRow(rowPtr++);
			int maxCols = 3;
			int startRow=0;
			int endRow=0;
			int startCol=0;
			
			//write title
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write sub title
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write date range
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			poiUtil.putCellValue(cell, "for Date range: "+ startDate + " - " + endDate );
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			currentRow = sheet.getRow(rowPtr++);
			
			//populate table
			parseTemplate(sheet,rowPtr,trialBalanceEntries);
//			insertItem(sheet, rowPtr++, 5, trialBalanceEntries);
			
			//delete cell styles
			deleteStyleCells(wb);

			//adjust column sizes for better presentation
			autosizeColumns(sheet, maxCols);
			
			wb.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}



	public InputStream generateCoGM(String dateFrom, String dateTo,
			Session session) {
		Date startDate;
		Date endDate;
		
		startDate = dfh.parseStringToTime(dateFrom);
		endDate = dfh.parseStringToTime(dateTo);
		
		//get data
		List trialBalanceEntries = generateTrialBalanceEntries(startDate,endDate,session);
				
		//load template
		InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.COGM_TEMPLATE);
		
		
		try {
			fsFileSystem = new POIFSFileSystem(pdInputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
			
			//load styles from pre styled cells
			loadStyles(wb);		
			
			//writer headers
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowPtr=0;
			HSSFRow currentRow = sheet.getRow(rowPtr++);
			int maxCols = 3;
			int startRow=0;
			int endRow=0;
			int startCol=0;
			
			//write title
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write sub title
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write date range
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			poiUtil.putCellValue(cell, "for Date range: "+ startDate + " - " + endDate );
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			currentRow = sheet.getRow(rowPtr++);
			
			//populate table
			parseTemplate(sheet,rowPtr,trialBalanceEntries);
//			insertItem(sheet, rowPtr++, 5, trialBalanceEntries);
			
			//delete cell styles
			deleteStyleCells(wb);

			//adjust column sizes for better presentation
			autosizeColumns(sheet, maxCols);
			
			wb.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}



	public InputStream generateCoGS(String dateFrom, String dateTo,
			Session session) {
		Date startDate;
		Date endDate;
		
		startDate = dfh.parseStringToTime(dateFrom);
		endDate = dfh.parseStringToTime(dateTo);
		
		//get data
		List trialBalanceEntries = generateTrialBalanceEntries(startDate,endDate,session);
				
		//load template
		InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.COGS_TEMPLATE);
		
		
		try {
			fsFileSystem = new POIFSFileSystem(pdInputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
			
			//load styles from pre styled cells
			loadStyles(wb);		
			
			//writer headers
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowPtr=0;
			HSSFRow currentRow = sheet.getRow(rowPtr++);
			int maxCols = 3;
			int startRow=0;
			int endRow=0;
			int startCol=0;
			
			//write title
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write sub title
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write date range
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			poiUtil.putCellValue(cell, "for Date range: "+ startDate + " - " + endDate );
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			currentRow = sheet.getRow(rowPtr++);
			
			//populate table
			parseTemplate(sheet,rowPtr,trialBalanceEntries);
//			insertItem(sheet, rowPtr++, 5, trialBalanceEntries);
			
			//delete cell styles
			deleteStyleCells(wb);

			//adjust column sizes for better presentation
			autosizeColumns(sheet, maxCols);
			
			wb.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}



	public InputStream generateScheduleOfChangesInStockholdersEquity(
			String dateFrom, String dateTo, Session session) {
		Date startDate;
		Date endDate;
		
		startDate = dfh.parseStringToTime(dateFrom);
		endDate = dfh.parseStringToTime(dateTo);
		
		//get data
		List trialBalanceEntries = generateTrialBalanceEntries(startDate,endDate,session);
				
		//load template
		InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.STOCKHOLDERS_EQUITY_TEMPLATE);
		
		
		try {
			fsFileSystem = new POIFSFileSystem(pdInputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
			
			//load styles from pre styled cells
			loadStyles(wb);		
			
			//writer headers
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowPtr=0;
			HSSFRow currentRow = sheet.getRow(rowPtr++);
			int maxCols = 3;
			int startRow=0;
			int endRow=0;
			int startCol=0;
			
			//write title
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write sub title
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write date range
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			poiUtil.putCellValue(cell, "for Date range: "+ startDate + " - " + endDate );
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			currentRow = sheet.getRow(rowPtr++);
			
			//populate table
			parseTemplate(sheet,rowPtr,trialBalanceEntries);
//			insertItem(sheet, rowPtr++, 5, trialBalanceEntries);
			
			//delete cell styles
			deleteStyleCells(wb);

			//adjust column sizes for better presentation
			autosizeColumns(sheet, maxCols);
			
			wb.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public InputStream generateBalanceSheet(String dateFrom, String dateTo,
			Session session) {
		Date startDate;
		Date endDate;
		
		startDate = dfh.parseStringToTime(dateFrom);
		endDate = dfh.parseStringToTime(dateTo);
		
		//get data
		List trialBalanceEntries = generateTrialBalanceEntries(startDate,endDate,session);
				
		//load template
		InputStream pdInputStream = this.getClass().getResourceAsStream(SASConstants.BALANCE_SHEET_REPORT_TEMPLATE);
		
		
		try {
			fsFileSystem = new POIFSFileSystem(pdInputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fsFileSystem);
			
			//load styles from pre styled cells
			loadStyles(wb);		
			
			//writer headers
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowPtr=0;
			HSSFRow currentRow = sheet.getRow(rowPtr++);
			int maxCols = 3;
			int startRow=0;
			int endRow=0;
			int startCol=0;
			
			//write title
			HSSFCell cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(titleStyle);
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write sub title
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			//write date range
			currentRow = sheet.getRow(rowPtr++);
			cell = poiUtil.getCurrentCell(currentRow,0);
			cell.setCellStyle(subTitleStyle);
			poiUtil.putCellValue(cell, "for Date range: "+ startDate + " - " + endDate );
			//last digit is based on how many columns the results have
			applyStyleToMergedRegion(startRow,endRow,startCol, maxCols,wb,subTitleStyle);
			sheet.addMergedRegion(new CellRangeAddress(startRow++, endRow++, startCol, maxCols));
			
			currentRow = sheet.getRow(rowPtr++);
			
			//populate table
			parseTemplate(sheet,rowPtr,trialBalanceEntries);
//			insertItem(sheet, rowPtr++, 5, trialBalanceEntries);
			
			//delete cell styles
			deleteStyleCells(wb);

			//adjust column sizes for better presentation
			autosizeColumns(sheet, maxCols);
			
			wb.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
