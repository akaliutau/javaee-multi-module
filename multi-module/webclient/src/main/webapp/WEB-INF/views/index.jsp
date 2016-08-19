<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Main page</title>
<link rel="stylesheet" href="./resources/css/index.css" type="text/css"/>

<script type="text/javascript" src="./resources/js/jquery-1.10.1.min.js"></script>
<script type="text/javascript" src="./resources/js/helper.js"></script>

</head>
<body>
<%-- included elements --%>
<jsp:include page="top.jsp" flush="true"/>

<div class="content">
<h2>Departments List</h2>
        <div class="commonerrorblock" id="errmsgdeplist"> </div>
         <div class="commonerrorblock" id="errmsg"> </div>
<table id="deptbl">
<thead class="deptbl-header">
<tr>  <th align="center" width="60%">Title</th>  
<th align="center" width="20%">Average Salary</th>   </tr>   
</thead>
<tbody>
<%--  any deleteable departments has index <1000 --%>
<c:if test="${not empty depList}">
	<c:forEach items="${depList}" var="dep" varStatus="i" begin="0" >
	<c:set var="id" value="${i.index}" />
	<c:choose>
	<c:when test="${empty dep.avrSalary}"><tr id="${id}"></c:when>
	<c:otherwise><tr id="${id+1000}"></c:otherwise>
	</c:choose>
	<td align="center" width="60%">${dep.title}</td>
	<td align="right" width="20%"><fmt:formatNumber value="${dep.avrSalary}" type="currency"  pattern=".00"/></td>
	</tr>
	</c:forEach>
</c:if>
</tbody>
</table>
<input type="button" value="Add" id="adddep"/>&nbsp;&nbsp;
<input type="button" value="Edit" id="editdep"/>&nbsp;&nbsp;
<input type="button" value="Delete" id="deldep"/>&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<c:if test="${depPage.isVisible}">
<input type="button" value="Previous" id="depprev" <c:if test="${not depPage.isPrevious}">disabled="disabled"</c:if> />&nbsp;&nbsp;
<input type="button" value="Next" id="depnext" <c:if test="${not depPage.isNext}">disabled="disabled"</c:if>/>&nbsp;&nbsp;
</c:if>

<h2>Employees List</h2>
        <div class="commonerrorblock" id="errmsgempllist1"> </div>
         <div class="commonerrorblock" id="errmsg1"> </div>
<c:if test="${not empty depOpt}"> 
<select id="depopt" name="depid"
>
      
<c:set var="sel" value="${depOpt.selected}" />
<c:forEach items="${depOpt.list}" var="i">
            
<option <c:if test="${i.id eq sel}">selected="selected"</c:if> value="${i.id}">${i.title}</option>
 
 </c:forEach>

</select>
</c:if>
<table id="empltbl">
<thead class="empltbl-header">
<tr>  <th align="center" width="10%">First name</th>
  <th align="center" width="10%">Middle name</th>  
  <th align="center" width="20%">Surname</th>  
  <th align="center" width="10%">DoB</th>  
  <th align="right" width="20%">Salary</th>   
</tr>   
</thead>
<tbody>
<c:if test="${not empty emplList}"> 
	<c:forEach items="${emplList}" var="empl" varStatus="i" begin="0" >
	<c:set var="id" value="${i.index}" />
	<tr id="${id}">
	<td align="center" width="10%">${empl.firstName}</td>
	<td align="center" width="10%">${empl.secondName}</td>
	<td align="center" width="20%">${empl.surname}</td>
	<td align="center" width="20%"><fmt:formatDate value="${empl.dob}" type="Date"/></td>
	<td align="right" width="20%"><fmt:formatNumber value="${empl.salary}" type="currency" pattern=".00"/></td>
	</tr>
	</c:forEach>
 </c:if>
</tbody>
 </table>
<input type="button" value="Add" id="addempl"/>&nbsp;&nbsp;
<input type="button" value="Edit" id="editempl"/>&nbsp;&nbsp;
<input type="button" value="Delete" id="delempl"/>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<c:if test="${emplPage.isVisible}">
<input type="button" value="Previous" id="emplprev" <c:if test="${not emplPage.isPrevious}">disabled="disabled"</c:if> />&nbsp;&nbsp;
<input type="button" value="Next" id="emplnext" <c:if test="${not emplPage.isNext}">disabled="disabled"</c:if>/>&nbsp;&nbsp;
</c:if>

</div>
       <noscript>
            <div class="commonerrorblock">
                Your browser does not support JavaScript. Upgrade your browser, turn on JavaScript and reload this page.
            </div>
        </noscript>
</body>
</html>