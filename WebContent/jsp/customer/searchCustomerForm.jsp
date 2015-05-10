<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ taglib prefix="auth" uri="/tld/Authorization.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
  <script type="text/javascript" src="js/expandingSection.js"></script> 
    <script type="text/javascript" src="js/onChangeType.js"></script> 
      <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
  <sx:head parseContent="true"></sx:head>
  <script type="text/javascript" src="js/expandingMenu.js"></script> 	
	 <script type='text/javascript'>
	var startWith=1;
	var subMenu=4;
	</script>
<title>Customer Search Form</title>
</head>

<body>
<div class="mainForm">
	<h4 class="title">CUSTOMER</h4>
		<div class="errors">
			<s:actionerror/>
			<s:actionmessage/>	
		</div>
	<s:form action="searchCustomerAction" validate="true" id="searchForm">
		<s:hidden name="clicked" id="clicked" value="true"/>
		<s:set name="customerModule" value="customerModule"/>
		<s:set name="moduleParameter" value="moduleParameter"/>
		<s:set name="moduleParameterValue" value="moduleParameterValue"/>	
			<p>
				<table class="form">
					<tr>
						<td>
							<s:select label="Customer Module:" 
						headerKey="-1" headerValue="--Choose Module--"
						list="#{'profile':'Profile', 'purchaseOrder':'Purchase Order', 'deliveryReceipt':'Delivery Receipt', 'invoice':'Sales Invoice'}" 
						name="customerModule" onchange="javascript:onTypeChangeCustomer();"/>
						</td>
					<s:if test="%{#customerModule == 'profile'}">
						<td>
							<s:select label="Search Profile By:" 
								list="#{'ALL':'ALL','customerNo':'Customer No', 'customerName':'Customer Name'}" 
							name="moduleParameter" onchange="javascript:onTypeChangeCustomer();"/>
						</td>
					</s:if>
					<s:if test="%{#customerModule == 'purchaseOrder'}">
						<td>
							<s:select label="Search PO By:" 
							list="#{'ALL':'ALL','customerPurchaseOrderId':'PO Number', 'purchaseOrderDate':'PO Date','customerName':'Customer Name'}" 
							name="moduleParameter" onchange="javascript:onTypeChangeCustomer();"/>
						</td>
					</s:if>
					<s:if test="%{#customerModule == 'deliveryReceipt'}">
						<td>
							<s:select label="Search DR By:" 
							list="#{'ALL':'ALL','deliveryReceiptNo':'DR Number', 'deliveryReceiptDate':'DR Date','customerPurchaseOrderId':'PO No','customerName':'Customer Name'}" 
							name="moduleParameter" onchange="javascript:onTypeChangeCustomer();"/>
						</td>
					</s:if>
					<s:if test="%{#customerModule == 'invoice'}">
						<td>
							<s:select label="Search Invoice By:" 
							list="#{'ALL':'ALL','customerInvoiceNo':'Invoice Number', 'customerInvoiceDate':'Invoice Date','deliveryReceiptNo':'DR No','customerName':'Customer Name'}" 
							name="moduleParameter" onchange="javascript:onTypeChangeCustomer();"/>
						</td>
					</s:if>
					</tr>
					<tr>
					<s:if test="%{#customerModule == 'purchaseOrder' || #customerModule == 'deliveryReceipt' || #customerModule == 'invoice' }">
						<s:if test="%{#moduleParameter  == 'purchaseOrderDate' || #moduleParameter == 'deliveryReceiptDate' || #moduleParameter == 'customerInvoiceDate'}">
							<td class="others">Date:</td>
							<td>
								<sx:datetimepicker displayFormat="MMM-dd-yyyy" displayWeeks="5" toggleType="explode" name="moduleParameterValue"></sx:datetimepicker>
							</td>
						</s:if>
						<s:else>
							<td>
								<s:textfield label="Search Value:" id="moduleParameterValue" name="moduleParameterValue"/>
							</td>
						</s:else>
					</s:if>
					<s:elseif test="%{#customerModule == 'ALL'}">
					</s:elseif>
					<s:else>
						<td>
							<s:textfield label="Search Value:" name="moduleParameterValue"/>
						</td>
					</s:else>
					
						<td><s:submit cssClass="myButtons" label="Submit" value="SEARCH"></s:submit></td>
					</tr>
				</table>
			</p>
	</s:form>
		
		<div class="results">
			<p>
			
				<table class="results">
					<s:if test="%{#customerModule == 'profile'}">
						<tr>
							<th>Customer No</th>
							<th width="300px">Customer Name</th>
							<th>Contact Name</th>
							<th>Contact Phone No</th>
							<th>Email Address</th>
						</tr>
					</s:if> 
					<s:elseif test="%{#customerModule == 'purchaseOrder'}">
						<tr>
							<th>Customer PO NO</th>
							<th>PO Date</th>
							<th width="200px">Customer Name</th>
							<th>Payment Term</th>
							<th>Delivery Date</th>
							<th>Total Amount</th>
						</tr>
					</s:elseif>
					<s:elseif test="%{#customerModule == 'deliveryReceipt'}">
						<tr>
							<th width="100px">Delivery Receipt No</th>
							<th>RR Date</th>
							<th>PO NO</th>
							<th width="200px">Customer Name</th>
							<th>Total Amount</th>
						</tr>
					</s:elseif>
					<s:elseif test="%{#customerModule == 'invoice'}">
						<tr>
							<th>Invoice NO</th>
							<th>Invoice Date</th>
							<th>DR NO</th>
							<th width="150px">Sold To</th>
							<th>Total Sales</th>
							<th>Input Vat</th>
							<th>Total Vatable Amount</th>
						</tr>
					</s:elseif>
						<s:iterator value="customerList" status="customerList">
							<s:if test="%{#customerModule == 'profile'}">
							<tr>
								<td align="left">
									<auth:isAuth role="5">
									<s:url id="displayId" action="editCustomerAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="forWhatDisplay" value="%{'edit'}">forEdit</s:param>
										<s:param name="customer.customerNo" value="%{customerNo}">customerNo</s:param>
										<s:param name="customerModule" value="%{'profile'}">customerModule</s:param>
									</s:url>
									</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="customerNo"/></s:a>
								</td>
								<td align="left"><s:property value="customerName" /></td>
								<td><s:property value="contactName"/></td>
								<td><s:property value="phoneNumber"/></td>
								<td><s:property value="emailAddress"/></td>
							</tr>
							</s:if>
							<s:elseif test="%{#customerModule == 'purchaseOrder'}">
							<tr>
								<td align="left">
									<auth:isAuth role="6">
									<s:url id="displayId" action="editCustomerAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="custpo.customerPurchaseOrderId" value="%{customerPurchaseOrderId}">supplierPOId</s:param>
										<s:param name="customerModule" value="%{'purchaseOrder'}">customerModule</s:param>
									</s:url>
									</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="customerPurchaseOrderId"/></s:a>
								</td>
								<td align="left"><s:property value="purchaseOrderDate"/></td>
								<td align="left"><s:property value="customer.customerName"/></td>
								<td><s:property value="paymentTerm"/></td>
								<td align="left"><s:property value="dateOfDelivery"/></td>
								<td><s:property value="totalAmount"/></td>
							</tr>
							</s:elseif>
							<s:elseif test="%{#customerModule == 'deliveryReceipt'}">
							<tr>
								<td align="left">
									<auth:isAuth role="7">
									<s:url id="displayId" action="editCustomerAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="dr.deliveryReceiptNo" value="%{deliveryReceiptNo}">RRNo</s:param>
										<s:param name="customerModule" value="%{'deliveryReceipt'}">customerModule</s:param>
									</s:url>
									</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="deliveryReceiptNo"/></s:a>
								</td>
								<td align="left"><s:property value="deliveryReceiptDate"/></td>
								<td><s:property value="customerPurchaseOrder.customerPurchaseOrderId"/></td>
								<td align="left"><s:property value="customerPurchaseOrder.customer.customerName"/></td>
								<td><s:property value="totalAmount"/></td>
								
							</tr>
							</s:elseif>
							<s:elseif test="%{#customerModule == 'invoice'}">
							<tr>
								<td align="left">
									<auth:isAuth role="8">
									<s:url id="displayId" action="editCustomerAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="invoice.customerInvoiceNo" value="%{customerInvoiceNo}">invoiceNo</s:param>
										<s:param name="customerModule" value="%{'invoice'}">customerModule</s:param>
									</s:url>
									</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="customerInvoiceNo"/></s:a>
								</td>
								<td align="left"><s:property value="customerInvoiceDate"/></td>
								<td align="left"><s:property value="deliveryReceipt.deliveryReceiptNo"/></td>
								<td align="left"><s:property value="soldTo"/></td>
								<td><s:property value="totalSales"/></td>
								<td><s:property value="vatDetails.vatAmount"/></td>
								<td><s:property value="vatDetails.vattableAmount"/></td>
							</tr>
							</s:elseif>
						</s:iterator>
					</table>
			</p>
		</div>	
	</div>
</body>
</html>