<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="auth" uri="/tld/Authorization.tld"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
  <script type="text/javascript" src="js/expandingSection.js"></script>
  <script type="text/javascript" src="js/onChangeType.js"></script>  
    <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
   <script type='text/javascript'>
	var startWith=3;
	var subMenu=4;
	</script>
<title>Disbursement Search Form</title>
</head>

<body>
<div class="mainForm">
<h4 class="title">DISBURSEMENT</h4>
		<div class="errors">
			<s:actionerror/>
			<s:actionmessage/>
			
		</div>
	<s:form action="searchDisbursementAction" validate="true" id="searchForm">
	<s:hidden name="clicked" id="clicked" value="true"/>
	<s:set name="subModule" value="subModule"/>
	<s:set name="moduleParameterValue" value="moduleParameterValue"/>
	<s:set name="moduleParameter" value="moduleParameter"/>		
		<p>
			<table class="form">
				<tr>
					<td>
						<s:select label="Disbursement Module:" 
						headerKey="-1" headerValue="--Choose Module--"
						list="#{'AA':'PettyCash', 'BB':'CashPayment', 'CC':'CheckPayment','checkVoucher':'Invoice Check Voucher'}" 
						name="subModule" onchange="javascript:onTypeChangeDisbursement('searchForm');"/>
					</td>
						
					
				<s:if test="%{#subModule == 'AA'}">
					<td>
						<s:select label="Search Petty Cash By:" 
						list="#{'ALL':'ALL','pcVoucherNumber':'PT Voucher No.', 'payee':'PT Payee', 'referenceNo':'PT Reference No'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeDisbursement('searchForm');"/>
					</td>
				</s:if>
				<s:if test="%{#subModule == 'BB'}">
					<td>
						<s:select label="Search Cash Payment By:" 
						list="#{'ALL':'ALL','cashVoucherNumber':'CP Voucher No.', 'payee':'CP Payee'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeDisbursement('searchForm');"/>
					</td>
				</s:if>
				<s:if test="%{#subModule == 'CC'}">
					<td>
						<s:select label="Search Check Payment By:" 
						list="#{'ALL':'ALL','checkVoucherNumber':'CHP Voucher No.', 'payee':'CHP Payee', 'checkNo':'Check No'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeDisbursement('searchForm');"/>
					</td>
				</s:if>
				<s:if test="%{#subModule == 'checkVoucher'}">
					<td>
						<s:select label="Search Invoice Check Voucher By:" 
						list="#{'ALL':'ALL','checkVoucherNumber':'CHP Voucher No.','invoiceNo':'Invoice No', 'checkNo':'Check No','payee':'Supplier Name'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeDisbursement('searchForm');"/>
					</td>
				</s:if>
				</tr>
				<tr>
					<s:if test="%{#subModule == 'ALL'}">
						
					</s:if>
					<s:else>
					<td>
						<s:textfield label="Search Value:" name="moduleParameterValue"/>
					</td>
					</s:else>
					<td>
						<s:submit cssClass="myButtons" label="Submit" value="SEARCH"></s:submit>
					</td>
				</tr>
				
			</table>
		</p>
	</s:form>
		
	<div class="results">
		<p>
			  
				<table class="results">
					<s:if test="%{#subModule == 'AA'}">
						<tr>
							<th>PC Voucher No</th>
							<th width="400px">Payee</th>
							<th>Voucher Date</th>
							<th>Reference No</th>
							
						</tr>
					</s:if> 
					<s:elseif test="%{#subModule == 'BB'}">
						<tr>
							<th>CP Voucher No</th>
							<th width="400px">Payee</th>
							<th>Voucher Date</th>
						</tr>
					</s:elseif> 
					<s:elseif test="%{#subModule == 'CC'}">
						<tr>
							<th>CHP Voucher No</th>
							<th width="400px">Payee</th>
							<th>Voucher Date</th>
							<th>Check No</th>
						</tr>
					</s:elseif> 
					<s:elseif test="%{#subModule == 'checkVoucher'}">
						<tr>
							<th>CHP Voucher No</th>
							<th>Voucher Date</th>
							<th width="400px">Supplier Name</th>
							<th>Due Date</th>
							<th>Invoice No</th>
							<th>Check No</th>
						</tr>
					</s:elseif> 
					
				<s:iterator value="disbursementList" status="disbursementList">
					<s:if test="%{#subModule == 'AA'}">
						<tr>
							<td align="left">
								<auth:isAuth role="19">
									<s:url id="displayId" action="editDisbursementAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="pc.pcVoucherNumber" value="%{pcVoucherNumber}">voucherNo</s:param>
										<s:param name="subModule" value="%{'AA'}">subModule</s:param>
									</s:url>
								</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="pcVoucherNumber"/></s:a>
							</td>
							<td align="left"><s:property value="payee"/></td>
							<td><s:property value="pcVoucherDate" /></td>
							<td><s:property value="referenceNo" /></td>
							
							
						</tr>
					</s:if>
					<s:elseif test="%{#subModule == 'BB'}">
						<tr>
							<td align="left">
								<auth:isAuth role="20">
									<s:url id="displayId" action="editDisbursementAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="cp.cashVoucherNumber" value="%{cashVoucherNumber}">cashVoucherNumber</s:param>
										<s:param name="subModule" value="%{'BB'}">subModule</s:param>
									</s:url>
								</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="cashVoucherNumber"/></s:a>
							</td>
						
							<td align="left"><s:property value="payee"/></td>
							<td align="left"><s:property value="cashVoucherDate" /></td>
						</tr>
					</s:elseif>
					
					<s:elseif test="%{#subModule == 'CC'}">
						<tr>
							<td align="left">
								<auth:isAuth role="21">
									<s:url id="displayId" action="editDisbursementAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="chp.checkVoucherNumber" value="%{checkVoucherNumber}">checkVoucherNumber</s:param>
										<s:param name="subModule" value="%{'CC'}">subModule</s:param>
									</s:url>
								</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="checkVoucherNumber"/></s:a>
							</td>
							
							<td align="left"><s:property value="payee"/></td>
							<td align="left"><s:property value="checkVoucherDate" /></td>
							<td align="left"><s:property value="checkNo" /></td>
						
							
						</tr>
					</s:elseif>
					<s:elseif test="%{#subModule == 'checkVoucher'}">
						<tr>
							<td align="left">
								<auth:isAuth role="22">
									<s:url id="displayId" action="editDisbursementAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="chp.checkVoucherNumber" value="%{checkVoucherNumber}">checkVoucherNumber</s:param>
										<s:param name="subModule" value="%{'checkVoucher'}">subModule</s:param>
									</s:url>
								</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="checkVoucherNumber"/></s:a>
							</td>
							
							<td align="left"><s:property value="checkVoucherDate" /></td>
							<td align="left"><s:property value="payee"/></td>
							<td align="left"><s:property value="dueDate"/></td>
					<!--  	invoice.receivingReport.supplierPurchaseOrder.paymentDate"/></td>-->	
							<td><s:property value="invoice.supplierInvoiceNo"/></td>
							<td><s:property value="checkNo" /></td>
						</tr>
					</s:elseif>
				</s:iterator>
			</table>		
		</p>
	</div>
</div>
</body>
</html>