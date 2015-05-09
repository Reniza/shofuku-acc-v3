<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="CSS/menu.css" type="text/css"/>
<title><tiles:insertAttribute name="title" ignore="true" /></title>
</head>
<body>
<div align="center">
<table class="layout">
	<tr>
		<td height="50px" colspan="2" BGCOLOR="aad6f5" align="right">
			<tiles:insertAttribute name="header" />
			<label style="font-family:verdana, sans-serif; font-size: 14px;color: #426c85;font-weight: 700;">
			Welcome [ ${user.fullName} ] 
			<s:a style="text-decoration: none;color: #426c85;" href="logout.action">[ LOGOUT ]</s:a></label>
		</td>
	</tr>
	<tr height="500px">
		<td width="150px" BGCOLOR="#8ad3e4">
			<div style="padding-bottom:500px"><tiles:insertAttribute name="mainMenu" /></div>
		</td>
		<td width="750px" BGCOLOR="#ffffff">
			<div><tiles:insertAttribute name="body" /></div>
		</td>
	</tr>
	<tr>
		<td height="15px" colspan="2" BGCOLOR="#ffffff">
			<tiles:insertAttribute name="footer" />
		</td>
	</tr>
</table>
</div>
</body>
</html>