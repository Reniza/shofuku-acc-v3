package com.shofuku.accsystem.utils;


public class SASConstants {

	private SASConstants() {

	}

	// public final static String MYSQL_HOST =
	// "jdbc:mysql://192.168.1.5:3306/shofuku";
//	public final static String MYSQL_HOST = "jdbc:mysql://127.0.0.1:3306/shofuku";
//	public final static String MYSQL_USERNAME = "root";
//	public final static String MYSQL_PASSWORD = "";

	// Template directory
	public final static String templateDirectory = "c:\\";
	public final static String templateFile = "supplier.xls";
	public static final String RECEIPTTEMPLATE = "receipt_template";

	// file extensions
	public final static String POITemplateExtension = ".xls";
	public final static String CVSReportsTemplateExtension = ".cvs";

	// report types
	public final static String IMPORT_ORDERING_FORM = "2";

	//ordering form blank template
	public final static String ORDERING_FORM_ROOT = "/../templates/OrderingFormBlankTemplate_v2.xls";
	
	//search results export template
	public final static String SEARCH_RESULTS_TEMPLATE_ROOT = "/../templates/SearchResultsExportTemplate.xls";
	
	//ordering form filename
	public final static String ORDERING_FORM_FILENAME = "OrderingFormTemplate.xls";
	
	//BASE HIBERNATE CONSTANTS
	public static final String ORDER = "ORDER";
	
	//POI CONSTANTS
	public final static int ORDERING_FORM_FIRST_ROW = 11;
	public final static int ORDERING_FORM_FIRST_COLUMN = 0;
	public final static int ORDERING_FORM_SECOND_COLUMN = 7;
	
	public final static String EXISTS = "RECORD ALREADY EXIST";
	public final static String NON_EXISTS = "RECORD NOT EXIST";
	public final static String ADD_SUCCESS = "RECORD ADDED SUCCESSFULLY";
	public final static String FAILED = "FAILED TO ADD THIS RECORD";
	public final static String AEP_FAILED = "FAILED TO ADD THIS RECORD: Please provide correct account code ";
	public final static String NO_LIST = "NO RECORDS FOUND";
	public final static String TAKEN = "ALREADY TAKEN";
	
	public final static String UPDATED = "RECORD UPDATED SUCCESSFULLY";
	public final static String UPDATE_FAILED = "RECORD UPDATE FAILED";
	
	public final static String DELETED= "RECORD and ALL RECORDS THAT WERE ASSOCIATED TO THIS HAD BEEN DELETED";
	public final static String NON_DELETED= "RECORD DELETION FAILED";
	
	public final static String RETURN_SLIP_INVALID= "CANNOT RETURN FROM BOTH SUPPLIERS OR CUSTOMERS";
	

	public final static String TEMPLATE_TYPE_SUMMARY = "SUMMARY";
	public final static int SUMMARY_TEMPLATE_ROW_START = 5;
	public static final int SUMMARY_TEMPLATE_FIRST_COL = 0;
	
	public final static int SUMMARY_TEMPLATE_PETTY_CASH_TOTAL_AMOUNT_COL = 4;
	public final static String SUMMARY_TEMPLATE_PETTY_CASH_TOTAL_AMOUNT_LABEL = "TOTAL AMOUNT: ";
	public final static String SUMMARY_TEMPLATE_PETTY_CASH_RELEASED_BY = "RELEASED BY: ";
	public final static String SUMMARY_TEMPLATE_PETTY_CASH_APPROVED_BY = "APPROVED BY: ";
	
	public final static int SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_COL_START = 11;
	public final static int SUMMARY_TEMPLATE_SUPPLIER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START = 18;
	public final static int SUMMARY_TEMPLATE_SUPPLIER_RECEIVING_COL_START = 26;
	public final static int SUMMARY_TEMPLATE_SUPPLIER_RECEIVING_PURCHASE_DETAILS_COL_START = 38;
	public final static int SUMMARY_TEMPLATE_SUPPLIER_INVOICE_COL_START = 46;
	public final static int SUMMARY_TEMPLATE_SUPPLIER_INVOICE_PURCHASE_DETAILS_COL_START = 55;
	
	public final static String SUMMARY_TEMPLATE_COMPANY_HEADER = "SHOFUKU TRADING CORP";
	
	public final static String SUMMARY_TEMPLATE_HEADER_SUPPLIER = "SUPPLIER SUMMARY";
	public final static String SUMMARY_TEMPLATE_HEADER_SUPPLIER_PO = "SUPPLIER PURCHASE ORDERSUMMARY";
	public final static String SUMMARY_TEMPLATE_HEADER_SUPPLIER_PO_ORDERS=  "SUPPLIER PURCHASE ORDER SUMMARY - ORDER DETAILS";
	public final static String SUMMARY_TEMPLATE_HEADER_SUPPLIER_RR = "SUPPLIER RECEIVING REPORT SUMMARY";
	public final static String SUMMARY_TEMPLATE_HEADER_SUPPLIER_INV = "SUPPLIER INVOICE SUMMARY";
	public final static String SUMMARY_TEMPLATE_HEADER_SUPPLIER_INV_ORDERS = "SUPPLIER INVOICE SUMMARY - ORDER DETAILS";
	
	public static final String SUMMARY_TEMPLATE_HEADER_CASH_RECEIPTS = "CASH RECEIPTS SUMMARY";
	public static final String SUMMARY_TEMPLATE_HEADER_OR_SALES = "OR SALES SUMMARY";
	public static final String SUMMARY_TEMPLATE_HEADER_OR_OTHERS = "OR OTHERS SUMMARY";
	
	public final static int SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_COL_START = 11;
	public final static int SUMMARY_TEMPLATE_CUSTOMER_PURCHASE_ORDER_PURCHASE_DETAILS_COL_START = 19;
	public final static int SUMMARY_TEMPLATE_CUSTOMER_DELIVERY_COL_START = 27;
	public final static int SUMMARY_TEMPLATE_CUSTOMER_DELIVERY_PURCHASE_DETAILS_COL_START = 38;
	public final static int SUMMARY_TEMPLATE_CUSTOMER_INVOICE_COL_START = 46;
	public final static int SUMMARY_TEMPLATE_CUSTOMER_INVOICE_PURCHASE_DETAILS_COL_START = 57;
	
	public final static int SUMMARY_TEMPLATE_RECEIPTS_SECOND_PAGE = 9;
	
	public final static String SUMMARY_TEMPLATE_HEADER_CUSTOMER = "CUSTOMER PROFILE SUMMARY";
	public final static String SUMMARY_TEMPLATE_HEADER_CUSTOMER_PO = "CUSTOMER PURCHASE ORDERSUMMARY";
	public final static String SUMMARY_TEMPLATE_HEADER_CUSTOMER_PO_ORDERS=  "CUSTOMER PURCHASE ORDER SUMMARY - ORDER DETAILS";
	public final static String SUMMARY_TEMPLATE_HEADER_CUSTOMER_RR = "CUSTOMER DELIVERY REPORT SUMMARY";
	public final static String SUMMARY_TEMPLATE_HEADER_CUSTOMER_INV = "CUSTOMER INVOICE SUMMARY";
	public final static String SUMMARY_TEMPLATE_HEADER_CUSTOMER_INV_ORDERS = "CUSTOMER INVOICE SUMMARY - ORDER DETAILS";
	public static final String SUMMARY_TEMPLATE_HEADER_ITEMS_SOLD_TO_CUSTOMERS = "ITEMS SOLD TO CUSTOMERS";
	public static final String SUMMARY_TEMPLATE_HEADER_ITEMS_PURCHASED_FROM_SUPPLIER = "ITEMS PURCHASED FROM SUPPLIER";
	public static final String SUMMARY_TEMPLATE_HEADER_STATEMENT_OF_ACCOUNT = "STATEMENT OF ACCOUNT";
	
	public final static String SUMMARY_TEMPLATE_HEADER_PETTY_CASH = "PETTY CASH";
	public final static String SUMMARY_TEMPLATE_HEADER_CASH_PAYMENT = "CASH PAYMENT";
	public final static String SUMMARY_TEMPLATE_HEADER_CHECK_PAYMENT = "CHECK PAYMENT";
	
	public static final String SUMMARY_TEMPLATE_HEADER_RAW_MATERIALS = "RAW MATERIALS SUMMARY";
	public static final String SUMMARY_TEMPLATE_HEADER_FINISHED_GOODS = "FINISHED PRODUCTS SUMMARY";
	public static final String SUMMARY_TEMPLATE_HEADER_TRADED_ITEMS = "TRADED ITEMS SUMMARY";
	public static final String SUMMARY_TEMPLATE_HEADER_UTENSILS = "UTENSILS SUMMARY";
	public static final String SUMMARY_TEMPLATE_HEADER_OFFICE_SUPPLIES = "OFFICE SUPPLIES SUMMARY";
	public static final String SUMMARY_TEMPLATE_HEADER_STOCK_STATUS_REPORT = "STOCK STATUS REPORT";
	public static final String SUMMARY_TEMPLATE_HEADER_FPTS = "FINISHED PRODUCT TRANSFER SLIP SUMMARY";
	public static final String SUMMARY_TEMPLATE_HEADER_RF = "ORDER REQUISITION SUMMARY";
	public static final String SUMMARY_TEMPLATE_HEADER_RETURN_SLIP = "RETURN SLIP SUMMARY";
	
	
	public static final int SUMMARY_TEMPLATE_FPTS_COL_START = 0;
	public static final int SUMMARY_TEMPLATE_FPTS_PURCHASE_DETAILS_RECEIVED_COL_START = 16;
	public static final int SUMMARY_TEMPLATE_FPTS_PURCHASE_DETAILS_TRANSFERRED_COL_START = 10;
	public static final int SUMMARY_TEMPLATE_RF_PURCHASE_DETAILS_ORDERED_COL_START = 7;
	public static final int SUMMARY_TEMPLATE_RF_PURCHASE_DETAILS_RECEIVED_COL_START = 12;
	public static final int SUMMARY_TEMPLATE_RETURN_SLIP_PURCHASE_DETAILS_COL_START = 7;
	
	
	
	public final static int SUMMARY_TEMPLATE_HEADER_FINISHED_GOODS_INGREDIENTS_COL_START = 18;
	
	public final static String REPORT_TYPE_SUMMARY = "Summary";
	
	public final static String REPORT_SUMMARY_TEMPLATE_PATH="/WEB-INF/reporttemplates/";
	public final static String REPORT_SUMMARY_TEMPLATE_TYPE=".xls";
	public static final String BLANKCOLUMN = "-";
	
	
	public static final int ReceiptORNoRowMapping = 6;
	public static final int ReceiptORNoColMapping = 5;
	public static final int ReceiptORDateRowMapping = 7;
	public static final int ReceiptORDateColMapping = 7;
	public static final int ReceiptReceivedFromRowMapping = 8;
	public static final int ReceiptReceivedFromColMapping = 5;
	public static final int ReceiptAddressRowMapping = 10;
	public static final int ReceiptAddressColMapping = 4;
	public static final int ReceiptBusStyleRowMapping = 11;
	public static final int ReceiptBusStyleColMapping = 4;
	public static final int ReceiptTinRowMapping = 11;
	public static final int ReceiptTinColMapping = 8;
	public static final int ReceiptAmountInWordsRowMapping = 13;
	public static final int ReceiptAmountInWordsColMapping = 5;
	public static final int ReceiptAmountOfRowMapping = 14;
	public static final int ReceiptAmountOfColMapping = 9;
	public static final int ReceiptInPartialFullAmountOfRowMapping = 15;
	public static final int ReceiptInPartialFullAmountOfColMapping = 6;
	public static final int ReceiptAmountRowMapping = 15;
	public static final int ReceiptAmountColMapping = 7;
	public static final int ReceiptSalesInvoiceNoRowMapping = 4;
	public static final int ReceiptSalesInvoiceNoColMapping = 0;
	public static final int ReceiptSalesInvoiceAmountRowMapping = 4;
	public static final int ReceiptSalesInvoiceAmountColMapping = 1;
	public static final int ReceiptCashRowMapping = 11;
	public static final int ReceiptCashColMapping = 1;
	public static final int ReceiptCheckRowMapping = 12;
	public static final int ReceiptCheckColMapping = 1;
	public static final int ReceiptCheckNoRowMapping = 14;
	public static final int ReceiptCheckNoColMapping = 1;
	public static final int ReceiptCashCheckTotalAmountRowMapping = 16;
	public static final int ReceiptCashCheckTotalAmountColMapping = 1;
	
	public static final int DisbursementCheckVoucherPayeeRowMapping = 6;
	public static final int DisbursementCheckVoucherPayeeColMapping = 1;
	public static final int DisbursementCheckVoucherDateRowMapping = 5;
	public static final int DisbursementCheckVoucherDateColMapping = 9;
	public static final int DisbursementCheckVoucherAmountInWordsRowMapping = 7;
	public static final int DisbursementCheckVoucherAmountInWordColMapping = 1;
	public static final int DisbursementCheckVoucherBankNoRowMapping = 8;
	public static final int DisbursementCheckVoucherBankNoColMapping = 2;
	public static final int DisbursementCheckVoucherAmountRowMapping = 6;
	public static final int DisbursementCheckVoucherAmountColMapping = 9;
	public static final int DisbursementCheckVoucherPaymentFromRowMapping = 9;
	public static final int DisbursementCheckVoucherPaymentFromColMapping = 2;
	public static final int DisbursementCheckVoucherBankNameRowMapping = 8;
	public static final int DisbursementCheckVoucherAmount2RowMapping = 21;
	public static final int DisbursementCheckVoucherAmount2ColMapping = 9;
	
	public static final int DisbursementCheckVoucherReferenceRowMapping = 13;
	public static final int DisbursementCheckVoucherReferenceColMapping = 0;
	
	public static final int DisbursementCheckVoucherAccountTitleRowMapping = 13;
	public static final int DisbursementCheckVoucherAccountTitleColMapping = 2;
	
	public static final int DisbursementCheckVoucherCreditRowMapping = 13;
	public static final int DisbursementCheckVoucherCreditColMapping = 10;
	
	public static final int DisbursementCheckVoucherAmountToVatRowMapping =19;
	public static final int DisbursementCheckVoucherAmountToVatColMapping =9;
	
	public static final int DisbursementCheckVoucherVatAmountRowMapping = 20;
	public static final int DisbursementCheckVoucherVatAmountColMapping = 9;
	
	public static final int DisbursementCheckVoucherTotalPurchasesRowMapping = 19;
	public static final int DisbursementCheckVoucherTotalPurchasesColMapping = 4;
	
	public static final int DisbursementCheckVoucherDebitRowMapping = 13;
	public static final int DisbursementCheckVoucherDebitColMapping = 9;
	
	public static final int DisbursementCheckVoucherAmountPaidRowMapping = 20;
	public static final int DisbursementCheckVoucherAmountPaidColMapping = 5;
	
	public static final int DisbursementCheckVoucherAmountPaidLabelRowMapping = 20;
	public static final int DisbursementCheckVoucherAmountPaidLabelColMapping = 4;
	
	public static final int DisbursementCheckVoucherNoRowMapping = 5;
	public static final int DisbursementCheckVoucherNoColMapping = 1;
	
	public static final int DisbursementCheckVoucherPaymentTermsColMapping = 0;
	
	public static final int DisbursementCheckPaymentDateRowMapping = 1;
	public static final int DisbursementCheckPaymentDateColMapping = 9;
	public static final int DisbursementCheckPaymentPayeeRowMapping = 3;
	public static final int DisbursementCheckPaymentPayeeColMapping = 2;
	public static final int DisbursementCheckPaymentAmountRowMapping = 3;
	public static final int DisbursementCheckPaymentAmountColMapping = 9;
	public static final int DisbursementCheckPaymentAmountInWordsRowMapping = 5;
	public static final int DisbursementCheckPaymentAmountInwordsColMapping = 2;
	public static final int DisbursementCheckPaymentBankNoRowMapping = 7;
	public static final int DisbursementCheckPaymentBankNoColMapping = 2;

	public static final int CustomerSalesInvoiceDateRowMapping = 7;
	public static final int CustomerSalesInvoiceDateColMapping = 5;
	public static final int CustomerSalesInvoiceDRNoRowMapping = 7;
	public static final int CustomerSalesInvoiceDRNoColMapping = 1;
	public static final int CustomerSalesInvoiceSoldToRowMapping = 8;
	public static final int CustomerSalesInvoiceSoldToColMapping = 1;
	public static final int CustomerSalesInvoiceAddressRowMapping = 9;
	public static final int CustomerSalesInvoiceAddressColMapping = 1;
//	public static final int CustomerSalesInvoiceBusStyleRowMapping = 0;
//	public static final int CustomerSalesInvoiceBusStyleColMapping = 0;
//	public static final int CustomerSalesInvoiceTinRowMapping = 0;
//	public static final int CustomerSalesInvoiceTinColMapping = 1;
//	//itemcode
	public static final int CustomerSalesInvoiceQuantityRowMapping = 16;
	public static final int CustomerSalesInvoiceQuantityColMapping = 1;
	//uom
	
	public static final int CustomerSalesInvoiceItemCodeColMapping = 0;
	
	public static final int CustomerSalesInvoiceUnitRowMapping = 16;
	public static final int CustomerSalesInvoiceUnitColMapping = 5;
	public static final int CustomerSalesInvoiceDescriptionRowMapping = 16;
	public static final int CustomerSalesInvoiceDescriptionColMapping = 2;
	public static final int CustomerSalesInvoiceUnitPriceRowMapping = 16;
	public static final int CustomerSalesInvoiceUnitPriceColMapping = 6;
	public static final int CustomerSalesInvoiceAmountRowMapping = 16;
	public static final int CustomerSalesInvoiceAmountColMapping = 7;
	public static final int CustomerSalesInvoiceTotalSalesRowMapping = 11;
	public static final int CustomerSalesInvoiceTotalSalesColMapping = 1;
	public static final int CustomerSalesInvoiceVatRowMapping = 12;
	public static final int CustomerSalesInvoiceVatColMapping = 1;
	public static final int CustomerSalesInvoiceTotalAmountRowMapping = 13;
	public static final int CustomerSalesInvoiceTotalAmountColMapping = 1;
	public static final int CUSTOMER_INVOICE_TOTAL_SALES = 6;
	
	//Print Old Sales Invoice
	public static final int CustomerBIRSalesInvoiceDateRowMapping = 4;
	public static final int CustomerBIRSalesInvoiceDateColMapping = 5;
	public static final int CustomerBIRSalesInvoiceSoldToRowMapping = 5;
	public static final int CustomerBIRSalesInvoiceSoldToColMapping = 2;
	public static final int CustomerBIRSalesInvoiceAddressRowMapping = 6;
	public static final int CustomerBIRSalesInvoiceAddressColMapping = 2;
	public static final int CustomerBIRSalesInvoiceBusStyleRowMapping = 8;
	public static final int CustomerBIRSalesInvoiceBusStyleColMapping = 2;
	public static final int CustomerBIRSalesInvoiceTinRowMapping = 8;
	public static final int CustomerBIRSalesInvoiceTinColMapping = 5;
	public static final int CustomerBIRSalesInvoiceQuantityRowMapping = 11;
	public static final int CustomerBIRSalesInvoiceQuantityColMapping = 0;
	public static final int CustomerBIRSalesInvoiceUnitRowMapping = 11;
	public static final int CustomerBIRSalesInvoiceUnitColMapping = 1;
	public static final int CustomerBIRSalesInvoiceDescriptionRowMapping = 11;
	public static final int CustomerBIRSalesInvoiceDescriptionColMapping = 2;
	public static final int CustomerBIRSalesInvoiceUnitPriceRowMapping = 11;
	public static final int CustomerBIRSalesInvoiceUnitPriceColMapping = 5;
	public static final int CustomerBIRSalesInvoiceAmountRowMapping = 11;
	public static final int CustomerBIRSalesInvoiceAmountColMapping = 6;
	public static final int CustomerBIRSalesInvoiceTotalSalesRowMapping = 30;
	public static final int CustomerBIRSalesInvoiceTotalSalesColMapping = 6;
	public static final int CustomerBIRSalesInvoiceVatRowMapping = 31;
	public static final int CustomerBIRSalesInvoiceVatColMapping = 6;
	public static final int CustomerBIRSalesInvoiceTotalAmountRowMapping = 32;
	public static final int CustomerBIRSalesInvoiceTotalAmountColMapping = 6;

	//INVENTORY PRICING TYPES
	public static final String RAW_MATERIAL_ABBR = "RM";
	public static final String FINISHED_GOOD_ABBR = "FG";
	
	// AUTO NUMBERING CONSTANTS
	
	public static final String SUPPLIER = "Supplier";
	public static final String SUPPLIERPO = "SupplierPurchaseOrder";
	public static final String RECEIVINGREPORT = "ReceivingReport";
	public static final String SUPPLIERINVOICE = "SupplierInvoice";
	public static final String RETURNSLIP = "ReturnSlip";
	public static final String FPTS = "FPTS";
	public static final String RF = "RequisitionForm";

	public static final String SUPPLIER_PREFIX = "S";
	public static final String SUPPLIERPO_PREFIX = "SPO-";
	public static final String RECEIVINGREPORT_PREFIX = "RR-";
	
	public static final String CUSTOMER = "CUSTOMER";
	public static final String CUSTOMERPO = "CustomerPurchaseOrder";
	public static final String DELIVERYREPORT = "DeliveryReceipt";
	public static final String CUSTOMERINVOICE = "CustomerInvoice";

	
	public static final String INVENTORY_FPTS = "FPTS";
	public static final String INVENTORY_FPTS_PREFIX = "FPTS-";
	public static final String INVENTORY_REQUISITION_FORM = "RequisitionForm";
	public static final String INVENTORY_RF_PREFIX = "ORF-";
	public static final String INVENTORY_RETURN_SLIP_FORM = "ReturnSlip";
	public static final String INVENTORY_RETURN_SLIP_PREFIX = "RS-";
	
	public static final String CUSTOMER_PREFIX = "C";
	public static final String CUSTOMERPO_PREFIX = "CPO-";
	public static final String DELIVERYREPORT_PREFIX = "DR-";
	public static final String CUSTOMERINVOICE_PREFIX = "CI-";
	
	public static final String PETTYCASH = "PettyCash";
	public static final String CASHPAYMENT = "CashPayment";
	public static final String CHECKPAYMENT = "CheckPayment";
	
	public static final String PETTYCASH_PREFIX = "PC-";
	public static final String CASHPAYMENT_PREFIX = "CP-";
	
	public static final String CHECK_VOUCHER = "CheckVoucher";
	public static final String CHECK_VOUCHER_PREFIX = "CV-";
	
	public static final String CASHCHECKRECEIPTS = "CashCheckReceipts";
	public static final String OROTHERS = "OROthers";
	public static final String ORSALES = "ORSales";
	
	public static final String CASHCHECKRECEIPTS_PREFIX = "CCR-";
	public static final String REFERENCE_NO = "referenceNo";
	
	//VALIDATION
	public static final String MAXIMUM_LENGTH_200 = "MAXIMUM LENGTH: 200 characters";
	public static final String MAXIMUM_LENGTH_100 = "MAXIMUM LENGTH: 100 characters";

	public static final String EMPTY_ORDER_DETAILS = "ORDER DETAILS IS EMPTY";
	
	public static final String FROM_PERIOD = "FOR THE PERIOD FROM ";							
	public static final String TO = " TO ";
	
	//SUpplier Purchase Order Printing
	public static final int SupplierPurchaseOrderDateRowMapping = 7;
	public static final int SupplierPurchaseOrderDateColMapping = 5;
	public static final int SupplierPurchaseOrderNoRowMapping = 7;
	public static final int SupplierPurchaseOrderNoColMapping = 1;
	public static final int SupplierPurchaseOrderDeliveryDateRowMapping = 8;
	public static final int SupplierPurchaseOrderDeliveryDateColMapping = 1;
	public static final int SupplierPurchaseOrderAddressRowMapping = 10;
	public static final int SupplierPurchaseOrderAddressColMapping = 1;
	public static final int SupplierPurchaseOrderSupplierNameRowMapping = 9;
	public static final int SupplierPurchaseOrderSupplierNameColMapping = 1;
	public static final int SupplierPurchaseOrderContactNameRowMapping = 8;
	public static final int SupplierPurchaseOrderContactNameColMapping = 5;
	public static final int SupplierPurchaseOrderPaymentTermsRowMapping = 12;
	public static final int SupplierPurchaseOrderPaymentTermsColMapping = 1;
	public static final int SupplierPurchaseOrderFaxNoRowMapping = 12;
	public static final int SupplierPurchaseOrderFaxNoColMapping = 5;
	
	///Supplier Purchase Order Details
	public static final int SupplierPurchaseOrderQuantityRowMapping = 14;
	
	public static final int SupplierPurchaseOrderAmountColMapping = 7;
	public static final int SupplierPurchaseOrderQuantityColMapping = 1;
	public static final int SupplierPurchaseOrderUnitColMapping = 5;
	public static final int SupplierPurchaseOrderItemCodeColMapping = 0;
	public static final int SupplierPurchaseOrderDescriptionColMapping = 2;
	public static final int SupplierPurchaseOrderUnitPriceColMapping = 6;
	
	public static final int SupplierPurchaseOrderTotalAmountColMapping = 6;
	public static final int SupplierPurchaseOrderTotalAmountRowMapping = 19;
	public static final String TRADED_ITEM_ABBR ="TI";
	public static final String UTENSILS_ABBR ="U";
	public static final String OFFICE_SUPPLIES_ABBR = "OS";
	public static final double VAT_PERCENT = 1.12;
	public static final double VAT_AMOUNT_PERCENT = 0.12;
	

	//INVENTORY MOVEMENT ORDER TYPES
	public static final String ORDER_TYPE_DR ="DR";
	public static final String ORDER_TYPE_WAREHOUSE_TO_SUPPLIER ="WtoS";
	public static final String ORDER_TYPE_ORDER_REQUISITION ="OR";
	
	public static final String ORDER_TYPE_RR ="RR";
	public static final String ORDER_TYPE_CUSTOMER_TO_WAREHOUSE ="CtoW";
	public static final String ORDER_TYPE_PRODUCTION_TO_WAREHOUSE_INPUT ="PtoW";
	public static final String ORDER_TYPE_FPTS ="FPTS";
	

	public static final String CHANGE_TYPE_ADD = "ADD";
	public static final String CHANGE_TYPE_DEDUCT = "DEDUCT";
	
	//RETURN SLIPS ORDER TYPES
	public static final String RS_CUSTOMER_TO_WAREHOUSE ="CtoW";
	public static final String RS_WAREHOUSE_TO_SUPPLIER ="WtoS";
	public static final String RS_PRODUCTION_TO_WAREHOUSE ="PtoW";
	public static final String TIMESTAMP_FORMAT = "YYYY-MM-dd";
	public static final String RS_WAREHOUSE_TO_PRODUCTION = "WtoP";
	public static final int SUMMARY_TEMPLATE_FPTS_ORDER_COL_START = 10;
	public static final int SUMMARY_TEMPLATE_ORF_ORDER_COL_START = 7;
	public static final int SUMMARY_TEMPLATE_RS_ORDER_COL_START = 7;
	public static final String DATE_PREFIX = "DATE: ";
	
	
	
	//FINANCIALS 
	
	//Account Entries
	public static final String ACCOUNTS_PAYABLE = "Account Payable";
	public static final String INPUT_TAX = "Input Tax";
	public static final String TRANSACTION_IN_USE = "IN USE";
	public static final String TRANSACTION_NOT_IN_USE = "NOT IN USE";
	
	//LEDGER REPORTS 
	
	public static final String LEDGER_REPORTS_TITLE = "LEDGER OF ACCOUNTS";
	public static final String LEDGER_REPORTS_TEMPLATE = "/../financialreports/LedgeOfAccountsTemplate.xls";
	public static final String LEDGER_REPORTS_FILENAME = "Ledger Of Accounts - ";
	
	public static final String VAT_REPORT_TEMPLATE = "/../financialreports/VatReportTemplate.xls";
	public static final String VAT_REPORT_FILENAME = "Vat Report - ";

	public static final String TRIAL_BALANCE_REPORT_TEMPLATE = "/../financialreports/TrialBalanceTemplate.xls";
	public static final String TRIAL_BALANCE_REPORT_FILENAME = "Trial Balance - ";
	
	public static final String INCOME_STATEMENT_TEMPLATE = "/../financialreports/IncomeStatementTemplate.xls";
	public static final String INCOME_STATEMENT_FILENAME = "Income Statement - ";
	public static final int INCOME_STATEMENT_VARIABLES_COLUMN_START = 3;
	public static final int INCOME_STATEMENT_VALUES_COLUMN = 2;
	
	public static final String COGS_TEMPLATE = "/../financialreports/CoGSTemplate.xls";
	public static final String COGS_FILENAME = "Cost Of Goods Sold - ";
	public static final int COGS_VARIABLES_COLUMN_START = 3;
	public static final int COGS_VALUES_COLUMN = 2;
	
	public static final String COGM_TEMPLATE = "/../financialreports/CoGMTemplate.xls";
	public static final String COGM_FILENAME = "Cost Of Goods Manufactured - ";
	public static final int COGM_VARIABLES_COLUMN_START = 3;
	public static final int COGM_VALUES_COLUMN = 2;
	
	public static final String STOCKHOLDERS_EQUITY_TEMPLATE = "/../financialreports/StockHoldersEquityTemplate.xls";
	public static final String STOCKHOLDERS_EQUITY_FILENAME = "Schedule Of Changes in Stockholders Equity - ";
	public static final int STOCKHOLDERS_EQUITY_VARIABLES_COLUMN_START = 3;
	public static final int STOCKHOLDERS_EQUITY_VALUES_COLUMN = 2;
	
	public static final String BALANCE_SHEET_REPORT_TEMPLATE = "/../financialreports/BalanceSheetTemplate.xls";
	public static final String BALANCE_SHEET_REPORT_FILENAME = "Balance Sheet - ";
	public static final int BALANCE_SHEET_VARIABLES_COLUMN_START = 3;
	public static final int BALANCE_SHEET_VALUES_COLUMN = 2;
	public static final String SUMMARY_TEMPLATE_HEADER_ACCOUNT_ENTRY_SUMMARY = "Chart of Accounts";
	public static final String CHART_OF_ACCOUNT_FILENAME = "Chart of Accounts";
	public static final String JOURNAL_ENTRIES_TEMPLATE = "/../financialreports/JournalEntriesTemplate.xls";
	public static final String JOURNAL_ENTRIES_FILENAME = "Journal Entry Report - ";
	public static final int JOURNAL_ENTRIES_VARIABLES_COLUMN_START = 3;
	public static final int JOURNAL_ENTRIES_VALUES_COLUMN = 2;

	
	public static final String UNLISTED_ITEMS = "Unlisted Items";
	public static final String NOT_APPLICABLE = "N/A";
	
	
	/*
	 * IMPORT OFFLINE ORDERS INDEX VALUES
	 */
	public static final int IMPORT_OFFLINE_ORDER_STARTING_ROW = 3;
	
	public final static int IMPORT_COLUMN_CUSTOMER_NO =0;
	public final static int IMPORT_COLUMN_PO_DATE =1;
	public final static int IMPORT_COLUMN_PO_DELIVERY_DATE =2;
	public final static int IMPORT_COLUMN_PO_PAYMENT_DATE =3;
	public final static int IMPORT_COLUMN_PO_PAYMENT_TERMS =4;
	public final static int IMPORT_COLUMN_PO_ITEM_CODE =5;
	public final static int IMPORT_COLUMN_PO_UNLISTED_ITEM_DESCRIPTION =6;
	public final static int IMPORT_COLUMN_PO_UNLISTED_ITEM_UOM =7;
	public final static int IMPORT_COLUMN_PO_ITEM_QUANTITY =8;

	public final static int IMPORT_COLUMN_PO_NUMBER=9;
	
	public final static int IMPORT_COLUMN_DR_DATE =10;
	public final static int IMPORT_COLUMN_DR_SHIPPING_METHOD =11;
	public final static int IMPORT_COLUMN_DR_SHIPPING_DATE =12;
	public final static int IMPORT_COLUMN_DR_DUE_DATE =13;
	public final static int IMPORT_COLUMN_DR_REMARKS =14;
	public final static int IMPORT_COLUMN_DR_ITEM_CODE =15;
	public final static int IMPORT_COLUMN_DR_UNLISTED_ITEM_DESCRIPTION =16;
	public final static int IMPORT_COLUMN_DR_ITEM_QUANTITY =17;
	
	
	/**
	 * USER LOCATIONS
	 */
	public static final String LOCATION_MANILA = "MNL";
	


	
	
}
