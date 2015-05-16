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
  <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
  <script type="text/javascript" src="js/Transactions.js"></script>
  <sx:head parseContent="true"/> 
 	 <script type='text/javascript'>
	var startWith=1;
	var subMenu=2;
	</script>
<title>Customer Delivery Receipt Form</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">

<div class="mainForm">

	<h4 class="title">CUSTOMER</h4>
	<s:form action="newCustomerEntryAction" validate="true" id="delrForm">
		<div class="form" id="wholeForm">
			<div class="errors">
			<s:actionerror/>
			<s:actionmessage/>
			
			</div>
			<h3 class="form" onclick="javascript:collapseSection('adrow1','div1')"><img id="adrow1"  src="images/expand2.jpg"/>PROFILE</h3>
			<div id="div1" class="sectionDiv">
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Profile Details</th>
						</tr>
						<tr>
							<td width="100px"><s:textfield disabled="true" label="Customer ID:" value="%{dr.customerPurchaseOrder.customer.customerNo}"></s:textfield></td>
							<td><s:textfield label="Customer Type:" 
								value="%{dr.customerPurchaseOrder.customer.customerType}"/></td>
						</tr>
						<tr>
							<td class="others" >Customer Name:</td>
							<td colspan ="4"><s:textfield disabled="true" theme="simple"  size="70" value="%{dr.customerPurchaseOrder.customer.customerName}"></s:textfield>
							</tr>
					</table>
				</p>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Contact Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Contact Name:" value="%{dr.customerPurchaseOrder.customer.contactName}" size="50"></s:textfield></td>
							<td><s:textfield disabled="true" label="Contact Title:" value="%{dr.customerPurchaseOrder.customer.contactTitle}"></s:textfield></td>
						</tr>
						
					</table>
					<table class="form">
						<tr>
							<td><s:label label="Email Address:"/><a href="mailto:<s:property value="%{dr.customerPurchaseOrder.customer.emailAddress}"/>"><s:property value="%{dr.customerPurchaseOrder.customer.emailAddress}"/></a></td>
						</tr>
						<tr>
							<td><s:label label="Website:"/><a href="<s:url value="%{dr.customerPurchaseOrder.customer.website}"/>" target="_blank"><s:property value="%{dr.customerPurchaseOrder.customer.website}"/></a></td>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Billing Address:" value="%{dr.customerPurchaseOrder.customer.billingAddress}" size="90"></s:textfield></td>
						</tr>
					</table>
					<table class="form">
						<tr>
							<td><s:textfield disabled="true" label="Phone Number:" value="%{dr.customerPurchaseOrder.customer.phoneNumber}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Mobile Number:" value="%{dr.customerPurchaseOrder.customer.mobileNumber}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Fax Number:" value="%{dr.customerPurchaseOrder.customer.faxNumber}"></s:textfield></td>
						</tr>
					</table>
				</p>			
			</div>
	
			<h3 class="form" onclick="javascript:collapseSection('adrow2','div2')"><img id="adrow2"  src="images/expand2.jpg"/>PURCHASE ORDER</h3>
			<div id="div2" class="sectionDiv">
				<p>
					<table class="form">
						<tr>
							<th colspan="6">PO Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Purchase Order No:" value="%{dr.customerPurchaseOrder.customerPurchaseOrderId}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Purchase Order Date:" value="%{dr.customerPurchaseOrder.purchaseOrderDate}"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Customer ID:" value="%{dr.customerPurchaseOrder.customer.customerNo}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Delivery Date:" value="%{dr.customerPurchaseOrder.dateOfDelivery}"></s:textfield></td>
						</tr>
						<tr>	
							<td><s:textfield disabled="true" label="Payment Date" value="%{dr.customerPurchaseOrder.paymentDate}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Payment Term:" value="%{dr.customerPurchaseOrder.paymentTerm}"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Total Amount: PHP" value="%{dr.customerPurchaseOrder.totalAmount}"></s:textfield></td>
						</tr>
					</table>
				</p>
			</div>
	
			<h3 class="form">DELIVERY RECEIPT</h3>
			<div id="drForm">
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Delivery Receipt Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" readOnly="readOnly" label="Delivery Receipt No:" name="dr.deliveryReceiptNo" id="delrId"></s:textfield></td>
							<s:hidden name="drId" value="%{dr.deliveryReceiptNo}"/>
							<s:hidden name="custpoid" value="%{custpo.customerPurchaseOrderId}"/>
							<td class="others">Delivery Receipt Date:</td>
							<td><sx:datetimepicker name="dr.deliveryReceiptDate" displayFormat="MMM-dd-yyyy" displayWeeks="5"></sx:datetimepicker></td>
							<td><s:select disabled="%{forWhat}" label="Shipping Method:" 
									list="#{'Air Freight':'Air Freight','Land Freight':'Land Freight','Sea Freight':'Sea Freight'}" 
									name="dr.shippingMethod"/></td>
						</tr>
						<tr>
							<td class="others">Purchase Order No:</td>
							<td><sx:autocompleter listValue="customerPurchaseOrderId" list="purchaseOrderNoList" maxlength="50" resultsLimit="-1" name="dr.customerPurchaseOrder.customerPurchaseOrderId"></sx:autocompleter></td>
							
							<td class="others">Shipping Date:</td>
							<td><sx:datetimepicker name="dr.shippingDate" displayFormat="MMM-dd-yyyy" displayWeeks="5"></sx:datetimepicker></td>
							
							<td><s:textfield disabled="%{forWhat}" name="dr.dueDate" label="Due Date:"></s:textfield></td>
						</tr>
						<tr>	
							<td><s:select disabled="%{forWhat}" label="Remarks:" 
								list="#{'CORRECT':'CORRECT','OVER':'OVER','SHORT':'SHORT','SPOILED':'SPOILED','RETURN TO SUPPLIER':'RETURN TO SUPPLIER'}" 
								name="dr.remarks"/></td>
							<td><s:textfield disabled="%{forWhat}" label="Total Amount: PHP" name="dr.totalAmount"></s:textfield></td>
					
						</tr>
					</table>
					<table class="form">
						<tr>
							<th colspan="6">VAT Details</th>
						</tr>
					<tr>
						<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
						<td><s:textfield disabled="%{forWhat}" label="Vatable Amount: PHP" value="%{poDetailsHelper.totalVattableAmount}"></s:textfield></td>
						<td><s:textfield disabled="%{forWhat}" label="VAT Amount: PHP" value="%{poDetailsHelper.totalVatAmount}"></s:textfield></td>
						<!--END: 2013 - PHASE 3 : PROJECT 4: AZHEE-->
					</tr>
					</table>
				</p>	
				
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
					 	<td><input type="text" name="transactionList[${ctr.index}].amount" value="${transactions.amount}"/></td>
					 		<c:forEach items="${accountProfileCodeList}" var="profile">
					 		<input type="hidden" name="accountCodeActions.${profile.accountCode}" id="accountCodeActions.${profile.accountCode}" value="${profile.accountingRules.customerDeliveryReceipt}"/>
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
				<s:if test="%{dr.returnSlipList != null}">
					<table class="form">
						<tr  valign="top">
							<th >Return Slip Details</th>
						</tr>
					</table>
					<table class="lists">
						<tr class="others">
							<td class="header">Return Slip No</td>
							<td class="header">Transaction Date</td>
						</tr>
					<s:iterator value="dr.returnSlipList">
						<tr>
							<td align="left"><s:url id="displayId" action="editInventoryAction">
											<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
											<s:param name="rs.returnSlipNo" value="returnSlipNo">itemCode</s:param>
											<s:param name="subModule" value="%{'returnSlip'}">subModule</s:param>
										</s:url>
										<s:a href="%{displayId}"><s:property value="returnSlipNo"/></s:a>
							</td>
							<td class="desc"><s:property value="returnDate" /></td>
						</tr>
					</s:iterator>
					</table>
				</s:if>
					
				<p style="text-align:left;">
					<table class="form">
						<tr>
								<s:hidden name="parentPage" value="DeliveryReceipt"/>
								<s:hidden name="forWhat" value="%{forWhat}"/>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode"     	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription"	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity"       value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM" 		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
								<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVatAmount"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVattableAmount"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
								<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
											
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsItemCode"     	value="%{poDetailsHelper.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsDescription"	value="%{poDetailsHelper.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsQuantity"       value="%{poDetailsHelper.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsUOM" 		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsCost"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
								<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
								<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
						<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
									
						<th colspan="6">Order Details</th>
						</tr>
					</table>
					<table class="results">
						<tr valign="top">
							<td>
								<table class="compare">
									<tr>
										<td colspan="6" class="others">Delivery Receipt Details</td>
									</tr>
									<tr class="others">
										<td class="desc">Item Code</td>
										<td class="desc">QTY</td>
										<td class="header" width="250px">Description</td>
										<td>UOM</td>
										<td>Unit Cost</td>
										<td>Amount</td>
										<td>Input Tax</td>
										<td>Vattable Amount</td>
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
										<td><s:property value="vatAmount" /> </td>
										<td><s:property value="vattableAmount" /> </td>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="7" class="total">Total: PHP</td>
									<td class="totalAmount"><s:property value="%{poDetailsHelper.totalAmount}" /></td>
					<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
								</tr>
								<tr>
									<td colspan="7" class="total">Total Non Vatable Amount: PHP</td>
									<td class="totalAmount"><s:property value="%{poDetailsHelper.totalNonVattableAmount}" /></td>
								</tr>
							</table>
							</td>
							<td>
								<table class="compare">
									<tr>
										<td colspan="6" class="others">Purchase Order Details</td>
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
									<td class="totalAmount"><s:property value="%{dr.customerPurchaseOrder.totalAmount}" /></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					</p>
				</div>
			</div>
		</div>

		<div class="forButtons">
			<p>
			<table class="forButtons" align="center">
				<tr>
				<s:hidden name="subModule" value="deliveryReceipt"/>
				<s:if test="%{forWhatDisplay == 'edit'}">
						<td><input class="myButtons" name="edit" type="button" onclick="javascript:toggleAlert('drForm','delrId');" value="Edit" id="Edit"></input></td>
						<td><s:submit cssClass="myButtons" type="button" id="bManageOrderDetails" disabled="%{forWhat}"
											value="Manage Order Details" name="addOrderDetailAction"
											action="addOrderDetailAction"></s:submit></td>
						<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateCustomerAction"></s:submit></td>
						<td><s:submit cssClass="myButtons" type="button" value="Delete" onclick="javascript:customerConfirmation('delrForm');"></s:submit></td>
						<td><s:submit cssClass="myButtons" type="button" value="Print" action="printCustomerAction" onclick="javascript:removeDiv('wholeForm');"></s:submit></td>
						<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','delrForm');" value="New Entry"></input></td>
				</s:if>
				<s:else>
						<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="New Entry" name="newReceivingReport" action="addCustomerAction"></s:submit></td>
						<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','delrForm');" value="Cancel"></input></td>
				</s:else>
				</tr>
			</table>
			</p>
		</div>
	</s:form>
</div>
</s:if>

<s:else>
<div class="print">
	<jsp:include page="/jsp/util/companyHeader.jsp"/>
		<h3 class="form">DELIVERY RECEIPT</h3>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Delivery Receipt Details</th>
						</tr>
						<tr>
							<td><s:textfield  label="Delivery Receipt No:" value="%{dr.deliveryReceiptNo}" id="delrId"></s:textfield></td>
							<td><s:textfield label="Delivery Receipt Date" value="%{dr.deliveryReceiptDate}" /></td>
							<td><s:textfield  label="Shipping Method:" value="%{dr.shippingMethod}"/></td>
						</tr>
						<tr>
							<td><s:textfield  label="Purchase Order No:" value="%{dr.customerPurchaseOrder.customerPurchaseOrderId}"></s:textfield></td>
							
							<td><s:textfield label="Shipping Date:" value="%{dr.shippingDate}"/></td>
							<td><s:textfield label="Due Date:" value="%{dr.dueDate}"/></td>
						</tr>
						<tr>	
							<td><s:textfield  label="Remarks:" value="%{dr.remarks}"/></td>
							<td><s:textfield  label="Total Amount: PHP" value="%{dr.totalAmount}"></s:textfield></td>
						</tr>
					</table>
				</p>	
				<p>
					<table class="form">
						<tr>
							<th>Order Details</th>
						</tr>
					</table>
					<table class="lists" border="1px">
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
							<td colspan="5" class="total">Total: PHP</td>
							<td class="totalAmount"><s:property value="%{poDetailsHelper.totalAmount}" /></td>
					<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
						</tr>
						<tr>
							<td colspan="5" class="total">Total Non Vatable Amount: PHP</td>
							<td class="totalAmount"><s:property value="%{poDetailsHelper.totalNonVattableAmount}" /></td>
						</tr>
						<tr>
							<td colspan="5" class="total">Total Vat Amount: PHP</td>
							<td class="totalAmount"><s:property value="%{poDetailsHelper.totalVatAmount}" /></td>
						</tr>
						<tr>
							<td colspan="5" class="total">Total Vatable Amount: PHP</td>
							<td class="totalAmount"><s:property value="%{poDetailsHelper.totalVattableAmount}" /></td>
						</tr>
					</table>
				
					<table>
						<tr>
							<td  class="others">PREPARED BY:</td>
						</tr>
						<tr>
							<td style="padding-top:10px;">_____________________</td>
						</tr>
					</table>
				</p>
			</div>
</s:else>
</body>
</html>