<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search page</title>
<link rel="stylesheet" href="./resources/css/index.css" type="text/css"/>

</head>

<body>
<%-- included elements --%>


<div class="content">

<div>
<a class="link1" href="./"> Back to main page</a>      
</div>

<h2>Search form</h2>

<table border="0" width="100%">

<form:form  method="POST" action="search1" modelAttribute="searchmodelattr">
<tr>
<td align="center" width="10%"> <input type="submit" value="Search"/></td>
<td align="left" width="30%">employees born in</td>
<td align="left" width="10%"><form:input path="dob" size="10"/>
</td><td width="10%"><form:errors path="dob" cssClass="error"/></td>
</tr>
</form:form>

<form:form  method="POST" action="search2" modelAttribute="searchmodelattr2">
<tr>
<td align="center" width="10%"> <input type="submit" value="Search"/></td>
<td align="left" width="30%">employees born between</td>
<td align="left" width="10%"><form:input path="dob1" size="10"/>
</td><td width="10%"><form:errors path="dob1" cssClass="error"/></td><td>and</td>
<td align="left" width="10%"><form:input path="dob2" size="10"/></td>
<td width="10%"><form:errors path="dob2" cssClass="error"/></td>
</tr>
</form:form>
</table>

<h2>${resultstitle}</h2>

<c:if test="${not empty foundList}"> 
<table id="empltbl">
<thead class="empltbl-header">
<tr>  <th align="center" width="10%">First name</th>
  <th align="center" width="10%">Middle name</th>  
  <th align="center" width="20%">Surname</th>  
  <th align="center" width="20%">DoB</th>  
  <th align="right" width="10%">Salary</th>   
</tr>   
</thead>
<tbody>
	<c:forEach items="${foundList}" var="empl" varStatus="i" begin="0" >
	<tr class="${i.index % 2 == 0 ? "even" : "odd"}">
	<td align="center" width="10%">${empl.firstName}</td>
	<td align="center" width="10%">${empl.secondName}</td>
	<td align="center" width="20%">${empl.surname}</td>
	<td align="center" width="20%"><fmt:formatDate value="${empl.dob}" type="Date"/></td>
	<td align="right" width="10%"><fmt:formatNumber value="${empl.salary}" type="currency"  pattern=".00"/></td>
	</tr>
	</c:forEach>
</tbody>
 </table>
 </c:if>

</div>
  
</body>
</html>