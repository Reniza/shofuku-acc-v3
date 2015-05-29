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
	<link rel="stylesheet" href="menos.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenos.js"></script> 	
	<script type="text/javascript" src="js/onChangeType.js"></script> 

	 <script type='text/javascript'>
	var startWith=2;
	var subMenu=11;
	</script>
	<sx:head parseContent="true"/>
	<title>Office Supplies</title>
</head>
<body>
<s:if test="%{forWhat == 'print'}">
	<div class="print">
		<jsp:include page="/jsp/util/companyHeader.jsp"/>
	</div>
</s:if>

<div class="mainForm">
	<h4 class="title">INVENTORY</h4>
		<s:form action="showOfficeSuppliesForm" value="true" id="osForm" validate="true">
			<div class="form" id="wholeForm">
			<div class="errors">
				<s:actionerror/>
				<s:actionmessage/>
			</div>
			<div id="inventoryForm">
			<h3 class="form">Office Supplies</h3>
				<p>
					<table class="form">
						<tr>
							<th colspan="8">Details</th>
						</tr>
						<tr>
							<td width="70"><s:textfield disabled="%{forWhat}" label="Item Code:" name="os.itemCode" id="itemId"/></td>
							<s:hidden name="itemNo" value="%{os.itemCode}"/>
							<s:if test="%{forWhat != 'print'}">
								<td>UOM: 
									<select id="uom" name="os.unitOfMeasurement"
											onchange="othersSelected(this)">
											<c:forEach items="${UOMList}" var="uom">
												<option	${uom == os.unitOfMeasurement ? 'selected' : ''}
														value="${uom}">${uom}</option>
											</c:forEach> 
									</select>
								</td>
								<td>
									<s:textfield style="visibility:hidden" disabled="true" id="UOMTextField"
												name="os.unitOfMeasurementText" size="10" />
								</td>
								<td><s:select disabled="%{forWhat}" label="Template :" name="os.template" value="%{os.template}" list="#{'N':'None','C':'Commissary','S':'Store','B':'Both'}" ></s:select>
								</td>
							</s:if>
							<s:else>
								<td><s:textfield label="UOM:" value="%{os.unitOfMeasurement}"/></td>
							</s:else>
						</tr>
						</table>
						
						<table class="form">
						<tr>
							<td><s:textfield disabled="%{forWhat}" label="Description:" size="90" name="os.description"/></td>
						</tr>
						</table>
					
						<s:if test="%{forWhat == 'print'}">
							<table class="form">
								<tr>
									<td width="90"><s:textfield name="os.classification" label="Classification:"/>
									<td><s:textfield name="os.subClassification" label="Sub Classification:"></s:textfield>
								</tr>
								<tr>
									<td><s:textfield label="Is Vatable? :" name="os.isVattable"/></td>
									<td><s:textfield label="Is Active? :" name="os.isActive"/></td>
								</tr>
							</table>
						</s:if>
					
						<s:else>
							<table class="form">
								<tr>
									<td width="90"><s:select disabled="%{forWhat}" label="Classification:" name="os.classification" list="#{'WET':'WET','DRY':'DRY'}"  
									onchange="javascript:loadItemSubClassification('osForm');" id="classif"  headerValue="Choose One:" headerKey="-1"></s:select></td>
									<s:hidden name="classification" id = "tempClassif"/>
									<td class="others">Sub Classification:</td>
									<td><sx:autocompleter headerValue="Choose One" dropdownHeight="50px" size="90" resultsLimit="-1"
									listValue="subClassification" list="itemSubClassificationList" maxlength="50"  name="os.subClassification"></sx:autocompleter>
								</tr>
								<tr>	
									<td>
										<s:select disabled="%{forWhat}" label="Is Vatable? :" name="os.isVattable" list="#{'N':'NO','Y':'YES'}" headerKey="-1"></s:select>
									</td>
									<td><s:select disabled="%{forWhat}" label="Is Active? :" name="os.isActive" value="%{os.isActive}" 
										list="#{'Y':'YES','N':'NO'}" ></s:select></td>
								</tr>
							</table>
						</s:else>
					
						<table class="form">
							<tr>
								<th colspan="6">Company Owned Prices</th>
							</tr>
							<tr>
								<td><s:textfield disabled="%{forWhat}" label="Standard Price:" name="os.itemPricing.companyOwnedStandardPricePerUnit"/></td>
								<td><s:textfield disabled="%{forWhat}" label="Actual Price:" name="os.itemPricing.companyOwnedActualPricePerUnit"/></td>
								<td><s:textfield disabled="%{forWhat}" label="Transfer Price:" name="os.itemPricing.companyOwnedTransferPricePerUnit"/></td>		
							</tr>
						</table>
						<p></p>
						
						<table class="form">
							<tr>
								<th colspan="6">Franchise Prices</th>
							</tr>
							<tr>
								<td><s:textfield disabled="%{forWhat}" label="Standard Price:" name="os.itemPricing.franchiseStandardPricePerUnit"/></td>
								<td><s:textfield disabled="%{forWhat}" label="Actual Price:" name="os.itemPricing.franchiseActualPricePerUnit"/></td>
								<td><s:textfield disabled="%{forWhat}" label="Transfer Price:" name="os.itemPricing.franchiseTransferPricePerUnit"/></td>		
							</tr>
							</table>
						<p></p>
						
						<table class="form">
							<tr>
								<th colspan="6">Record Count</th>
							</tr>
							
							<tr>
								<td><s:textfield readOnly="readOnly" label="Quantity Per Record:" name="os.warehouse.quantityPerRecord"/></td>
								<td><s:textfield disabled="%{forWhat}" label="Quantity Per Count:" name="os.warehouse.quantityPerPhysicalCount"/></td>
							</tr>
						</table>
			</div>
		</div>
		
		<div class="forButtons">
		<p>
		<table class="forButtons" align="center">	
			<tr><s:hidden name="subModule" value="ofcSup"/>
				<s:hidden name="requestingModule" value="ofcSup" />
				<s:hidden name="forWhatDisplay" value="%{forWhatDisplay}"/>
					
				<s:if test="%{forWhatDisplay == 'edit'}">
					<td><input class="myButtons" id="bEdit" type="button" onclick="javascript:toggleAlert('inventoryForm','itemId');" value="Edit"></input></td>
					<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateInventoryAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" id="bDel" value="Delete" onclick="javascript:inventoryConfirmation('osForm');"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" id="bPrint" value="Print" action="printInventoryAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" action="loadLookUpItemsInOfficeSupplies" type="button" id="bNew" value="New Entry"></s:submit></td>
				</s:if>
				
				<s:elseif test="%{forWhat == 'print'}">
				</s:elseif>
				
				<s:else>
				<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" id="bNew" value="New Entry" action="addInventoryAction"></s:submit></td>
				<td><s:submit cssClass="myButtons" action="loadLookUpItemsInOfficeSupplies" type="button" id="bCancel" value="Cancel"></s:submit></td>
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