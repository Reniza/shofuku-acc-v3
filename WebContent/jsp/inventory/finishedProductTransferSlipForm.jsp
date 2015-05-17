<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ taglib prefix="c" uri="/tld/c.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/hideParameter.js"></script>
<script type="text/javascript" src="js/expandingSection.js"></script>
<script type="text/javascript" src="js/deleteConfirmation.js"></script>
<script type="text/javascript" src="js/onChangeType.js"></script>
<script type="text/javascript" src="js/manageOrderDetails.js"></script>
<LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
<link rel="stylesheet" href="menu.css" type="text/css" />
<script type="text/javascript" src="js/expandingMenu.js"></script>
<script type="text/javascript" src="js/Transactions.js"></script>
<script type='text/javascript'>
	var startWith = 2;
	var subMenu = 3;
</script>
<sx:head parseContent="true" />
<title>Finished Product Transfer Slip</title>
</head>
<body>
	<s:if test="%{forWhat != 'print'}">
		<div class="mainForm">
			<h4 class="title">INVENTORY</h4>

			<s:form action="newInventoryEntryAction" value="true" id="fptsForm">
			
				<div class="form" id="wholeForm">
					<div class="errors">
						<s:actionerror />
						<s:actionmessage />
					</div>
					<div id="inventoryForm">
						<h3 class="form">Finished Product Transfer Slip</h3>
						<p>
						<table class="form">
							<tr>
								<th colspan="8">Details</th>
							</tr>
							<tr>
								<td><s:textfield disabled="%{forWhat}" label="FPTS No:" name="fpts.fptsNo" id="fptsId" size="15" />
								<s:hidden name="fptsNo" value="%{fpts.fptsNo}" /> 
								</td>
							  	
							<td class="others">Transaction Date:</td>
							<td><sx:datetimepicker name="fpts.transactionDate" displayFormat="MMM-dd-yyyy" displayWeeks="5"></sx:datetimepicker></td>
							
							</tr>
							</table>
							<table class="form">
							<tr>
								<td><s:textfield disabled="%{forWhat}" label="From (Location):"
										name="fpts.fptsFrom" size="80" /></td>
							</tr>
							<tr>
								<td><s:textfield disabled="%{forWhat}" label="To (Location):"
										name="fpts.fptsTo" size="80" /></td>
							</tr>
							</table>
			
							<table class="form">
							
							<tr>
								<td><s:textfield disabled="%{forWhat}" label="Transferred by:"
										name="fpts.transferredBy" size="30" /></td>
								<td><s:textfield disabled="%{forWhat}" label="Received by:"
										name="fpts.receivedBy" size="30" /></td>
							</tr>
							<tr>
							<td class="others">Approved Date:</td>
								<td><sx:datetimepicker displayFormat="MMM-dd-yyyy" displayWeeks="5"
										name="fpts.approvedDate" /></td>
							<td class="others">Received Date:</td>
								<td><sx:datetimepicker displayFormat="MMM-dd-yyyy" displayWeeks="5"
										name="fpts.receivedDate" /></td>
							</tr>
							<tr>
							<td>Order Requisition Reference No:</td>
							<td><sx:autocompleter resultsLimit="-1" listValue="requisitionNo" list="rfNoList" name="fpts.requisitionForm.requisitionNo"></sx:autocompleter></td>
						
							</tr>
						</table>
						
						<!--START 2013 - PHASE 3 : PROJECT 1: MARK -->
			<s:if test="%{forWhatDisplay == 'edit'}">			
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
					 		<input type="hidden" name="accountCodeActions.${profile.accountCode}" id="accountCodeActions.${profile.accountCode}" value="${profile.accountingRules.inventoryFPTS}"/>
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
					</div>

					<s:if test="%{fpts.returnSlipList != null}">
				
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
						<s:iterator value="fpts.returnSlipList">
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
					<div>
					<table class="form">
					<tr>
						<s:hidden name="parentPage" value="InventoryFPTS"/>
								<s:hidden name="forWhat" value="%{forWhat}"/>
								<s:hidden name="fptsId" value="%{fpts.fptsNo}" /> 
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode"     	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription"	value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity"       value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM" 		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
								<s:hidden name="poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount"		    value="%{poDetailsHelperToCompare.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
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
						
						<th colspan="6" style="text-align: right;">Product Transfer Details</th>
					</tr>
					</table>
						<table class="lists" border=1>
							
							<tr valign="top">
							<td>
							<table class="compare">
								<tr valign="top">
									<td class="others" colspan="6">TRANSFERRED ITEMS</td>
								</tr>
							<tr class="others" valign="top">
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
									<td><s:property value="amount" /></td>
								</tr>


							</s:iterator>
							<tr>
							<td colspan=5 align="right" class="totalAmount">Total Amount:</td>
							<td align="right" class="totalAmount"><s:property value="poDetailsHelper.totalAmount" /></td>
							</tr>
						
						</table>
						
						</td>
						<td>
						<table class="compare">
								<tr valign="top">
									<td class="others" colspan="6">ORDER REQUISITION ITEMS</td>
								</tr>
								<tr class="others" valign="top">
									<td class="desc">Item Code</td>
									<td class="desc">Quantity</td>
									<td class="header" width="300px">Description</td>
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
									<td colspan=5 align="right" class="totalAmount">Total Amount:</td>
									<td align="right" class="totalAmount"><s:property value="poDetailsHelperToCompare.totalAmount" /></td>
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
						<tr>
							<s:hidden name="subModule" value="fpts" />
							<s:hidden id ="manageFPTSOrderDetailIdentifier" name="manageFPTSOrderDetailIdentifier" value="" />				
							

							<s:if test="%{forWhatDisplay == 'edit'}">
				
								<td><input class="myButtons" id="bEdit" type="button"
									onclick="javascript:toggleAlert('inventoryForm','fptsId');"
									value="Edit"></input></td>
								
								<td>
								<s:submit cssClass="myButtons" type="button" id="bManageOrderDetails"
								value="Manage Transferred Items" onclick="javascript:ManageFPTS('fptsForm','T');"
								action="addOrderDetailAction"></s:submit></td>
								
							<!-- 	<td>
											<s:submit cssClass="myButtons" type="button" id="bManageItemDetails"
								value="Manage Received" onclick="javascript:ManageFPTS('fptsForm','R');"
								action="addOrderDetailAction"></s:submit></td> -->	
								
									
								<td><s:submit disabled="%{forWhat}" id="bUpdate"
										cssClass="myButtons" type="button" value="Update"
										action="updateInventoryAction"></s:submit></td>
								<td><s:submit cssClass="myButtons" type="button" id="bDel"
										value="Delete"
										onclick="javascript:inventoryConfirmation('fptsForm');"></s:submit></td>
								<td><s:submit cssClass="myButtons" type="button"
										id="bPrint" value="Print" action="printInventoryAction"></s:submit></td>
								<td><input class="myButtons" name="clear" id="bCancel"
									type="button"
									onclick="javascript:clearAll('wholeForm','fptsForm');"
									value="New Entry"></input></td>

							</s:if>

							<s:else>

								<td><s:submit disabled="%{forWhat}" cssClass="myButtons"
										type="button" id="bNew" value="New Entry"
										action="addInventoryAction"></s:submit></td>
								<td><input class="myButtons" name="clear" type="button"
									id="bCancel"
									onclick="javascript:clearAll('wholeForm','fptsForm');"
									value="Cancel"></input></td>
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
			<jsp:include page="/jsp/util/companyHeader.jsp" />

			<h3 class="form">Finished Product Transfer Slip</h3>

			<table class="form">
				<tr>
					<th colspan="8">Details</th>
				</tr>
				<tr>
					<td><s:textfield disabled="%{forWhat}" label="FPTS No:"
							value="%{fpts.fptsNo}" id="fptsId" size="15" /></td>

					<td><s:textfield label="Transaction Date:"
										value="%{fpts.transactionDate}" /></td>

					
				</tr>
				<tr>
						
								<td><s:textfield label="From:"
										value="%{fpts.fptsFrom}" /></td>
							
								<td><s:textfield label="To:"
										value="%{fpts.fptsTo}" /></td>
							</tr>
				<tr>
								<td><s:textfield disabled="%{forWhat}" label="Transferred by:"
										value="%{fpts.transferredBy}" size="30" /></td>
								<td><s:textfield disabled="%{forWhat}" label="Received by:"
										value="%{fpts.receivedBy}" size="30" /></td>
							</tr>
							<tr>
						
								<td><s:textfield label="Received Date:"
										value="%{fpts.approvedDate}" /></td>
							
								<td><s:textfield label="Received Date:"
										value="%{fpts.receivedDate}" /></td>
							</tr>
			<tr><td><s:textfield label="Order Requisition No:"
										value="%{fpts.requisitionForm.requisitionNo}" /></td></tr>							


			</table>

			<table class="form">
				<tr>
					<th colspan="6">Product Transfer Details</th>
				</tr>
			</table>
			
						<table class="results">
							
							<tr valign="top">
							<td>
							<table class="compare" border="1px">
								<tr valign="top">
									<td class="others" colspan="6">TRANSFERRED ITEMS</td>
								</tr>
							<tr class="others" valign="top">
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
									<td><s:property value="amount" /></td>
								</tr>
							</s:iterator>
						<tr>
							<td colspan=5 align="right" class="totalAmount">Total Amount:</td>
							<td align="right" class="totalAmount"><s:property value="poDetailsHelper.totalAmount" /></td>
							</tr>
						</table>
						
						</td>
						<td>
						<table class="compare" border="1px">
											<tr valign="top">
												<td class="others" colspan="6">ORDERED REQUISITION ITEMS</td>
											</tr>
											
											<tr class="others" valign="top">
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
											<tr>
												<td colspan=5 align="right" class="totalAmount">Total Amount:</td>
												<td align="right" class="totalAmount"><s:property value="poDetailsHelperToCompare.totalAmount" /></td>
											</tr>
						</table>
						</td> 
						
						</tr>
						</table>
		</div>

	</s:else>

</body>
</html>