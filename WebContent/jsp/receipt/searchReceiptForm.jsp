<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ taglib prefix="auth" uri="/tld/Authorization.tld"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
  <script type="text/javascript" src="js/expandingSection.js"></script> 
  <script type="text/javascript" src="js/onChangeType.js"></script>
  <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
<sx:head parseContent="true"/>
<script type="text/javascript" src="js/expandingMenu.js"></script> 	
	 <script type='text/javascript'>
	var startWith=4;
	var subMenu=3;
	</script>
<title>Receipts Search Form</title>
</head>

<body>
<div class="mainForm">
<h4 class="title">RECEIPTS</h4>
		<div class="errors">
			<s:actionerror/>
			<s:actionmessage/>	
		</div>
		<s:form action="searchReceiptAction" validate="true" id="searchForm">
			<s:hidden name="clicked" id="clicked" value="true"/>
			<s:set name="receiptModule" value="receiptModule"/>
			<s:set name="moduleParameterValue" value="moduleParameterValue"/>
			<s:set name="moduleParameter" value="moduleParameter"/>		
			<p>
			<table class="form">
				<tr>
					<td>
						<s:select label="Receipt Module:" 
						headerKey="-1" headerValue="--Choose Module--"
						list="#{'orSales':'ORSales', 'orOthers':'OROthers', 'ccReceipts':'Cash/Check Receipts'}" 
						name="receiptModule" onchange="javascript:onTypeChangeReceipt();"/>
					</td>
						
				<s:if test="%{#receiptModule == 'orSales'}">
					<td>
						<s:select label="Search ORSales By:" 
						list="#{'ALL':'ALL','orNumber':'OR No.', 'orDate':'OR Date'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeReceipt();"/>
					</td>
				</s:if>
				<s:if test="%{#receiptModule == 'orOthers'}">
					<td>
						<s:select label="Search OROthers By:" 
						list="#{'ALL':'ALL','orNumber':'OR No.', 'orDate':'OR Date'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeReceipt();"/>
					</td>
				</s:if>
				<s:if test="%{#receiptModule == 'ccReceipts'}">
					<td>
						<s:select label="Search Cash/Check Receipts By:" 
						list="#{'ALL':'ALL','cashReceiptNo':'CR No.','cashReceiptDate':'CR Date'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeReceipt();"/>
					</td>
				</s:if>
				</tr>
									
				<tr>
					<s:if test="%{#moduleParameter  == 'orDate' || #moduleParameter == 'cashReceiptDate'}">
						<td class="others">Date:</td>
						<td>
							<sx:datetimepicker displayFormat="MMM-dd-yyyy" displayWeeks="5" id="moduleParameterValue" name="moduleParameterValue"></sx:datetimepicker>
						</td>
					</s:if>
					<s:elseif test="%{#subModule == 'ALL'}">
						
					</s:elseif>
					<s:else>
						<td>
							<s:textfield label="Search Value:" id="moduleParameterValue" name="moduleParameterValue"/>
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
					<s:if test="%{#receiptModule == 'orSales'}">
						<tr>
							<th>OR No</th>
							<th>OR Date</th>
							<th>Sales Invoice</th>
							<th>Received From</th>
						</tr>
					</s:if> 
					<s:elseif test="%{#receiptModule == 'orOthers'}">
						<tr>
							<th>OR No</th>
							<th>OR Date</th>
							<th>Sales Invoice</th>
							<th>Received From</th>
						</tr>
					</s:elseif> 
					<s:elseif test="%{#receiptModule == 'ccReceipts'}">
						<tr>
							<th>Cash/Check Receipt No</th>
							<th>Cash/Check Date</th>
							<th width="300px">Particulars</th>
							<th>Amount</th>
						</tr>
					</s:elseif> 
				<s:iterator value="receiptList" status="receiptList">
					<s:if test="%{#receiptModule == 'orSales'}">
						<tr>
							<td>
								<auth:isAuth role="24">
									<s:url id="displayId" action="editReceiptAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="orSales.orNumber" value="%{orNumber}">orNumber</s:param>
										<s:param name="receiptModule" value="%{'orSales'}">receiptModule</s:param>
									</s:url>
								</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="orNumber"/></s:a>
							</td>
						
							<td><s:property value="orDate"/></td>
							<td><s:property value="salesInvoiceNumber"/></td>
							<td><s:property value="receivedFrom"/></td>
							
							
						</tr>
					</s:if>
					<s:elseif test="%{#receiptModule == 'orOthers'}">
						<tr>
							<td>
								<auth:isAuth role="25">
									<s:url id="displayId" action="editReceiptAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="orOthers.orNumber" value="%{orNumber}">orNumber</s:param>
										<s:param name="receiptModule" value="%{'orOthers'}">receiptModule</s:param>
									</s:url>
								</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="orNumber"/></s:a>
							</td>
							<td><s:property value="orDate"/></td>
							<td><s:property value="salesInvoiceNumber"/></td>
							<td><s:property value="receivedFrom"/></td>
						</tr>
					</s:elseif>
					<s:elseif test="%{#receiptModule == 'ccReceipts'}">
						<tr>
							<td>
								<auth:isAuth role="26">
									<s:url id="displayId" action="editReceiptAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="ccReceipts.cashReceiptNo" value="%{cashReceiptNo}">cashReceiptNo</s:param>
										<s:param name="receiptModule" value="%{'ccReceipts'}">receiptModule</s:param>
									</s:url>
								</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="cashReceiptNo"/></s:a>
							</td>
							
							<td><s:property value="cashReceiptDate"/></td>
							<td><s:property value="particulars"/></td>
							<td><s:property value="amount"/></td>
						</tr>
					</s:elseif>
				</s:iterator>
				</table>
		</p>
	</div>
</div>
</body>
</html>