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
 	<script type="text/javascript" src="js/expandingSection.js"></script> 
 	<script type="text/javascript" src="js/deleteConfirmation.js"></script>
 	<link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	 
  <script type="text/javascript" src="js/Transactions.js"></script>
	 <script type='text/javascript'>
	var startWith=4;
	var subMenu=1;
	</script>
<sx:head parseContent="true"/>
<title>OR Other Incomes</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
<h4 class="title">RECEIPTS</h4>
	
	<s:form action="showOROtherIncomeForm" validate="ture" id="oroForm">
	<div class="form" id="wholeForm">
	<div class="errors">
		<s:actionerror/>
		<s:actionmessage/>
		
	</div>
		<div id="receiptForm">
		<h3 class="form">OR Other Incomes</h3>
			<p>
				<table class="form">
					<tr>
						<th colspan="6">Payment Details</th>
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="OR No.:" name="orOthers.orNumber" id="oroId"/></td>
						<s:hidden name="orONo" value="%{orOthers.orNumber}"/>
						<td class="others">OR Date:</td>
						<td><sx:datetimepicker name="orOthers.orDate" displayFormat="MMM-dd-yy" displayWeeks="5"/></td>
						</tr>
				</table>
				<table class="form">
					<tr>
						<s:if test="%{forWhatDisplay == 'edit'}">	
							<td>
								<s:textfield disabled="%{forWhat}" label ="Received From" size="90" name="orOthers.receivedFrom"></s:textfield>
							</td> 
						</s:if> 
						<s:else>  
						    <td>Received From: </td>
						    <td>
					    	<sx:autocompleter listValue="customerName" list="customerList" name="orOthers.receivedFrom"></sx:autocompleter>
							</td>
						</s:else> 
					</tr>
				</table>
				<table class="form">		
					<tr>
						<td><s:textfield label="Address:" disabled="%{forWhat}" size="95" name="orOthers.address"/></td>
					</tr>
				</table>
				<table class="form">
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="Sales Invoice No:" name="orOthers.salesInvoiceNumber"/></td>
						<td><s:textfield disabled="%{forWhat}" label="TIN:" name="orOthers.tin"/></td>
						<td><s:textfield disabled="%{forWhat}" label="Bus. Style:" name="orOthers.busStyle"/></td>
					</tr>
					<tr>
						<td><s:select disabled="%{forWhat}" label="Payment Type:" name="orOthers.inFullPartialPaymentOf"
							list="#{'Partial':'Partial','Full':'Full'}"/></td>
						<td><s:textfield disabled="%{forWhat}" label="Amount: PHP" name="orOthers.theAmountOf"></s:textfield></td>
						<td>of <s:textfield disabled="%{forWhat}" label="Full Amount: PHP" name="orOthers.amount"></s:textfield></td>
					</tr>
				</table>
				<table class="form">
					<tr>
						<td><s:textfield label="Amount in Words:" disabled="%{forWhat}" size="90" name="orOthers.amountInWords"/></td>
					</tr>
				
				</table>
				<table class="form">
						<tr>
							<th colspan="6">VAT Details</th>
						</tr>
					<tr>
						<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
						<td><s:textfield readonly="true" label="Vatable Amount: PHP" name="orOthers.vatDetails.vattableAmount"  id="credit1Amount"></s:textfield></td>
						<td><s:textfield readonly="true" label="VAT Amount: PHP" name="orOthers.vatDetails.vatAmount"></s:textfield></td>
						<!--END: 2013 - PHASE 3 : PROJECT 4: AZHEE-->
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="Reference No:" name="orOthers.vatDetails.orNo"></s:textfield></td>
					</tr>
					</table>
					
				<table class="form">
					<tr>
						<th colspan="6">Cash/Check Details</th>
					</tr>
					<tr>
						
						<td><s:textfield disabled="%{forWhat}" label="Amount in Check: PHP" name="orOthers.check"></s:textfield></td>
						<td><s:textfield disabled="%{forWhat}" label="Check No." name="orOthers.bankCheckNo"></s:textfield></td>
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="Amount in Cash: PHP" name="orOthers.cash"></s:textfield></td>
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="Cash/Check Total Amount: PHP" name="orOthers.total"></s:textfield></td>
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
					 		<input type="hidden" name="accountCodeActions.${profile.accountCode}" id="accountCodeActions.${profile.accountCode}" value="${profile.accountingRules.receiptsOrOther}"/>
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
			</p>
		</div>
	</div>
	<div class="forButtons">
	<p>
		<table class="forButtons" align="center">
				<tr>
					<s:hidden name="subModule" value="orOthers"/> 
			
			<s:if test="%{forWhatDisplay == 'edit'}">
				
					<td><input class="myButtons" name="edit" type="button" onclick="javascript:toggleAlert('receiptForm','oroId');" value="Edit"></input></td>
					<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateReceiptAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Delete" onclick="javascript:receiptConfirmation('oroForm');"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Print" action="printReceiptAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Print Triplicate" action="printReceiptInExcelAction"></s:submit></td>
					
					<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','oroForm');" value="New Entry"></input></td>
				
				</s:if>
					
				<s:else>
				<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="New Entry" action="addReceiptAction"></s:submit></td>
					
					<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','oroForm');" value="Cancel"></input></td>
				   
				</s:else>
			</tr>
		</table>
</p>
</div>
</s:form>
</div>
</s:if>
<!--  div print -->
<s:else>
<div class="print">
		<jsp:include page="/jsp/util/companyHeader.jsp"/>

<h3 class="form">OR Other Incomes</h3>
			<p>
				<table class="form">
					<tr>
						<th colspan="6">Payment Details</th>
					</tr>
					<tr>
						<td><s:textfield  label="OR No.:" value="%{orOthers.orNumber}"/></td>
						<td><s:textfield label="OR Date:" value="%{orOthers.orDate}" displayFormat="MMM-dd-yyyy"/></td>
						
					</tr>
				</table>
				<table class="form">
				<tr>
					<td><s:textfield  label="Received from" value="%{orOthers.receivedFrom}" size="90"/></td>
				</tr>
					<tr>
						<td><s:textarea cols="70" rows="3" label="Address:"  value="%{orOthers.address}"/></td>
					</tr>
				</table>
				<table class="form">
					<tr>
						<td><s:textfield  label="Sales Invoice No:" value="%{orOthers.salesInvoiceNumber}"/></td>
						<td><s:textfield  label="TIN:" value="%{orOthers.tin}"/></td>
						<td><s:textfield  label="Bus. Style:" value="%{orOthers.busStyle}"/></td>
					</tr>
					<tr>
						<td><s:textfield  label="Payment Type:" value="%{orOthers.inFullPartialPaymentOf}"/></td>
						<td><s:textfield  label="Amount: PHP" value="%{orOthers.theAmountOf}"></s:textfield></td>
						<td>of <s:textfield  label="Full Amount: PHP" value="%{orOthers.amount}"></s:textfield></td>
					</tr>
				</table>
				<table class="form">
					<tr>
						<td><s:textfield label="Amount in Words:"  size="90" value="%{orOthers.amountInWords}"/></td>
					</tr>
				
				</table>
				<table class="form">
					<tr>
						<th colspan="6">VAT Details</th>
					</tr>
					<tr>
						<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
						<td><s:textfield readonly="true" label="Vatable Amount: PHP" value="%{orOthers.vatDetails.vattableAmount}"></s:textfield></td>
						<td><s:textfield readonly="true" label="VAT Amount: PHP" value="%{orOthers.vatDetails.vatAmount}"></s:textfield></td>
						<!--END: 2013 - PHASE 3 : PROJECT 4: AZHEE-->
					</tr>
					<tr>
						<td><s:textfield readonly="true" label="Reference No:" value="%{orOthers.vatDetails.orNo}"></s:textfield></td>
					</tr>
				</table>
				<table class="form">
					<tr>
						<th colspan="6">Cash/Check Details</th>
					</tr>
					<tr>
						
						<td><s:textfield  label="Check No." value="%{orOthers.bankCheckNo}"></s:textfield></td>
						<td><s:textfield  label="Cash/Check Total Amount: PHP" value="%{orOthers.total}"></s:textfield></td>
					</tr>
					<tr>
						<td><s:textfield  label="Amount in Check: PHP" value="%{orOthers.check}"></s:textfield></td>
						<td><s:textfield  label="Amount in Cash: PHP" value="%{orOthers.cash}"></s:textfield></td>
					</tr>
				</table>
			</p>
		</div>
</s:else>
</body>
</html>