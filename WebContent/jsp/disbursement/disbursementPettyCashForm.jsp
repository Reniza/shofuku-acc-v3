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
   <script type="text/javascript" src="js/deleteConfirmation.js"></script> 
     <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
	<script type="text/javascript" src="js/Transactions.js"></script>
	<script type="text/javascript" src="js/onChangeType.js"></script>
  <sx:head/>
 	
	 <script type='text/javascript'>
	var startWith=3;
	var subMenu=0;
	</script>
<title>Petty Cash Form</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
	<h4 class="title">DISBURSEMENT</h4>
	<s:form action="newDisbursementEntryAction" validate="true" id="pcForm">

	<div class="form" id="wholeForm">
		<div class="errors">
		<s:actionerror/>
		<s:actionmessage/>
	</div>
<div id="voucherForm">
<h3 class="form">PETTY CASH</h3>
		<p>
		<table class="form">
			<tr>
				<th colspan="6">Voucher Details</th>
			</tr>
			<tr>
				<td><s:textfield disabled="%{forWhat}" readOnly="readOnly" label="PC Voucher No:" name="pc.pcVoucherNumber" id="pcId"></s:textfield></td>
				<s:hidden name="pcNo" value="%{pc.pcVoucherNumber}"/>
				<td class="others">PC Voucher Date:</td>
				<td><sx:datetimepicker displayWeeks="5" displayFormat="MMM-dd-yy" name="pc.pcVoucherDate"></sx:datetimepicker></td>
				<td><s:textfield disabled="%{forWhat}" label="Report Reference No:" name="pc.referenceNo" id="pcId"></s:textfield></td>
				
			</tr>
		</table>
		<table class="form">
			<tr>
				<s:if test="%{forWhatDisplay == 'edit'}">	
						<td>
							<s:textfield disabled="%{forWhat}" label ="Payor:" size="90" name="pc.payee"></s:textfield>
						</td> 
					</s:if> 
					<s:else>  
					    <td>
					    	<sx:autocompleter  label="Payor " listValue="supplierName" list="supplierList" maxlength="50" resultsLimit="-1" name="pc.payee"></sx:autocompleter>
						</td>
					</s:else>
			</tr>
			<tr>
				<td><s:textfield disabled="%{forWhat}" label="Particulars:" size="90" name="pc.particulars"></s:textfield></td>
			</tr>
		</table>
		<table class="form">
			<tr>
				<td class="others">Description:</td>
				<td><sx:autocompleter headerValue="Choose One" dropdownHeight="50px" size="90" resultsLimit="-1"
				listValue="value" list="classifList" maxlength="50"  name="pc.description"></sx:autocompleter>
				</td>
				<td width="90"><s:textfield disabled="%{forWhat}" label="Amount: PHP" name="pc.amount" id="amount"></s:textfield></td>
			</tr>
		</table>
		<table class="form">
						<tr>
							<th colspan="6">VAT Details</th>
						</tr>
					<tr>
						<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
						<td><s:textfield readonly="true" label="Vatable Amount: PHP" name="pc.vatDetails.vattableAmount"  id="vattableAmount"></s:textfield></td>
						<td><s:textfield readonly="true" label="VAT Amount: PHP" name="pc.vatDetails.vatAmount" id="vatAmount"></s:textfield></td>
						<td>No VAT? <s:checkbox id="checkVat" name="checkVat" onclick="javascript:computeVat()"></s:checkbox></td>
						<!--END: 2013 - PHASE 3 : PROJECT 4: AZHEE-->
						
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="TIN:" name="pc.vatDetails.tinNumber"></s:textfield></td>
						<td><s:textfield disabled="%{forWhat}" label="Reference No:" name="pc.vatDetails.orNo"></s:textfield></td>
					</tr>
					</table>
					<table class="form">
						<tr>
							<td>Address:</td>
							<td ><s:textfield size="90" disabled="%{forWhat}" name="pc.vatDetails.address"></s:textfield></td>
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
					 		<input type="hidden" name="accountCodeActions.${profile.accountCode}" id="accountCodeActions.${profile.accountCode}" value="${profile.accountingRules.disbursementPettyCash}"/>
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
		<s:if test="%{forWhat == 'print'}">
		<p>
			<table class="form">
			<tr>
				<th colspan="6">Approval Details</th>
			</tr>
			<tr>
				<td><s:label label="Released By:"></s:label></td>
				<td><s:label label="Approved By:"></s:label></td>
			</tr>
			</table>
		</p>
		</s:if>				
</div>
</div>


<div class="forButtons">
<p>
	<table class="forButtons" align="center">	
	<tr>
	<s:hidden name="subModule" value="pettyCash"/>	
				<s:if test="%{forWhatDisplay == 'edit'}">
					
					<td><input class="myButtons" name="edit" type="button" onclick="javascript:toggleAlert('voucherForm','pcId');" value="Edit"></input></td>
					<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateDisbursementAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Delete" onclick="javascript:disbursementConfirmation('pcForm');"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Print" action="printDisbursementAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','pcForm');" value="New Entry"></s:submit></td>
				
				</s:if>
				
				<s:else>
					<td><s:submit cssClass="myButtons" type="button" value="New Entry" action="addDisbursementAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','pcForm');" value="Cancel"></s:submit></td>
				</s:else>
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
	<h3 class="form">PETTY CASH</h3>
		<p>
		<table class="form">
			<tr>
				<th colspan="6">Voucher Details</th>
			</tr>
			<tr>
				<td><s:textfield  label="PC Voucher No:" value="%{pc.pcVoucherNumber}"></s:textfield></td>
				<td><s:textfield label="PC Voucher Date:" displayFormat="MMM-dd-yyyy" value="%{pc.pcVoucherDate}"></s:textfield></td>
			</tr>
		</table>
		<table class="form">
			<tr>
				<td><s:textfield size="90" label="Payee:" value="%{pc.payee}"></s:textfield></td>
			</tr>
			<tr>
				<td><s:textarea cols="70" rows="3" label="Particulars:" value="%{pc.particulars}"></s:textarea></td>
			</tr>
		</table>
		<table class="form">
			<tr>
				<td><s:textfield size="50" label="Description:"  value="%{pc.description}"></s:textfield></td>
				<td width="90"><s:textfield  label="Amount: PHP" value="%{pc.amount}"></s:textfield></td>
			</tr>
		</table>
		</p>
		<p>
			<table class="form">
				<tr>
						<th colspan="6">Vat Details</th>
				</tr>
				<tr>
					<td><s:textfield  label="Vattable Amount: PHP" value="%{pc.vatDetails.vattableAmount}"></s:textfield></td>
					<td><s:textfield  label="Vat Amount: PHP" value="%{pc.vatDetails.vatAmount}"></s:textfield></td>
				</tr>
				<tr>
					<td><s:textfield  label="TIN #:" value="%{pc.vatDetails.tinNumber}"></s:textfield></td>
					<td><s:textfield  label="Reference No:" value="%{pc.vatDetails.orNo}"></s:textfield></td>
				</tr>
			</table>
			<table class="form">
				<tr>
					<td><s:textfield  label="Address:" size = "90" value="%{pc.vatDetails.address}"></s:textfield></td>
				</tr>
			</table>
		</p>	
		<p>
		<table class="form">
					<tr>
						<th colspan="6">Approval Details</th>
					</tr>
				</table>
				<p></p>
				<table width="670px">
							<tr>
								<td width="100px" class="others">RELEASED BY : </td>
								<td width="400px"></td>
								<td width="150px"class="others" style="text-align:right;" >APPROVED BY :</td>							
							</tr>
							<tr>
								<td style="padding-top:10px;">_____________________</td>
								<td width="400px"></td>
								<td style="padding-top:10px;text-align:right;">____________________</td>
								
							</tr>
						</table>
		</p>

</div>
</s:else>
</body>
</html>