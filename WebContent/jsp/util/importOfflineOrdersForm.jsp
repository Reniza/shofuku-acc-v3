<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
<script type="text/javascript" src="js/expandingSection.js"></script> 
<script type="text/javascript" src="js/hideParameter.js"></script> 
<script type="text/javascript" src="js/deleteConfirmation.js"></script> 
  <link rel="stylesheet" href="menu.css" type="text/css"/>
   <script type='text/javascript'>
		var startWith=6;
		var subMenu=4;
	</script>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
<sx:head parseContent="true"/>
<title>Import Offline Orders Form</title>


</head>
<body>
<div class="mainForm">
<h4 class="title">Import Offline Orders</h4>
<s:form id="importForm" action="exportOrderingFormTemplateAction" enctype="multipart/form-data" >
		<div class="errors">
			<s:actionerror/>
			<s:actionmessage/>
		</div>
		<div class="form">
			<p>
				<h3>Import offline orders when SHOFUKU application is not accessible...</h3>
				<table class="form">
					<tr>	
						<td>Browse file to import (*.xls only):  &nbsp;&nbsp;
							<s:file size="70" theme="simple"  cssClass="myButtons" name="fileUpload" />
						</td>
						<td colspan="2" style="text-align: left;"> 
							<input type="button"  value="Import File" />
						</td>
					</tr>
				</table>
			</p>
			
			<div class="results">
			<p></p><p></p>
			<table class="results">
				<tr class="others" align="left">
					<td class="desc" width="20px">Error Logs</td>
				</tr>
				<s:iterator value="errorStringList" >
				<tr align="left">
					<td><s:property/></td>
				</tr>
				</s:iterator>
			</table>
		</div>
		</div>
		
		</s:form>
		
</div>
</body>
</html>