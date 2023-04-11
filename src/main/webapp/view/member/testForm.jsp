<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Insert title here</title>
</head>

<body>

<form action="test.jsp" method="post">

   이름:<input type="text" name="name"><br>

   나이:<input type="text" name="age"><br>

   성별:<input type="radio" name="gender" value="1">남

     <input type="radio" name="gender" value="2">여<br>

 출생연도 : <select name="year">

 			<c:forEach var="i" begin="1980" end="1999">
			<option>
 				${i}
 			</option>
 			</c:forEach>

 		</select><br> 

<input type="submit" value="전송">
</form>
</body>
</html>