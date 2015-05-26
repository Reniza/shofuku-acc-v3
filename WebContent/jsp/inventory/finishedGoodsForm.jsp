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
<script type="text/javascript" src="js/onChangeType.js"></script>
<LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
<link rel="stylesheet" href="menu.css" type="text/css" />
<script type="text/javascript" src="js/expandingMenu.js"></script>

<!-- test3 -->

<script type='text/javascript'>
	var startWith = 2;
	var subMenu = 2;
</script>
<sx:head parseContent="true" />
<title>Finished Goods</title>
</head>
<body>
	<s:if test="%{forWhat != 'print'}">
		<div class="mainForm">
			<h4 class="title">Finished Goods</h4>

			<s:form action="loadLookUpItems" value="true" id="finForm" >
				<div class="form" id="wholeForm">
					<div class="errors">
						<s:actionerror />
						<s:actionmessage />
					</div>
					
					<div id="inventoryForm">
						<h3 class="form">FINISHED GOODS</h3>
						<p>
						<table class="form">
							<tr>
								<th colspan="8">Details</th>
							</tr>
							<tr>
								<td><s:textfield disabled="%{forWhat}"
										label="Product Code:" name="fg.productCode" id="pId" size="10" /></td>
								<s:hidden name="productNo" value="%{fg.productCode}" />
								<td>
								<td>UOM: <select id="uom" name="fg.unitOfMeasurement"
									onchange="othersSelected(this)">
										<c:forEach items="${UOMList}" var="uom">
											<option ${uom == fg.unitOfMeasurement ? 'selected' : ''}
												value="${uom}">${uom}</option>
										</c:forEach>

										</select>
								</td>
								<td><s:textfield style="visibility:hidden" disabled="true"
										id="UOMTextField" name="fg.unitOfMeasurementText" size="10" />
								</td>
								<td><s:select disabled="%{forWhat}" label="Template:" name="fg.template" value="%{fg.template}" list="#{'N':'None','C':'Commissary','S':'Store','B':'Both'}" ></s:select>
							</tr>
							<tr>
								<td><s:textfield disabled="%{forWhat}" label="Yield/s:"
										name="fg.yields" size="5" /></td>
									<s:if test="%{forWhat == 'true'}">
										<s:hidden name="fg.yields" value="%{fg.yields}" />
									</s:if>
								<td><s:textfield disabled="%{forWhat}" label="Mark Up %:"
										name="fg.markUp" size="5" />
									<s:if
										test="%{forWhat == 'true'}">
										<s:hidden name="fg.markUp" value="%{fg.markUp}" />
									</s:if>
								</td>
							</tr>
						</table>
						
						<table class="form">
							<tr>
								<td><s:textfield label="Description:" size="90" name="fg.description" /></td>
							</tr>
						</table>

						<table class="form">
							<tr>
								<td width="90"><s:select disabled="%{forWhat}"
										label="Classification:" name="fg.classification"
										list="#{'WET':'WET','DRY':'DRY'}"
										onchange="javascript:loadItemSubClassification('finForm');"
										id="classif" headerValue="Choose One:" headerKey="-1"></s:select></td>
								<s:hidden name="classification" id="tempClassif" />
								<td class="others">Sub Classification:</td>
								<td><sx:autocompleter headerValue="Choose One"
										dropdownHeight="50px" size="90" resultsLimit="-1"
										listValue="subClassification" list="itemSubClassificationList"
										maxlength="50" name="fg.subClassification"></sx:autocompleter>
							</tr>
							<tr>	
								<td><s:select disabled="%{forWhat}" label="Is Vatable? :" name="fg.isVattable" list="#{'N':'NO','Y':'YES'}" headerKey="-1"></s:select></td>
							</tr>
						</table>


						<table class="form">
							<tr>
								<th colspan="6">Company Owned Prices</th>
							</tr>
							<tr>
								<td><s:textfield label="Standard Price:"
										name="fg.itemPricing.companyOwnedStandardPricePerUnit" />
									<s:if test="%{forWhat == 'true'}">
									<s:hidden
										name="fg.itemPricing.companyOwnedStandardPricePerUnit"
										value="%{fg.itemPricing.companyOwnedStandardPricePerUnit}" />
									</s:if>
								</td>
								<td><s:textfield label="Actual Price:"
										name="fg.itemPricing.companyOwnedActualPricePerUnit" />
									<s:if test="%{forWhat == 'true'}">
										<s:hidden name="fg.itemPricing.companyOwnedActualPricePerUnit"
											value="%{fg.itemPricing.companyOwnedActualPricePerUnit}" />
									</s:if>
								</td>
								<td><s:textfield label="Transfer Price:"
										name="fg.itemPricing.companyOwnedTransferPricePerUnit" />
									<s:if test="%{forWhat == 'true'}">
									<s:hidden name="fg.itemPricing.companyOwnedTransferPricePerUnit"
										value="%{fg.itemPricing.companyOwnedTransferPricePerUnit}" />
									</s:if>
								</td>
							</tr>
							<tr>
								<th colspan="6">Franchise Prices</th>
							</tr>
							<tr>
								<td><s:textfield label="Standard Price:"
										name="fg.itemPricing.franchiseStandardPricePerUnit" />
									<s:if test="%{forWhat == 'true'}">
										<s:hidden name="fg.itemPricing.franchiseStandardPricePerUnit"
										value="%{fg.itemPricing.franchiseStandardPricePerUnit}" />
									</s:if>
								</td>
								<td><s:textfield label="Actual Price:"
										name="fg.itemPricing.franchiseActualPricePerUnit" />
									<s:if test="%{forWhat == 'true'}">
										<s:hidden name="fg.itemPricing.franchiseActualPricePerUnit"
											value="%{fg.itemPricing.franchiseActualPricePerUnit}" />
									</s:if>
								</td>
								<td><s:textfield label="Transfer Price:"
										name="fg.itemPricing.franchiseTransferPricePerUnit" />
									<s:if test="%{forWhat == 'true'}">
										<s:hidden name="fg.itemPricing.franchiseTransferPricePerUnit"
											value="%{fg.itemPricing.franchiseTransferPricePerUnit}" />
									</s:if>
								</td>
							</tr>
							<tr>
								<th colspan="6">Ingredients Total Costs</th>
							</tr>
							<tr>
								<td><s:textfield label="Standard Total Cost:"
										name="fg.standardTotalCost"></s:textfield>
									<s:if test="%{forWhat == 'true'}">
										<s:hidden name="fg.standardTotalCost"
											value="%{fg.standardTotalCost}" />
									</s:if>
								</td>
								<td><s:textfield label="Actual Total Cost:"
										name="fg.actualTotalCost"></s:textfield>
									<s:if test="%{forWhat == 'true'}">
										<s:hidden name="fg.actualTotalCost"
											value="%{fg.actualTotalCost}" />
									</s:if>
								</td>
								<td><s:textfield label="Transfer Total Cost:"
										name="fg.transferTotalCost"></s:textfield>
									<s:if test="%{forWhat == 'true'}">
										<s:hidden name="fg.transferTotalCost"
											value="%{fg.transferTotalCost}" />
									</s:if>
								</td>
							</tr>
							<tr>
								<th colspan="6">Record Count</th>
							</tr>
							<tr>
								<td><s:textfield readOnly="readOnly"
										label="Quantity Per Record:" name="fg.warehouse.quantityPerRecord" />
									<s:if test="%{forWhat == 'true'}">
										<s:hidden name="fg.quantityPerRecord"
											value="%{fg.quantityPerRecord}" />
									</s:if>
								</td>
								<td><s:textfield disabled="%{forWhat}"
										label="Quantity Per Count:" name="fg.warehouse.quantityPerPhysicalCount" />
									<s:if test="%{forWhat == 'true'}">
										<s:hidden name="fg.quantityPerCount"
											value="%{fg.quantityPerCount}" />
									</s:if>
								</td>
							</tr>
						</table>
					</div>

					<p>
					<div>
						<table class="form">
							<tr>
								<th colspan="6">Ingredients</th>
							</tr>
						</table>
						
						<table>
							<tr class="others">
								<td class="desc">Item Code</td>
								<td class="desc">QTY</td>
								<td class="header" width="230px">Description</td>
								<td>UOM</td>
								<td>Standard Price</td>
								<td>Actual Price</td>
								<td>Transfer Price</td>

							</tr>
							<tr>
								<td><s:hidden name="forWhatDisplay"
										value="%{forWhatDisplay}" /> <select
									name="sangkap.productCode"
									onchange="getIngredientDetails(this)">
										<c:forEach items="${itemCodeList}" var="ingredient">
											<option
												${ingredient.itemCode == sangkap.productCode ? 'selected' : ''}
												value="${ingredient.itemCode}">${ingredient.itemCode}</option>
										</c:forEach>

								</select></td>
								<td><s:textfield size="4" theme="simple"
										name="sangkap.quantity"></s:textfield></td>
								<td><s:textfield readOnly="readOnly"
										name="sangkap.description" theme="simple" size="33"></s:textfield></td>
								<td><s:textfield size="4" readOnly="readOnly"
										theme="simple" name="sangkap.unitOfMeasurement"></s:textfield></td>
								<td><s:textfield size="7" readOnly="readOnly"
										name="sangkap.standardPricePerUnit" theme="simple"></s:textfield></td>
								<td><s:textfield size="7" readOnly="readOnly"
										name="sangkap.actualPricePerUnit" theme="simple"></s:textfield></td>
								<td><s:textfield size="7" readOnly="readOnly"
										name="sangkap.transferPricePerUnit" theme="simple"></s:textfield></td>

							</tr>
						</table>
						<table>
						<s:hidden name="forWhat" value="%{forWhat}"/>
							<tr>
								<td>
									<s:submit cssClass="myButtons" id="bAddItem"
										type="button" label="Add Item" action="addIngredientAction">
									</s:submit>
								</td>
								<td><s:submit cssClass="myButtons" id="bDeleteItem"
										type="button" label="Delete Item"
										action="deleteIngredientAction">
									</s:submit>
								</td>
							</tr>
						</table>
					</div>
					</p>

					<div>
						<table class="lists" border="1px">
							<tr class="others">
								<td class="desc" width="20px">Item Code</td>
								<td class="desc" width="5px">QTY</td>
								<td class="header" width="290px">Description</td>
								<td width="5px">UOM</td>
								<td width="10px">Standard Price</td>
								<td width="10px">Actual Price</td>
								<td width="10px">Transfer Price</td>

							</tr>
							<s:iterator value="ingredients" status="stat">
								<tr>
									<td class="desc"><s:property value="productCode" /> <s:hidden
											name="pcItr" value="%{productCode}"></s:hidden></td>
									<td class="desc"><s:property value="quantity" /> <s:hidden
											name="qtyItr" value="%{quantity}"></s:hidden></td>
									<td class="desc" width="300px"><s:property
											value="description" /> <s:hidden name="descItr"
											value="%{description}"></s:hidden></td>
									<td><s:property value="unitOfMeasurement" /> <s:hidden
											name="uomItr" value="%{unitOfMeasurement}"></s:hidden></td>
									<td><s:property value="standardPricePerUnit" /> <s:hidden
											name="sppItr" value="%{standardPricePerUnit}"></s:hidden></td>
									<td><s:property value="actualPricePerUnit" /> <s:hidden
											name="appItr" value="%{actualPricePerUnit}"></s:hidden></td>
									<td><s:property value="transferPricePerUnit" /> <s:hidden
											name="tppItr" value="%{transferPricePerUnit}"></s:hidden></td>
								</tr>
							</s:iterator>

							<tr>
								<td colspan="4" class="total">Total: PHP</td>
								<td class="totalAmount"><s:property
										value="%{fg.standardTotalCost}" /></td>
								<td class="totalAmount"><s:property
										value="%{fg.actualTotalCost}" /></td>
								<td class="totalAmount"><s:property
										value="%{fg.transferTotalCost}" /></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="forButtons">
					<p>
					<table class="forButtons" align="center">
						<tr>
							<s:hidden name="subModule" value="finGood" />
							<s:hidden name="requestingModule" value="finishedGood" />

							<s:if test="%{forWhatDisplay == 'edit'}">

								<td><input class="myButtons" id="bEdit" type="button"
									onclick="javascript:toggleAlert('inventoryForm','pId');"
									value="Edit"></input></td>
								<td><s:submit disabled="%{forWhat}" id="bUpdate"
										cssClass="myButtons" type="button" value="Update"
										action="updateInventoryAction"></s:submit></td>
								<td><s:submit cssClass="myButtons" type="button" id="bDel"
										value="Delete"
										onclick="javascript:inventoryConfirmation('finForm');"></s:submit></td>
								<td><s:submit cssClass="myButtons" type="button"
										id="bPrint" value="Print" action="printInventoryAction"></s:submit></td>
								<td><input class="myButtons" name="clear" id="bCancel"
									type="button"
									onclick="javascript:clearAll('wholeForm','finForm');"
									value="New Entry"></input></td>

							</s:if>

							<s:else>

								<td><s:submit disabled="%{forWhat}" cssClass="myButtons"
										type="button" id="bNew" value="New Entry"
										action="addInventoryAction"></s:submit></td>
								<td><input class="myButtons" name="clear" type="button"
									id="bCancel"
									onclick="javascript:clearAll('wholeForm','finForm');"
									value="Cancel"></input></td>

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
			<jsp:include page="/jsp/util/companyHeader.jsp" />
			<h3 class="form">FINISHED GOODS</h3>

			<table class="form">
				<tr>
					<th colspan="8">Details</th>
				</tr>
				<tr>
					<td width="70"><s:textfield label="Product Code:"
							value="%{fg.productCode}" size="15" />
					<td><s:textfield label="UOM:" value="%{fg.unitOfMeasurement}"
							size="5" /></td>	
					
				</tr>
				<tr>
					<td><s:textfield label="Yield/s:" value="%{fg.yields}"
							size="5" /></td>
					<td><s:textfield label="Mark Up %:" value="%{fg.markUp}"
							size="5" /></td>
				<td><s:textfield size="10"  label="Is Vatable? :" name="fg.isVattable"/></td>
								
				</tr>
			</table>
			<table class="form">
				<tr>
					<td><s:textarea rows="3" cols="70" label="Description:"
							value="%{fg.description}" /></td>
				</tr>
			</table>

			<table class="form">
				<tr>

					<td width="90"><s:textfield name="fg.classification"
							label="Classification:" />
					<td><s:textfield name="fg.subClassification"
							label="Sub Classification:"></s:textfield>
				</tr>
			</table>

			<table class="form">
				<tr>
					<th colspan="6">Company Owned Prices</th>
				</tr>
				<tr>
					<td><s:textfield label="Standard Price:"
							value="%{fg.itemPricing.companyOwnedStandardPricePerUnit}" />
					<td><s:textfield label="Actual Price:"
							value="%{fg.itemPricing.companyOwnedActualPricePerUnit}" />
					<td><s:textfield label="Transfer Price:"
							value="%{fg.itemPricing.companyOwnedTransferPricePerUnit}" />
				</tr>

				<tr>
					<th colspan="6">Franchise Prices</th>
				</tr>
				<tr>
					<td><s:textfield label="Standard Price:"
							value="%{fg.itemPricing.FranchiseStandardPricePerUnit}" />
					<td><s:textfield label="Actual Price:"
							value="%{fg.itemPricing.franchiseActualPricePerUnit}" />
					<td><s:textfield label="Transfer Price:"
							value="%{fg.itemPricing.franchiseTransferPricePerUnit}" />
				</tr>

				<tr>
					<th colspan="6">Ingredients Total Costs</th>
				</tr>
				<tr>
					<td><s:textfield label="Standard Total Cost:"
							value="%{fg.standardTotalCost}" />
					<td><s:textfield label="Actual Total Cost:"
							value="%{fg.actualTotalCost}" />
					<td><s:textfield label="Transfer Total Cost:"
							value="%{fg.transferTotalCost}" />
				</tr>
				<tr>
					<th colspan="6">Record Count</th>
				</tr>
			<!-- 	<tr>
					<td><s:textfield label="Quantity IN:" value="%{fg.quantityIn}" />
					<td><s:textfield label="Quantity OUT:"
							value="%{fg.quantityOut}" />
				</tr>
			 -->
				<tr>
					<td><s:textfield label="Quantity Per Record:"
							value="%{fg.quantityPerRecord}" />
					<td><s:textfield label="Quantity Per Count:"
							value="%{fg.quantityPerCount}" />
				</tr>
			</table>
			<table class="form">
				<tr>
					<th colspan="6">Ingredients</th>
				</tr>
			</table>
			<table class="lists" border="1px">
				<tr class="others">
					<td class="desc" width="20px">Item Code</td>
					<td class="desc" width="5px">QTY</td>
					<td class="header" width="250px">Description</td>
					<td width="5px">UOM</td>
					<td width="10px">Standard Price</td>
					<td width="10px">Actual Price</td>
					<td width="10px">Transfer Price</td>

				</tr>
				<s:iterator value="ingredients" status="stat">
					<tr>
						<td class="desc"><s:property value="productCode" /></td>
						<td class="desc"><s:property value="quantity" /></td>
						<td class="desc" width="300px"><s:property
								value="description" /></td>
						<td><s:property value="unitOfMeasurement" /></td>
						<td><s:property value="standardPricePerUnit" /></td>
						<td><s:property value="actualPricePerUnit" /></td>
						<td><s:property value="transferPricePerUnit" /></td>
					</tr>
				</s:iterator>

				<tr>
					<td colspan="4" class="total">Total: PHP</td>
					<td class="totalAmount"><s:property
							value="%{fg.standardTotalCost}" /></td>
					<td class="totalAmount"><s:property
							value="%{fg.actualTotalCost}" /></td>
					<td class="totalAmount"><s:property
							value="%{fg.transferTotalCost}" /></td>
				</tr>
			</table>
		</div>

	</s:else>
	<script type='text/javascript'>
		othersSelected();
	</script>
</body>
</html>