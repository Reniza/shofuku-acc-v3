<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ taglib prefix="c" uri="/tld/c.tld"%>
<%@ taglib prefix="auth" uri="/tld/Authorization.tld"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
<script type="text/javascript" src="js/hideParameter.js"></script>
<script type="text/javascript" src="js/deleteConfirmation.js"></script>
<script type="text/javascript" src="js/expandingSection.js"></script> 
 <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
	<script type="text/javascript" src="js/Transactions.js"></script>
  <sx:head parseContent="true"/>
	 <script type='text/javascript'>
	var startWith=0;
	var subMenu=3;
	</script>
<title>Supplier Invoice Form</title>
</head>

<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
<h4 class="title">SUPPLIER</h4>
	<s:form action="newSupplierEntryAction" validate="true" id="invoiceForm">

	<div class="form" id="wholeForm">
			<div class="errors">
			<s:actionerror/>
			<s:actionmessage/>	
	</div>

		<h3 class="form" onclick="javascript:collapseSection('arrow1','div1')"><img id="arrow1"  src="images/expand2.jpg"/>PROFILE</h3>
		<div id="div1" class="sectionDiv">
			<p>
				<table class="form">
					<tr>
						<th colspan="6">Profile Details</th>
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Supplier ID:" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.supplierId}"></s:textfield></td>
						<td><s:textfield disabled="true" label="Supplier Status:" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.supplierStatus}"></s:textfield></td> 			
						<td><s:textfield disabled="true" label="Payment Term:" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.paymentTerm}"></s:textfield></td> 
					</tr>
					</table>
					<table class="form">
					<tr>
						<td class="others">Supplier Name:</td>
						<td colspan="4"><s:textfield disabled="true" theme="simple" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.supplierName}" size="90"></s:textfield>
						</tr>
				</table>
			</p>
			<p>
				<table class="form">
					<tr>
						<th colspan="6">Contact Details</th>
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Contact Name:" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.contactName}"  size="50" ></s:textfield></td>
						<td><s:textfield disabled="true" label="Contact Title:" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.contactTitle}"></s:textfield></td>
					</tr>
				</table>
				<table class="form">
					<tr>
						<td><s:label label="Email Address:"/><a href="mailto:<s:property value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.emailAddress}"/>"><s:property value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.emailAddress}"/></a></td>
					</tr>
					<tr>
						<td><s:label label="Website:"/><a href="<s:url value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.website}"/>" target="_blank"><s:property value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.website}"/></a></td>
					</tr>
					<tr>
						<td><s:textfield  label="Company Address:" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.companyAddress}" size="90"></s:textfield></td>
					</tr>
				</table>
				<table class="form">
				    <tr>
						<th colspan="6">Contact Numbers Details</th>
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Phone Number:(##)###-####" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.phoneNumber}"></s:textfield></td>
						<td><s:textfield disabled="true" label="Fax Number:(##)###-####" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.faxNumber}"></s:textfield></td>
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Mobile Number:(##)###-####" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.mobileNumber}"></s:textfield></td>
					</tr>
				</table>
			</p>	
		</div>
		<h3 class="form" onclick="javascript:collapseSection('arrow2','div2')"><img id="arrow2"  src="images/expand2.jpg"/>PURCHASE ORDER</h3>
		<div id="div2" class="sectionDiv">
		<p>
			<table class="form">
				<tr>
					<th colspan="6">Purchase Order Details</th>
				</tr>
				<tr>
					<td><s:textfield disabled="true" label="Purchase Order No:" value="%{invoice.receivingReport.supplierPurchaseOrder.supplierPurchaseOrderId}"></s:textfield></td>
					<td><s:textfield disabled="true" label="Purchase Order Date:" value="%{invoice.receivingReport.supplierPurchaseOrder.purchaseOrderDate}"></s:textfield>
					<td><s:textfield disabled="true" label="Supplier ID:" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.supplierId}"></s:textfield></td>
				
				</tr>
				<tr>
					<td><s:textfield disabled="true" label="Delivery Date:" value="%{invoice.receivingReport.supplierPurchaseOrder.dateOfDelivery}"></s:textfield></td>
					<td><s:textfield disabled="true" label="Payment Term:" value="%{invoice.receivingReport.supplierPurchaseOrder.supplier.paymentTerm}"></s:textfield></td>
					<td><s:textfield disabled="true" size="10" value="%{invoice.receivingReport.supplierPurchaseOrder.totalAmount}" label="Total Amount: PHP"></s:textfield></td>
				
					<!--  <td><s:textfield disabled="true" label="Payment Date:" value="%{invoice.receivingReport.supplierPurchaseOrder.paymentDate}"></s:textfield></td> -->
				</tr>
				
			</table>
		</p>	
		</div>

		<h3 class="form" onclick="javascript:collapseSection('arrow3','div3')"><img id="arrow3"  src="images/expand2.jpg"/>RECEIVING REPORT</h3>
		<div id="div3" class="sectionDiv">
			<p>
				<table class="form">
					<tr>
						<th colspan="6">Receiving Report Details</th>
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Receiving Report No:" value="%{invoice.receivingReport.receivingReportNo}"></s:textfield></td>
						<td><s:textfield disabled="true" label="Receiving Report Date:" value="%{invoice.receivingReport.receivingReportDate}"></s:textfield></td>
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Purchase Order No:" value="%{invoice.receivingReport.supplierPurchaseOrder.supplierPurchaseOrderId}"></s:textfield></td>
						<td><s:textfield disabled="true" label="Remarks:" value="%{invoice.receivingReport.remarks}"/>
						</td>
					</tr>
					<tr>
					<td><s:textfield disabled="true" label="Payment Date:" value="%{invoice.receivingReport.receivingReportPaymentDate}"/>
						</td>
					<td><s:textfield disabled="true" size="10" value="%{invoice.receivingReport.totalAmount}" label="Total Amount: PHP"></s:textfield></td>
					</tr>
				</table>
			</p>	
		</div>

		<div id="invForm">
		<h3 class="form">SUPPLIER INVOICE</h3>
			<p>
				<table class="form">
					<tr>
						<th colspan="6">Invoice Details</th>
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="Supplier Invoice No:" name="invoice.supplierInvoiceNo" id="supplierInvoiceNo"></s:textfield></td>
						<s:hidden name="rrId" value="%{rr.receivingReportNo}"/>
						<s:hidden name="invId" value="%{invoice.supplierInvoiceNo}"/>
						
						<td class="others">Invoice Date:</td>
						<td><sx:datetimepicker name="invoice.supplierInvoiceDate" displayFormat="MMM-dd-yyyy" displayWeeks="5"></sx:datetimepicker></td>
					</tr>
					<tr>		
						<td class="others">Receiving Report No:</td>
						<td><sx:autocompleter listValue="receivingReportNo" resultsLimit="-1" list="receivingReportNoList" maxlength="30"  name="invoice.receivingReport.receivingReportNo"></sx:autocompleter></td>
						
						<td><s:select disabled="%{forWhat}" label="Remarks:" name="invoice.remarks"
							list="#{'CORRECT':'CORRECT','OVER':'OVER','SHORT':'SHORT'}"/></td>
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="Total Purchases: PHP" name="invoice.debit1Amount"></s:textfield></td>
					</tr>
					
					</table>
					<table class="form">
						<tr>
							<th colspan="6">VAT Details</th>
						</tr>
					<tr>
						<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
						<td><s:textfield disabled="%{forWhat}" label="Vatable Amount: PHP" name="invoice.vatDetails.vattableAmount"></s:textfield></td>
						<td><s:textfield disabled="%{forWhat}" label="VAT Amount: PHP" name="invoice.vatDetails.vatAmount"></s:textfield></td>
						<!--END: 2013 - PHASE 3 : PROJECT 4: AZHEE-->
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="TIN:" name="invoice.vatDetails.tinNumber"></s:textfield></td>
					<!--  	<td><s:textfield disabled="%{forWhat}" label="Reference No:" name="invoice.vatDetails.orNo"></s:textfield></td> -->
					</tr>
					</table>
			
			<s:if test="%{forWhatDisplay == 'edit'}">			
					<!--START 2013 - PHASE 3 : PROJECT 1: MARK -->
			<div id ="transactions">
			<table class="form">
				<tr>
					<th>Accounting Entries</th>
				</tr>
			</table>
			
			
			<table class="results" id="Transactions">
					<tr class="others">
					<th class="desc">Delete?</th>
					<th class="desc">Accounting Profile</th>
					<th class="desc">Amount</th>
					<th class="desc">Debit/Credit</th>
					</tr>
					<c:forEach items="${transactionList}" var="transactions" varStatus="ctr">
					<tr>
						<td><input type="checkbox" name="chk"/></td>
						<td>
							<select  id="parentCode" name="transactionList[${ctr.index}].accountEntry.accountCode" onChange = "javascript:showTransactionAction([${ctr.index}],this)">
								<c:forEach items="${accountProfileCodeList}" var="profile">
									<option ${profile.accountCode == transactions.accountEntry.accountCode ? 'selected="selected"' : ''}
										value="${profile.accountCode}">${profile.accountCode} - ${profile.name}</option>
								</c:forEach>
							</select>
						</td>
					 	<td><input type="text" name="transactionList[${ctr.index}].amount" value="${transactions.amount}" size="15"/></td>
					 		
					 	<c:forEach items="${accountProfileCodeList}" var="profile">
					 		<input type="hidden" name="accountCodeActions.${profile.accountCode}" id="accountCodeActions.${profile.accountCode}" value="${profile.accountingRules.supplierInvoice}"/>
						</c:forEach>
					 	<td><input size="10" readonly="readonly" type="text" id="transactionList[${ctr.index}].transactionAction" name="transactionList[${ctr.index}].transactionAction" value="${transactions.transactionAction}"/></td>
					</tr>
					</c:forEach>
				</table>
				<table>
					<tr>
						<td><input class="myButtons" type="button" value="add" onclick="javascript:addRow('Transactions')"></input></td>
						<td><input class="myButtons" type="button" value="delete" onclick="javascript:deleteRow('Transactions')"></input></td>
					</tr>
				</table>
				<p>
			</div>
			</s:if>
			<!--END 2013 - PHASE 3 : PROJECT 1: MARK -->
					
				
			</p>
			<table class="form">
			<tr>
						<th colspan="6">Cheque Voucher Details</th>
					</tr>
					<tr>
						<td><s:textfield readonly="true" label="Remaining Balance: PHP" name="invoice.remainingBalance"></s:textfield></td>
					</tr>
			</table>
			
			<table class="results">
					
					<tr>
						<th>CHEQUE VOUCHER NO</th>
						<th>CHEQUE VOUCHER DATE</th>
						<th>AMOUNT PAID</th>
					</tr>
					<s:iterator value="checkVoucherList">
					<tr>
						<!-- <td align="left"><s:property value="checkVoucherNumber"/></td> -->
						<td align="left"><s:url id="displayId" action="editDisbursementAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="chp.checkVoucherNumber" value="%{checkVoucherNumber}">checkVoucherNumber</s:param>
										<s:param name="subModule" value="%{'checkVoucher'}">subModule</s:param>
									</s:url>
									<s:a href="%{displayId}"><s:property value="checkVoucherNumber"/></s:a>
							</td>
						<td align="left"><s:property value="checkVoucherDate"/></td>
						<td align="right"><s:property value="amountToPay"/></td>			
					</tr>
					
					</s:iterator>
					<tr>
						<td colspan=2 align="right" class="totalAmount">Total Amount Paid PHP:</td>
						<td align="right" class="totalAmount"><s:property value="tempTotal"/></td>
					</tr>
					</table>
					<p></p>
				<table class="form">
					<tr>
						
								<s:hidden name="parentPage" value="SupplierInvoice"/>
								<s:hidden name="forWhat" value="%{forWhat}"/>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode"     	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription"	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity"       value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM" 		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVatAmount"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVattableAmount"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
								
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsItemCode"     	value="%{poDetailsHelper.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsDescription"	value="%{poDetailsHelper.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsQuantity"       value="%{poDetailsHelper.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsUOM" 		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsCost"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
						
						<th colspan="6">Order Details</th>
					</tr>
				</table>
		<table class="results">
		<tr valign="top">
		<td>	
				<table class="compare">
					<tr>
						<td colspan="6" class="others">Supplier Invoice Order Details</td>
					</tr>
					<tr class="others">
								<td class="desc">Item Code</td>
								<td class="desc">QTY</td>
								<td class="header" width="250px">Description</td>
								<td>UOM</td>
								<td>Unit Cost</td>
								<td>Amount</td>
					</tr>
							<s:iterator value="poDetailsHelper.purchaseOrderDetailsList">
					<!--START: 2014 - ITEM COLORING-->
					<s:set name="currentItrItemType" value="itemType"/>
					<s:if test="%{#currentItrItemType=='rawMat'}">
						<tr style="color:green">
					</s:if>
					<s:elseif  test="%{#currentItrItemType == 'tradedItems'}">
						<tr style="color:blue">
					</s:elseif >
					<s:elseif  test="%{#currentItrItemType == 'finGood'}">
						<tr style="color:red">
					</s:elseif >
					<s:else>
						<tr style="color:black">
					</s:else>
					<!--END: 2014 - ITEM COLORING-->
									<td class="desc"><s:property value="itemCode" /></td>
									<td class="desc"><s:property value="quantity" /></td>
									<td class="desc"><s:property value="description" /></td>
									<td><s:property value="unitOfMeasurement" /></td>
									<td><s:property value="unitCost" /></td>
									<td><s:property value="amount" /> </td>
					</tr>
							</s:iterator>
					<tr>
						
							<td colspan="5" class="total">Total: PHP</td>
							<td class="totalAmount"><s:property value="%{poDetailsHelper.totalAmount}" />
							
							<!-- 
							<td class="totalAmount"><s:property value="%{invoice.purchaseOrderDetailsTotalAmount}" />
							<s:hidden id="totalAmountOfPurchases" name="totalAmountOfPurchases" value="%{invoice.purchaseOrderDetailsTotalAmount}" /> 
							</td>-->
					</tr>
							
				</table>
		</td>
		<td>
				<table class="compare">
					<tr>
							<td colspan="6" class="others">Receiving Report Details</td>
					</tr>
					
					<tr class="others">
								<td class="desc">Item Code</td>
								<td class="desc">QTY</td>
								<td class="header" width="250px">Description</td>
								<td>UOM</td>
								<td>Unit Cost</td>
								<td>Amount</td>
					</tr>

					<s:iterator value="poDetailsHelperToCompare.purchaseOrderDetailsList">
					<tr>
									<td class="desc"><s:property value="itemCode" /></td>
									<td class="desc"><s:property value="quantity" /></td>
									<td class="desc"><s:property value="description" /></td>
									
									<td><s:property value="unitOfMeasurement" /></td>
									<td><s:property value="unitCost" /></td>
									<td><s:property value="amount" /></td>
					</tr>
					</s:iterator>
					<tr>
							<td colspan="5" class="total">Total: PHP</td>
							<td class="totalAmount"><s:property value="%{poDetailsHelperToCompare.totalAmount}" /></td>
					</tr>
				</table>
		</td>
		</tr>
		</table>
		
		</div>
	</div>

	<div class="forButtons">
		<p>
			<table class="forButtons" align="center">
				<tr><s:hidden name="subModule" value="invoice"/>
					
				
				<s:if test="%{forWhatDisplay == 'edit'}">	
					<td><input class="myButtons" type="button" value="Edit" name="editSupplierProfile" onclick="javascript:toggleAlert('invForm','supplierInvoiceNo');isCheckOnly();"></input></td>
					<td><s:submit cssClass="myButtons" type="button" disabled="%{forWhat}" id="bManageOrderDetails"
								value="Manage Order Details" name="addOrderDetailAction"
								action="addOrderDetailAction"></s:submit></td>
					<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateSupplierAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Delete" onclick="javascript:supplierConfirmation('invoiceForm')"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Print" name="newSupplierProfile"  action="printSupplierAction"></s:submit></td>
					<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','invoiceForm');" value="New Entry"></input></td>
					<s:hidden name="voucher" value="checkVoucher"/>
				</s:if>
				
				<s:else>
					<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="New Entry" name="newSupplierProfile" action="addSupplierAction"></s:submit></td>
					<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','invoiceForm');" value="Cancel"></input></td>
				
				</s:else>
				</tr>
			</table>
		</p>
	</div>
</s:form>
</div>
</s:if>
<!-- DIV for print -->
<s:else>
<div class="print">
	<jsp:include page="/jsp/util/companyHeader.jsp"/>
	<h3 class="form">SUPPLIER INVOICE</h3>
			<p>
				<table class="form">
					<tr>
						<th colspan="6">Invoice Details</th>
					</tr>
					<tr>
						<td><s:textfield label="Supplier Invoice No:" value="%{invoice.supplierInvoiceNo}"></s:textfield></td>
						<td><s:textfield label="Invoice Date:" value="%{invoice.supplierInvoiceDate}"></s:textfield></td>
					</tr>
					<tr>		
						<td><s:textfield label="Receiving Report No:" value="%{invoice.receivingReport.receivingReportNo}"></s:textfield></td>
						<td><s:textfield label="Remarks:" value="%{invoice.remarks}"/></td>
					</tr>
					
				</table>
				<table class="form">
						<tr>
							<th colspan="6">VAT Details</th>
						</tr>
					<tr>
						<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
						<td><s:textfield label="Vatable Amount: PHP" value="%{invoice.vatDetails.vattableAmount}"></s:textfield></td>
						<td><s:textfield label="VAT Amount: PHP" value="%{invoice.vatDetails.vatAmount}"></s:textfield></td>
						<!--END: 2013 - PHASE 3 : PROJECT 4: AZHEE-->
					</tr>
					<tr>
						<td><s:textfield label="Total Purchases:" value="%{invoice.debit1Amount}"></s:textfield></td>
						<td><s:textfield label="Reference No:" value="%{invoice.vatDetails.vatReferenceNo}"></s:textfield></td>
					</tr>
				</table>
			</p>
						<!--START 2013 - PHASE 3 : PROJECT 1: MARK -->
			<div id ="transactions">
			<table class="form">
				<tr>
					<th>Accounting Entries</th>
				</tr>
			</table>
			<table class="results" border="1">
					<tr class="others">
					<th class="desc">CODE</th>
					<th class="desc">ACCOUNT NAME</th>
					<th class="desc">AMOUNT</th>
					</tr>
						<s:iterator value="transactionList" var="transactionList">
					<tr>
							<td align="left"><s:property value="accountEntry.accountCode"/> </td>
							<td align="left"><s:property value="accountEntry.name"/></td>
					 		<td><s:property value="amount"/></td>
					</tr>
						</s:iterator>
				</table>
				<p>
			</div>
			
			<!--END 2013 - PHASE 3 : PROJECT 1: MARK -->
			<p>
				<table class="form">
					<tr>
						<th colspan="4">Order Details</th>
					</tr>
				</table>
		<table class="results">
		<tr valign="top">
		<td>	
				<table class="compare" border="1px">
					<tr>
						<td colspan="6" class="others">Supplier Invoice Order Details</td>
					</tr>
					<tr class="others">
								<td class="desc">Item Code</td>
								<td class="desc">QTY</td>
								<td class="header" width="250px">Description</td>
								<td>UOM</td>
								<td>Unit Cost</td>
								<td>Amount</td>
					</tr>

							<s:iterator value="poDetailsHelper.purchaseOrderDetailsList">
					<tr>
									<td class="desc"><s:property value="itemCode" /></td>
									<td class="desc"><s:property value="quantity" /></td>
									<td class="desc"><s:property value="description" /></td>
									<td><s:property value="unitOfMeasurement" /></td>
									<td><s:property value="unitCost" /></td>
									<td><s:property value="amount" /> </td>
					</tr>
							</s:iterator>
					<tr>
					<tr>
							<td colspan="5" class="total">Total: PHP</td>
							<td class="totalAmount"><s:property value="%{poDetailsHelper.totalAmount}"/></td>
					</tr>
							
				</table>
			</td>
			<td>
				<table class="compare" border="1px">
					<tr>
							<td colspan="6" class="others">Receiving Report Details</td>
					</tr>
					
					<tr class="others">
								<td>Item Code</td>
								<td>QTY</td>
								<td class="header" width="250px">Description</td>
								<td>UOM</td>
								<td>Unit Cost</td>
								<td>Amount</td>
					</tr>

					<s:iterator value="poDetailsHelperToCompare.purchaseOrderDetailsList">
					<tr>
									<td class="desc"><s:property value="itemCode" /></td>
									<td class="desc"><s:property value="quantity" /></td>
									<td class="desc"><s:property value="description" /></td>
									<td><s:property value="unitOfMeasurement" /></td>
									<td><s:property value="unitCost" /></td>
									<td><s:property value="amount" /></td>
					</tr>
					</s:iterator>
					<tr>
								<td colspan="5" class="total">Total: PHP</td>
								<td class="totalAmount"><s:property value="%{poDetailsHelperToCompare.totalAmount}"/></td>
					</tr>
				</table>
			</td>
			</tr>
			</table>
			<p></p>
				<table>
					<tr>
						<td>PREPARED BY:</td>
					</tr>
					<tr>
						<td style="padding-top:10px;">_________________________</td>
					</tr>
				</table>
		
			</p>	
		</div>
</div>
</s:else>
<script type="text/javascript">
isCheckOnly();
</script>


</body>
</html>