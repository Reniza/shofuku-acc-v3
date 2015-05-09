<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
   <title>Login Page</title>
   <LINK Rel="stylesheet" Href="CSS/design.css" Type="text/css">
</head>
<body background="images/background.jpg">
  <div align="center">
  	<s:form action="login" validate="true">
  	<h1 id="login">Welcome to SHO FUKU Trading Corp Portal </h1>
  	<div class="errors">
				<s:actionerror/>
				<s:actionmessage/>
			</div>
 	 <table id="login">
 		<tr>
		 	<td id="error" colspan="2"><s:actionerror/></td>  
		</tr>
		<tr> 
		 	<td><s:textfield name="username" label="Username:"/></td>
		</tr>
		<tr>
		    <td><s:password name="password" label="Password:"/></td>
		</tr>
		<tr>
		  <td><s:submit value="Submit" name="Submit"/></td>
		</tr> 
		  
	 </table>
 	 </s:form>
   </div>
  </body>
</html>