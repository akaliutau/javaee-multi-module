<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add new Department</title>
<link rel="stylesheet" href="../resources/css/index.css" type="text/css"/>

</head>
<body>
<%-- included elements --%>
<%-- non --%>

<div align="center">
	
<h2>Department Form</h2>

<div> ${message}</div>
<table border="0" width="90%">

<form:form  method="POST" action="add.do" modelAttribute="depmodelattr">

<tr>
<td align="left" width="20%">Title: </td>
<td align="left" width="40%"><form:input path="title" size="30"/></td>
<td align="left"><form:errors path="title" cssClass="error"/></td>
</tr>
				
<tr>
<td></td>
<td align="center">
<input type="submit" value="Submit"/>
<input type="reset" value="Reset"/>
</td>
<td></td>
</tr>

</form:form>

</table>
</div> 
<a class="link1" href="../"> Back to main page</a>      


</body>
</html>