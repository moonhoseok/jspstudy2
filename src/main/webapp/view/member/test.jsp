<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<fmt:requestEncoding value="utf-8"/>
이름 : ${param.name }<br>
나이 : ${param.age }<br>
성별 : <c:if test="${param.gender =='1'}">남<br></c:if>
<c:if test="${param.gender =='2'}">여<br></c:if>
출생년도 : ${param.year}<br>
<fmt:parseNumber value="${param.year}" var="num" />
나이(2023년기준): 만 ${2023-num }
</body>
</html>