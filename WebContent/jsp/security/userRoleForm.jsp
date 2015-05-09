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
<script type="text/javascript" src="js/roles.js"></script>
 <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
	 <script type='text/javascript'>
	var startWith=6;
	var subMenu=2;
	</script>
	
	
<title>User Account Profile</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
	<h4 class="title">Role</h4>
	<s:form action="showUserRoleForm" validate="true" id="userRoleForm">
		<div class="form" id="wholeForm">
			<div class="errors">
				<s:actionerror/>
				<s:actionmessage/>
			
			</div>
			<div id="profileForm">
				<h3 class="form">Role Management</h3>
				<p>
					<table class="form">
						<tr>
							<th colspan="9">Role Details</th>
						</tr>
						<tr>
							<td>
								<s:textfield label="Role Name:" name="role.roleName"></s:textfield></td>
								<s:textfield label="Role Name:" name="role.roleId"></s:textfield></td>
						</tr>
					</table>
				</p>
				<p>
					<table class="form">
						<tr>
							<th colspan="5">Roles</th>
						</tr>
						<tr>
						<td>
						
						<s:select  id = "modulesNotGrantedList" name="modulesNotGrantedList" size="16" label="Modules List"  list="modulesNotGrantedList" listKey="moduleId" listValue="moduleName" multiple="true"/>
							
						</td>
						
						<td >
						<input type="button" onClick="move(this.form.modulesNotGrantedList,this.form.modulesGrantedList)" 
						value="->"><br />
						<input type="button" onClick="move(this.form.modulesGrantedList,this.form.modulesNotGrantedList)" value="<-">
						</td>
						
						<td>
						<s:select  id = "modulesGrantedList" name="modulesGrantedList" size="16" label="Accessible Modules" property="selectedValues" list="modulesGrantedList" listKey="moduleId" listValue="moduleName" multiple="true"/>
						</td>
					</table>
				</p>			
			</div>
		</div>

		<div class="forButtons">
			<p>
				<table class="forButtons" align="center">	
					<tr>
						<s:hidden name="subModule" value="userRole"/>
						<s:if test="%{forWhatDisplay == 'edit'}">
						
						<td><input class="myButtons" name="edit" type="button" onclick="javascript:toggleAlert('profileForm','supplierId');" value="Edit"></input></td>  
						<!--<td><s:submit cssClass="myButtons" type="button" value="Delete" onclick="javascript:securityConfirmation('userRoleForm');"></s:submit></td> -->
						<!--<td><s:submit cssClass="myButtons" type="button" value="Print" action="printSupplierAction"></s:submit></td> -->
							<td><s:submit disabled="%{forWhat}" cssClass="myButtons" onClick="selectGrantedListItems(this.form.modulesGrantedList)" type="button" value="Update" id="bUpdate" action="updateSecurityAction"></s:submit></td>
							<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','userRoleForm');" value="New Entry"></input></td>
						</s:if>
						<s:else>
							<td><s:submit disabled="%{forWhat}" cssClass="myButtons" onClick="selectGrantedListItems(this.form.modulesGrantedList)" type="button" value="New Entry" name="newUserProfile" action="addSecurityAction"></s:submit></td>
							<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','userRoleForm');" value="Cancel"></input></td>
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
			
	</div>
</s:else>
</body>
</html>