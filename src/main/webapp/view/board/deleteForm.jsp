<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<%-- /jspstudy1/src/main/webapp/model1/board/deleteForm.jsp 
	
	1. 공지사항인 경우 관리자가 아닌 경우 화면 출력 불가
	
--%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 삭제</title>
</head>
<body>
<div>
<h2>게시물 삭제</h2>
<form action="delete" method="post">
	<input type="hidden" name="num" value="${param.num}">
	<label>Password:</label>
	<input type="password" class="w3-input" name="pass">
<div class="w3-center" id="center" style="padding: 3px;">
	<button type="submit"> [게시물삭제]</button>
</div>
</form>
</div>
</body>
</html>
