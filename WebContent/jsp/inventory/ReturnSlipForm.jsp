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
<script type="text/javascript" src="js/hideParameter.js"></script>
<script type="text/javascript" src="js/returnSlip.js"></script>
<script type="text/javascript" src="js/deleteConfirmation.js"></script>
<script type="text/javascript" src="js/expandingSection.js"></script> 
 <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script>
	<script type="text/javascript" src="js/Transactions.js"></script> 	
	 <script type='text/javascript'>
	var startWith=2;
	var subMenu=7;
	</script>
	<sx:head parseContent="true" />
<title>Return Slip Form</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
	<h4 class="title">Inventory</h4>
	<s:form action="newInventoryEntryAction" validate="true" id="returnSlipForm">
		<div class="form" id="wholeForm">
			<div class="errors">
				<s:actionerror/>
				<s:actionmessage/>
			
			</div>
			<div id="returnSlipForm">
				<h3 class="form">Return Slip</h3>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Return Slip Details</th>
						</tr>
						<tr>
							<td><s:textfield label="Return Slip No:" value="%{rs.returnSlipNo}" id="returnSlipNo" name="rs.returnSlipNo"></s:textfield></td>
							<s:hidden name="rsIdNo" value="%{rs.returnSlipNo}"/>
							<td>Return Date:</td>
						<td><sx:datetimepicker name="rs.returnDate" displayFormat="MMM-dd-yyyy" displayWeeks="5"></sx:datetimepicker></td>
						
						</tr>
					</table>
					
						
						<table class="form">
						
						<tr>
							<td><s:textfield disabled="%{forWhat}" name="rs.preparedBy" label="PreparedBy:" value="%{rs.preparedBy}" size="30"></s:textfield></td>
							<td><s:textfield disabled="%{forWhat}" name="rs.approvedBy" label="ApprovedBy:" value="%{rs.approvedBy}" size="30"></s:textfield></td>
						</tr>
						<tr>
							<td>Return TO/FROM:</td>
							<td>
							<s:hidden name="returnSlipToValue" value="%{rs.returnSlipTo}"/>
							<select name="rs.returnSlipTo" disabled=disabled >
							<s:if test="%{rs.returnSlipTo == 'CtoW'}">
									<option value="WtoS">WAREHOUSE TO SUPPLIER</option>
									<option selected value="CtoW">CUSTOMER TO WAREHOUSE</option>
									<option value="PtoW">PRODUCTION TO WAREHOUSE</option>
									<option value="WtoP">WAREHOUSE TO PRODUCTION </option>
							</s:if>
							<s:elseif test="%{rs.returnSlipTo == 'PtoW'}">
									<option value="WtoS">WAREHOUSE TO SUPPLIER</option>
									<option value="CtoW">CUSTOMER TO WAREHOUSE</option>
									<option selected value="PtoW">PRODUCTION TO WAREHOUSE</option>
									<option value="WtoP">WAREHOUSE TO PRODUCTION </option>
							</s:elseif>
							<s:elseif test="%{rs.returnSlipTo == 'WtoP'}">
									<option value="WtoS">WAREHOUSE TO SUPPLIER</option>
									<option value="CtoW">CUSTOMER TO WAREHOUSE</option>
									<option value="PtoW">PRODUCTION TO WAREHOUSE</option>
									<option selected value="WtoP">WAREHOUSE TO PRODUCTION </option>
							</s:elseif>
							<s:else>
									<option selected value="WtoS">WAREHOUSE TO SUPPLIER</option>
									<option value="CtoW">CUSTOMER TO WAREHOUSE</option>
									<option value="PtoW">PRODUCTION TO WAREHOUSE</option>
									<option value="WtoP">WAREHOUSE TO PRODUCTION </option>
							</s:else>
							</select>							
							</td>
							
							<td><s:textarea label="Comments" name="rs.memo.memo" value="%{rs.memo.memo}" cols="25" rows="5"/></td>
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
					 		<input type="hidden" name="accountCodeActions.${profile.accountCode}" id="accountCodeActions.${profile.accountCode}" value="${profile.accountingRules.inventoryRS}"/>
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
				<p>
				
				<s:if test="%{forWhatDisplay == 'edit'}">
					<table class="form" border="1px">
						<tr>
							<td><s:textfield readonly="true" label="Order Reference No:" value="%{rs.returnSlipReferenceOrderNo}" size="50" name="rs.returnSlipReferenceOrderNo"></s:textfield></td>
						</tr>
					</table>	
				</s:if>
				<s:else>
				<table class="form">
						<tr>
						<th colspan="6">ORDER REFERENCE</th>
						</tr>
				</table>
				<h4 style="color:red;style:bold">Search for RR No, DR No, FPTS No and Requisition No</h4>
				<table class="form" border="1px">
						<tr>
						<td><s:textfield disabled="%{forWhat}" label="Order Reference No:" value="%{rs.returnSlipReferenceOrderNo}" size="50" name="rs.returnSlipReferenceOrderNo"></s:textfield></td>
						<td><s:submit cssClass="myButtons" type="button" label="Get Details" action="loadOrdersByReferenceNoAction">	</s:submit></td>
						</tr>
				</table>
				</s:else>
				</p>
				
				<h4 class="form" onclick="javascript:collapseSection('arrow1','div1')"><img id="arrow1" src="images/expand2.jpg"/>ITEMS ORDERED REFERENCE</h4>
				<div id="div1" class="sectionDiv">
				
				<table border="1px" class="lists">
			
							<tr class="others">
								<td class="desc" width="20px">Item Code</td>
								<td class="desc" width="5px">QTY</td>
								<td class="header" width="290px">Description</td>
								<td width="5px">UOM</td>
								<td width="10px">Unit Cost</td>
							</tr>
							<s:iterator value="poDetailsHelperToCompare.purchaseOrderDetailsList" >
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
									<td><s:property value="itemCode" /></td>
									<td><s:property value="quantity" /></td>
									<td><s:property value="description" /></td>
									<td><s:property value="unitOfMeasurement" /></td>
									<td><s:property value="unitCost" /></td>
								</tr>
							</s:iterator>
				</table>
				</div>
				
				<p>
						<table class="form">
						<tr>
								<s:hidden name="parentPage" value="ReturnSlip"/>
								<s:hidden name="forWhat" value="%{forWhat}"/>
								<s:hidden name="forWhatDisplay" value="%{forWhatDisplay}" />
								<s:hidden name="poDetailsHelperDraft.hiddenDelimetedOrderDetailsItemCode"     	value="%{poDetailsHelperDraft.hiddenDelimetedOrderDetailsItemCode}"></s:hidden>
								<s:hidden name="poDetailsHelperDraft.hiddenDelimetedOrderDetailsDescription"	value="%{poDetailsHelperDraft.hiddenDelimetedOrderDetailsDescription}"></s:hidden>
								<s:hidden name="poDetailsHelperDraft.hiddenDelimetedOrderDetailsQuantity"       value="%{poDetailsHelperDraft.hiddenDelimetedOrderDetailsQuantity}"></s:hidden> 
								<s:hidden name="poDetailsHelperDraft.hiddenDelimetedOrderDetailsUOM" 		    value="%{poDetailsHelperDraft.hiddenDelimetedOrderDetailsUOM}"></s:hidden>
								<s:hidden name="poDetailsHelperDraft.hiddenDelimetedOrderDetailsCost"		    value="%{poDetailsHelperDraft.hiddenDelimetedOrderDetailsCost}"></s:hidden> 
								<s:hidden name="poDetailsHelperDraft.hiddenDelimetedOrderDetailsAmount"		    value="%{poDetailsHelperDraft.hiddenDelimetedOrderDetailsAmount}"></s:hidden>
								<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
								<s:hidden name="poDetailsHelperDraft.hiddenDelimetedOrderDetailsIsVattable"		    value="%{poDetailsHelperDraft.hiddenDelimetedOrderDetailsIsVattable}"></s:hidden>
								<s:hidden name="poDetailsHelperDraft.hiddenDelimetedOrderDetailsVatAmount"		    value="%{poDetailsHelperDraft.hiddenDelimetedOrderDetailsVatAmount}"></s:hidden>
								<s:hidden name="poDetailsHelperDraft.hiddenDelimetedOrderDetailsVattableAmount"		    value="%{poDetailsHelperDraft.hiddenDelimetedOrderDetailsVattableAmount}"></s:hidden>
								<!--END: 2013 - PHASE 3 : PROJECT 4: AZ-->
								
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
						<th colspan="6">SEARCH ITEMS TO BE RETURNED</th>
				</tr>
			</table>
			<div>
							
					<table class="results">
							<tr class="others">
								<td class="desc">Item Code</td>
								<td class="header" width="230px">Description</td>
								<td class="desc">QTY IN</td>
								<td>QTY OUT</td>
								<td>UOM </td>
								<td>UNIT COST</td>

							</tr>

							<tr>
								<td><sx:autocompleter headerValue="Choose One" resultsLimit="-1" list="itemCodeList" maxlength="20" name="orderDetails.itemCode"></sx:autocompleter></td>
								<td><s:textfield readOnly="readOnly" name="orderDetails.description" theme="simple" size="35"></s:textfield></td>
								<td><s:textfield size="5" name="orderDetails.quantityIn" theme="simple"></s:textfield></td>
								<td><s:textfield size="5" name="orderDetails.quantityOut" theme="simple"></s:textfield></td>
								<td><s:textfield size="10" readOnly="readOnly" name="orderDetails.unitOfMeasurement" theme="simple"></s:textfield></td>
								<td><s:textfield size="10" readOnly="readOnly" name="orderDetails.unitCost" theme="simple"></s:textfield>
								<s:hidden name="orderDetails.quantity"		    value="%{orderDetails.quantity}"></s:hidden>
								</td>
							</tr>
						</table>
						</div>
						<table>
							<tr>
								<td><s:submit cssClass="myButtons" type="button" label="Get Details" action="loadOrderDetailsAction">	</s:submit> </td>
								<td><s:submit cssClass="myButtons" type="button" label="Update Item" action="addOrderDetailAction">		</s:submit></td>
							</tr>
					</table>
						
						<table class="form">
						<tr>
						<th colspan="6">ITEMS RETURNED</th>
						</tr>
						</table>
						<table class="results">
							<tr class="others">
								<td class="desc" width="30px">Item Code</td>
								<td class="desc" width="10px">QTY</td>
								<td class="header" width="175px">Description</td>
								<td width="10px">UOM</td>
								<td width="30px">Unit Cost</td>
								<td width="10px">Amount</td>
							</tr>
							<s:iterator value="poDetailsHelperDraft.purchaseOrderDetailsList" >
								<tr>
									<td><s:property value="itemCode" /></td>
									<td><s:property value="quantity" /></td>
									<td><s:property value="description" /></td>
									<td><s:property value="unitOfMeasurement" /></td>
									<td><s:property value="unitCost" /></td>
									<td><s:property value="amount" /></td>
								</tr>
							</s:iterator>
							<tr>
							<td colspan=5 align="right" class="totalAmount">Total Amount:</td>
							<td align="right" class="totalAmount"><s:property value="poDetailsHelperDraft.totalAmount" /></td>
							</tr>
						</table>
				</p>
			</div>
		</div>

		<div class="forButtons">
			<p>
				<table class="forButtons" align="center">
						<tr>
							<s:hidden name="subModule" value="returnSlip" />
							
							<s:if test="%{forWhatDisplay == 'edit'}">

								<td><input class="myButtons" id="bEdit" type="button"
									onclick="javascript:toggleAlert('returnSlipForm','returnSlipNo');"
									value="Edit"></input></td>
								<td><s:submit disabled="%{forWhat}" id="bUpdate"
										cssClass="myButtons" type="button" value="Update"
										action="updateInventoryAction"></s:submit></td>
								<td><s:submit cssClass="myButtons" type="button" id="bDel"
										value="Delete"
										onclick="javascript:inventoryConfirmation('returnSlipForm');"></s:submit></td>
								<td><s:submit cssClass="myButtons" type="button"
										id="bPrint" value="Print" action="printInventoryAction"></s:submit></td>
								<td><input class="myButtons" name="clear" id="bCancel"
									type="button"
									onclick="javascript:clearAll('wholeForm','returnSlipForm');"
									value="New Entry"></input></td>

							</s:if>

							<s:else>

								<td><s:submit disabled="%{forWhat}" cssClass="myButtons"
										type="button" id="bNew" value="New Entry"
										action="addInventoryAction"></s:submit></td>
								<td><input class="myButtons" name="clear" type="button"
									id="bCancel"
									onclick="javascript:clearAll('wholeForm','returnSlipForm');"
									value="Cancel"></input></td>

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
	<jsp:include page="/jsp/util/companyHeader.jsp" />
				<h3 class="form">Return Slip</h3>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Return Slip Details</th>
						</tr>
						<tr>
							<td><s:textfield label="Return Slip No:" value="%{rs.returnSlipNo}"></s:textfield></td>
							<td><s:textfield disabled="%{forWhat}" label="Order Reference No:" value="%{rs.returnSlipReferenceOrderNo}" size="20"></s:textfield></td>
						
						</tr>
					</table>
					<table class="form">
						<tr>
							<td><s:textfield label="Return Date:" value="%{rs.returnDate}" ></s:textfield></td>
							<s:if test="%{rs.returnSlipTo != 'CtoW'}">
									<td><s:textfield value="WAREHOUSE TO SUPPLIER" label="Return TO/FROM:" size="40"></s:textfield></td>
								</s:if>
								<s:else>
									<td><s:textfield value="CUSTOMER TO WAREHOUSE" label="Return TO/FROM:" size="40"></s:textfield></td>
								</s:else>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" label="PreparedBy:" value="%{rs.preparedBy}" size="30"></s:textfield></td>
							<td><s:textfield disabled="%{forWhat}" label="ApprovedBy:" value="%{rs.approvedBy}" size="30"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textarea label="Comments" value="%{rs.memo.memo}" cols="25" rows="5"/></td>
						</tr>
						
					</table>
				</p>
				<p>
					<table class="form">
						<tr>
						<th colspan="6">ITEMS RETURNED</th>
						</tr>
				</table>
				<table class="results">
				
						</table>
						<div>
						<table border="1px" class="lists">
							<tr class="others">
								<td class="desc" width="20px">Item Code</td>
								<td class="desc" width="5px">QTY</td>
								<td class="header" width="290px">Description</td>
								<td width="5px">UOM</td>
								<td width="10px">Unit Cost</td>
								<td width="10px">Amount</td>
							</tr>
							<s:iterator value="poDetailsHelperDraft.purchaseOrderDetailsList" >
								<tr>
									<td><s:property value="itemCode" /></td>
									<td><s:property value="quantity" /></td>
									<td><s:property value="description" /></td>
									<td><s:property value="unitOfMeasurement" /></td>
									<td><s:property value="unitCost" /></td>
									<td><s:property value="amount" /></td>
								</tr>
							</s:iterator>
							<tr>
								<td colspan=5 align="right" class="totalAmount">Total Amount:</td>
								<td align="right" class="totalAmount"><s:property value="poDetailsHelperDraft.totalAmount" /></td>
							</tr>
						</table>
					</div>
				
				</p>
			</div>

</s:else>
</body>
</html>