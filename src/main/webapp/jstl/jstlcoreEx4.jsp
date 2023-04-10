<%@page import="java.util.Date"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
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
<h4>forEach태그를 이용하여 map객체 출력하기</h4>
<%
	Map<String,Object> map = new HashMap<>();
	map.put("name","홍길동");
	map.put("today", new Date());
	map.put("age",20);
	pageContext.setAttribute("map", map);
%>
<c:forEach var="m" items="${map }">
	${m.key }=${m.value }<br>
</c:forEach>

<h3> map 객체를 EL을 이용하여 출력하기</h3>
name=${map.name }<br>
today=${map.today }<br>
age=${map.age }<br>

<h3>forEach태그를 이용하여 배열객체를 출력하기</h3>
<c:set var="arr" value="<%=new int[]{10,20,30,40,50} %>"/>
<c:forEach var="a" items="${arr }" varStatus="st">
	arr[${st.index }]=${a }<br>	
</c:forEach>

<h3>배열객체를 EL을 이용하여 출력하기</h3>
${arr[0] }<br>
${arr[1] }<br>
${arr[2] }<br>
${arr[3] }<br>
${arr[4] }<br>
<br><br>
<c:forEach var="a" begin="0" end="4">
	${arr[a] }<br>
</c:forEach>

<h3>forEach태그를 이용하여 배열 객체의 두번째 요소부터 세번째 요소만 출력하자</h3>
<c:forEach var="a" items="${arr }" begin="1" end="2" varStatus="st">
	arr[${st.index }] = ${a }<br>
</c:forEach>
<h3>forEach태그를 이용하여 배열 짝수 요소만 출력하자</h3>
<c:forEach var="a" items="${arr }" varStatus="st" step="2">
	arr[${st.index }] = ${a }<br>
</c:forEach>
<h3>forEach태그를 이용하여 배열 홀수 요소만 출력하자</h3>
<c:forEach var="a" items="${arr }" begin="1" end="3" varStatus="st" step="2">
	arr[${st.index }] = ${a }<br>
</c:forEach>
</body>
</html>












