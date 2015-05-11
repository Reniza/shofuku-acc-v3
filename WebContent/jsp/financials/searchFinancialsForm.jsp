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
	var startWith=5;
	var subMenu=3;
	</script>
<title>Financials Search Form</title>
</head>

<body>
<div class="mainForm">
<h4 class="title">FINANCIALS</h4>
		<div class="errors">
			<s:actionerror/>
			<s:actionmessage/>
			
		</div>
	<s:form action="searchFinancialsAction" validate="true" id="searchForm">
	<s:hidden name="clicked" id="clicked" value="true"/>
	<s:set name="financialModule" value="financialModule"/>
	<s:set name="moduleParameterValue" value="moduleParameterValue"/>
	<s:set name="moduleParameter" value="moduleParameter"/>		
		<p>
			<table class="form">
				<tr>
					<td>
						<s:select label="Financials Module:" 
						headerKey="-1" headerValue="--Choose Module--"
						list="#{'accountEntryProfile':'Account Entry Profile', 'journalEntryProfile':'General Journal'}" 
						name="financialModule" onchange="javascript:onTypeChangeFinancials('searchForm');"/>
					</td>
						
					
				<s:if test="%{#financialModule == 'accountEntryProfile'}">
					<td>
						<s:select label="Search Account Entry Profile By:" 
						list="#{'ALL':'ALL','accountCode':'Account Code', 'name':'Account Name'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeFinancials('searchForm');"/>
					</td>
				</s:if>
				<s:if test="%{#financialModule == 'journalEntryProfile'}">
					<td>
						<s:select label="Search Journal Entries By:" 
						list="#{'ALL':'ALL','entryNo':'Entry No','entryName':'Entry Name'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeFinancials('searchForm');"/>
					</td>
				</s:if>
				
				</tr>
				<tr>
					<s:if test="%{#financialModule == 'ALL'}">
						
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
					<s:if test="%{#financialModule == 'accountEntryProfile'}">
						<tr>
							<th>Account Code</th>
							<th width="400px">Account Name</th>
							
							
						</tr>
					</s:if> 
					<s:elseif test="%{#financialModule == 'journalEntryProfile'}">
						<tr>
							<th>Entry No</th>
							<th width="400px">Journal Name</th>
							<th>Account Debit</th>
							<th>Account Credit</th>
							
						</tr>
					</s:elseif> 
					
					
				<s:iterator value="financialsList" status="financialsList">
					<s:if test="%{#financialModule == 'accountEntryProfile'}">
						<tr>
							<td align="left">
								<auth:isAuth role="28">
									<s:url id="displayId" action="editFinancialsAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="aep.accountCode" value="%{accountCode}">Account Code</s:param>
										<s:param name="financialModule" value="%{financialModule}">financialModule</s:param> 
									</s:url>
								</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="accountCode"/></s:a>
							</td>
							<td align="left"><s:property value="name"/></td>
						
						</tr>
					</s:if>
					<s:elseif test="%{#financialModule == 'journalEntryProfile'}">
						<tr>
							<td align="left">
								<auth:isAuth role="29">
									<s:url id="displayId" action="editFinancialsAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="jep.entryNo" value="%{entryNo}">entryNo</s:param>
										<s:param name="financialModule" value="%{financialModule}">financialModule</s:param>
									</s:url>
								</auth:isAuth>
									<s:a href="%{displayId}"><s:property value="entryNo"/></s:a>
							</td>
							<td align="left"><s:property value="entryName"/></td>
							<td align="left"><s:property value="aepDebit.name"/></td>
							<td align="left"><s:property value="aepCredit.name"/></td>
						</tr>
					</s:elseif>
					</s:iterator>
				</table>		
		</p>
	</div>
</div>

</body>
</html>