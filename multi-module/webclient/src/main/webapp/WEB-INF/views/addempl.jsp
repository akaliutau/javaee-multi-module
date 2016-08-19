<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add new employee</title>
<link rel="stylesheet" href="../resources/css/index.css" type="text/css"/>

</head>
<body>
<%-- included elements --%>
<%-- non --%>


<div align="center">
	
<h2>Employee Form</h2>
<div> ${message}</div>

<table border="0" width="90%">

<form:form  method="POST" action="add.do" modelAttribute="emplmodelattr">

<tr>
<td align="left" width="20%"></td>
<td align="left" width="40%"><form:hidden path="id" size="0"/></td>
<td align="left"></td>
</tr>

<tr>
<td align="left" width="20%">First name: </td>
<td align="left" width="40%"><form:input path="firstName" size="30"/></td>
<td align="left"><form:errors path="firstName" cssClass="error"/></td>
</tr>

<tr>
<td align="left" width="20%">Middle name: </td>
<td align="left" width="40%"><form:input path="secondName" size="30"/></td>
<td align="left"><form:errors path="secondName" cssClass="error"/></td>
</tr>

<tr>
<td align="left" width="20%">Surname: </td>
<td align="left" width="40%"><form:input path="surname" size="30"/></td>
<td align="left"><form:errors path="surname" cssClass="error"/></td>
</tr>

<tr>
<td align="left" width="20%">DoB: </td>
<td align="left" width="40%"><form:input path="dob" size="30"/></td>
<td align="left"><form:errors path="dob" cssClass="error"/></td>
</tr>

<tr>
<td align="left" width="20%">Department: </td>
<td align="left" width="40%">
<form:select id="selectCategoryId" path="depId"
 multiple="">
      
<c:set var="sel" value="${depOpt.selected}" />
<c:forEach items="${depOpt.list}" var="i">
            
<option <c:if test="${i.id eq sel}">selected="selected"</c:if> value="${i.id}">${i.title}</option>
 
 </c:forEach>

</form:select>  
<td align="left"><form:errors path="depId" cssClass="error"/></td>
</tr>

<tr>
<td align="left" width="20%">Salary: </td>
<td align="left" width="40%"><form:input path="salary" size="30"/></td>
<td align="left"><form:errors path="salary" cssClass="error"/></td>
</tr>

<%-- buttons panel: for edit mode - go back to main page after edit --%>
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