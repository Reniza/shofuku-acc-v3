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
	var subMenu=5;
	</script>
	<sx:head parseContent="true"/>
	<title>Unlisted Items</title>
</head>
<body>
<s:if test="%{forWhat == 'print'}">
<div class="print">
			<jsp:include page="/jsp/util/companyHeader.jsp"/>
	</div>
</s:if>
<div class="mainForm">
<h4 class="title">INVENTORY</h4>
	
<s:form action="showUnlistedItemsForm" value="true" id="unlistedItemsForm" validate="true">
	<div class="form" id="wholeForm">
		<div class="errors">
		<s:actionerror/>
		<s:actionmessage/>
	</div>
		<div id="inventoryForm">
		<h3 class="form">Unlisted Items</h3>
			<p>
				<table class="form">
					<tr>
						<th colspan="8">Details</th>
					</tr>
					<tr>
						<s:hidden name="itemNo" value="%{unl.itemCode}"/>
						
						<s:if test="%{forWhat != 'print'}">
						<td>UOM: 
							<select id="uom" name="unl.uom"
									onchange="othersSelected(this)">
										<c:forEach items="${UOMList}" var="uom">
											<option	${uom == unl.uom ? 'selected' : ''}
												value="${uom}">${uom}</option>
										</c:forEach> 
							</select>
						</td>
						<td>
								<s:textfield style="visibility:hidden" disabled="true" id="UOMTextField"
										name="unl.unitOfMeasurementText" size="10" />
						</td>
							<td><s:select disabled="%{forWhat}" label="Template :" name="unl.template" value="%{unl.template}" list="#{'N':'None','C':'Commissary','S':'Store','B':'Both'}" ></s:select>
						</s:if>
						<s:else>
							<td><s:textfield label="UOM:" name="unl.uom" value="%{unl.uom}"/></td>
							<td><s:select disabled="true" label="Template :" name="unl.template" value="%{unl.template}" list="#{'N':'None','C':'Commissary','S':'Store','B':'Both'}" ></s:select>
						
						</s:else>
					
					
					</tr>
					</table>
					
					<table class="form">
						<tr>
							<td><s:textfield disabled="%{forWhat}" label="Description:" size="90" name="unl.description"/></td>
						</tr>
					</table>
					<table class="form">
						<s:if test="%{forWhat == 'print'}">
							<td><s:textfield disabled="%{forWhat}" label="Classification:" size="90" name="unl.classification"/></td>
						</s:if>
						<s:else>
						<tr>
							<td width="90"><s:select disabled="%{forWhat}" label="Classification:" name="unl.classification" list="#{'WET':'WET','DRY':'DRY'}"  
							id="classif"  headerValue="Choose One:" headerKey="-1"></s:select></td>
							<s:hidden name="classification" id = "tempClassif"/>
						</tr>
						</s:else>
					</table>
		</div>
	</div>
	<div class="forButtons">
<p>
		<table class="forButtons" align="center">	
				<tr><s:hidden name="subModule" value="unlistedItems"/>
				<s:hidden name="requestingModule" value="unlistedItems" />
				<s:hidden name="forWhatDisplay" value="%{forWhatDisplay}"/>
				<s:hidden name="forWhat" value="%{forWhat}"/>
					
				<s:if test="%{forWhatDisplay == 'edit'}">
				
					<td><input class="myButtons" id="bEdit" type="button" onclick="javascript:toggleAlert('inventoryForm','itemId');" value="Edit"></input></td>
					<td><s:submit disabled="%{forWhat}" id="bUpdate" cssClass="myButtons" type="button" value="Update" action="updateInventoryAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" id="bDel" value="Delete" onclick="javascript:inventoryConfirmation('unlistedItemsForm');"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" id="bPrint" value="Print" action="printInventoryAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" action="loadLookUpItemsInUnlistedItems" type="button" id="bNew" value="New Entry"></s:submit></td>
			
				</s:if>
				<s:elseif test="%{forWhat == 'print'}">
				</s:elseif>
				<s:else>
				
				<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" id="bNew" value="New Entry" action="addInventoryAction"></s:submit></td>
				<td><s:submit cssClass="myButtons" action="loadLookUpItemsInUnlistedItems" type="button" id="bCancel" value="Cancel"></s:submit></td>
			
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