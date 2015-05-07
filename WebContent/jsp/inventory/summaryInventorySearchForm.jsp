<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
  <script type="text/javascript" src="js/expandingSection.js"></script> 
  <script type="text/javascript" src="js/onChangeType.js"></script>
  <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
  <sx:head parseContent="true"></sx:head>
  	
	 <script type='text/javascript'>
	var startWith=2;
	var subMenu=9;
	</script>
<title>Inventory Summary Search Form</title>
</head>

<body >
<div class="mainForm">
	<h4 class="title">INVENTORY SUMMARY</h4>
	<div class="errors">
				<s:actionerror/>
				<s:actionmessage/>
			</div>
	<div class ="form">
		<s:form action="generateSummaryAction" validate="true" id="searchForm">
			<div>
				<s:hidden name="clicked" id="clicked" value="true"/>
				<s:set name="inventoryModule" value="inventoryModule"/>
					<p>
						<table class="form">
							<tr>
								<td>
									<s:select label="Inventory Module:" 
								headerKey="-1" headerValue="--Choose Module--" id="inventoryModule"
								list="#{'RawMaterials':'Raw Materials','TradedItems':'Traded Items', 'Utensils':'Utensils', 'OfficeSupplies':'Office Supplies', 'FinishedGoods':'Finished Goods','FinishedProductTransferSlip':'FP Transfer Slip','OrderRequisition':'Order Requisition','ReturnSlip':'Return Slip'}" 
								name="inventoryModule" onchange="javascript:showSearchByDate();" />
								</td>
							<td><s:submit label="Generate Report" value="Generate and Export Report"></s:submit></td>
							</tr>
						</table>
						<p></p>
						<table class="form" id="searchByDateTbl">
						<tr>
								<td class="others">Date From:</td>
								<td>
									<sx:datetimepicker displayFormat="MMM-dd-yyyy" displayWeeks="5" name="dateFrom"></sx:datetimepicker>
								</td>
								<td class="others">Date To:</td>
								<td>
									<sx:datetimepicker displayFormat="MMM-dd-yyyy" displayWeeks="5" name="dateTo"></sx:datetimepicker>
								</td>
							</tr>
							
						</table>
						<table class="form" id="searchByStatusTbl">
						<tr>
								<td class="others">Date From:</td>
								<td>
									<td><s:select label="Status :" name="searchByStatus" value="%{searchByStatus}" list="#{'A':'Active','I':'Inactive','B':'All'}" ></s:select>
								</td>
							</tr>
						</table>
						
					</p>
				</div>
				
		</s:form>		
	</div>
</div>

</body>
</html>