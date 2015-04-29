<!doctype html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <meta charset="utf-8">
    <title>Main Menu</title>
  
    <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
  </head>

<body>
<s:form>
<div style="float:left;" id="formNya">
	<ul id="menu">
	  <li>SUPPLIERS
	    <ol id="supplierMenu">
	      <li id="supplierMenu0"><a href="showSupplierProfileForm.action" >SUPPLIER PROFILE</a></li> 
	      <li id="supplierMenu1"><a href="<s:url action="newSupplierEntryAction"><s:param name="subModule" value="%{'purchaseOrder'}"/></s:url>">PURCHASE ORDER</a></li>
	      <li id="supplierMenu2"><a href="<s:url action="newSupplierEntryAction"><s:param name="subModule" value="%{'receivingReport'}"/></s:url>">RECEIVING REPORT</a></li>
		  <li id="supplierMenu3"><a href="<s:url action="newSupplierEntryAction"><s:param name="subModule" value="%{'supplierInvoice'}"/></s:url>">SUPPLIER INVOICE</a></li>
		  <li id="supplierMenu4"><a href="showSearchSupplierForm.action">--Search--</a></li>
		  <li id="supplierMenu5"><a href="showSupplierSummaryForm.action">--Summary Report--</a></li>
	    </ol>
	  </li>
	  <li>CUSTOMERS
	 
	    <ol id="customerMenu">
	      <li id="customerMenu0"><a href="showCustomerProfileForm.action">CUSTOMER PROFILE</a></li>
	      <li id="customerMenu1"><a href="<s:url action="newCustomerEntryAction"><s:param name="subModule" value="%{'purchaseOrder'}"/></s:url>">PURCHASE ORDER</a></li>
	      <li id="customerMenu2"><a href="<s:url action="newCustomerEntryAction"><s:param name="subModule" value="%{'deliveryReceipt'}"/></s:url>">DELIVERY RECEIPT</a></li>
	      <li id="customerMenu3"><a href="<s:url action="newCustomerEntryAction"><s:param name="subModule" value="%{'invoice'}"/></s:url>">SALES INVOICE</a></li>
		  <li id="customerMenu4"><a href="showSearchCustomerForm.action">--Search--</a></li>
		  <li id="customerMenu5"><a href="showCustomerSummaryForm.action">--Summary Report--</a></li>
	    </ol>
	  </li>
	  <li>INVENTORY
	    <ol id="inventoryMenu">
	      <li id="inventoryMenu0"><a href="loadLookUpItems.action?requestingModule=rawMaterial">RAW MATERIALS</a></li>
	      <li id="inventoryMenu2"><a href="loadLookUpItems.action?requestingModule=finishedGood">FINISHED GOODS</a></li>
	      <li id="inventoryMenu1"><a href="loadLookUpItems.action?requestingModule=tradedItems">TRADED ITEMS</a></li>
  <!--  <li id="inventoryMenu4"><a href="loadLookUpItems.action?requestingModule=utensils">UTENSILS</a></li>-->
	      <li id="inventoryMenu5"><a href="loadLookUpItems.action?requestingModule=unlistedItems">UNLISTED ITEMS</a></li>
	      <li id="inventoryMenu6"><a href="<s:url action="newInventoryEntryAction"><s:param name="subModule" value="%{'rf'}"/></s:url>">ORDER REQUISITION</a></li>
	      <li id="inventoryMenu3"><a href="<s:url action="newInventoryEntryAction"><s:param name="subModule" value="%{'fpts'}"/></s:url>">FP TRANSFER SLIP</a></li>
	      <li id="inventoryMenu7"><a href="<s:url action="newInventoryEntryAction"><s:param name="subModule" value="%{'returnSlip'}"/></s:url>">RETURN SLIPS</a></li>
	      <li id="inventoryMenu8"><a href="showSearchInventoryForm.action">--Search--</a></li>
	      <li id="inventoryMenu9"><a href="showInventorySummaryForm.action">--Summary Report--</a></li>
	      <li id="inventoryMenu10"><a href="showStockStatusForm.action">--Stock Status--</a></li>
	     </ol>
	  </li>	
	  <li>DISBURSEMENTS
	    <ol id="disbursementMenu">
	      <li id="disbursementMenu0"><a href="<s:url action="newDisbursementEntryAction"><s:param name="subModule" value="%{'pettyCash'}"/></s:url>">PETTY CASH</a></li>
	      <li id="disbursementMenu1"><a href="<s:url action="newDisbursementEntryAction"><s:param name="subModule" value="%{'cashPayment'}"/> </s:url>">CASH PAYMENT</a></li>
	      <li id="disbursementMenu2"><a href="<s:url action="newDisbursementEntryAction"><s:param name="subModule" value="%{'checkPayment'}"/></s:url>">CHECK PAYMENT</a></li>
	      <li id="disbursementMenu3"><a href="<s:url action="newDisbursementEntryAction"><s:param name="subModule" value="%{'checkVoucher'}"/></s:url>">CHECK VOUCHER</a></li>
	      <li id="disbursementMenu4"><a href="showSearchDisbursementForm.action">--Search--</a></li>
		  <li id="disbursementMenu5"><a href="<s:url action="showDisbursementSummaryForm.action"><s:param name="disbursementModule" value="%{'PettyCash'}"/></s:url>">--Summary Report--</a></li>
	    </ol>
	  </li>
	  <li>RECEIPTS
	    <ol id="receiptsMenu">
	      <li id="receiptsMenu0"><a href="showORSalesForm.action">OR-SALES</a></li>
	      <li id="receiptsMenu1"><a href="showOROtherIncomeForm.action">OR-OTHER INCOMES</a></li>
	      <li id="receiptsMenu2"><a href="newReceiptEntry.action">CASH/CHECK</a></li>
	      <li id="receiptsMenu3"><a href="showSearchReceiptForm.action">--Search--</a></li>
		  <li id="receiptsMenu4"><a href="<s:url action="showReceiptSummaryForm.action"><s:param name="receiptsModule" value="%{'ORSales'}"/></s:url>">--Summary Report--</a></li>
	    </ol>
	  </li>
	   <li>FINANCIALS
	    <ol id="financialsMenu">
	      <li id="financialsMenu0"><a href="<s:url action= "loadParentCodeAction"><s:param name="subModule" value="%{'accountEntryProfile'}"/></s:url>">Account Entry Profile</a></li>
	      <li id="financialsMenu1"><a href="<s:url action="loadParentCodeAction"><s:param name="subModule" value="%{'journalEntryProfile'}"/></s:url>">General Journal</a></li>
	      <li id="financialsMenu2"><a href="<s:url action= "generateFinancialReportsAction"><s:param name="reportType" value="%{'none'}"/></s:url>">Reports</a></li>
	      <li id="financialsMenu3"><a href="showSearchFinancialsForm.action">--Search--</a></li>
	    </ol>
	  </li>
	  <li>TOOLS
	    <ol id="toolsMenu">
	      <li id="toolsMenu0"><a href="<s:url action="listCustomer.action"><s:param name="subModule" value="%{'template'}"/></s:url>">Template Generator</a></li>
	      <li id="toolsMenu1"><a href="<s:url action="loadRoleListAction"><s:param name="securityModule" value="%{'userAccount'}"/></s:url>">User Account</a></li>
	      <li id="toolsMenu2"><a href="showUserRoleForm.action">User Role</a></li>
	      <li id="toolsMenu3"><a href="showSearchSecurityForm.action">-=Search=-</a></li>
	    
	    </ol>
	  </li>
	  
	</ul>
</div>
</s:form>
</body>
</html>