<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3> 문제 : 1부터 10까지 3개씩 출력하기</h3>
<c:forEach var="i" begin="1" end="10">
	${i }
	<c:if test="${i%3==0 }">
	<br>
	</c:if>
</c:forEach><br>

<c:forEach var="i" begin="1" end="10">
	<c:choose>
		<c:when test="${i%3==0 }">
			${i }<br>
		</c:when>
		<c:otherwise>
			${i }
		</c:otherwise>
	</c:choose>
</c:forEach><br>

<h3> 문제 : list 10 부터 100까지를 3개씩 출력하기</h3>
<%
	List<Integer> list = new ArrayList<>();
	for(int i=1;i<=10;i++)list.add(i*10);
	pageContext.setAttribute("list", list);
%>
<c:forEach var="i" items="${list }" varStatus="st">
	${i }
	<c:if test="${st.count%3==0 }">
		<br>
	</c:if>
</c:forEach>

</body>
</html>