<!doctype html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="/tld/Authorization.tld"%>
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
	      <auth:isAuth role="0"> <li id="supplierMenu0"><a href="showSupplierProfileForm.action" >SUPPLIER PROFILE</a></li> </auth:isAuth>
	      <auth:isAuth role="1"> <li id="supplierMenu1"><a href="<s:url action="newSupplierEntryAction"><s:param name="subModule" value="%{'purchaseOrder'}"/></s:url>">PURCHASE ORDER</a></li></auth:isAuth>
	      <auth:isAuth role="2"> <li id="supplierMenu2"><a href="<s:url action="newSupplierEntryAction"><s:param name="subModule" value="%{'receivingReport'}"/></s:url>">RECEIVING REPORT</a></li></auth:isAuth>
		  <auth:isAuth role="3"> <li id="supplierMenu3"><a href="<s:url action="newSupplierEntryAction"><s:param name="subModule" value="%{'supplierInvoice'}"/></s:url>">SUPPLIER INVOICE</a></li></auth:isAuth>
		  <auth:isAuth role="37">  <li id="supplierMenu4"><a href="showSearchSupplierForm.action">--Search--</a></li></auth:isAuth>
		  <auth:isAuth role="4">  <li id="supplierMenu5"><a href="showSupplierSummaryForm.action">--Summary Report--</a></li></auth:isAuth>
	    </ol>
	  </li>
	  <li>CUSTOMERS
	    <ol id="customerMenu">
	       <auth:isAuth role="5"><li id="customerMenu0"><a href="showCustomerProfileForm.action">CUSTOMER PROFILE</a></li></auth:isAuth>
	     <auth:isAuth role="6"> <li id="customerMenu1"><a href="<s:url action="newCustomerEntryAction"><s:param name="subModule" value="%{'purchaseOrder'}"/></s:url>">PURCHASE ORDER</a></li></auth:isAuth>
	     <auth:isAuth role="7"> <li id="customerMenu2"><a href="<s:url action="newCustomerEntryAction"><s:param name="subModule" value="%{'deliveryReceipt'}"/></s:url>">DELIVERY RECEIPT</a></li></auth:isAuth>
	     <auth:isAuth role="8"> <li id="customerMenu3"><a href="<s:url action="newCustomerEntryAction"><s:param name="subModule" value="%{'invoice'}"/></s:url>">SALES INVOICE</a></li></auth:isAuth>
		 <auth:isAuth role="38"> <li id="customerMenu4"><a href="showSearchCustomerForm.action">--Search--</a></li></auth:isAuth>
		 <auth:isAuth role="9"> <li id="customerMenu5"><a href="showCustomerSummaryForm.action">--Summary Report--</a></li></auth:isAuth>
	    </ol>
	  </li>
	  <li>INVENTORY
	    <ol id="inventoryMenu">
	     <auth:isAuth role="10"> <li id="inventoryMenu0"><a href="loadLookUpItems.action?requestingModule=rawMaterial">RAW MATERIALS</a></li></auth:isAuth>
	     <auth:isAuth role="14"> <li id="inventoryMenu2"><a href="loadLookUpItems.action?requestingModule=finishedGood">FINISHED GOODS</a></li></auth:isAuth>
	     <auth:isAuth role="11"> <li id="inventoryMenu1"><a href="loadLookUpItems.action?requestingModule=tradedItems">TRADED ITEMS</a></li></auth:isAuth>
  		 <auth:isAuth role="13"> <li id="inventoryMenu4"><a href="loadLookUpItems.action?requestingModule=utensils">UTENSILS</a></li></auth:isAuth>
	     <auth:isAuth role="12"> <li id="inventoryMenu11"><a href="loadLookUpItems.action?requestingModule=ofcSup">OFFICE SUPPLIES</a></li> </auth:isAuth>
	     <auth:isAuth role="34"> <li id="inventoryMenu5"><a href="loadLookUpItems.action?requestingModule=unlistedItems">UNLISTED ITEMS</a></li></auth:isAuth>
	     <auth:isAuth role="16"> <li id="inventoryMenu6"><a href="<s:url action="newInventoryEntryAction"><s:param name="subModule" value="%{'rf'}"/></s:url>">ORDER REQUISITION</a></li></auth:isAuth>
	     <auth:isAuth role="15"> <li id="inventoryMenu3"><a href="<s:url action="newInventoryEntryAction"><s:param name="subModule" value="%{'fpts'}"/></s:url>">FP TRANSFER SLIP</a></li></auth:isAuth>
	     <auth:isAuth role="17"> <li id="inventoryMenu7"><a href="<s:url action="newInventoryEntryAction"><s:param name="subModule" value="%{'returnSlip'}"/></s:url>">RETURN SLIPS</a></li></auth:isAuth>
	     <auth:isAuth role="39"> <li id="inventoryMenu8"><a href="showSearchInventoryForm.action">--Search--</a></li></auth:isAuth>
	     <auth:isAuth role="18"> <li id="inventoryMenu9"><a href="showInventorySummaryForm.action">--Summary Report--</a></li></auth:isAuth>
	     <auth:isAuth role="35"> <li id="inventoryMenu10"><a href="showStockStatusForm.action">--Stock Status--</a></li></auth:isAuth>
	     </ol>
	  </li>	
	  <li>DISBURSEMENTS
	    <ol id="disbursementMenu">
	     <auth:isAuth role="19"> <li id="disbursementMenu0"><a href="<s:url action="newDisbursementEntryAction"><s:param name="subModule" value="%{'pettyCash'}"/></s:url>">PETTY CASH</a></li></auth:isAuth>
	     <auth:isAuth role="20"> <li id="disbursementMenu1"><a href="<s:url action="newDisbursementEntryAction"><s:param name="subModule" value="%{'cashPayment'}"/> </s:url>">CASH PAYMENT</a></li></auth:isAuth>
	     <auth:isAuth role="21"> <li id="disbursementMenu2"><a href="<s:url action="newDisbursementEntryAction"><s:param name="subModule" value="%{'checkPayment'}"/></s:url>">CHECK PAYMENT</a></li></auth:isAuth>
	      <auth:isAuth role="22"><li id="disbursementMenu3"><a href="<s:url action="newDisbursementEntryAction"><s:param name="subModule" value="%{'checkVoucher'}"/></s:url>">CHECK VOUCHER</a></li></auth:isAuth>
	     <auth:isAuth role="41"> <li id="disbursementMenu4"><a href="showSearchDisbursementForm.action">--Search--</a></li></auth:isAuth>
		 <auth:isAuth role="23"><li id="disbursementMenu5"><a href="<s:url action="showDisbursementSummaryForm.action"><s:param name="disbursementModule" value="%{'PettyCash'}"/></s:url>">--Summary Report--</a></li></auth:isAuth>
	    </ol>
	  </li>
	  <li>RECEIPTS
	    <ol id="receiptsMenu">
	     <auth:isAuth role="24"> <li id="receiptsMenu0"><a href="<s:url action="newReceiptEntry.action"><s:param name="subModule" value="%{'orSales'}"/></s:url>">OR-SALES</a></li></auth:isAuth>
	     <auth:isAuth role="25"> <li id="receiptsMenu1"><a href="<s:url action="newReceiptEntry.action"><s:param name="subModule" value="%{'orOthers'}"/></s:url>">OR-OTHER INCOMES</a></li></auth:isAuth>
	     <auth:isAuth role="26"> <li id="receiptsMenu2"><a href="<s:url action="newReceiptEntry.action"><s:param name="subModule" value="%{'ccReceipts'}"/></s:url>">CASH/CHECK</a></li></auth:isAuth>
	     <auth:isAuth role="40"> <li id="receiptsMenu3"><a href="showSearchReceiptForm.action">--Search--</a></li></auth:isAuth>
		 <auth:isAuth role="27"> <li id="receiptsMenu4"><a href="<s:url action="showReceiptSummaryForm.action"><s:param name="receiptsModule" value="%{'ORSales'}"/></s:url>">--Summary Report--</a></li></auth:isAuth>
	    </ol>
	  </li>
	   <li>FINANCIALS
	    <ol id="financialsMenu">
	     <auth:isAuth role="28"> <li id="financialsMenu0"><a href="<s:url action= "loadParentCodeAction"><s:param name="subModule" value="%{'accountEntryProfile'}"/></s:url>">Account Entry Profile</a></li></auth:isAuth>
	     <auth:isAuth role="29"> <li id="financialsMenu1"><a href="<s:url action="loadParentCodeAction"><s:param name="subModule" value="%{'journalEntryProfile'}"/></s:url>">General Journal</a></li></auth:isAuth>
	     <auth:isAuth role="42"> <li id="financialsMenu2"><a href="<s:url action= "generateFinancialReportsAction"><s:param name="reportType" value="%{'none'}"/></s:url>">Reports</a></li></auth:isAuth>
	      <auth:isAuth role="30"><li id="financialsMenu3"><a href="showSearchFinancialsForm.action">--Search--</a></li></auth:isAuth>
	    </ol>
	  </li>
	  <li>TOOLS
	    <ol id="toolsMenu">
	     <auth:isAuth role="31"> <li id="toolsMenu0"><a href="<s:url action="listCustomer.action"><s:param name="subModule" value="%{'template'}"/></s:url>">Template Generator</a></li></auth:isAuth>
	     <auth:isAuth role="36"> <li id="toolsMenu4"><a href="showImportOfflineOrdersForm.action">Import Offline Orders</a></li></auth:isAuth>
	     <auth:isAuth role="32"> <li id="toolsMenu1"><a href="<s:url action="loadRoleListAction"><s:param name="securityModule" value="%{'userAccount'}"/></s:url>">User Account</a></li></auth:isAuth>
	     <auth:isAuth role="33"> <li id="toolsMenu2"><a href="loadModulesAction.action">User Role</a></li></auth:isAuth>
	     <auth:isAuth role="43"> <li id="toolsMenu3"><a href="showSearchSecurityForm.action">-=Search=-</a></li></auth:isAuth>
	    
	    </ol>
	  </li>
	  
	</ul>
</div>
</s:form>
</body>
</html>