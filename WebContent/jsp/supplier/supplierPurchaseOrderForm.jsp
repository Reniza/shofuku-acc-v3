<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
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
<sx:head parseContent="true" />
<script type="text/javascript" src="js/expandingMenu.js"></script> 	
	 <script type='text/javascript'>
	var startWith=0;
	var subMenu=1;
	</script>
<title>Supplier Purchase Order Form</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
	<h4 class="title">SUPPLIER</h4>
	<s:form action="newSupplierEntryAction" validate="true" id="POForm" enctype="multipart/form-data" >
	<div class="form" id="wholeForm">
		<div class="errors">
			<s:actionerror/>
			<s:actionmessage/>
		
		</div>
		<h3 class="form" onclick="javascript:collapseSection('arrow1','div1')"><img id="arrow1" src="images/expand2.jpg"/>PROFILE</h3>
			<div id="div1" class="sectionDiv" >
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Profile Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Supplier ID:" value="%{po.supplier.supplierId}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Supplier Status:" value="%{po.supplier.supplierStatus}"></s:textfield></td> 			
							<td><s:textfield disabled="true" label="Payment Term:" value="%{po.supplier.paymentTerm}"></s:textfield></td> 			
						</tr>
						</table>
						<table class="form">
						<tr>
							<td><s:textfield disabled="true" label="Supplier Name:" size="90" value="%{po.supplier.supplierName}"></s:textfield>
						</tr>
					</table>
				</p>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Contact Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Contact Name:" size="50" value="%{po.supplier.contactName}" ></s:textfield></td>
							<td><s:textfield disabled="true" label="Contact Title:" value="%{po.supplier.contactTitle}"></s:textfield></td>
						</tr>
					</table>
					
					<table class="form">
						<tr>
							<td><s:label label="Email Address:"/><a href="mailto:<s:property value="%{po.supplier.emailAddress}"/>"><s:property value="%{po.supplier.emailAddress}"/></a></td>
						</tr>
						<tr>
							<td><s:label label="Website:"/><a href="<s:url value="%{po.supplier.website}"/>" target="_blank"><s:property value="%{po.supplier.website}"/></a></td>
						</tr>
						
						<tr>
							<td><s:textfield disabled="true" label="Company Address:" value="%{po.supplier.companyAddress}" size="90"></s:textfield></td>
						</tr>
					</table>
					
					<table class="form">
					    <tr>
							<th colspan="6">Contact Numbers Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Phone Number:" value="%{po.supplier.phoneNumber}"></s:textfield></td>
							<td><s:textfield disabled="true" label="Fax Number:" value="%{po.supplier.faxNumber}"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield disabled="true" label="Mobile Number:" value="%{po.supplier.mobileNumber}"></s:textfield></td>
						</tr>
					</table>
				</p>	
			</div>
	
			<div id="poForm">
				<h3 class="form">PURCHASE ORDER</h3>
				
				<p>
					<table class="form">
						<tr>
							<th colspan="6">PO Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" readOnly="readOnly" label="Purchase Order No:" name="po.supplierPurchaseOrderId" id="supplierPurchaseOrderId"></s:textfield></td>
							<s:hidden name="poId" value="%{po.supplierPurchaseOrderId}"/>
							<td class="others">Purchase Order Date:</td>
							<td><sx:datetimepicker name="po.purchaseOrderDate" displayFormat="MMM-dd-yyyy" displayWeeks="5"></sx:datetimepicker></td>				
							<td class="others">Supplier ID:</td>
							<td><sx:autocompleter resultsLimit="-1" listValue="supplierId" list="supplierNoList" name="po.supplier.supplierId"></sx:autocompleter></td>
						</tr>
						<tr>
							<td class="others">Delivery Date:</td>
							<td><sx:datetimepicker name="po.dateOfDelivery" displayFormat="MMM-dd-yyyy" displayWeeks="5" ></sx:datetimepicker></td>	
							<!-- <td class="others">Payment Date:</td> -->
							<!--  <td><sx:datetimepicker name="po.paymentDate" displayFormat="MMM-dd-yyyy" displayWeeks="5"></sx:datetimepicker></td>	-->			
							<td><s:textfield disabled="true" id="payTerm" readOnly="readOnly" label="Payment Term:" value="%{po.supplier.paymentTerm}"></s:textfield></td> 			
						</tr>
						<!--<tr>
							<td><s:textfield disabled="%{forWhat}" label="Ordered By:" name="po.orderedBy" value="Mildred Ricafranca"></s:textfield></td>	
						</tr>	-->
					</table>
					
				</p>
		
				<div>
						<p>
						
						<table class="form">
							<tr>
									<s:if test="%{forWhat != 'print'}">
									<td class="others">NOTE: Import *.xls file NOT .xlsx</td>
									<td colspan = "4">
											<s:file size="70" theme="simple"  cssClass="myButtons" name="fileUpload" />
																		
									</td>
									<td colspan="2" style="text-align: left;">
									<s:submit theme="simple" type="button" id="bManageOrderDetails" disabled="%{forWhat}"
									value="Import Order Details" name="checkSupplierOrderingForm"
									action="checkSupplierOrderingForm"/>
									
									</td>
									</s:if>
									<s:hidden name="parentPage" value="SupplierPurchaseOrder"/>
									<s:hidden name="forWhat" value="%{forWhat}"/>
									<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsItemCode"     value="%{poDetailsHelper.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
									<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsDescription"	 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
									<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsQuantity"     value="%{poDetailsHelper.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
									<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsUOM" 		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
									<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsCost"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
									<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsAmount"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
									<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
									<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
									<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
									<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
									<!--END: 2013 - PHASE 3 : PROJECT 4: MARK-->
									
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
									<!--END: 2013 - PHASE 3 : PROJECT 4: AZ-->
									<s:hidden name="poDetailsGrouped.hiddenDelimetedOrderDetailsItemCode"     	value="%{poDetailsGrouped.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
									<s:hidden name="poDetailsGrouped.hiddenDelimetedOrderDetailsDescription"	value="%{poDetailsGrouped.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
									<s:hidden name="poDetailsGrouped.hiddenDelimetedOrderDetailsQuantity"       value="%{poDetailsGrouped.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
									<s:hidden name="poDetailsGrouped.hiddenDelimetedOrderDetailsUOM" 		    value="%{poDetailsGrouped.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
									<s:hidden name="poDetailsGrouped.hiddenDelimetedOrderDetailsCost"		    value="%{poDetailsGrouped.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
									<s:hidden name="poDetailsGrouped.hiddenDelimetedOrderDetailsAmount"		    value="%{poDetailsGrouped.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
									
									<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
									<s:hidden name="poDetailsGrouped.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsGrouped.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
									<s:hidden name="poDetailsGrouped.hiddenDelimetedOrderDetailsVatAmount"		    value="%{poDetailsGrouped.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
									<s:hidden name="poDetailsGrouped.hiddenDelimetedOrderDetailsVattableAmount"		    value="%{poDetailsGrouped.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
									<!--END: 2013 - PHASE 3 : PROJECT 4: MARK-->
								</tr>
				<!-- 	<tr>
						<td colspan="5" class="total">Total: PHP</td>
						<td class="totalAmount"><s:property value="%{po.totalAmount}" /></td>
					</tr> -->
				</table>
				<table class="results">
								<tr valign="top">
									<td>
										<table class="compare">
											<tr class="others">
												<td class="desc">Item Code</td>
												<td class="desc">QTY</td>
												<td class="header" width="300px">Description</td>

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
													<td><s:property value="amount" /></td>
												</tr>
											</s:iterator>
											<tr>
												<td colspan="5" class="total">Total: PHP</td>
												<td class="totalAmount"><s:property
														value="poDetailsHelper.totalAmount" /></td>
											</tr>
										</table>
									</td>
									<td>

										<table class="compare">
											<tr>
												<td class="others" colspan="6">ITEMS HERE ARE NOT
													AVAILABLE IN INVENTORY DATABASE</td>
											</tr>
											<tr>
												<td class="others" colspan="6" style="color: red;">..Please
													add first before proceeding..</td>
											</tr>
											<tr class="others">
												<td class="desc">Item Code</td>
												<td class="desc">Quantity</td>
												<td class="header" width="300px">Description</td>

												<td>UOM</td>
												<td>Unit Cost</td>
												<td>Amount</td>
											</tr>
											<s:iterator
												value="poDetailsHelperToCompare.purchaseOrderDetailsList">
												<tr>
													<td class="desc"><s:property value="itemCode" /></td>
													<td class="desc"><s:property value="quantity" /></td>
													<td class="desc"><s:property value="description" /></td>

													<td><s:property value="unitOfMeasurement" /></td>
													<td><s:property value="unitCost" /></td>
													<td><s:property value="amount" /></td>
												</tr>
											</s:iterator>
											<!--  <tr>
												<td colspan="5" class="total">Total: PHP</td>
												<td class="totalAmount"><s:property
														value="%{po.totalAmount}" /></td>
											</tr> -->
										</table>
									</td>
								</tr>
							</table>
									
					</div>
		</div>
	</div>
		
	<div class="forButtons">
		<p>
			<table class="forButtons" align="center">
				<tr><s:hidden name="subModule" value="purchaseOrder"/>
				<s:if test="%{forWhatDisplay == 'edit'}">
					<td><input class="myButtons" type="button" value="Edit" id="bEdit" onclick="javascript:toggleAlert('poForm','supplierPurchaseOrderId')"></input></td>
					
					<td colspan="2" style="text-align: left;">
					<s:submit cssClass="myButtons" type="button" id="bManageOrderDetails" value="Manage Order Details" name="addOrderDetailAction" action="addOrderDetailAction"></s:submit></td>  
					
					<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateSupplierAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Delete" id="bDelete" onclick="javascript:supplierConfirmation('POForm');"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Print" id="bPrint" action="printSupplierAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Print Triplicate"  action="printSupplierInExcelAction"></s:submit></td>
					<td><input class="myButtons" type="button" onclick="javascript:clearAll('wholeForm','POForm');" value="New Entry"></input></td>
			
				</s:if>
				<s:else>
					<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="New Entry"  action="addSupplierAction"></s:submit></td>
					<td><input class="myButtons" type="button" onclick="javascript:clearAll('wholeForm','POForm');" value="Cancel"></input></td>
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

	
	<h3 class="form">PURCHASE ORDER</h3>			
				<p>
					<table class="form">
						<tr>
							<th colspan="6">PO Details</th>
						</tr>
						<tr>
							<td><s:textfield label="Purchase Order No:" value="%{po.supplierPurchaseOrderId}"></s:textfield></td>
							<!--  <td><s:textfield label="Purchase Order Date:" value="%{po.purchaseOrderDate}"></s:textfield></td>		 -->		
						</tr>
						<tr>
							<td><s:textfield label="Delivery Date:" value="%{po.dateOfDelivery}"></s:textfield></td>	
							<!-- <td><s:textfield label="Payment Date:" value="%{po.paymentDate}"></s:textfield></td> -->				
						</tr>	
					</table>
					
					<table class="form">
						<tr>
						<th colspan="8">Supplier Details</th>
						</tr>
						<tr>
							<td colspan="2" style="font-weight:bold;font-size:20px;"><s:property value="%{po.supplier.supplierName}"></s:property></td>
							<td width="150px">FAX No. : <s:property  value="%{po.supplier.faxNumber}"></s:property></td>		
						</tr>
						<tr>
							<td colspan="4"><s:property  value="%{po.supplier.companyAddress}"></s:property></td>
						</tr>
						<tr>
							<td colspan="2" style="font-weight:bold;">ATTN : <s:property  value="%{po.supplier.contactName}" /></td>
							<td >Payment Term : <s:property  value="%{po.supplier.paymentTerm}" /></td>
						</tr>
					</table>	
				</p>	
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
								<td class="desc">Quantity</td>
								<td class="header" width="300px">Description</td>
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
									<td><s:property value="amount" /> 
									</td>
								</tr>
							</s:iterator>
							<tr>
								<td colspan="5" class="total">Total: PHP</td>
								<td class="totalAmount"><s:property value="%{poDetailsHelper.totalAmount}" /></td>
							</tr>
							</table>		
					</div>
					<p>				
						<table width="670px">
							<tr>
								<td width="100px" class="others">ORDERED BY : </td>
								<td width="400px"></td>
								<td width="150px"class="others" style="text-align:right;" >APPROVED BY :</td>							
							</tr>
							<tr>
								<td style="padding-top:10px;">_________________________</td>
								<td width="400px"></td>
								<td style="padding-top:10px;text-align:right;">________________</td>
								
							</tr>
							<tr>
								<td style="text-align:center;">MILDRED RICAFRANCA</td>
								<td width="400px"></td>
								<td style="text-align:center;">BDG</td>
							</tr>
						</table>
		</p>
	</div>		
</s:else>
</body>
</html>