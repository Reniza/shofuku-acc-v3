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
  <sx:head/>
  <script type="text/javascript" src="js/Transactions.js"></script>	
  <script type="text/javascript" src="js/onChangeType.js"></script>
	 <script type='text/javascript'>
	var startWith=3;
	var subMenu=2;
	</script>
<title>Check Payment Form</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
	<h4 class="title">DISBURSEMENT</h4>
	<s:form action="newDisbursementEntryAction" validate="true" id="chpForm">
	
	<div class="form" id="wholeForm">
		<div class="errors">
		<s:actionerror/>
		<s:actionmessage/>
	</div>
<div id="voucherForm">
<h3 class="form">CHECK PAYMENT</h3>
		<p>
		<table class="form">
			<tr>
					<th colspan="6">Voucher Details</th>
			</tr>
			<tr>
				<td><s:textfield disabled="%{forWhat}" label="Check Payment No:" name="chp.checkVoucherNumber" id="chpId"></s:textfield></td>
				<s:hidden name="chpNo" value="%{chp.checkVoucherNumber}"/>
				<td class="others">Check Payment Date:</td>
				<td><sx:datetimepicker displayWeeks="5" displayFormat="MMM-dd-yy" name="chp.checkVoucherDate"></sx:datetimepicker></td>
			</table>
		<table class="form">
			<tr>
				<s:if test="%{forWhatDisplay == 'edit'}">	
						<td>
							<s:textfield disabled="%{forWhat}" label ="Payor:" size="90" name="chp.payee"></s:textfield>
						</td> 
					</s:if> 
					<s:else>  
					    <td>
					    	<sx:autocompleter label="Payor " listValue="supplierName" list="supplierList" maxlength="50" resultsLimit="-1" name="chp.payee"></sx:autocompleter>
						</td>
					</s:else>
			</tr>
			<tr>
				<td><s:textfield disabled="%{forWhat}" label="Particulars:" size="90" name="chp.particulars"></s:textfield></td>
			</tr>
			<tr>
				<td><s:textfield disabled="%{forWhat}" label="Amount in Words:" size="90" name="chp.amountInWords"></s:textfield></td>
			</tr>
		</table>
		<table class="form">
			<tr>
				<td class="others">Description:</td>
				<td><sx:autocompleter headerValue="Choose One" dropdownHeight="50px" size="90" 
				listValue="value" list="classifList" maxlength="50" resultsLimit="-1" name="chp.description"></sx:autocompleter>
				</td>
				<td ><s:textfield disabled="%{forWhat}" label="Amount: PHP" name="chp.amount" id="amount"></s:textfield>
			</tr>
		</table>
		</p>
		<p>
		<!--  REMOVED VAT
		<table class="form">
			<tr>
				<td><s:select disabled="%{forWhat}" label="VAT? :" name="chp.vat" value="%{chp.vat}" list="#{'NON VAT':'NON VAT','VAT':'VAT'}">
				</s:select></td>
				<td><s:textfield disabled="%{forWhat}" label="Vat Amount:" name="chp.vatAmount">
				</s:textfield></td>
				<td><s:textfield disabled="%{forWhat}" label="Final Amount:" name="chp.finalAmount">
				</s:textfield></td>
			</tr>
		</table>
		 -->
			<table class="form">
						<tr>
							<th colspan="6">VAT Details</th>
						</tr>
					<tr>
						<!--START: 2013 - PHASE 3 : PROJECT 4: AZ- removed read only VAT-->
						<td><s:textfield label="Vatable Amount: PHP" name="chp.vatDetails.vattableAmount"  id="vattableAmount"></s:textfield></td>
						<td><s:textfield label="VAT Amount: PHP" name="chp.vatDetails.vatAmount" id="vatAmount"></s:textfield></td>
						<td>No VAT? <s:checkbox id="checkVat" name="checkVat" onclick="javascript:computeVat()"></s:checkbox></td>
						<!--END: 2013 - PHASE 3 : PROJECT 4: AZHEE-->
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="TIN:" name="chp.vatDetails.tinNumber"></s:textfield></td>
						<td><s:textfield disabled="%{forWhat}" label="Reference No:" name="chp.vatDetails.orNo"></s:textfield></td>
					</tr>
					</table>
					<table class="form">
						<tr>
							<td>Address:</td>
							<td ><s:textfield size="90" disabled="%{forWhat}" name="chp.vatDetails.address"></s:textfield></td>
						</tr>
					</table>
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
					 		<input type="hidden" name="accountCodeActions.${profile.accountCode}" id="accountCodeActions.${profile.accountCode}" value="${profile.accountingRules.disbursementCheckPayment}"/>
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
	<tr>	<s:hidden name="subModule" value="checkPayment"/>
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
	<h3 class="form">CHECK PAYMENT</h3>
		<p>
		<table class="form">
			<tr>
					<th colspan="6">Voucher Details</th>
			</tr>
			<tr>
				<td><s:textfield  label="Check Payment No:" value="%{chp.checkVoucherNumber}"></s:textfield></td>
				<td><s:textfield label="Check Payment Date:" displayFormat="MMM-dd-yyyy" value="%{chp.checkVoucherDate}"></s:textfield></td>
			</table>
		<table class="form">
			<tr>
				<td><s:textfield size="90" label ="Payor:" value="%{chp.payee}"></s:textfield></td>
			</tr>
			<tr>
				<td><s:textarea cols="70" rows="3"  label="Particulars:" value="%{chp.particulars}"></s:textarea></td>
			</tr>
			<tr>
				<td><s:textfield  label="Amount in Words:" size="90" value="%{chp.amountInWords}"></s:textfield></td>
			</tr>
		</table>
		<table class="form">
			<tr>
				<td><s:textfield label="Description" size="50" value="%{chp.description}"></s:textfield></td>
				<td><s:textfield  label="Amount: PHP" value="%{chp.amount}"></s:textfield>
			</tr>
		</table>
		</p>
		<p>
		<!--  START -P3 AZ - REMOVED 
		<table class="form">
			<tr>
				<td><s:textfield disabled="%{forWhat}" label="VAT? :" value="%{chp.vat}">
				</s:textfield></td>
				<td><s:textfield disabled="%{forWhat}" label="Vat Amount:" value="%{chp.vatAmount}">
				</s:textfield></td>
				<td><s:textfield disabled="%{forWhat}" label="Final Amount:" value="%{chp.finalAmount}">
				</s:textfield></td>
			</tr>
		</table>
		-->
		<table class="form">
				<tr>
						<th colspan="6">Vat Details</th>
				</tr>
				<tr>
					<td><s:textfield  label="Vattable Amount: PHP" value="%{chp.vatDetails.vattableAmount}"></s:textfield></td>
					<td><s:textfield  label="Vat Amount: PHP" value="%{chp.vatDetails.vatAmount}"></s:textfield></td>
				</tr>
				<tr>
					<td><s:textfield  label="TIN #:" value="%{chp.vatDetails.tinNumber}"></s:textfield></td>
					<td><s:textfield  label="Reference No:" value="%{chp.vatDetails.orNo}"></s:textfield></td>
				</tr>
			</table>
			<table class="form">
				<tr>
					<td><s:textfield  label="Address:" size = "90" value="%{chp.vatDetails.address}"></s:textfield></td>
				</tr>
			</table>
		</p>	
		
		<p>
			<table class="form">
				<tr>
					<th colspan="8">Bank Details</th>
				</tr>
				<tr>
					<td><s:textfield  label="Bank Name:" value="%{chp.bankName}"></s:textfield></td>
					<td><s:textfield  label="Bank Account No:" value="%{chp.bankAccountNumber}"></s:textfield></td>
				</tr>
				<!--  START: add cheque date -->
				<tr>
					<td><s:textfield label="Cheque No:" value="%{chp.checkNo}"></s:textfield></td>
					<td><s:textfield label="Cheque Date:" value="%{chp.chequeDate}"></s:textfield></td>
				</tr>
			    <!-- END: add cheque date -->
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