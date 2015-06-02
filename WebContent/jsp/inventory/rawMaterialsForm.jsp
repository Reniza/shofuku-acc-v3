<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ taglib prefix="c" uri="/tld/c.tld"%>
<%@ taglib prefix="auth" uri="/tld/Authorization.tld"%>
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
	var subMenu=0;
	</script>
	<sx:head parseContent="true"/>
	<title>Raw Materials</title>
</head>
<body>
<s:if test="%{forWhat == 'print'}">
<div class="print">
			<jsp:include page="/jsp/util/companyHeader.jsp"/>

	</div>
</s:if>
<div class="mainForm">
<h4 class="title">Raw Materials</h4>
	
<s:form action="showRawMaterialsForm" value="true" id="rawForm" validate="true">
	<div class="form" id="wholeForm">
		<div class="errors">
		<s:actionerror/>
		<s:actionmessage/>
	</div>
		<div id="inventoryForm">
		<h3 class="form">Raw Materials</h3>
			<p>
				<table class="form">
					<tr>
						<th colspan="8">Details</th>
					</tr>
					<tr>
						<td width="70"><s:textfield disabled="%{forWhat}" label="Item Code:" name="rm.itemCode" id="itemId" size="15"/></td>
						<s:hidden name="itemNo" value="%{rm.itemCode}"/>
						
						<s:if test="%{forWhat != 'print'}">
						<td>UOM: 
							<select id="uom" name="rm.unitOfMeasurement"
									onchange="othersSelected(this)">
										<c:forEach items="${UOMList}" var="uom">
											<option	${uom == rm.unitOfMeasurement ? 'selected' : ''}
												value="${uom}">${uom}</option>
										</c:forEach> 
							</select>
						</td>
						<td>
							<s:textfield style="visibility:hidden" disabled="true" id="UOMTextField" name="rm.unitOfMeasurementText" size="10" />
						</td>
						<td><s:select disabled="%{forWhat}" label="Template :" name="rm.template" value="%{rm.template}" 
							list="#{'N':'None','C':'Commissary','S':'Store','B':'Both'}" ></s:select>
						</td>
						
						</s:if>

						<s:else>
								<td><s:textfield label="Unit of Measurement:" value="%{rm.unitOfMeasurement}"/></td>
						</s:else>
					
					</tr>
					</table>
					<table class="form">
					<tr>
						<td><s:textfield disabled="%{forWhat}" label="Description:" size="90" name="rm.description"/></td>
					</tr>
					</table>
					
				<s:if test="%{forWhat == 'print'}">
					<table class="form">
						<tr>
							<td width="90"><s:textfield name="rm.classification" label="Classification:"/>
							<td><s:textfield name="rm.subClassification" label="Sub Classification:"></s:textfield>
						</tr>
						<tr>
							<td><s:textfield label="Is Vatable? :" name="rm.isVattable"/></td>
							<td><s:textfield label="Is Active? :" name="rm.isActive"/></td>
						</tr>
					</table>
				</s:if>
				
				<s:else>
					<table class="form">
					<tr>
						
						<td width="90"><s:select disabled="%{forWhat}" label="Classification:" name="rm.classification" list="#{'WET':'WET','DRY':'DRY'}"  
						onchange="javascript:loadItemSubClassification('rawForm');" id="classif"  headerValue="Choose One:" headerKey="-1"></s:select></td>
						<s:hidden name="classification" id = "tempClassif"/>
						<td class="others">Sub Classification:</td>
						<td><sx:autocompleter headerValue="Choose One" dropdownHeight="50px" size="90" resultsLimit="-1"
						listValue="subClassification" list="itemSubClassificationList" maxlength="50"  name="rm.subClassification"></sx:autocompleter>
					</tr>
					<tr>	
							<td>
									<s:select disabled="%{forWhat}" label="Is Vatable? :" name="rm.isVattable" list="#{'Y':'YES','N':'NO'}" headerKey="-1"></s:select>			
							</td>
							<td><s:select disabled="%{forWhat}" label="Is Active? :" name="rm.isActive" value="%{rm.isActive}" 
							list="#{'Y':'YES','N':'NO'}" ></s:select>
							</td>
						</tr>
					</table>
				</s:else>
					<table class="form">
					<tr>
						<th colspan="6">Company Owned Prices</th>						
					</tr>
					<tr>
						<td><s:textfield readonly="%{itemPriceEditor}" label="Standard Price:" name="rm.itemPricing.companyOwnedStandardPricePerUnit"/></td>
						<td><s:textfield readonly="%{itemPriceEditor}" label="Actual Price:" name="rm.itemPricing.companyOwnedActualPricePerUnit"/></td>
						<td><s:textfield readonly	="%{itemPriceEditor}" label="Transfer Price:" name="rm.itemPricing.companyOwnedTransferPricePerUnit"/></td>		
					</tr>
					</table>
					<p></p>
					<table class="form">
					<tr>
						<th colspan="6">Franchise Prices</th>
					</tr>
					<tr>
						<td><s:textfield readonly="%{itemPriceEditor}"  label="Standard Price:" name="rm.itemPricing.franchiseStandardPricePerUnit"/></td>
						<td><s:textfield readonly="%{itemPriceEditor}"  label="Actual Price:" name="rm.itemPricing.franchiseActualPricePerUnit"/></td>
						<td><s:textfield readonly="%{itemPriceEditor}"  label="Transfer Price:" name="rm.itemPricing.franchiseTransferPricePerUnit"/></td>		
					</tr>
					</table>
					<p></p>
					<table class="form">
					<tr>
						<th colspan="6">Record Count</th>
					</tr>
			<!-- 	<tr>
						<td><s:textfield disabled="%{forWhat}" label="Quantity IN:" name="rm.quantityIn"/></td>
						<td><s:textfield disabled="%{forWhat}" label="Quantity OUT:" name="rm.quantityOut"/></td>
					</tr>
			 -->	
					<tr>
						<td><s:textfield label="Quantity Per Record:" readOnly="readOnly" name="rm.warehouse.quantityPerRecord"/></td>
						<td><s:textfield disabled="%{forWhat}" label="Quantity Per Count:" name="rm.warehouse.quantityPerPhysicalCount"/></td>
					</tr>
					</table>
				
				
		</div>
	</div>
	<div class="forButtons">
<p>
		<table class="forButtons" align="center">	
				<tr><s:hidden name="subModule" value="rawMat"/>
				<s:hidden name="requestingModule" value="rawMaterial" />
				<s:hidden name="forWhatDisplay" value="%{forWhatDisplay}"/>
				
					
				<s:if test="%{forWhatDisplay == 'edit'}">
				
					<td><input class="myButtons" id="bEdit" type="button" onclick="javascript:toggleAlert('inventoryForm','itemId');" value="Edit"></input></td>
					<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateInventoryAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" id="bDel" value="Delete" onclick="javascript:inventoryConfirmation('rawForm');"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" id="bPrint" value="Print" action="printInventoryAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" action="loadLookUpItemsInRawMat" type="button" id="bNew" value="New Entry"></s:submit></td>
			
				</s:if>
				<s:elseif test="%{forWhat == 'print'}">
				</s:elseif>
				<s:else>
				
				<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" id="bNew" value="New Entry" action="addInventoryAction"></s:submit></td>
				<td><s:submit cssClass="myButtons" action="loadLookUpItemsInRawMat" type="button" id="bCancel" value="Cancel"></s:submit></td>
			
				<!--  <td><input class="myButtons" name="clear" type="button" id="bCancel" onclick="javascript:clearAll('wholeForm','rawForm');" value="Cancel"></input></td>
			
			-->
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