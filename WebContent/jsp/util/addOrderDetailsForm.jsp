<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
<script type="text/javascript" src="js/addingRows.js"></script>
<script type="text/javascript" src="js/hideParameter.js"></script>
<script type="text/javascript" src="js/deleteConfirmation.js"></script>
<title>Order Details</title>
<sx:head parseContent="true"/>
</head>
<body>

	<div align="center">
		<img src="images/header.jpg" width="60%" height="50px" />
		<div class="importPage">
			<s:form action="addOrderDetailAction" validate="true" >
			
				<div class="order">
					<p>   
					<table class="order">
						<tr>
							<th align="left">Item Code</th>
							<th align="left">Description</th>
							<th>Quantity In</th> 
							<th>Quantity Out</th> 
							<th>UOM</th>
							<th>Unit Cost</th>
						</tr>
						<tr>
						
						<td align="left"><sx:autocompleter headerValue="Choose One" resultsLimit="-1"
							listValue="itemCode" list="itemCodeList" maxlength="20"  name="orderDetails.itemCode"></sx:autocompleter></td>
				
<%-- 							<td><s:textfield size="10" name="orderDetails.itemCode" --%>
<%-- 									theme="simple"></s:textfield></td> --%>
							<td align="left"><s:textfield readOnly="readOnly" name="orderDetails.description"
									theme="simple"></s:textfield></td>
							<td><s:textfield size="5" name="orderDetails.quantityIn"
									theme="simple"></s:textfield></td>
							<td><s:textfield size="5" name="orderDetails.quantityOut"
									theme="simple"></s:textfield></td>
							<td><s:textfield size="10" readOnly="readOnly" name="orderDetails.unitOfMeasurement" 
									theme="simple"></s:textfield></td>
							<td><s:textfield size="10" readOnly="readOnly" name="orderDetails.unitCost"
									theme="simple"></s:textfield></td>
							<td><s:hidden name="orderDetails.amount"/></td>
							<td><s:submit cssClass="myButtons" type="button"
									label="Get Details" action="loadOrderDetailsAction"></s:submit></td>
							<td><s:submit cssClass="myButtons" type="button"
									label="Modify Item" action="addOrderDetailAction">
								</s:submit></td>
							<td><s:submit cssClass="myButtons" type="button"
									label="Delete Item" action="deleteOrderDetailAction">
								</s:submit></td>
						</tr>
					</table>
						<s:hidden name="poId" value="%{poId}"/>
						<s:hidden name="rrId" value="%{rrId}"/>
						<s:hidden name="invId" value="%{invId}"/>
						
						<s:hidden name="custpoid" value="%{custpoid}"/>
						<s:hidden name="drId" value="%{drId}"/>
						<s:hidden name="custinvId" value="%{custinvId}"/>
						
						<s:hidden name="parentPage" value="%{parentPage}"/>
						<s:hidden name="forWhat" value="%{forWhat}" />
						<s:hidden name="forWhatDisplay" value="edit" />
						<s:hidden name="fptsId" value="%{fptsId}"/>
						<s:hidden name="rfId" value="%{rfId}"/>
						<s:hidden id ="manageFPTSOrderDetailIdentifier" name="manageFPTSOrderDetailIdentifier" value="%{manageFPTSOrderDetailIdentifier}" />
						<s:hidden id ="manageOrderRequisitionOrderDetailIdentifier" name="manageOrderRequisitionOrderDetailIdentifier" value="%{manageOrderRequisitionOrderDetailIdentifier}" />
						
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsItemCode"     value="%{poDetailsHelper.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsDescription"	 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsQuantity"     value="%{poDetailsHelper.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsUOM" 		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsCost"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsAmount"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
					<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
					<!--END: 2013 - PHASE 3 : PROJECT 4: MARK-->
						
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode"     	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription"	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity"       value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM" 		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
					<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVatAmount"		value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVattableAmount"	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
					<!--END: 2013 - PHASE 3 : PROJECT 4: MARK-->
								
						
					<p />
				</div>
			</s:form>
			<s:form action="submitOrderDetailsAction" >
				<div class="results">
					<p>
					<td>
					<table border="1px" class="lists">
						<tr class="others">
							<td align="left">Item Code</td>
							<td align="left" width="300px">Description</td>
							<td>Quantity</td>
							<td>UOM</td>
							<td>Unit Cost</td>
							<td>Amount</td>
							
						</tr>
						<s:iterator value="poDetailsHelperDraft.purchaseOrderDetailsList" >
							<tr>
								<td align="left"><s:property value="itemCode" /></td>
								<td align="left"><s:property value="description" /></td>
								<td><s:property value="quantity" /></td>
								<td><s:property value="unitOfMeasurement" /></td>
								<td><s:property value="unitCost" /></td>
								<td><s:property value="amount" /></td>
							</tr>
						</s:iterator>
					</table>
					<p />
						<s:hidden name="poId" value="%{poId}"/>
						<s:hidden name="rrId" value="%{rrId}"/>
						<s:hidden name="invId" value="%{invId}"/>
						
						<s:hidden name="custpoid" value="%{custpoid}"/>
						<s:hidden name="drId" value="%{drId}"/>
						<s:hidden name="custinvId" value="%{custinvId}"/>
						
						<s:hidden name="parentPage" value="%{parentPage}"/>
						<s:hidden name="forWhat" value="%{forWhat}" />
						<s:hidden name="forWhatDisplay" value="edit" />
						<s:hidden name="fptsId" value="%{fptsId}"/>
						<s:hidden name="rfId" value="%{rfId}"/>
						<s:hidden id ="manageFPTSOrderDetailIdentifier" name="manageFPTSOrderDetailIdentifier" value="%{manageFPTSOrderDetailIdentifier}" />
						<s:hidden id ="manageOrderRequisitionOrderDetailIdentifier" name="manageOrderRequisitionOrderDetailIdentifier" value="%{manageOrderRequisitionOrderDetailIdentifier}" />
						
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsItemCode"     value="%{poDetailsHelper.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsDescription"	 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsQuantity"     value="%{poDetailsHelper.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsUOM" 		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsCost"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsAmount"		 value="%{poDetailsHelper.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
					<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
						<s:hidden name="poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount"		    value="%{poDetailsHelper.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
					<!--END: 2013 - PHASE 3 : PROJECT 4: MARK-->
						
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode"     	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription"	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity"       value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM" 		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
					<!--START: 2013 - PHASE 3 : PROJECT 4: MARK-->
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVatAmount"		value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
						<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVattableAmount"	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
					<!--END: 2013 - PHASE 3 : PROJECT 4: MARK-->						
						
				</div>
				<div class="forButtons">
					<table class="forButtons" border="1px">
						<tr>
							<td><s:submit cssClass="myButtons" type="button"
									value="Finalize Order Details" action="submitOrderDetailsAction"></s:submit></td>
						</tr>
					</table>
				</div>
				
			</s:form>


		</div>
	</div>
</body>
</html>