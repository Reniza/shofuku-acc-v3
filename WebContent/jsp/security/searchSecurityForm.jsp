<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
  <script type="text/javascript" src="js/expandingSection.js"></script>
  <script type="text/javascript" src="js/onChangeType.js"></script>  
    <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
 	 <script type='text/javascript'>
	var startWith=6;
	var subMenu=3;
	</script>
<title>Security Search Form</title>
</head>

<body>
<div class="mainForm">
<h4 class="title">Security</h4>
		<div class="errors">
			<s:actionerror/>
			<s:actionmessage/>
			
		</div>
	<s:form action="searchSecurityAction" validate="true" id="searchForm">
	<s:hidden name="clicked" id="clicked" value="true"/>
	<s:set name="securityModule" value="securityModule"/>
	<s:set name="moduleParameterValue" value="moduleParameterValue"/>
	<s:set name="moduleParameter" value="moduleParameter"/>		
		<p>
			<table class="form">
				<tr>
					<td>
						<s:select label="Security Module:" 
						headerKey="-1" headerValue="--Choose Module--"
						list="#{'userAccount':'User Account Profile', 'userRole':'User Role'}" 
						name="securityModule" onchange="javascript:onTypeChangeSecurity('searchForm');"/>
					</td>
						
					
				<s:if test="%{#securityModule == 'userAccount'}">
					<td>
						<s:select label="Search User Profile By:" 
						list="#{'ALL':'ALL','userName':'User Name', 'fullName':'Full Name'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeSecurity('searchForm');"/>
					</td>
				</s:if>
				<s:if test="%{#securityModule == 'userRole'}">
					<td>
						<s:select label="Search User Role By:" 
						list="#{'ALL':'ALL','roleName':'Role Name'}" 
						name="moduleParameter" onchange="javascript:onTypeChangeSecurity('searchForm');"/>
					</td>
				</s:if>
				
				</tr>
				<tr>
					<s:if test="%{#securityModule == 'ALL'}">
						
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
					<s:if test="%{#securityModule == 'userAccount'}">
						<tr>
							<th>Username</th>
							<th width="400px">Full Name</th>
							
							
						</tr>
					</s:if> 
					<s:elseif test="%{#securityModule == 'userRole'}">
						<tr>
							<th>Role Name</th>
						</tr>
					</s:elseif> 
					
					
				<s:iterator value="securityList" status="securityList">
					<s:if test="%{#securityModule == 'userAccount'}">
						<tr>
							<td align="left"><s:url id="displayId" action="editSecurityAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="user.userName" value="%{userName}">userName</s:param>
										<s:param name="securityModule" value="%{securityModule}">securityModule</s:param> 
									</s:url>
									<s:a href="%{displayId}"><s:property value="userName"/></s:a>
							</td>
							<td align="left"><s:property value="fullName"/></td>
						
						</tr>
					</s:if>
					<s:elseif test="%{#securityModule == 'userRole'}">
						<tr>
							<td align="left"><s:url id="displayId" action="editSecurityAction">
										<s:param name="forWhat" value="%{'true'}">forEdit</s:param>
										<s:param name="role.roleName" value="%{roleName}">roleName</s:param>
										<s:param name="securityModule" value="%{securityModule}">securityModule</s:param>
									</s:url>
									<s:a href="%{displayId}"><s:property value="roleName"/></s:a>
							</td>
						</tr>
					</s:elseif>
					</s:iterator>
				</table>		
		</p>
	</div>
</div>

</body>
</html>