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
<link rel="stylesheet" href="menu.css" type="text/css" />
<script type="text/javascript" src="js/expandingMenu.js"></script>
<script type='text/javascript'>
	var startWith = 2;
	var subMenu = 8;
</script>
<sx:head parseContent="true"></sx:head>

<title>Inventory Search Form</title>
</head>

<body>
	<div class="mainForm">
		<h4 class="title">INVENTORY</h4>

		<div class="errors">
			<s:actionerror />
			<s:actionmessage />

		</div>
		<s:form action="searchInventoryAction" validate="true" id="searchForm">

			<s:hidden name="clicked" id="clicked" value="true" />
			<s:set name="subModule" value="subModule" />
			<s:set name="moduleParameterValue" value="moduleParameterValue" />
			<s:set name="moduleParameter" value="moduleParameter" />
			<p>
			<table class="form">
				<tr>
					
					<td><s:select label="Inventory Module:" headerKey="-1"
							headerValue="--Choose Module--"
							list="#{'items':'Item/s','rawMat':'Raw Materials', 'finGood':'Finished Goods', 'tradedItems':'Traded Items','utensils':'Utensils','unlistedItems':'Unlisted Items','fpts':'FP Transfer Slip','rf':'Order Requisition', 'returnSlip':'Return Slip'}"
							name="subModule"
							onchange="javascript:onTypeChangeInventory('searchForm');" /></td>
					
					

					<s:if test="%{#subModule == 'rawMat'}">
						<td><s:select label="Search Raw Materials By:"
								list="#{'ALL':'ALL','itemCode':'Item Code', 'description':'Description'}"
								name="moduleParameter"
								onchange="javascript:onTypeChangeInventory('searchForm');" /></td>
					</s:if>
					<s:if test="%{#subModule == 'tradedItems'}">
						<td><s:select label="Search Traded Items By:"
								list="#{'ALL':'ALL','itemCode':'Item Code', 'description':'Description'}"
								name="moduleParameter"
								onchange="javascript:onTypeChangeInventory('searchForm');" /></td>
					</s:if>
					<s:if test="%{#subModule == 'utensils'}">
						<td><s:select label="Search Utensils By:"
								list="#{'ALL':'ALL','itemCode':'Item Code', 'description':'Description'}"
								name="moduleParameter"
								onchange="javascript:onTypeChangeInventory('searchForm');" /></td>
					</s:if>
					<s:if test="%{#subModule == 'unlistedItems'}">
						<td><s:select label="Search Unlisted Items By:"
								list="#{'ALL':'ALL','description':'Description'}"
								name="moduleParameter"
								onchange="javascript:onTypeChangeInventory('searchForm');" /></td>
					</s:if>
					<s:if test="%{#subModule == 'finGood'}">
						<td><s:select label="Search Finished Goods By:"
								list="#{'ALL':'ALL','productCode':'Product Code', 'description':'Description'}"
								name="moduleParameter"
								onchange="javascript:onTypeChangeInventory('searchForm');" /></td>
					</s:if>
					<s:if test="%{#subModule == 'fpts'}">
						<td><s:select label="Search FP Transfer Slip By:"
								list="#{'ALL':'ALL','fptsNo':'FPTS No', 'transactionDate':'Transaction Date','fptsFrom':'FPTS From','fptsTo':'FPTS To','requisitionNo':'Order Requisition No'}"
								name="moduleParameter"
								onchange="javascript:onTypeChangeInventory('searchForm');" /></td>
					</s:if>
					<s:if test="%{#subModule == 'rf'}">
						<td><s:select label="Search Order Requisition By:"
								list="#{'ALL':'ALL','requisitionNo':'Requisition No', 'requisitionDate':'Requisition Date','requisitionTo':'Requisition To','requisitionBy':'Requested By'}"
								name="moduleParameter"
								onchange="javascript:onTypeChangeInventory('searchForm');" /></td>
					</s:if>
					<s:if test="%{#subModule == 'returnSlip'}">
						<td><s:select label="Search Finished Goods By:" 
								list="#{'ALL':'ALL','returnSlipNo':'Return Slip No', 'orderReferenceNo':'Reference No'}" 
								name="moduleParameter" onchange="javascript:onTypeChangeInventory('searchForm');"/></td>
					</s:if>
					<s:if  test="%{#subModule == 'items'}">
						<td><s:select label="Search By:" 
								list="#{'itemCode':'Item Code','description':'Description'}" 
								name="moduleParameter"/></td>
					</s:if>
				
				</tr>


				<tr>

					<s:if test="%{#subModule == 'fpts' || #subModule == 'rf'}">

						<s:if test="%{#moduleParameter  == 'transactionDate'}">
							<td class="others">Date:</td>
							<td><sx:datetimepicker displayFormat="MMM-dd-yyyy"
									displayWeeks="5" toggleType="explode" id="moduleParameterValue"
									name="moduleParameterValue"></sx:datetimepicker></td>
						</s:if>
						<s:else>
							<td><s:textfield label="Search Value:"
									id="moduleParameterValue" name="moduleParameterValue" /></td>
						</s:else>
					</s:if>
					<s:else>
						<td><s:textfield label="Search Value:"
								name="moduleParameterValue" /></td>
					</s:else>

					<td><s:submit cssClass="myButtons" label="Submit"
							value="SEARCH"></s:submit></td>
				</tr>
				
			</table>
			</p>
		</s:form>
		
		<div class="results">
			<p>
			<table class="results">
				<s:if test="%{#subModule == 'rawMat'}">
					<tr>
						<th>PRODUCT CODE</th>
						<th width="300px">DESCRIPTION</th>
						<th>CO STANDARD PRICE</th>
						<th>CO ACTUAL PRICE</th>
						<th>CO TRANSFER PRICE</th>

						<th>FR STANDARD PRICE</th>
						<th>FR ACTUAL PRICE</th>
						<th>FR TRANSFER PRICE</th>
					</tr>
				</s:if>
				<s:elseif test="%{#subModule == 'finGood'}">
					<tr>
						<th>ITEM CODE</th>
						<th width="300px">DESCRIPTION</th>
						<th>CO STANDARD PRICE</th>
						<th>CO ACTUAL PRICE</th>
						<th>CO TRANSFER PRICE</th>

						<th>FR STANDARD PRICE</th>
						<th>FR ACTUAL PRICE</th>
						<th>FR TRANSFER PRICE</th>

					</tr>
				</s:elseif>
				<s:elseif test="%{#subModule == 'tradedItems'}">
					<tr>
						<th>ITEM CODE</th>
						<th width="300px">DESCRIPTION</th>
						<th>CO STANDARD PRICE</th>
						<th>CO ACTUAL PRICE</th>
						<th>CO TRANSFER PRICE</th>

						<th>FR STANDARD PRICE</th>
						<th>FR ACTUAL PRICE</th>
						<th>FR TRANSFER PRICE</th>

					</tr>
				</s:elseif>
				<s:elseif test="%{#subModule == 'utensils'}">
					<tr>
						<th>ITEM CODE</th>
						<th width="300px">DESCRIPTION</th>
						<th>CO STANDARD PRICE</th>
						<th>CO ACTUAL PRICE</th>
						<th>CO TRANSFER PRICE</th>

						<th>FR STANDARD PRICE</th>
						<th>FR ACTUAL PRICE</th>
						<th>FR TRANSFER PRICE</th>

					</tr>
				</s:elseif>
				<s:elseif test="%{#subModule == 'unlistedItems'}">
					<tr>
						<th>ITEM CODE</th>
						<th width="300px">DESCRIPTION</th>
						<th>UOM</th>
						<th>CLASSIFICATION</th>

					</tr>
				</s:elseif>
				<s:elseif test="%{#subModule == 'fpts'}">
					<tr>
						<th>FPTS NO</th>
						<th width="300px">TRANS DATE</th>
						<th width="300px">FPTS FROM</th>
						<th width="300px">FPTS TO</th>
					</tr>
				</s:elseif>
				<s:elseif test="%{#subModule == 'rf'}">
					<tr>
						<th>REQUISITION NO</th>
						<th width="300px">REQUISITION DATE</th>
						<th width="300px">REQUISITION TO</th>
						<th width="300px">REQUISITION BY</th>
					</tr>
				</s:elseif>
				<s:elseif test="%{#subModule == 'returnSlip'}">
					<tr>
						<th>RETURN NO</th>
						<th width="300px">RETURN DATE</th>
						<th width="300px">ORDER REFERENCE NO</th>
					</tr>
				</s:elseif>
				<s:elseif test="%{#subModule == 'items'}">
					<tr>
						<th>ITEM CODE</th>
						<th width="300px">DESCRIPTION</th>
						
					</tr>
				</s:elseif>

			<s:if test="%{#subModule != 'items'}">
				<s:iterator value="inventoryList" status="inventoryList">
					<s:if test="%{#subModule == 'rawMat'}">
						<tr>
							<td align="left"><s:url id="displayId" action="editInventoryAction">
									<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
									<s:param name="rm.itemCode" value="%{itemCode}">itemCode</s:param>
									<s:param name="subModule" value="%{'rawMat'}">subModule</s:param>
								</s:url> <s:a href="%{displayId}">
									<s:property value="itemCode" />
								</s:a></td>
							<td align="left"><s:property value="description" /></td>
							<td><s:property
									value="itemPricing.companyOwnedStandardPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.companyOwnedActualPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.companyOwnedTransferPricePerUnit" /></td>

							<td><s:property
									value="itemPricing.franchiseStandardPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.franchiseActualPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.franchiseTransferPricePerUnit" /></td>


						</tr>
					</s:if>
					<s:elseif test="%{#subModule == 'finGood'}">
						<tr>
							<td align="left"><s:url id="displayId" action="editInventoryAction">
									<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
									<s:param name="fg.productCode" value="%{productCode}">productCode</s:param>
									<s:param name="subModule" value="%{'finGood'}">subModule</s:param>
								</s:url> <s:a href="%{displayId}">
									<s:property value="productCode" />
								</s:a></td>
							<td align="left"><s:property value="description" /></td>
							<td><s:property
									value="itemPricing.companyOwnedStandardPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.companyOwnedActualPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.companyOwnedTransferPricePerUnit" /></td>

							<td><s:property
									value="itemPricing.franchiseStandardPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.franchiseActualPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.franchiseTransferPricePerUnit" /></td>

						</tr>
					</s:elseif>
					<s:elseif test="%{#subModule == 'tradedItems'}">
						<tr>
							<td align="left"><s:url id="displayId" action="editInventoryAction">
									<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
									<s:param name="ti.itemCode" value="%{itemCode}">itemCode</s:param>
									<s:param name="subModule" value="%{'tradedItems'}">subModule</s:param>
								</s:url> <s:a href="%{displayId}">
									<s:property value="itemCode" />
								</s:a></td>
							<td align="left"><s:property value="description" /></td>
							<td><s:property
									value="itemPricing.companyOwnedStandardPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.companyOwnedActualPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.companyOwnedTransferPricePerUnit" /></td>

							<td><s:property
									value="itemPricing.franchiseStandardPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.franchiseActualPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.franchiseTransferPricePerUnit" /></td>


						</tr>
					</s:elseif>
					<s:elseif test="%{#subModule == 'utensils'}">
						<tr>
							<td align="left"><s:url id="displayId" action="editInventoryAction">
									<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
									<s:param name="u.itemCode" value="%{itemCode}">itemCode</s:param>
									<s:param name="subModule" value="%{'utensils'}">subModule</s:param>
								</s:url> <s:a href="%{displayId}">
									<s:property value="itemCode" />
								</s:a></td>
							<td align="left"><s:property value="description" /></td>
							<td><s:property
									value="itemPricing.companyOwnedStandardPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.companyOwnedActualPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.companyOwnedTransferPricePerUnit" /></td>

							<td><s:property
									value="itemPricing.franchiseStandardPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.franchiseActualPricePerUnit" /></td>
							<td><s:property
									value="itemPricing.franchiseTransferPricePerUnit" /></td>


						</tr>
					</s:elseif>
					<s:elseif test="%{#subModule == 'unlistedItems'}">
						<tr>
							<td align="left"><s:url id="displayId" action="editInventoryAction">
									<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
									<s:param name="unl.description" value="%{description}">description</s:param>
									<s:param name="subModule" value="%{'unlistedItems'}">subModule</s:param>
								</s:url> <s:a href="%{displayId}">
									<s:property value="itemCode" />
								</s:a></td>
							<td align="left"><s:property value="description" /></td>
							<td><s:property
									value="uom" /></td>
							<td><s:property
									value="classification" /></td>
						</tr>
					</s:elseif>
					<s:elseif test="%{#subModule == 'fpts'}">
						<tr>
							<td align="left"><s:url id="displayId" action="editInventoryAction">
									<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
									<s:param name="fpts.fptsNo" value="%{fptsNo}">fptsNo</s:param>
									<s:param name="subModule" value="%{'fpts'}">subModule</s:param>
								</s:url> <s:a href="%{displayId}">
									<s:property value="fptsNo" />
								</s:a></td>
							<td align="left"><s:property value="transactionDate" /></td>
							<td align="left"><s:property value="fptsFrom" /></td>
							<td align="left"><s:property value="fptsTo" /></td>

						</tr>
					</s:elseif>
					<s:elseif test="%{#subModule == 'rf'}">
						<tr>
							<td align="left"><s:url id="displayId" action="editInventoryAction">
									<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
									<s:param name="rf.requisitionNo" value="%{requisitionNo}">rfNo</s:param>
									<s:param name="subModule" value="%{'rf'}">subModule</s:param>
								</s:url> <s:a href="%{displayId}">
									<s:property value="requisitionNo" />
								</s:a></td>
							<td align="left"><s:property value="requisitionDate" /></td>
							<td align="left"><s:property value="requisitionTo" /></td>
							<td align="left"><s:property value="requisitionBy" /></td>


						</tr>
					</s:elseif>
					<s:elseif test="%{#subModule == 'returnSlip'}">
						<tr>
							<td align="left"><s:url id="displayId" action="editInventoryAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="rs.returnSlipNo" value="%{returnSlipNo}">itemCode</s:param>
										<s:param name="subModule" value="%{'returnSlip'}">subModule</s:param>
									</s:url>
									<s:a href="%{displayId}"><s:property value="returnSlipNo"/></s:a>
							</td>
							<td align="left"><s:property value="returnDate" /></td>
							<td align="left"><s:property value="returnSlipReferenceOrderNo"/></td>
						</tr>
					</s:elseif>
				</s:iterator>
			</s:if>
				
				<s:else>
				<s:iterator value="resultList" status="resultList">
					
					<tr>
							<td align="left"><s:url id="displayId" action="editInventoryAction">
									<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
									<s:param name="itemCode" value="%{itemCode}">itemCode</s:param>
									<s:param name="isGeneralSearch" value="%{'true'}">isGeneralSearch</s:param>
									<s:param name="subModule" value="%{itemType}">subModule</s:param>
								</s:url> <s:a href="%{displayId}">
									<s:property value="itemCode" />
								</s:a></td>
							<td align="left"><s:property value="description" /></td>
					</tr>
				</s:iterator>
				</s:else>
			</table>
			
			
		</div>
	</div>

</body>
</html>