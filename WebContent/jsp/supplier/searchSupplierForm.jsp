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
 
	 <script type='text/javascript'>
	var startWith=0;
	var subMenu=4;
	</script>
<title>Supplier Search Form</title>
</head>

<body>
<div class="mainForm">
	<h4 class="title">SUPPLIER</h4>
	
		<div class="errors">
			<s:actionerror/>
			<s:actionmessage/>
			
		</div>
	<s:form action="searchSupplierAction" validate="true" id="searchForm">
		<div>
		<s:hidden name="clicked" id="clicked" value="true"/>
		<s:set name="supplierModule" value="supplierModule"/>
		<s:set name="moduleParameter" value="moduleParameter"/>
		<s:set name="moduleParameterValue" value="moduleParameterValue"/>	
			<p>
				<table class="form">
					<tr>
						<td>
							<s:select label="Supplier Module:" 
						headerKey="-1" headerValue="--Choose Module--"
						list="#{'profile':'Profile', 'purchaseOrder':'Purchase Order', 'receivingReport':'Receiving Report', 'invoice':'Supplier Invoice'}" 
						name="supplierModule" onchange="javascript:onTypeChangeSupplier();"/>
						</td>
				 
					<s:if test="%{#supplierModule == 'profile'}">
						<td>
							<s:select label="Search Profile By:" 
								list="#{'ALL':'ALL','supplierId':'Supplier ID', 'supplierName':'Supplier Name'}" 
							name="moduleParameter" onchange="javascript:onTypeChangeModuleParameter();"/>
						</td>
					</s:if>
				
					<s:if test="%{#supplierModule == 'purchaseOrder'}">
						<td>
							<s:select label="Search PO By:" 
								list="#{'ALL':'ALL','supplierPurchaseOrderId':'PO Number', 'purchaseOrderDate':'PO Date','supplierName':'Supplier Name'}" 
							name="moduleParameter" onchange="javascript:onTypeChangeModuleParameter();"/>
						</td>
					</s:if>
					<s:if test="%{#supplierModule == 'receivingReport'}">
						<td>
							<s:select label="Search RR By:" 
							list="#{'ALL':'ALL','receivingReportNo':'RR Number', 'receivingReportDate':'RR Date','supplierPurchaseOrderId':'PO No','supplierName':'Supplier Name'}" 
							name="moduleParameter" onchange="javascript:onTypeChangeModuleParameter();"/>
						</td>
					</s:if>
					<s:if test="%{#supplierModule == 'invoice'}">
						<td>
							<s:select label="Search Invoice By:" 
							list="#{'ALL':'ALL','supplierInvoiceNo':'Invoice Number', 'supplierInvoiceDate':'Invoice Date','receivingReportNo':'RR No','supplierName':'Supplier Name'}" 
							name="moduleParameter" onchange="javascript:onTypeChangeModuleParameter();"/>
						</td>
					</s:if>
					</tr>
					<tr>
					<s:if test="%{#supplierModule == 'purchaseOrder' || #supplierModule == 'receivingReport' || #supplierModule == 'invoice' }">
						<s:if test="%{#moduleParameter  == 'purchaseOrderDate' || #moduleParameter == 'receivingReportDate' || #moduleParameter == 'supplierInvoiceDate'}">
							<td class="others">Date:</td>
							<td>
								<sx:datetimepicker displayFormat="MMM-dd-yyyy" displayWeeks="5" toggleType="explode" id="moduleParameterValue" name="moduleParameterValue"></sx:datetimepicker>
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
								<s:textfield label="Search Value:" id="moduleParameterValue" name="moduleParameterValue"/>
							</td>
						</s:else>
							<td><s:submit cssClass="myButtons" label="Submit" value="SEARCH"></s:submit></td>
							
						</tr>
				</table>
			</p>
		</div>
	</s:form>
		
		<div class="results">
			<p>
			
				<table class="results">
					<s:if test="%{#supplierModule == 'profile'}">
						<tr>
							<th>Supplier ID</th>
							<th width="300px">Supplier Name</th>
							<th>Contact Name</th>
							<th>Contact Phone No</th>
						</tr>
					</s:if> 
					<s:elseif test="%{#supplierModule == 'purchaseOrder'}">
						<tr>
							<th>Supplier PO NO</th>
							<th>PO Date</th>
							<th width="300px">Supplier Name</th>
							<th>Delivery Date</th>
							<th>Total Amount</th>
						</tr>
					</s:elseif>
					<s:elseif test="%{#supplierModule == 'receivingReport'}">
						<tr>
							<th width="150px">Receiving Report No</th>
							<th>RR Date</th>
							<th>Supplier Name</th>
							<th>PO NO</th>
							<th>Total Amount</th>
						</tr>
					</s:elseif>
					<s:elseif test="%{#supplierModule == 'invoice'}">
						<tr>
							<th>Invoice No</th>
							<th>Invoice Date</th>
							<th>RR No</th>
							<th>Supplier Name</th>
							<th>Remarks</th>
							<th>Total Purchases</th>
							<th>Input Vat</th>
							<th>Vattable Amount</th>
						</tr>
					</s:elseif>
		
						<s:iterator value="supplierList" status="supplierList">
							<s:if test="%{#supplierModule == 'profile'}">
							<tr>
								<td align="left"><s:url id="displayId" action="editSupplierAction">
										<s:param name="forWhat" value="%{'true'}">forDisable</s:param>
										<s:param name="forWhatDisplay" value="%{'edit'}">forEdit</s:param> 
										<s:param name="supplier.supplierId" value="%{supplierId}">supplierId</s:param>
										<s:param name="supplierModule" value="%{'profile'}">supplierModule</s:param>
									</s:url>
								<auth:isAuth role="0">
									<s:a href="%{displayId}"><s:property value="supplierId"/>
									</s:a> 
								</auth:isAuth>
								</td>
								<td align="left"><s:property value="supplierName" /></td>
								<td><s:property value="contactName"/></td>
								<td><s:property value="phoneNumber"/></td>
							</tr>
							</s:if>
							<s:elseif test="%{#supplierModule == 'purchaseOrder'}">
							<tr>
								<td align="left"><s:url id="displayId" action="editSupplierAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="forWhatDisplay" value="%{'edit'}">forEdit</s:param> 
										<s:param name="po.supplierPurchaseOrderId" value="%{supplierPurchaseOrderId}">supplierPOId</s:param>
										<s:param name="supplierModule" value="%{'purchaseOrder'}">supplierModule</s:param>
									</s:url>
									<auth:isAuth role="5"><s:a href="%{displayId}"><s:property value="supplierPurchaseOrderId"/></s:a></auth:isAuth>
								</td>
								<td align="left"><s:property value="purchaseOrderDate"/></td>
								<td align="left"><s:property value="supplier.supplierName"/></td>
								<td><s:property value="dateOfDelivery"/></td>
								<td><s:property value="totalAmount"/></td>
							</tr>
							</s:elseif>
							<s:elseif test="%{#supplierModule == 'receivingReport'}">
							<tr>
								<td align="left"><s:url id="displayId" action="editSupplierAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="forWhatDisplay" value="%{'edit'}">forEdit</s:param> 
										<s:param name="rr.receivingReportNo" value="%{receivingReportNo}">RRNo</s:param>
										<s:param name="supplierModule" value="%{'receivingReport'}">supplierModule</s:param>
									</s:url>
									<s:a href="%{displayId}"><s:property value="receivingReportNo"/></s:a>
								</td>
								<td align="left"><s:property value="receivingReportDate"/></td>
								<td align="left"><s:property value="supplierPurchaseOrder.supplier.supplierName"/></td>
								<td><s:property value="supplierPurchaseOrder.supplierPurchaseOrderId"/></td>
								<td><s:property value="totalAmount"/></td>	
							</tr>
							</s:elseif>
							<s:elseif test="%{#supplierModule == 'invoice'}">
							<tr>
								<td align="left"><s:url id="displayId" action="editSupplierAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="forWhatDisplay" value="%{'edit'}">forEdit</s:param> 
										<s:param name="invoice.supplierInvoiceNo" value="%{supplierInvoiceNo}">invoiceNo</s:param>
										<s:param name="supplierModule" value="%{'invoice'}">supplierModule</s:param>
									</s:url>
									<s:a href="%{displayId}"><s:property value="supplierInvoiceNo"/></s:a>
								</td>
								<td align="left"><s:property value="supplierInvoiceDate"/></td>
								<td><s:property value="receivingReport.receivingReportNo"/></td>
								<td align="left"><s:property value="receivingReport.supplierPurchaseOrder.supplier.supplierName"/></td>
								<td><s:property value="remarks"/></td>
								<td><s:property value="debit1Amount"/></td>
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