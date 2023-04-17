<%@page import="model.Book"%>
<%@page import="model.BookDao"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /jspstudy2/src/main/webapp/view/test/testlist.jsp : 방명록 목록 조회하기
   1. db에서 방명록 목록 조회하기
   2. 조회된 내용 화면에 출력하기
   url : http://localhost:8080/jspstudy2/book/testlist
--%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>방명록 목록</title>
</head>
<body>
<h2>회원목록</h2>
<table class="w3-table-all">
<tr><th>작성자</th><th>제목</th><th>내용</th></tr>
<c:forEach var="b" items="${list}">
<tr><td>${b.writer}</td>
<td>${b.title}</td>
<td>${b.content}</td>
</tr>
</c:forEach>
</table>
</body>
</html>