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
  <sx:head parseContent="true"></sx:head>
 
	 <script type='text/javascript'>
	var startWith=0;
	var subMenu=5;
	</script>
<title>Supplier Summary Search Form</title>

</head>

<body>
<div class="mainForm">
	<h4 class="title">SUPPLIER SUMMARY</h4>
	
	<div class ="form">
		
		<s:form action="generateSummaryAction" id="searchForm">
			<div>
				<s:hidden name="clicked" id="clicked" value="true"/>
					
					<p>
						<table class="form">
							<tr>
								<td>
									<s:select label="Supplier Module:" 
								list="#{'Supplier':'Profile', 'SupplierPurchaseOrder':'Purchase Order', 'ReceivingReport':'Receiving Report', 'SupplierInvoice':'Supplier Invoice', 'ItemPurchasedFromSupplier':'Items Purchased From Supplier'}" 
								name="supplierModule" id="supplierModule" onchange="javascript:showSupplierList();"/>
								</td>
								<td colspan="2" style="text-align:left;"> 
								<s:submit label="Generate Report" value="Generate and Export Report" method="generate"></s:submit></td>
							</tr>
						</table>
					<br/>
					<div id="searchParameters" style="display: none;">
						
						<table class="form" cellpadding=5>
							<tr>
								<td colspan=4><h4 class="title">SELECT DATE RANGE & SUPPLIER/s TO BE INCLUDED IN THE REPORT:</h4></td>
							</tr>
							<tr>
								<td class="others">Date From:</td>
								<td><sx:datetimepicker displayFormat="MMM-dd-yyyy" displayWeeks="5" name="dateFrom"></sx:datetimepicker></td>
								<td class="others">Date To:</td>
								<td><sx:datetimepicker displayFormat="MMM-dd-yyyy" displayWeeks="5" name="dateTo"></sx:datetimepicker></td>
							</tr>
						</table>
						<table class="result" width="100%">
							<tr>
								<td><b>TICK BELOW CHECK BOX FOR THE FOLLOWING REPORT:  </b><s:checkbox id="isFormatReport" name="isFormatReport" onclick="javascript:showFilterTable();"></s:checkbox> </td>
							</tr>
							<tr>
								<td> 1. Report per Module per Supplier</td>
							</tr>
							<tr>
								<td>2. Required for Items Purchased From Supplier Report</td>
							</tr>
							<tr>
								<td> 3. Required for Simple Format of Reports</td>
							</tr>
						</table>
						
						<div id="filterTbl" style="display: none;">	
						<table class="form" cellpadding=5>		
							<tr>
								<td>Choose at least one supplier:</td>
								<td><select id="supplierList" name="supplierList" multiple="multiple">
									<c:forEach items="${supplierList}" var="supplier">
										<option value="${supplier.supplierId}">${supplier.supplierName}</option>
									</c:forEach>
									</select>
								</td>
							</tr>
							
						</table>
					</div>
			</div>
			<div class="errors">
				<s:actionerror/>
				<s:actionmessage/>
			</div>
		</s:form>		
	</div>
</div>
<script type='text/javascript'>
	showSupplierList();
</script>
</body>
</html>