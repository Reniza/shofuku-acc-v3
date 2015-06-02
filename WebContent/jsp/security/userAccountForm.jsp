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
<script type="text/javascript" src="js/expandingSection.js"></script> 
 <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 
 <sx:head/>
	 <script type='text/javascript'>
	var startWith=6;
	var subMenu=1;
	</script>
<title>User Account Profile</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
	<h4 class="title">User Account</h4>
	<s:form action="showUserAccountForm" validate="true" id="userAccountForm">
		<div class="form" id="wholeForm">
			<div class="errors">
				<s:actionerror/>
				<s:actionmessage/>
			
			</div>
			<div id="profileForm">
				<h3 class="form">User Profile</h3>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Profile Details</th>
						</tr>
						<tr>
							<td>
								<s:textfield label="Username:" name="user.userName"></s:textfield></td>
								<s:hidden name="userId" value="%{user.userId}"/>
						</tr>
						<tr>
							<td><s:password  disabled="%{forWhat}" size="20" label="Password:" name="user.password"></s:password></td>
							<td><s:password  disabled="%{forWhat}" size="20" label="Confirm Password:" name="tempPassword"></s:password></td>
						</tr>
					</table>
				</p>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Account Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" label="Full Name:" name="user.fullName" size="50"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" label="Location (MNL/CEB):" name="user.location" size="50"></s:textfield></td>
						</tr>
					</table>
					<table class="form">
						<tr>
							<td>Role Name:</td>
							<td><sx:autocompleter headerValue="Choose One" dropdownHeight="50px" listValue="roleName" list="roleList" maxlength="10"  name="user.role.roleName"></sx:autocompleter> 
							</td>
						</tr>
					</table>
				</p>			
			</div>
		</div>

		<div class="forButtons">
			<p>
				<table class="forButtons" align="center">	
					<tr>
						<s:hidden name="subModule" value="userAccount"/>
						<s:if test="%{forWhatDisplay == 'edit'}">
						
							<td><input class="myButtons" name="edit" type="button" onclick="javascript:toggleAlert('profileForm','supplierId');" value="Edit"></input></td>
							<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="Update" id="bUpdate" action="updateSecurityAction"></s:submit></td>
							<td><s:submit cssClass="myButtons" type="button" value="New Entry" name="newEntry" action="loadRoleListAction"></s:submit></td>
						
						</s:if>
						<s:else>
							<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="Add User" name="newUserProfile" action="addSecurityAction"></s:submit></td>
							<td><s:submit cssClass="myButtons" type="button" value="Cancel" name="cancel" action="loadRoleListAction"></s:submit></input></td>
						   
						</s:else>
					</tr>
				</table>
			</p>
		</div>
	</s:form>
</div>
</s:if>

<!-- div for print -->
<s:else>
<div class="print">
<jsp:include page="/jsp/util/companyHeader.jsp"/>
	<h3 class="form">PROFILE</h3>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Profile Details</th>
						</tr>
						<tr>
							<td>
								<s:textfield label="Username:" value="%{user.username}"></s:textfield>
							</td>
						</tr>
					</table>
					<table class="form">
						<tr>
							<th colspan="6">Account Details</th>
						</tr>
						<tr>
							<td><s:textfield label="Full Name:" value="%{user.fullName}" size="50"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield label="Role:" value="%{user.role.roleName}" size="30"></s:textfield></td>
						</tr>
					</table>
				</p>	
	</div>
</s:else>
</body>
</html>