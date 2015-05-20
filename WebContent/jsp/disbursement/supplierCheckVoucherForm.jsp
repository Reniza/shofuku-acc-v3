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
  <sx:head/>
	 <script type='text/javascript'>
	var startWith=3;
	var subMenu=3;
	</script>
<title>Check Voucher Form</title>
</head>
<body>

<s:if test="%{forWhat != 'print'}">
	<div class="mainForm">

	<h4 class="title">Supplier Invoice Check Voucher</h4>
	<s:form action="newDisbursementEntryAction" validate="true" id="chpForm">
	
	<div class="form" id="wholeForm">
		<div class="errors">
		<s:actionerror/>
		<s:actionmessage/>
	</div>
<div id="voucherForm">
<p>
<h3 class="form">CHECK VOUCHER</h3>
		
		<table class="form">
			<tr>
					<th colspan="7">Voucher Details</th>
			</tr>
			<tr>
				<td><s:textfield disabled="%{forWhat}" label="Check Voucher No:" name="chp.checkVoucherNumber" id="chpId"></s:textfield></td>
				<s:hidden name="chpNo" value="%{chp.checkVoucherNumber}"/>
				<td class="others">Check Voucher Date:</td>
				<td><sx:datetimepicker displayWeeks="5" displayFormat="MMM-dd-yyyy" name="chp.checkVoucherDate"></sx:datetimepicker></td>
				<td class="others">Invoice Due Date:</td>
				<td><s:date format="MMM-dd-yyyy"  name="chp.dueDate"></s:date></td>
		</table>
		<table class="form">
			<tr>
				<td class="others">Supplier Name (Payee):</td>
				<td colspan="4"><s:textfield readonly="true" name="chp.payee" size="60"></s:textfield></td>
			</tr>
			<tr>
				<td class="others">Amount in Words:</td>
				<td colspan="4"><s:textfield disabled="%{forWhat}" name="chp.amountInWords" size="60"></s:textfield></td>
			</tr>
			<tr>
				<td class="others">Remaining Balance</td>
				<td colspan="4"><s:textfield readOnly="readOnly" name="chp.remainingBalance" size="60"></s:textfield></td>
			</tr>	
			<tr>
				<td class="others">Amount to Pay</td>
				<td colspan="4"><s:textfield disabled="%{forWhat}" name="chp.amountToPay" size="60"></s:textfield></td>
			</tr>		
		</table>
		<table class="form">
			<tr>
				<td class="others">Invoice Reference No:</td>
				<td><sx:autocompleter listValue="supplierInvoiceNo" list="invoiceNoList" maxlength="50" resultsLimit="-1" name="chp.invoice.supplierInvoiceNo"></sx:autocompleter></td>
				<s:hidden name="invId" value="%{chp.invoice.supplierInvoiceNo}"/>
				<td><s:textfield disabled="disabled" label="Invoice Amount PHP" name="chp.amount"></s:textfield></td>
				<s:hidden name="invAmount" value="%{chp.invoice.purchaseOrderDetailsTotalAmount}"/>
			</tr>
		</table>
		
		<table class="form">
						<tr>
							<th colspan="6">VAT Details</th>
						</tr>
					<tr>
						<!--START: 2013 - PHASE 3 : PROJECT 4: AZ-->
						<td><s:textfield readonly="true" label="Vatable Amount: PHP" name="chp.vatDetails.vattableAmount"  id="credit1Amount"></s:textfield></td>
						<td><s:textfield readonly="true" label="VAT Amount: PHP" name="chp.vatDetails.vatAmount"></s:textfield></td>
						
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="TIN:" name="chp.vatDetails.tinNumber"></s:textfield></td>
						<!--  <td><s:textfield disabled="%{forWhat}" label="Reference No:" name="chp.vatDetails.orNo"></s:textfield></td> -->  
					</tr>
					</table>
					<table class="form">
						<tr>
							<td>Address:</td>
							<td ><s:textfield size="90" readonly="true" name="chp.vatDetails.address"></s:textfield></td>
						</tr>
					<!--END: 2013 - PHASE 3 : PROJECT 4: AZHEE-->
			</table>
		<!--END: 2013 - PHASE 3 : PROJECT 4: AZHEE-->
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
					 		<input type="hidden" name="accountCodeActions.${profile.accountCode}" id="accountCodeActions.${profile.accountCode}" value="${profile.accountingRules.disbursementCheckVoucher}"/>
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
			<table class="form">
				<tr>
					<th colspan="8">Bank Details</th>
				</tr>
				<tr>
					<td><s:select disabled="%{forWhat}" label="Bank Name:" name="chp.bankName" list="#{'MBTC-Skyland':'MBTC-Skyland','BDO-Hyper Makati':'BDO-Hyper Makati','BDO-Hyper Adriatico':'BDO-Hyper Adriatico'}"></s:select></td>
					<td><s:select disabled="%{forWhat}" label="Bank Account No:" name="chp.bankAccountNumber" list="#{'007-514-02627-7':'007-514-02627-7','7360047010':'7360047010','4470183744':'4470183744'}"></s:select></td>
				</tr>
				<tr>
					<td><s:textfield disabled="%{forWhat}" label="Check No:" name="chp.checkNo"></s:textfield></td>
				<!--START: add cheque date -->
					<td class="others">Cheque Date:</td>
					<td><sx:datetimepicker displayWeeks="5" displayFormat="MMM-dd-yyyy"  name="chp.chequeDate"></sx:datetimepicker></td>
				<!-- END -->
				</tr>
			</table>	
		<table class="form">
			<tr>
				<td><s:select disabled="%{forWhat}" label="Released?" name="chp.isPrinted" list="#{'NO':'NO','YES':'YES'}"></s:select></td>
				<td><s:select disabled="%{forWhat}" label="Is Encashed?" name="chp.isEncashed" list="#{'NO':'NO','YES':'YES'}"></s:select></td>
			</tr>
		</table>
		</p>
		<p>
			<table class="form">
			<tr>
					<th colspan="6">Invoice Order Details</th>
				</tr>
			</table>
			<table class="results" border="1px">
				
				<tr class="others">
					<td class="desc">Item Code</td>
					<td class="desc">QTY</td>
					<td class="header" width="300px">Description</td>
					<td>UOM</td>
					<td>UNIT COST</td>
					<td>Amount</td>
						
				</tr>
			
				<s:iterator value="orderDetails">
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
						<td class="totalAmount"><s:property value="%{chp.invoice.purchaseOrderDetailsTotalAmount}" /></td>
				</tr>
			</table>
		</p>
	</div>
</div>

<div class="forButtons">
<p>
	<table class="forButtons" align="center">	
	<tr><s:hidden name="subModule" value="supplierCheckVoucher"/>	
				<s:if test="%{forWhatDisplay == 'edit'}">
				
					<td><input class="myButtons" name="edit" type="button" onclick="javascript:toggleAlert('voucherForm','chpId');" value="Edit"></input>
					<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateDisbursementAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Delete" onclick="javascript:disbursementConfirmation('chpForm');"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Print" action="printDisbursementAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Print Check/Voucher" action="printDisbursementInExcelAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','chpForm');" value="New Entry"></s:submit></td>
				
	
				</s:if>
				<s:else>
					<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="New Entry" action="addDisbursementAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','chpForm');" value="Cancel"></s:submit></td>
				
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

	
	<h3 class="form">CHECK VOUCHER</h3>
		<p>
		<table class="form">
			<tr>
					<th colspan="7">Voucher Details</th>
			</tr>
			<tr>
				<td><s:textfield label="Check Voucher No:" value="%{chp.checkVoucherNumber}"/>
				<td><s:textfield label="Check Voucher Date:" value="%{chp.checkVoucherDate}"></s:textfield></td>
				<td><s:textfield label="Invoice Due Date:" value="%{chp.dueDate}"></s:textfield></td>
		</table>
		<table class="form">
			<tr>
				<td ><s:textarea cols="70" rows="3" label="Supplier Name (Payee):" value="%{chp.payee}" ></s:textarea></td>
			</tr>
			<tr>
				<td ><s:textfield label="Amount in Words:" value="%{chp.amountInWords}" size="60"></s:textfield>
			</tr>		
		</table>
		<table class="form">
			<tr>
				<td><s:textfield label="Reference Invoice No:" value="%{chp.invoice.supplierInvoiceNo}"/>
				<td><s:textfield label="Invoice Amount: PHP" value="%{chp.invoice.debit1Amount}"/>
			</tr>
		</table>
		</p>
		<p>
			<table class="form">
				<tr>
					<th colspan="8">Bank Details</th>
				</tr>
				<tr>
					<td><s:textfield label="Bank Name:" value="%{chp.bankName}"></s:textfield></td>
					<td><s:textfield label="Bank Account No:" value="%{chp.bankAccountNumber}"></s:textfield></td>
				</tr>
				<!--  START: add cheque date -->
				<tr>
					<td><s:textfield label="Cheque No:" value="%{chp.checkNo}"></s:textfield></td>
					<td><s:textfield label="Cheque No:" value="%{chp.chequeDate}"></s:textfield></td>
				</tr>
				<!-- END: add cheque date -->
		</table>	
		</p>
		<p>
			<table class="form">
			<tr>
					<th colspan="6">Invoice Order Details</th>
				</tr>
			</table>
			<table class="results" border="1px">
				
				<tr class="others">
					<td class="desc">Item Code</td>
					<td class="desc">QTY</td>
					<td class="header" width="300px">Description</td>
					<td>UOM</td>
					<td>UNIT COST</td>
					<td>Amount</td>
						
				</tr>
			
				<s:iterator value="orderDetails">
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
						<td class="totalAmount"><s:property value="%{chp.invoice.purchaseOrderDetailsTotalAmount}" /></td>
				</tr>
			</table>
		</p>
		
		<p>
			<table class="form">
			<tr>
				<th colspan="7">Approval Details</th>
			</tr>
			<tr>
				<td><s:label label="PREPARED BY:"></s:label></td>
				<td><s:label label="APPROVED BY:"></s:label></td>
			
			</tr>
			<tr>
				<td style="padding-top:10px;"><s:label label="_________________"></s:label></td>
				<td style="padding-top:10px;"><s:label label="_________________"></s:label></td>
			
			</tr>
			<tr>
				<td colspan="6" style="text-align:right;padding-top:10px;"><s:label label="RECEIVED BY:"></s:label></td>
			</tr>
			<tr>
				<td colspan="6" style="padding-top:10px;text-align:right;"><s:label label="_________________"></s:label></td>
			</tr>
			
			</table>
		</p>
	</div>
</s:else>

</body>
</html>