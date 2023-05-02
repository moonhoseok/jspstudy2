<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%--/jspstudy2/src/main/webapp/view/ajax/exchange.jsp --%>
<table class="w3-table-all"><caption> 수출입은행<br>${date}</caption>
<tr>
	<th>통화</th><th>기준율</th><th>받을때</th><th>보낼때</th>
</tr>
<c:forEach items="${list }" var="tdlist">
	<tr>
		<td>${tdlist[0]}<br>${tdlist[1]}</td><td>${tdlist[4]}</td>
		<td>${tdlist[2]}</td><td>${tdlist[3]}</td>
	</tr>
</c:forEach>
</table>