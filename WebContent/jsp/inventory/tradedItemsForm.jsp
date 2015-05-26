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
	<LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
	<link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
	<script type="text/javascript" src="js/onChangeType.js"></script> 

	 <script type='text/javascript'>
	var startWith=2;
	var subMenu=1;
	</script>
	<sx:head parseContent="true"/>
	<title>Traded Items</title>
</head>
<body>
<s:if test="%{forWhat == 'print'}">
<div class="print">
			<jsp:include page="/jsp/util/companyHeader.jsp"/>

	</div>
</s:if>
<div class="mainForm">
<h4 class="title">INVENTORY</h4>
	
<s:form action="showTradedItemsForm" value="true" id="tradedForm" validate="true">
	<div class="form" id="wholeForm">
		<div class="errors">
		<s:actionerror/>
		<s:actionmessage/>
	</div>
		<div id="inventoryForm">
		<h3 class="form">Traded Items</h3>
			<p>
				<table class="form">
					<tr>
						<th colspan="8">Details</th>
					</tr>
					<tr>
						<td width="70"><s:textfield disabled="%{forWhat}" label="Item Code:" name="ti.itemCode" id="itemId"/>
						
						</td>
						<s:hidden name="itemNo" value="%{ti.itemCode}"/>
						
						<s:if test="%{forWhat != 'print'}">
						<td>UOM: 
							<select id="uom" name="ti.unitOfMeasurement"
									onchange="othersSelected(this)">
										<c:forEach items="${UOMList}" var="uom">
											<option	${uom == ti.unitOfMeasurement ? 'selected' : ''}
												value="${uom}">${uom}</option>
										</c:forEach> 
							</select>
						</td>
						<td>
									<s:textfield
										style="visibility:hidden" disabled="true" id="UOMTextField"
										name="ti.unitOfMeasurementText" size="10" />
									
						</td>
						<td><s:select disabled="%{forWhat}" label="Template :" name="ti.template" value="%{ti.template}" list="#{'N':'None','C':'Commissary','S':'Store','B':'Both'}" ></s:select>
					
						</s:if>
						<s:else>
								<td><s:textfield label="UOM:" value="%{ti.unitOfMeasurement}"/></td>
								
								
						</s:else>
					
						
					</tr>
					</table>
					<table class="form">
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="Description:" size="90" name="ti.description"/></td>
					</tr>
					</table>
				
				<s:if test="%{forWhat == 'print'}">
					<table class="form">
						<tr>
							<td width="90"><s:textfield name="ti.classification" label="Classification:"/>
							<td><s:textfield name="ti.subClassification" label="Sub Classification:"></s:textfield>
						</tr>
						<tr>
							<td><s:textfield label="Is Vatable? :" name="ti.isVattable"/></td>
						</tr>
					</table>
				</s:if>
				
				<s:else>
					<table class="form">
						<tr>
							<td width="90"><s:select disabled="%{forWhat}" label="Classification:" name="ti.classification" list="#{'WET':'WET','DRY':'DRY'}"  
							onchange="javascript:loadItemSubClassification('tradedForm');" id="classif"  headerValue="Choose One:" headerKey="-1"></s:select></td>
							<s:hidden name="classification" id = "tempClassif"/>
							<td class="others">Sub Classification:</td>
							<td><sx:autocompleter headerValue="Choose One" dropdownHeight="50px" size="90" resultsLimit="-1"
							listValue="subClassification" list="itemSubClassificationList" maxlength="50"  name="ti.subClassification"></sx:autocompleter>
						</tr>
						<tr>	
							<td>
								<s:select disabled="%{forWhat}" label="Is Vatable? :" name="ti.isVattable" list="#{'N':'NO','Y':'YES'}" headerKey="-1"></s:select>
							</td>
						</tr>
					</table>
				</s:else>
				
				
					
					<table class="form">
					<tr>
						<th colspan="6">Company Owned Prices</th>
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="Standard Price:" name="ti.itemPricing.companyOwnedStandardPricePerUnit"/></td>
						<td><s:textfield disabled="%{forWhat}" label="Actual Price:" name="ti.itemPricing.companyOwnedActualPricePerUnit"/></td>
						<td><s:textfield disabled="%{forWhat}" label="Transfer Price:" name="ti.itemPricing.companyOwnedTransferPricePerUnit"/></td>		
					</tr>
					</table>
					<p></p>
					<table class="form">
					<tr>
						<th colspan="6">Franchise Prices</th>
					</tr>
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="Standard Price:" name="ti.itemPricing.franchiseStandardPricePerUnit"/></td>
						<td><s:textfield disabled="%{forWhat}" label="Actual Price:" name="ti.itemPricing.franchiseActualPricePerUnit"/></td>
						<td><s:textfield disabled="%{forWhat}" label="Transfer Price:" name="ti.itemPricing.franchiseTransferPricePerUnit"/></td>		
					</tr>
					</table>
					<p></p>
					<table class="form">
					<tr>
						<th colspan="6">Record Count</th>
					</tr>
					<!-- <tr>
						<td><s:textfield disabled="%{forWhat}" label="Quantity IN:" name="ti.quantityIn"/></td>
						<td><s:textfield disabled="%{forWhat}" label="Quantity OUT:" name="ti.quantityOut"/></td>
					</tr>
					 -->
					<tr>
						<td><s:textfield readOnly="readOnly" label="Quantity Per Record:" name="ti.warehouse.quantityPerRecord"/></td>
						<td><s:textfield disabled="%{forWhat}" label="Quantity Per Count:" name="ti.warehouse.quantityPerPhysicalCount"/></td>
					</tr>
					
					</table>
				
				
		</div>
	</div>
	<div class="forButtons">
<p>
		<table class="forButtons" align="center">	
				<tr><s:hidden name="subModule" value="tradedItems"/>
				<s:hidden name="requestingModule" value="tradedItems" />
				<s:hidden name="forWhatDisplay" value="%{forWhatDisplay}"/>
					
				<s:if test="%{forWhatDisplay == 'edit'}">
				
					<td><input class="myButtons" id="bEdit" type="button" onclick="javascript:toggleAlert('inventoryForm','itemId');" value="Edit"></input></td>
					<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateInventoryAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" id="bDel" value="Delete" onclick="javascript:inventoryConfirmation('tradedForm');"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" id="bPrint" value="Print" action="printInventoryAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" action="loadLookUpItemsInTradedItems" type="button" id="bNew" value="New Entry"></s:submit></td>
			
				</s:if>
				<s:elseif test="%{forWhat == 'print'}">
				</s:elseif>
				<s:else>
				
				<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" id="bNew" value="New Entry" action="addInventoryAction"></s:submit></td>
				<td><s:submit cssClass="myButtons" action="loadLookUpItemsInTradedItems" type="button" id="bCancel" value="Cancel"></s:submit></td>
			
				</s:else>
				
				</tr>
				</table>
	</p>
</div>
</s:form>
</div>
<script type='text/javascript'>
othersSelected();
</script>
</body>
</html>