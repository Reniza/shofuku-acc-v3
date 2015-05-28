package com.shofuku.accsystem.action.inventory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.shofuku.accsystem.controllers.AccountEntryManager;
import com.shofuku.accsystem.controllers.CustomerManager;
import com.shofuku.accsystem.controllers.FinancialsManager;
import com.shofuku.accsystem.controllers.InventoryManager;
import com.shofuku.accsystem.controllers.LookupManager;
import com.shofuku.accsystem.controllers.SupplierManager;
import com.shofuku.accsystem.controllers.TransactionManager;
import com.shofuku.accsystem.domain.inventory.FinishedGood;
import com.shofuku.accsystem.domain.inventory.Ingredient;
import com.shofuku.accsystem.domain.inventory.ItemPricing;
import com.shofuku.accsystem.domain.inventory.PurchaseOrderDetails;
import com.shofuku.accsystem.domain.inventory.RawMaterial;
import com.shofuku.accsystem.domain.inventory.ReturnSlip;
import com.shofuku.accsystem.domain.inventory.StockStatus;
import com.shofuku.accsystem.domain.inventory.StockStatusReport;
import com.shofuku.accsystem.domain.inventory.TradedItem;
import com.shofuku.accsystem.domain.security.UserAccount;
import com.shofuku.accsystem.utils.AccountEntryProfileUtil;
import com.shofuku.accsystem.utils.DateFormatHelper;
import com.shofuku.accsystem.utils.HibernateSessionWatcher;
import com.shofuku.accsystem.utils.HibernateUtil;
import com.shofuku.accsystem.utils.InventoryUtil;
import com.shofuku.accsystem.utils.PurchaseOrderDetailHelper;
import com.shofuku.accsystem.utils.RecordCountHelper;
import com.shofuku.accsystem.utils.SASConstants;
import com.shofuku.accsystem.utils.StockStatusReportPoiUtil;
import com.thoughtworks.xstream.XStream;

public class GenerateStockStatusReportAction extends ActionSupport implements Preparable {

	private static final long serialVersionUID = 1L;

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	Map actionSession;
	UserAccount user;

	InventoryManager inventoryManager;
	
	
	@Override
	public void prepare() throws Exception {
		actionSession = ActionContext.getContext().getSession();
		user = (UserAccount) actionSession.get("user");

		inventoryManager = (InventoryManager) actionSession.get("inventoryManager");
		
	}
	
	
	@SuppressWarnings("rawtypes")
	List stockStatusList;
	
	XStream xstream = new XStream();
	DateFormatHelper dfh = new DateFormatHelper();
	String dateFrom;
	String dateTo;
	
	String day;
	int month;
	int year;
	
	@SuppressWarnings("rawtypes")
	List stockStatusParameterFields;
	
	InputStream xmlStreamOut;
	
	//summary report vars
	InputStream excelStream;
	String contentDisposition;

	private Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	
	public Map<String,Object> populateParameterMap(String itemCode,int month,int year){

		Map<String,Object> parameterMap = new HashMap<String,Object>();
		parameterMap.put("itemCode", itemCode);
		parameterMap.put("month", month);
		parameterMap.put("year", year);
		
		return parameterMap;
	}
	
	@SuppressWarnings("unchecked")
	public void populateParameterFields() {
		
		@SuppressWarnings("rawtypes")
		List parameterFields = new ArrayList();
		parameterFields.add("itemCode");
		parameterFields.add("month");
		parameterFields.add("year");
		
		stockStatusParameterFields = parameterFields;
	}
	
	private void createAliases() {
		xstream.alias("StockStatusItems",List.class);
		xstream.alias("StockStatusItems",Set.class);
		xstream.alias("FinishedGood", FinishedGood.class);
		xstream.alias("RawMaterial", RawMaterial.class);
		xstream.alias("TradedItem", TradedItem.class);
		xstream.alias("ingredients", Ingredient.class);
		xstream.alias("itemPricing",ItemPricing.class);
		xstream.addImplicitCollection(FinishedGood.class, "ingredients");
		xstream.aliasField("StockStatusId", FinishedGood.class, "productCode");
		xstream.aliasField("StockStatusId", RawMaterial.class, "itemCode");
		xstream.aliasField("StockStatusId", TradedItem.class, "itemCode");
	}
	
	private String readInputStreamFromClob(Clob clob) {

		InputStream is = null;
		try {
			is = clob.getAsciiStream();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String generateStockStatusReport() {
		Date endDate=null;
		Date startDate=null;
		//validation
		if(dateTo!=null && !dateTo.equalsIgnoreCase("")){
			endDate = dfh.parseStringToTime(dateTo);
		}
		if(dateFrom!=null && !dateFrom.equalsIgnoreCase("")){
			startDate = dfh.parseStringToTime(dateFrom);
		}
		if(endDate==null||startDate==null) {
			addActionError("Dates cannot be null");
			return INPUT;
		}else {
			
			Session session = getSession();
			try{

				ServletContext servletContext = ServletActionContext
						.getServletContext();

				String subModule = "StockStatusReport";
				
				StockStatusReportPoiUtil poiHelper = new StockStatusReportPoiUtil();	
				
				try {
					createAliases();
					
					String startDay = dfh.getDateFromDateString(dateFrom);
					String startMonth = dfh.getMonthFromDateString(dateFrom);
					String startYear = dfh.getYearFromDateString(dateFrom);
					
					
					
					
					if(dateTo!=null && !dateTo.equalsIgnoreCase(""))
						poiHelper.setMaxDate(endDate.toString());
					if(dateFrom!=null && !dateFrom.equalsIgnoreCase(""))
						poiHelper.setMinDate(startDate.toString());
					
					
					List parameterFields = new ArrayList();
					parameterFields.add("month");
					parameterFields.add("year");
					
					Map<String,Object> hashMapStarting = null;
					//+1 since calendar MONTH starts from 0 , batch job starts with 1
					hashMapStarting = populateParameterMap("", Integer.valueOf(startMonth)+1, Integer.valueOf(startYear));
					List stockStatusListBeginning = inventoryManager.listInventoryByParametersLike(StockStatus.class, hashMapStarting,parameterFields,"itemCode" ,session);
					stockStatusListBeginning= generateStockStatusListByDate(stockStatusListBeginning,startDay);
					
					Timestamp timestampFrom = dfh.parseStringToTimestamp(dateFrom);
					Timestamp timestampTo = dfh.parseStringToTimestamp(dateTo);
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(timestampFrom);
					cal.add(Calendar.DAY_OF_WEEK, 1);
					timestampFrom.setTime(cal.getTime().getTime()); // or
					
					List list = null;
					
					while(timestampFrom.before(timestampTo)) {
						cal = Calendar.getInstance();
						cal.setTime(timestampFrom);
						
						String endDay = dfh.getDateFromDateString(dfh.parseDateToStringAsNumbers(timestampFrom));
						String endMonth = dfh.getMonthFromDateString(dfh.parseDateToStringAsNumbers(timestampFrom));
						String endYear = dfh.getYearFromDateString(dfh.parseDateToStringAsNumbers(timestampFrom));
						
						Map<String,Object> hashMapEnding = null;
						hashMapEnding = populateParameterMap("", Integer.valueOf(endMonth)+1, Integer.valueOf(endYear));
						List stockStatusListEnding = inventoryManager.listInventoryByParametersLike(StockStatus.class, hashMapEnding,parameterFields,"itemCode" ,session);
						stockStatusListEnding = generateStockStatusListByDate(stockStatusListEnding,endDay);
						
						stockStatusListBeginning = joinStockStausBeginningToMiddle(stockStatusListBeginning,stockStatusListEnding);
						
						cal = Calendar.getInstance();
						cal.setTime(timestampFrom);
						cal.add(Calendar.DAY_OF_WEEK, 1);
						timestampFrom.setTime(cal.getTime().getTime()); // or
					}
					//one more loop for the last day
					String endDay = dfh.getDateFromDateString(dfh.parseDateToStringAsNumbers(timestampFrom));
					String endMonth = dfh.getMonthFromDateString(dfh.parseDateToStringAsNumbers(timestampFrom));
					String endYear = dfh.getYearFromDateString(dfh.parseDateToStringAsNumbers(timestampFrom));
					
					Map<String,Object> hashMapEnding = null;
					hashMapEnding = populateParameterMap("", Integer.valueOf(endMonth)+1, Integer.valueOf(endYear));
					List stockStatusListEnding = inventoryManager.listInventoryByParametersLike(StockStatus.class, hashMapEnding,parameterFields,"itemCode" ,session);
					stockStatusListEnding = generateStockStatusListByDate(stockStatusListEnding,endDay);
					
					stockStatusListBeginning = joinStockStausBeginningToMiddle(stockStatusListBeginning,stockStatusListEnding);
					
					//get ending swap ending to the last joined
					
					endDay = dfh.getDateFromDateString(dateFrom);
					endMonth = dfh.getMonthFromDateString(dateFrom);
					endYear = dfh.getYearFromDateString(dateFrom);
					
					hashMapEnding = populateParameterMap("", Integer.valueOf(endMonth)+1, Integer.valueOf(endYear));
					stockStatusListEnding = stockStatusListBeginning;
					stockStatusListBeginning =	inventoryManager.listInventoryByParametersLike(StockStatus.class, hashMapEnding,parameterFields,"itemCode" ,session);
					stockStatusListBeginning = generateStockStatusListByDate(stockStatusListBeginning,endDay);
					
					list = joinStockStausBeginningToEnding(stockStatusListBeginning, stockStatusListEnding);
					
					//TODO: apply query for issuances and receipts once cleared to client
					
					HSSFWorkbook wb = new HSSFWorkbook();
					wb = poiHelper.generateSummary(servletContext, subModule, stockStatusListBeginning);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					wb.write(baos);
					excelStream = new ByteArrayInputStream(baos.toByteArray());
				} catch (IOException ioex) {
					logger.debug("generate stock status" + ioex.toString());
					ioex.printStackTrace();
				}
		
				contentDisposition = "filename=\""+subModule+"_"+startDate.toString()+"_TO_"+endDate.toString()+".xls\"";
				
				return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				addActionError("Error on generating stock status report summary with the supplied parameters");
				return contentDisposition;
			}finally {
				if(session.isOpen()){
					session.close();
					session.getSessionFactory().close();
				}
			}
		}
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List joinStockStausBeginningToEnding(List begginningList, List endingList) {
		
		List finalCombinedList = new ArrayList<>();
		
		HashMap endingMap = new HashMap();
		
		Iterator listIterator = endingList.iterator();
		while(listIterator.hasNext()) {
			StockStatusReport ssReport = (StockStatusReport) listIterator.next();
			endingMap.put(ssReport.getItemCode(), ssReport);
		}
		
		
		listIterator = begginningList.iterator();
		while(listIterator.hasNext()) {
			StockStatusReport ssReport = (StockStatusReport) listIterator.next();
// 			MANUAL DEBUG
//			if(ssReport.getItemCode().equalsIgnoreCase("FD01")) {
//				System.out.println("HERE");
//			}
			ssReport = populateIssuancesAndReceipts(ssReport);
			StockStatusReport endingSSReport =(StockStatusReport) endingMap.get(ssReport.getItemCode());
			
			if(endingSSReport!=null) {
				ssReport.setEndingBalanceQty(endingSSReport.getBeginningBalanceQty());			
				ssReport.setEndingBalanceAmount(endingSSReport.getBeginningBalanceAmount());
			}
			finalCombinedList.add(ssReport);
		}
		
		return finalCombinedList;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List joinStockStausBeginningToMiddle(List begginningList, List endingList) {
		
		List finalCombinedList = new ArrayList<>();
		
		HashMap endingMap = new HashMap();
		
		Iterator listIterator = endingList.iterator();
		while(listIterator.hasNext()) {
			StockStatusReport ssReport = (StockStatusReport) listIterator.next();
			endingMap.put(ssReport.getItemCode(), ssReport);
		}
		
		listIterator = begginningList.iterator();
		while(listIterator.hasNext()) {
			StockStatusReport ssReport = (StockStatusReport) listIterator.next();
			StockStatusReport endingSSReport =(StockStatusReport) endingMap.get(ssReport.getItemCode());
			
			if(endingSSReport!=null) {
				if(endingSSReport.getBeginningBalanceQty() > ssReport.getBeginningBalanceQty()) {
					double difference = endingSSReport.getBeginningBalanceQty() - ssReport.getBeginningBalanceQty();
					//START: 2013 - PHASE 3 : PROJECT 4: MARK
					//double tempAmount = difference * endingSSReport.getUnitCost()+  ssReport.getBeginningBalanceAmount();
					endingSSReport.setBeginningBalanceAmount(endingSSReport.getBeginningBalanceAmount());
					//START: 2013 - PHASE 3 : PROJECT 4: MARK
				}
				ssReport.setBeginningBalanceQty(endingSSReport.getBeginningBalanceQty());
				ssReport.setBeginningBalanceAmount(endingSSReport.getBeginningBalanceAmount());
			}
			finalCombinedList.add(ssReport);
		}
		
		return finalCombinedList;
	}
	

	//treats every list as a beginning balance. use it either as a beginngin list or ending list on joinStockStausBeginningAndEnding()
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List generateStockStatusListByDate(List stockStatusList,
			String endDay) {
		List tempList = new ArrayList<>();
		Iterator itr = stockStatusList.iterator();
		Object objectToInsert=null;
		while(itr.hasNext()){
			StockStatus ss = (StockStatus)itr.next();
			String stringOutput = readInputStreamFromClob(ss.getXmlInventoryCount());
			Set newSet =(Set)xstream.fromXML(stringOutput);
			Iterator xmlIterator = newSet.iterator();
			while(xmlIterator.hasNext()) {
				objectToInsert = (Object)xmlIterator.next();
				StockStatusReport ssReport = new StockStatusReport();
				if(objectToInsert instanceof RawMaterial) {
					RawMaterial rm = (RawMaterial)objectToInsert;
					if(rm.getStockStatusDay().equals(endDay)) {
							String itemCode = rm.getItemCode();
							ssReport = new StockStatusReport(rm.getItemCode(),rm.getDescription(),rm.getUnitOfMeasurement(),
									rm.getItemPricing().getCompanyOwnedStandardPricePerUnit(),rm.getWarehouse().getQuantityPerRecord(),rm.getWarehouse().getQuantityPerRecord()*rm.getItemPricing().getCompanyOwnedStandardPricePerUnit(),0.0,0.0,0.0,0.0,0.0,0.0,rm.getClassification(),rm.getSubClassification(),SASConstants.RAW_MATERIAL_ABBR);
					}else {
						continue;
					}
				}else if (objectToInsert instanceof FinishedGood) {
					FinishedGood fg = (FinishedGood)objectToInsert;
					if(fg.getStockStatusDay().equals(endDay)) {
							String itemCode = fg.getProductCode();
							ssReport = new StockStatusReport(fg.getProductCode(),fg.getDescription(),fg.getUnitOfMeasurement(),
									fg.getItemPricing().getCompanyOwnedStandardPricePerUnit(),fg.getWarehouse().getQuantityPerRecord(),fg.getWarehouse().getQuantityPerRecord()*fg.getItemPricing().getCompanyOwnedStandardPricePerUnit(),0.0,0.0,0.0,0.0,0.0,0.0,fg.getClassification(),fg.getSubClassification(),SASConstants.FINISHED_GOOD_ABBR);
					}else {
						continue;
					}
				}else if(objectToInsert instanceof TradedItem) {
					TradedItem ti = (TradedItem)objectToInsert;
					if(ti.getStockStatusDay().equals(endDay)) {
							String itemCode = ti.getItemCode();
							ssReport = new StockStatusReport(ti.getItemCode(),ti.getDescription(),ti.getUnitOfMeasurement(),
									ti.getItemPricing().getCompanyOwnedStandardPricePerUnit(),ti.getWarehouse().getQuantityPerRecord(),ti.getWarehouse().getQuantityPerRecord()*ti.getItemPricing().getCompanyOwnedStandardPricePerUnit(),0.0,0.0,0.0,0.0,0.0,0.0,ti.getClassification(),ti.getSubClassification(),SASConstants.TRADED_ITEM_ABBR);
					}else {
						continue;
					}
				}
				tempList.add(ssReport);
			}
		}
		return tempList;
	}
	
	private StockStatusReport populateIssuancesAndReceipts(
			StockStatusReport ssReport) {
		
		ssReport = getIssuancesQtyAndAmount(ssReport);
		ssReport = getReceiptsQtyAndAmount(ssReport);
		ssReport= getReturnsQtyAndAmount(ssReport);
		
		return ssReport;
	}

	private StockStatusReport getIssuancesQtyAndAmount(StockStatusReport ssReport) {
		double totalOrdered = 0.0;
		double totalAmount = 0.0;
		
		List<String> ordersRelated = new ArrayList<String>();
		ordersRelated.add("DELIVERY_RECEIPT_NO");
		ordersRelated.add("REQUISITION_NO_O");
		ordersRelated.add("REQUISITION_NO_R");
		
		List<PurchaseOrderDetails> orders = getRelatedOrders(ssReport.getItemCode(),ordersRelated);
		
		Timestamp timestampFrom = dfh.parseStringToTimestamp(dateFrom);
		Timestamp timestampTo = dfh.parseStringToTimestamp(dateTo);
		Calendar cal = Calendar.getInstance();
		cal.setTime(timestampTo);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		timestampTo.setTime(cal.getTime().getTime());
		
		
		Iterator itr = orders.iterator();
		while(itr.hasNext()) {
			PurchaseOrderDetails poDetails = (PurchaseOrderDetails)itr.next();
			if(poDetails.getOrderCreatedDate()!=null) {
				if(poDetails.getOrderCreatedDate().before(timestampFrom) || poDetails.getOrderCreatedDate().after(timestampTo)) {
				}else {
					totalOrdered = totalOrdered + poDetails.getQuantity();
					//assumed getting transfer since DR,OR and RS are used by transfer prices
					totalAmount = totalAmount + poDetails.getAmount();
				}
			}
		}
		
		ssReport.setIssuancesQuantity(totalOrdered);
		ssReport.setIssuancesAmount(totalAmount);
		
		return ssReport;
	}


	private StockStatusReport getReceiptsQtyAndAmount(StockStatusReport ssReport) {
		double totalOrdered = 0.0;
		double totalAmount = 0.0;
		
		List<String> ordersRelated = new ArrayList<String>();
		ordersRelated.add("RECEIVING_REPORT_NO");
		ordersRelated.add("FPTS_NO_T");
		
		List<PurchaseOrderDetails> orders = getRelatedOrders(ssReport.getItemCode(),ordersRelated);
		
		Timestamp timestampFrom = dfh.parseStringToTimestamp(dateFrom);
		
		Timestamp timestampTo = dfh.parseStringToTimestamp(dateTo);
		Calendar cal = Calendar.getInstance();
		cal.setTime(timestampTo);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		timestampTo.setTime(cal.getTime().getTime());
		
		
		Iterator itr = orders.iterator();
		while(itr.hasNext()) {
			PurchaseOrderDetails poDetails = (PurchaseOrderDetails)itr.next();
			if(poDetails.getOrderCreatedDate()!=null) {
				if(poDetails.getOrderCreatedDate().before(timestampFrom) || poDetails.getOrderCreatedDate().after(timestampTo)) {
				}else {
					totalOrdered = totalOrdered + poDetails.getQuantity();
					//assumed getting transfer since DR,OR and RS are used by transfer prices
					totalAmount = totalAmount + poDetails.getAmount();
				}
			}
		}
		
		ssReport.setReceiptsQty(totalOrdered);
		ssReport.setReceiptsAmount(totalAmount);
		
		return ssReport;
	}
	
	private StockStatusReport getReturnsQtyAndAmount(StockStatusReport ssReport) {
		double totalOrdered = 0.0;
		double totalAmount = 0.0;
		
		List<String> ordersRelated = new ArrayList<String>();
		ordersRelated.add("RETURN_SLIP_NO");
		
		Map<String,String> orders = getRelatedReturnSlipIds(ssReport.getItemCode(),ordersRelated);
		List<PurchaseOrderDetails> orderDetails = getRelatedOrders(ssReport.getItemCode(),ordersRelated);
		
		Timestamp timestampFrom = dfh.parseStringToTimestamp(dateFrom);
		Timestamp timestampTo = dfh.parseStringToTimestamp(dateTo);
		Calendar cal = Calendar.getInstance();
		cal.setTime(timestampTo);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		timestampTo.setTime(cal.getTime().getTime());
		
		
		double tempIssuancesQty=ssReport.getIssuancesQuantity();
		double tempIssuancesAmt=ssReport.getIssuancesAmount();
		double tempReceiptsQty=ssReport.getReceiptsQty();
		double tempReceiptsAmt=ssReport.getReceiptsAmount();
		
		Iterator itr = orderDetails.iterator();
		while(itr.hasNext()) {
			PurchaseOrderDetails poDetails = (PurchaseOrderDetails)itr.next();
			if(poDetails.getOrderCreatedDate()!=null) {
				if(poDetails.getOrderCreatedDate().before(timestampFrom) || poDetails.getOrderCreatedDate().after(timestampTo)) {
				}else {
					
					ReturnSlip rs = new ReturnSlip();
					
					rs = (ReturnSlip) inventoryManager.listInventoryByParameter(ReturnSlip .class, "returnSlipNo",
							orders.get(String.valueOf(poDetails.getId())),getSession()).get(0);
					if(rs.getReturnSlipTo().equalsIgnoreCase(SASConstants.RS_CUSTOMER_TO_WAREHOUSE) || rs.getReturnSlipTo().equalsIgnoreCase(SASConstants.RS_PRODUCTION_TO_WAREHOUSE)){
						tempReceiptsQty = tempReceiptsQty+poDetails.getQuantity();
						tempReceiptsAmt = tempReceiptsAmt+poDetails.getAmount();
					}else if(rs.getReturnSlipTo().equalsIgnoreCase(SASConstants.RS_WAREHOUSE_TO_SUPPLIER)||rs.getReturnSlipTo().equalsIgnoreCase(SASConstants.RS_WAREHOUSE_TO_PRODUCTION)) {
						tempIssuancesQty = tempIssuancesQty+poDetails.getQuantity();
						tempIssuancesAmt = tempIssuancesAmt+poDetails.getAmount();
					}
				}
			}
		}
		
		ssReport.setReceiptsQty(tempReceiptsQty);
		ssReport.setReceiptsAmount(tempReceiptsAmt);
		ssReport.setIssuancesQuantity(tempIssuancesQty);
		ssReport.setIssuancesAmount(tempIssuancesAmt);
		
		return ssReport;
	}

	

	
	private List<PurchaseOrderDetails> getRelatedOrders(String itemCode,List<String> ordersRelated){
		return inventoryManager.getRelatedOrders(itemCode,ordersRelated);
	}
	
	private Map<String, String> getRelatedReturnSlipIds(String itemCode,List<String> ordersRelated){
		return inventoryManager.getRelatedReturnSlipIds(itemCode,ordersRelated);
	}

	public InputStream getXmlStreamOut() {
		return xmlStreamOut;
	}

	public void setXmlStreamOut(InputStream xmlStreamOut) {
		this.xmlStreamOut = xmlStreamOut;
	}
	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		dateFrom = null == dateFrom ? "" : dateFrom;
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		dateTo = null == dateTo ? "" : dateTo;
		this.dateTo = dateTo;
	}
	

	@SuppressWarnings("rawtypes")
	public List getStockStatusList() {
		return stockStatusList;
	}

	@SuppressWarnings("rawtypes")
	public void setStockStatusList(List stockStatusList) {
		this.stockStatusList = stockStatusList;
	}
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	@SuppressWarnings("rawtypes")
	public List getStockStatusParameterFields() {
		return stockStatusParameterFields;
	}

	@SuppressWarnings("rawtypes")
	public void setStockStatusParameterFields(List stockStatusParameterFields) {
		this.stockStatusParameterFields = stockStatusParameterFields;
	}
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}


}