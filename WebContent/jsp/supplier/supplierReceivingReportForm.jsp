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
  <sx:head/>
	 <script type='text/javascript'>
	var startWith=0;
	var subMenu=2;
	</script>
<title>Supplier Receiving Report Form</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
<h4 class="title">SUPPLIER</h4>
<s:form action="newSupplierEntryAction" validate="true" id="RRForm">
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
						<td><s:textfield disabled="true" label="Supplier ID:" value="%{rr.supplierPurchaseOrder.supplier.supplierId}"></s:textfield></td>
						<td><s:textfield disabled="true" label="Supplier Status" value="%{rr.supplierPurchaseOrder.supplier.supplierStatus}"></s:textfield></td> 			
						<td><s:textfield disabled="true" label="Payment Term:" value="%{rr.supplierPurchaseOrder.supplier.paymentTerm}"></s:textfield></td> 
					</tr>
					</table>
					<table class="form">
						<tr>
						<td><s:textfield disabled="true" size="90" label="Supplier Name:" value="%{rr.supplierPurchaseOrder.supplier.supplierName}"></s:textfield>
					</tr>
				</table>
			</p>
			<p>
				<table class="form">
					<tr>
						<th colspan="6">Contact Details</th>
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Contact Name:" value="%{rr.supplierPurchaseOrder.supplier.contactName}"  size="50" ></s:textfield></td>
						<td><s:textfield disabled="true" label="Contact Title:" value="%{rr.supplierPurchaseOrder.supplier.contactTitle}"></s:textfield></td>
						</tr>
					
				</table>
				<table class="form">
					<tr>
						<td><s:label label="Email Address:"/><a href="mailto:<s:property value="%{rr.supplierPurchaseOrder.supplier.emailAddress}"/>"><s:property value="%{rr.supplierPurchaseOrder.supplier.emailAddress}"/></a></td>
					</tr>
					<tr>
						<td><s:label disabled="true" label="Website:"/><a href="<s:url value="%{rr.supplierPurchaseOrder.supplier.website}"/>" target="_blank"><s:property value="%{rr.supplierPurchaseOrder.supplier.website}"/></a></td>
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Company Address:" value="%{rr.supplierPurchaseOrder.supplier.companyAddress}" size="90"></s:textfield></td>
					</tr>
				</table>
				<table class="form">
				    <tr>
						<th colspan="6">Contact Numbers Details</th>
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Phone Number:" value="%{rr.supplierPurchaseOrder.supplier.phoneNumber}"></s:textfield></td>
						<td><s:textfield disabled="true" label="Fax Number: " value="%{rr.supplierPurchaseOrder.supplier.faxNumber}"></s:textfield></td>
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Mobile Number:" value="%{rr.supplierPurchaseOrder.supplier.mobileNumber}"></s:textfield></td>
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
						<td><s:textfield disabled="true" label="Purchase Order No:" value="%{rr.supplierPurchaseOrder.supplierPurchaseOrderId}"></s:textfield></td>
						<td><s:textfield disabled="true" label="Purchase Order Date:" value="%{rr.supplierPurchaseOrder.purchaseOrderDate}"></s:textfield></td>
						<td><s:textfield disabled="true" label="Supplier ID:" value="%{rr.supplierPurchaseOrder.supplier.supplierId}"></s:textfield></td>
					
					</tr>
					<tr>
						<td><s:textfield disabled="true" label="Delivery Date:" value="%{rr.supplierPurchaseOrder.dateOfDelivery}"></s:textfield></td>
						<td><s:textfield disabled="true" readOnly="readOnly" label="Payment Term:" value="%{rr.supplierPurchaseOrder.supplier.paymentTerm}" ></s:textfield></td>
						<td><s:textfield  disabled="true" value="%{rr.supplierPurchaseOrder.totalAmount}" label="Total Amount: PHP"></s:textfield></td>
					
						<!--  <td><s:textfield disabled="true" label="Payment Date:" value="%{rr.supplierPurchaseOrder.paymentDate}"></s:textfield></td> -->
					</tr>
					
				</table>
			</p>
		</div>
	
		<div id="rrForm">
			<h3 class="form">RECEIVING REPORT</h3>
			<p>
				<table class="form">
					<tr>
						<th colspan="6">Receiving Report Details</th>
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" readOnly="readOnly" label="Receiving Report No:" name="rr.receivingReportNo" id="receivingReportNo"></s:textfield></td>
						<s:hidden name="rrId" value="%{rr.receivingReportNo}"/>
						<s:hidden name="poId" value="%{po.supplierPurchaseOrderId}"/>
						<td class="others">Receiving Report Date:</td>
						<td><sx:datetimepicker name="rr.receivingReportDate" displayFormat="MMM-dd-yyyy" displayWeeks="5"></sx:datetimepicker></td>
					</tr>
					<tr>
						<td class="others">Purchase Order No:</td>
						<td><sx:autocompleter resultsLimit="-1" listValue="supplierPurchaseOrderId" list="purchaseOrderNoList" name="rr.supplierPurchaseOrder.supplierPurchaseOrderId"></sx:autocompleter></td>
						
						<td><s:select disabled="%{forWhat}" label="Remarks:" 
								list="#{'CORRECT':'CORRECT','OVER':'OVER','SHORT':'SHORT','SPOILED':'SPOILED','RETURN TO SUPPLIER':'RETURN TO SUPPLIER'}" 
								name="rr.remarks"/>
						</td>
					</tr>
					<tr>
						<td>Payment Date: </td>
						<td><s:date name="rr.receivingReportPaymentDate" format="MMM-dd-yyyy"></s:date></td>
						<td><s:textfield disabled="%{forWhat}" label="Total Purchases: PHP" name="rr.totalAmount" ></s:textfield></td>
					</tr>
				</table>
				<table class="form">
						<tr>
							<th colspan="6">VAT Details</th>
						</tr>
					<tr>
						<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
						<td><s:textfield readonly="true" label="Vatable Amount: PHP" value="%{poDetailsHelper.totalVattableAmount}"></s:textfield></td>
						<td><s:textfield readonly="true" label="VAT Amount: PHP" value="%{poDetailsHelper.totalVatAmount}"></s:textfield></td>
						<!--END: 2013 - PHASE 3 : PROJECT 4: AZHEE-->
					</tr>
				</table>
							
			</p>	
			<div>
			<s:if test="%{rr.returnSlipList != null}">
				
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
						<s:iterator value="rr.returnSlipList">
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
			<p>
			<table class="form">
				<tr>
						<s:hidden name="parentPage" value="ReceivingReport"/>
								<s:hidden name="forWhat" value="%{forWhat}"/>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode"     	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription"	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity"       value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM" 		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount"		value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
								<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVatAmount"		value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVattableAmount"	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
								<!--END: 2013 - PHASE 3 : PROJECT 4: AZ-->
								
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
								<!--END: 2013 - PHASE 3 : PROJECT 4: AZ-->
						<th colspan="6">Order Details</th>
				</tr>
			</table>
			<table class="results">
			<tr valign="top">
			<td>
					<table class="compare">
						<tr>
							<td colspan="8" class="others">Receiving Report Details</td>
						</tr>
					<tr class="others">
						<td class="desc">Item Code</td>
						<td class="desc">QTY</td>
						<td class="header" width="150px">Description</td>
						<td>UOM</td>
						<td>Unit Cost</td>
						<td>Amount</td>
						<td>Input Tax</td>
						<td>Vattable Amount</td>
					</tr>
					<s:iterator value="poDetailsHelper.purchaseOrderDetailsList">
					<tr>
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
					<!--END: 2013 - PHASE 3 : PROJECT 4: MARK-->
			</td>
			<td>
				
					<table class="compare">
					<tr>
						<td colspan="6"  class="others">Purchase Order Details</td>
					</tr>
					<tr class="others">
								<td class="desc">Item Code</td>
								<td class="desc">QTY</td>
								<td class="header" width="150px">Description</td> 
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
			</p>
			
			</div>
		</div>
	</div>

	<div class="forButtons">
		<p>
			<table class="forButtons" align="center">
				<tr><s:hidden name="subModule" value="receivingReport"/>
				
				<s:if test="%{forWhatDisplay == 'edit'}">
					<td><input class="myButtons" type="button" value="Edit" name="editSupplierRR" onclick="javascript:toggleAlert('rrForm','receivingReportNo')"></input></td>
					<td><s:submit cssClass="myButtons" type="button" disabled="%{forWhat}" id="bManageOrderDetails"
								value="Manage Order Details" name="addOrderDetailAction"
								action="addOrderDetailAction"></s:submit></td>
					<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateSupplierAction"></s:submit></td>
				
					<td><s:submit cssClass="myButtons" type="button" value="Delete" onclick="javascript:supplierConfirmation('RRForm');"></s:submit></td>
						<td><s:submit cssClass="myButtons" type="button" value="Print" name="newSupplierProfile"  action="printSupplierAction"></s:submit></td>
						<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','RRForm');" value="New Entry"></input></td>
				
				</s:if>
				
				<s:else>
					<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="New Entry" name="newReceivingReport" action="addSupplierAction"></s:submit></td>
					<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','RRForm');" value="Cancel"></input></td>
				
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

		<h3 class="form">RECEIVING REPORT</h3>
			<p>
				<table class="form">
					<tr>
						<th colspan="6">Receiving Report Details</th>
					</tr>
					<tr>
						<td><s:textfield label="Receiving Report No:" value="%{rr.receivingReportNo}"></s:textfield></td>
						<td><s:textfield label="Receiving Report Date:" value="%{rr.receivingReportDate}"></s:textfield></td>
					</tr>
					<tr>
						<td><s:textfield label="Purchase Order No:" value="%{rr.supplierPurchaseOrder.supplierPurchaseOrderId}"></s:textfield></td>
						<td><s:textfield label="Remarks:" value="%{rr.remarks}"/>
						</td>
					</tr>
					<tr>
						<td><s:textfield label="Payment Date:" value="%{rr.receivingReportPaymentDate}"></s:textfield></td>
						<td><s:textfield label="Total Amount: PHP" value="%{rr.totalAmount}" ></s:textfield></td>
					</tr>
				</table>		
			</p>	
			<table class="form">
						<tr>
						<th colspan="8">Supplier Details</th>
						</tr>
						<tr>
							<td colspan="2" style="font-weight:bold;font-size:20px;"><s:property value="%{rr.supplierPurchaseOrder.supplier.supplierName}"></s:property></td>
							<td width="150px">FAX No. : <s:property  value="%{rr.supplierPurchaseOrder.supplier.faxNumber}"></s:property></td>		
						</tr>
						<tr>
							<td colspan="4"><s:property  value="%{rr.supplierPurchaseOrder.supplier.companyAddress}"></s:property></td>
						</tr>
						<tr>
							<td colspan="2" style="font-weight:bold;">ATTN : <s:property  value="%{rr.supplierPurchaseOrder.supplier.contactName}" /></td>
							<td >Payment Term : <s:property  value="%{rr.supplierPurchaseOrder.supplier.paymentTerm}" /></td>
						</tr>
					</table>	
			<div>
			<p>
			
			
			<table class="form">
				<tr>
					<th>Order Details</th>
				</tr>
			</table>
			
			<table class="results">
				<tr valign="top">
					<td>
					<table class="compare" border="1px">
						<tr>
							<td colspan="8" class="others">Receiving Report Details</td>
						</tr>
					<tr class="others">
						<td class="desc">Item Code</td>
						<td class="desc">QTY</td>
						<td class="header" width="250px">Description</td>
						<td>UOM</td>
						<td>Unit Cost</td>
						<td>Amount</td>
						<!--  START: add vat details: azhee -->
						<td>Input Tax</td>
						<td>Vatable Amount</td>
						<!-- END -->
					</tr>
					<s:iterator value="poDetailsHelper.purchaseOrderDetailsList">
						<tr>
							<td class="desc"><s:property value="itemCode" /></td>
							<td class="desc"><s:property value="quantity" /></td>
							<td class="desc"><s:property value="description" /></td>
							<td><s:property value="unitOfMeasurement" /></td>
							<td><s:property value="unitCost" /></td>
							<td><s:property value="amount" /> </td>
							<!--  START: add vat details: azhee -->
							<td><s:property value="vatAmount" /> </td>
							<td><s:property value="vattableAmount" /> </td>
							<!-- END -->
						</tr>
					</s:iterator>
					<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
					<tr>
							<td colspan="7" class="total">Total Non Vatable Amount: PHP</td>
							<td class="totalAmount"><s:property value="%{poDetailsHelper.totalNonVattableAmount}" /></td>
					</tr>
					<tr>
							<td colspan="7" class="total">Total Vat Amount: PHP</td>
							<td class="totalAmount"><s:property value="%{poDetailsHelper.totalVatAmount}" /></td>
					</tr>
					<tr>
							<td colspan="7" class="total">Total Vatable Amount: PHP</td>
							<td class="totalAmount"><s:property value="%{poDetailsHelper.totalVattableAmount}" /></td>
					</tr>
					</table>
				</td>
				<td>
				
					<table class="compare" border="1px">
					<tr>
						<td colspan="6"  class="others">Purchase Order Details</td>
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
							<td class="totalAmount"><s:property  value="%{poDetailsHelperToCompare.totalAmount}" /></td>
					</tr>
				</table>
			</td>
			</tr>
			</table>
			</p>
			<p>
				<table>
					<tr>
						<td colspan="3">PREPARED BY:</td>
					</tr>
					<tr>
						<td style="padding-top:10px;">______________________</td>
					</tr>
					<tr>
						<td width="100px" style="text-align:center;">B. ABAD</td>
					</tr>
				</table>
			</p>
		</div>	
	</div>	
</s:else>

</body>
</html>