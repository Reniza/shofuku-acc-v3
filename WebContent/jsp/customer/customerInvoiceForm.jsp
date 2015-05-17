<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ taglib prefix="c" uri="/tld/c.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
<script type="text/javascript" src="js/expandingSection.js"></script> 
<script type="text/javascript" src="js/hideParameter.js"></script> 
<script type="text/javascript" src="js/deleteConfirmation.js"></script> 
   <script type="text/javascript" src="js/autoCompute.js"></script>
     <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
  <script type="text/javascript" src="js/Transactions.js"></script>
  <sx:head parseContent="true"/>
 	 <script type='text/javascript'>
	var startWith=1;
	var subMenu=3;
	</script>
<title>Customer Invoice Form</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
<h4 class="title">CUSTOMER</h4>
	<s:form action="newCustomerEntryAction" validate="true" id="invoiceForm">
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
							<td width="100px"><s:textfield disabled="true" label="Customer ID:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.customerNo}"></s:textfield></td>
						<td><s:textfield label="Customer Type:" 
								value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.customerType}"/></td>
						</tr>
						<tr>
							<td class="others" >Customer Name:</td>
							<td colspan ="4"><s:textfield disabled="true" theme="simple"  size="70" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.customerName}"></s:textfield>
						</tr>
					</table>
				</p>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Contact Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="true" size="50" label="Contact Name:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.contactName}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Contact Title:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.contactTitle}"></s:textfield></td>
						</tr>
						
					</table>
					<table class="form">
						<tr>
							<td><s:label label="Email Address:"/><a href="mailto:<s:property value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.emailAddress}"/>"><s:property value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.emailAddress}"/></a></td>
						</tr>
						<tr>
							<td><s:label label="Website:"/><a href="<s:url value="%{customer.website}"/>" target="_blank"><s:property value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.website}"/></a></td>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Billing Address:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.billingAddress}" size="90"></s:textfield></td>
						</tr>
					</table>
					<table class="form">
					<tr>
							<td><s:textfield disabled="true" label="Phone Number:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.phoneNumber}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Mobile Number:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.mobileNumber}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Fax Number:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.faxNumber}"></s:textfield></td>
						</tr>
					</table>
					
				</p>	
			</div>
			
			<h3 class="form" onclick="javascript:collapseSection('arrow2','div2')"><img id="arrow2"  src="images/expand2.jpg"/>PURCHASE ORDER</h3>
			<div id="div2" class="sectionDiv">
				<p>
					<table class="form">
						<tr>
							<th colspan="6">PO Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Purchase Order No:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customerPurchaseOrderId}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Customer ID:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customer.customerNo}"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Purchase Order Date:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.purchaseOrderDate}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Delivery Date:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.dateOfDelivery}"></s:textfield></td>
						</tr>
						<tr>	
							<td><s:textfield disabled="true" label="Payment Date" value="%{invoice.deliveryReceipt.customerPurchaseOrder.paymentDate}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Payment Term:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.paymentTerm}"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Total Amount: PHP" value="%{invoice.deliveryReceipt.customerPurchaseOrder.totalAmount}"></s:textfield></td>
						</tr>
					</table>
				</p>
			</div>
			
			<h3 class="form" onclick="javascript:collapseSection('arrow3','div3')"><img id="arrow3"  src="images/expand2.jpg"/>DELIVERY RECEIPT</h3>
			<div id="div3" class="sectionDiv">
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Delivery Receipt Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Delivery Receipt No:" value="%{invoice.deliveryReceipt.deliveryReceiptNo}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Purchase Order No:" value="%{invoice.deliveryReceipt.customerPurchaseOrder.customerPurchaseOrderId}"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Delivery Date:" value="%{invoice.deliveryReceipt.deliveryReceiptDate}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Shipping Date:" value="%{invoice.deliveryReceipt.shippingDate}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Due Date:" value="%{invoice.deliveryReceipt.dueDate}"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Shipping Method:" value="%{invoice.deliveryReceipt.shippingMethod}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Remarks:" value="%{invoice.deliveryReceipt.remarks}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Total Amount: PHP" value="%{invoice.deliveryReceipt.totalAmount}"></s:textfield></td>
						</tr>
					</table>
				</p>
			</div>
			
			
			<h3 class="form">INVOICE</h3>
			<div id="invForm">	
				<p>
					<table class="form">
						<tr>
							<th colspan="7">Invoice Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" readOnly="readOnly" label="Sales Invoice No:" name="invoice.customerInvoiceNo" id="cinvId"></s:textfield></td>
							<s:hidden name="invId" value="%{invoice.customerInvoiceNo}"/>
							<td class="others">Sales Invoice Date:</td>
							<td><sx:datetimepicker  name="invoice.customerInvoiceDate" displayFormat="MMM-dd-yyyy" displayWeeks="5"></sx:datetimepicker></td>
							<td class="others">Delivery Receipt No:</td>
							<td><sx:autocompleter listValue="deliveryReceiptNo" list="deliveryReceiptNoList" maxlength="50" resultsLimit="-1" name="invoice.deliveryReceipt.deliveryReceiptNo"></sx:autocompleter></td>
							
							</tr>
					</table>
					<table class="form">
						<tr>
							<td colspan="5"><s:textfield disabled="%{forWhat}" label="Sold To:" name="invoice.soldTo" size="90"></s:textfield></td>
						</tr>
						<tr>
							<td colspan="5"><s:textfield disabled="%{forWhat}" label="Address:" name="invoice.address" size="90"></s:textfield></td>
						</tr>
					</table>
					<table class="form">
						<tr>
						
							<td><s:textfield disabled="%{forWhat}" label="Business Style:" name="invoice.busStyle" ></s:textfield></td>
							<!--  <td><s:textfield disabled="%{forWhat}" label="TIN:" name="invoice.tin"></s:textfield></td>-->
							<td><s:textfield disabled="%{forWhat}" label="Total Sales: PHP" id ="invoice.totalSales" name="invoice.totalSales"></s:textfield></td>
						</tr>
					<!--START: 2013 - PHASE 3 : PROJECT 4: AZ
						<tr>
							<td><s:select disabled="%{forWhat}" label="Compute Vat ?" id="isVat" name="isVat" list="#{'0':'Non-Vat','1':'Vat'}" onchange="javascript:computeVat();"></s:select></td>
							<td><s:textfield disabled="%{forWhat}" label="VAT Amount: PHP" id="invoice.vat" name="invoice.vat"></s:textfield></td>
							<td><s:textfield disabled="%{forWhat}" label="Total Amount: PHP" id="invoice.totalAmount" name="invoice.totalAmount"></s:textfield></td>
						</tr>
					END: 2013 - PHASE 3 : PROJECT 4: AZ-->
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
						<td><s:textfield disabled="%{forWhat}" label="Reference No:" name="invoice.vatDetails.orNo"></s:textfield></td>
					</tr>
					</table>
				</p>
			</div>
			
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
							<select  id="parentCode" name="transactionList[${ctr.index}].accountEntry.accountCode" onChange ="javascript:showTransactionAction([${ctr.index}],this)">
								<c:forEach items="${accountProfileCodeList}" var="profile">
									<option ${profile.accountCode == transactions.accountEntry.accountCode ? 'selected="selected"' : ''}
										value="${profile.accountCode}">${profile.accountCode} - ${profile.name}</option>
								</c:forEach>
							</select>
						</td>
					 	<td><input type="text" name="transactionList[${ctr.index}].amount" value="${transactions.amount}"/></td>
						<c:forEach items="${accountProfileCodeList}" var="profile">
					 		<input type="hidden" name="accountCodeActions.${profile.accountCode}" id="accountCodeActions.${profile.accountCode}" value="${profile.accountingRules.customerInvoice}"/>
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
			
			<div>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Order Details from Delivery Receipt
							<s:hidden name="parentPage" value="DeliveryReceipt"/>
							<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsItemCode"     value="%{poDetailsHelper.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsDescription"	 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsQuantity"     value="%{poDetailsHelper.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsUOM" 		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsCost"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsAmount"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
							<!--  START: P3 AZ -->	
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
							<!--  END: P3 AZ -->	
							</th>
						</tr>
						</table>
						<table class="lists">
						<tr class="others">
							<td class="desc">Item Code</td>
							<td class="desc">QTY</td>
							<td class="header" width="300px">Description</td>
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
									<td><s:property value="amount" /> 
									</td>
								</tr>
							</s:iterator>
						<tr>
							<td colspan="5" class="total">Total: PHP</td>
							<td class="totalAmount"><s:property value="%{invoice.totalSales}"/></td>
						</tr>
					</table>
				</p>
			
			</div>
		</div>
		
		<div class="forButtons">
			<p>
				<table class="forButtons" align="center">
					<tr><s:hidden name="subModule" value="invoice"/>
					
					<s:if test="%{forWhatDisplay == 'edit'}">	
						<td><input class="myButtons" name="edit" type="button" onclick="javascript:toggleAlert('invForm','cinvId');" value="Edit" ></input></td>
						<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateCustomerAction"></s:submit></td>
						<td><s:submit cssClass="myButtons" type="button" value="Delete" onclick="javascript:customerConfirmation('invoiceForm');" ></s:submit></td>
						<td><s:submit cssClass="myButtons" type="button" value="Print" action="printCustomerAction"></s:submit></td>
						<td><s:submit cssClass="myButtons" type="button" value="Print Sales Invoice"  action="printCustomerInExcelAction"></s:submit></td>
						<!--  <td><s:submit cssClass="myButtons" type="button" value="Print INV as DR"  action="printCustomerInExcelAction"></s:submit></td>
						-->
						<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','invoiceForm');" value="New Entry"></input></td>
						
					</s:if>
					<s:else>
							<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="New Entry" action="addCustomerAction"></s:submit></td>
							<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','invoiceForm');" value="Cancel"></input></td>
					</s:else>
					</tr>
				</table>
			</p>
		</div>
	</s:form>
</div>
</s:if>


<!-- div for print -->
<s:else>
<div class="print">
	<jsp:include page="/jsp/util/companyHeader.jsp"/>

	
	<h3 class="form">INVOICE</h3>
			
				<p>
					<table class="form">
						<tr>
							<th colspan="7">Invoice Details</th>
						</tr>
						<tr>
							<td><s:textfield label="Sales Invoice No:" value="%{invoice.customerInvoiceNo}"></s:textfield></td>
							<td><s:textfield label="Sales Invoice Date" value="%{invoice.customerInvoiceDate}" displayFormat="MMM-dd-yyyy"></s:textfield></td>
							<td><s:textfield label="Delivery Receipt No:" value="%{invoice.deliveryReceipt.deliveryReceiptNo}"></s:textfield></td>
							</tr>
					</table>
					<table class="form">
						<tr>
							<td colspan="5"><s:textfield label="Sold To:" value="%{invoice.soldTo}" size="90"></s:textfield></td>
						</tr>
						<tr>
							<td colspan="5"><s:textarea rows="3" cols="70" label="Address:" value="%{invoice.address}"></s:textarea></td>
						</tr>
					</table>
					<table class="form">
						<tr>
							<td><s:textfield  label="Business Style:" value="%{invoice.busStyle}" ></s:textfield></td>
							<td><s:textfield  label="TIN:" value="%{invoice.vatDetails.tinNumber}"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield  label="Total Sales: PHP" id ="invoice.totalSales}" value="%{invoice.totalSales}"></s:textfield></td>
							<td><s:textfield  label="VAT Amount: PHP" value="%{invoice.vatDetails.vatAmount}"></s:textfield></td>
							<td><s:textfield  label="Vatable Amount: PHP" value="%{invoice.vatDetails.vattableAmount}"></s:textfield></td>
						</tr>
					</table>
				</p>
			</div>
			
			<div>
				<p>
				<table class="form">
					<tr>
						<th colspan="6">Order Details</th>
					</tr>
				</table>
					<table class="lists" border="1px">
						<tr class="others">
							<td class="desc">Item Code</td>
							<td class="desc">QTY</td>
							<td class="header"width="300px">Description</td>
							<td>UOM</td>
							<td>Unit Cost</td>
							<td>Amount</td>
						</tr>
						<s:iterator value="poDetailList">
								<tr>
									<td class="desc"><s:property value="itemCode" /></td>
									<td class="desc"><s:property value="quantity" /></td>
									<td class="desc"><s:property value="description" /></td>
									<td><s:property value="unitOfMeasurement" /></td>
									<td><s:property value="unitCost" /></td>
									<td><s:property value="amount" /> 
									</td>
								</tr>
							</s:iterator>
						<tr>
							<td colspan="5" class="total">Total: PHP</td>
							<td class="totalAmount"><s:property value="%{invoice.totalSales}"/></td>
						</tr>
					</table>
				</p>
				
						<table width="670px">
							<tr>
								<td class="others">PREPARED BY : </td>
							</tr>
							<tr>
								<td style="padding-top:10px;">_____________________</td>
								<td style="padding-top:10px;text-align:center;">__________________________</td>
								<td style="padding-top:10px;text-align:right;">_______________________</td>
							</tr>
							<tr>
								<td style="text-align:center;">ACCOUNTING</td>
								<td style="padding-top:10px;text-align:center;">WET & DRY WAREHOUSE</td>
								<td style="text-align:center;">LOGISCTICS</td>
							</tr>
							
							<tr>
								<td style="padding-top:20px;" width="100px" class="others">RECEIVED BY : </td>
							</tr>
							<tr>
								<td style="padding-top:10px;">_____________________</td>
							</tr>
							<tr>
								<td style="text-align:center;">STORE</td>
							</tr>
						</table>
						
			</div>
</s:else>
</body>
</html>