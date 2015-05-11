<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<%@ taglib prefix="c" uri="/tld/c.tld"%>
 <%@ taglib prefix="auth" uri="/tld/Authorization.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
<script type="text/javascript" src="js/hideParameter.js"></script>
<script type="text/javascript" src="js/deleteConfirmation.js"></script>
<script type="text/javascript" src="js/expandingSection.js"></script> 
 <link rel="stylesheet" href="menu.css" type="text/css"/>
	<script type="text/javascript" src="js/expandingMenu.js"></script> 	
	 <script type='text/javascript'>
	var startWith=0;
	var subMenu=0;
	</script>
<title>Supplier Profile Form</title>
</head>
<body>
<s:if test="%{forWhat != 'print'}">
<div class="mainForm">
	<h4 class="title">SUPPLIER</h4>
	<s:form action="showSupplierProfileForm" validate="true" id="supplierForm">
		<div class="form" id="wholeForm">
			<div class="errors">
				<s:actionerror/>
				<s:actionmessage/>
			
			</div>
			<div id="profileForm">
				<h3 class="form">PROFILE</h3>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Profile Details</th>
						</tr>
						<tr>
							<td><s:textfield readOnly="readOnly" label="Supplier ID:" name="supplier.supplierId" id="supplierId"></s:textfield></td>
							<s:hidden name="supId" value="%{supplier.supplierId}"/>
							<td><s:select disabled="%{forWhat}" label="Supplier Status" name="supplier.supplierStatus" list="#{'VAT':'VAT','NON-VAT':'NON-VAT','VAT-EXEMPT':'VAT-EXEMPT','ZERO-RATED':'ZERO-RATED'}"></s:select></td> 			
							<td><s:select disabled="%{forWhat}" label="Payment Term"
								list="#{'COD':'COD','7DAYS':'7DAYS','15DAYS':'15DAYS','30DAYS':'30DAYS','45DAYS':'45DAYS','60DAYS':'60DAYS','90DAYS':'90DAYS'}" 
								name="supplier.paymentTerm"/></td>
						</tr>
					</table>
					<table class="form">
						<tr>
							<td>
								<s:textfield  disabled="%{forWhat}" size="90" label="Supplier Name:" name="supplier.supplierName"></s:textfield>	
								<c:if test="${not empty supplier.emailAddress}">
								<a href="mailto:<s:property value="%{supplier.emailAddress}"/>" onclick="javascript:checkEmailAddress();">eMail Me</a>
								</c:if>
							</td>
						</tr>
						<tr><td>
								<s:textfield  disabled="%{forWhat}" size="30" label="TIN Number: " name="supplier.tin"></s:textfield></td>
						</tr>
					</table>
				</p>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Contact Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" label="Contact Name:" name="supplier.contactName" size="50"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" label="Contact Title:" name="supplier.contactTitle" size="30"></s:textfield></td>
							<td><s:textfield disabled="%{forWhat}" label="Email Address:" name="supplier.emailAddress" id="emailAdd" size="30"></s:textfield></td>
						</tr>
					</table>
					<table class="form">
					
						<tr>
							<td>
								<s:textfield  disabled="%{forWhat}" size="90" label="Website:" name="supplier.website" id="website"></s:textfield>
								<c:if test="${not empty supplier.website}">	
								<a onclick="javascript:checkWeb();" href="<s:property value="%{supplier.website}"/>">my website</a>
								</c:if>
							</td>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" label="Company Address:" name="supplier.companyAddress" size="90"></s:textfield></td>
						</tr>
					</table>
					<table class="form">
					    <tr>
							<th colspan="6">Contact Numbers Details</th>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" label="Phone Number: " onfocus="parsePhoneNumber(this)" onblur="formatPhoneNumber(this)" name="supplier.phoneNumber"></s:textfield></td>
							<td><s:textfield disabled="%{forWhat}" label="Fax Number: " onfocus="parsePhoneNumber(this)" onblur="formatPhoneNumber(this)" name="supplier.faxNumber"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield disabled="%{forWhat}" label="Mobile Number: " onfocus="parseMobileNumber(this)" onblur="formatMobileNumber(this)" name="supplier.mobileNumber"></s:textfield></td>
						</tr>
					</table>
				</p>			
			</div>
		</div>

		<div class="forButtons">
			<p>
				<table class="forButtons" align="center">	
				<tr>
				<s:hidden name="subModule" value="supplierProfile"/>
				<s:if test="%{forWhatDisplay == 'edit'}">
				
					<td><input class="myButtons" name="edit" type="button" onclick="javascript:toggleAlert('profileForm','supplierId');" value="Edit"></input></td>
					<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="Update" id="bUpdate" action="updateSupplierAction"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Delete" onclick="javascript:supplierConfirmation('supplierForm');"></s:submit></td>
					<td><s:submit cssClass="myButtons" type="button" value="Print" action="printSupplierAction"></s:submit></td>
					<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','supplierForm');" value="New Entry"></input></td>
				
				</s:if>
				<s:else>
					<td><s:submit disabled="%{forWhat}" cssClass="myButtons" type="button" value="New Entry" name="newSupplierProfile" action="addSupplierAction"></s:submit></td>
					<td><input class="myButtons" name="clear" type="button" onclick="javascript:clearAll('wholeForm','supplierForm');" value="Cancel"></input></td>
				   
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
	<h3 class="form">PROFILE</h3>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Profile Details</th>
						</tr>
						<tr>
							<td><s:textfield label="Supplier ID:" value="%{supplier.supplierId}"></s:textfield></td>
							<td><s:textfield label="Supplier Status" value="%{supplier.supplierStatus}"/> </td> 			
							<td><s:textfield label="Payment Term" value="%{supplier.paymentTerm}"/> </td> 			
							
						</tr>
					</table>
					<table class="form">
						<tr>
							<td><s:textfield label="Supplier Name:" value="%{supplier.supplierName}" size="90"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield label="Tin Number:" value="%{supplier.tin}" size="30"></s:textfield></td>
						</tr>
					</table>
				</p>
				<p>
					<table class="form">
						<tr>
							<th colspan="6">Contact Details</th>
						</tr>
						<tr>
							<td><s:textfield label="Contact Name:" value="%{supplier.contactName}" size="50"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield label="Contact Title:" value="%{supplier.contactTitle}" size="30"></s:textfield></td>
							<td><s:textfield label="Email Address:" value="%{supplier.emailAddress}" size="30"></s:textfield></td>
						</tr>
					</table>
					<table class="form">
						
						<tr>
							<td>
								<s:textfield label="Website:" value="%{supplier.website}" size="90"></s:textfield>	
							</td>
						</tr>
						<tr>
							<td><s:textarea rows="3" cols="70" label="Company Address:" value="%{supplier.companyAddress}"></s:textarea></td>
						</tr>
					</table>
					<table class="form">
					    <tr>
							<th colspan="6">Contact Numbers Details</th>
						</tr>
						<tr>
							<td><s:textfield label="Phone Number:" value="%{supplier.phoneNumber}"></s:textfield></td>
							<td><s:textfield label="Fax Number:" value="%{supplier.faxNumber}"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield label="Mobile Number:" value="%{supplier.mobileNumber}"></s:textfield></td>
						</tr>
					</table>
		</p>	
</div>
</s:else>
</body>
</html>