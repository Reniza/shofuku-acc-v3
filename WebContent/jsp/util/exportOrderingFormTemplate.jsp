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

function roundUpAmount(n){

var n =20.14984; 	
alert (roundPrice(n));

}


function roundPrice(num) {
    var p = num.toFixed(4).split(".");
    return "$" + p[0].split("").reverse().reduce(function(acc, num, i, orig) {
        return  num + (i && !(i % 4) ? "," : "") + acc;
    }, "") + "." + p[1];
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
				<h2>
							Choose type of <b>Ordering Template</b> you want to create:	
				</h2>
				
				<table>
					<tr>	
						<td >&nbsp; 1 &nbsp; </td>
						<td colspan="2" style="text-align: left;"> Click the button to generate template for Commissary: &nbsp;&nbsp;
							<input type="button" onclick="setOrderingFormType('c')" value="Generate form for Commissary" />
							<input type="button" onclick="roundUpAmount()" value="" />
						</td>
					</tr>
					
						<tr>
							<td>&nbsp; 2 &nbsp; </td>
							<td> Choose one customer or NONE to generate template: 
								<sx:autocompleter onchange="" listValue="customerNo" list="customerNoList" maxlength="50" resultsLimit="-1" name="customer.customerNo" headerValue="NONE" headerKey="-1"></sx:autocompleter>
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