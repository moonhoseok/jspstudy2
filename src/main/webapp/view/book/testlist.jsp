<%@page import="model.Book"%>
<%@page import="model.BookDao"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /jspstudy1/src/main/webapp/model1/test/testlist.jsp : 방명록 목록 조회하기
   1. db에서 방명록 목록 조회하기
   2. 조회된 내용 화면에 출력하기
--%>
<%
	request.setCharacterEncoding("UTF-8");
	String writer = request.getParameter("writer");
	//List<Book> list = new BookDao().list(writer);
	List<Book> list = new BookDao().list();
%> 
<!DOCTYPE html> 
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../../css/main.css">
</head>
<body>

<% for(Book b : list){ %>
<table><caption> 방명록 </caption>
	<tr><th>작성자</th><td><%=b.getWriter()%></td></tr>
	<tr><th>제목</th><td><%=b.getTitle() %></td></tr>
	<tr><th>내용</th><td><%=b.getContent() %></td></tr>
<%} %>
</table>
</body>
</html>
