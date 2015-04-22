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
		var subMenu=0;
	</script>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
<sx:head parseContent="true"/>
<title>Export Ordering Form</title>

<script>

function setOrderingFormType(type){
	document.getElementById("orderingFormType").value = type;
	document.getElementById("exportForm").submit();
}

</script>

</head>
<body>
<div class="mainForm">
<h4 class="title">Template Generator</h4>
<s:form id="exportForm" action="exportOrderingFormTemplateAction" enctype="multipart/form-data" >
				
		<div class="form">
			<input type="hidden" name="orderingFormType" id="orderingFormType"/>
			<p>
				<table class="form" align="center">
					<tr>
						<td colspan="4">
							Choose type of <b>Ordering Template</b> you want to create:	
						</td>
					</tr>
					<tr>	
						
						<td colspan="2" style="text-align: left;">
							
									<input type="button" onclick="setOrderingFormType('c')" value="Generate form for Commissary" />
						</td>
					</tr>
					</table>
					<table>
						<tr>
							<td>
								<sx:autocompleter listValue="customerNo" list="customerNoList" maxlength="50" resultsLimit="-1" name="custpo.customer.customerNo"></sx:autocompleter>
							</td>
							<td>
								<input type="button" onclick="setOrderingFormType('s')" value="Generate form for Stores" />
							</td>
						</tr>
				</table>
			</p>
		</div>
		<div class="errors">
					<s:actionerror/>
					<s:actionmessage/>
		</div>
</s:form>
</div>
</body>
</html>