<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    <%--/jspstudy2/src/main/webapp/view/member/info.jsp
    	4. 조회된 내용을 화면에 출력하기
     --%>
     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 조회</title>
</head>
<body>
<div class="container">
<h2 id="center">회원 상세 정보</h2>
<table class="table table-hover">
<tr><td rowspan="6" width="30%">
<img src ="picture/${mem.picture}" width="200" height="210"></td>
<th width="20%">아이디</th><td>${mem.id}</td></tr>
<tr><th>이름</th><td>${mem.name}</td></tr>
<tr><th>성별</th><td>${mem.gender==1?"남":"여"}</td></tr>
<tr><th>전화</th><td>${mem.tel}</td></tr>
<tr><th>이메일</th><td>${mem.email}</td></tr>
<tr><td colspan="2" id="center">
	<a href="updateForm?id=${ mem.id}">수정</a>
	<c:if test="${param.id !='admin'}">
	<a href="deleteForm?id=${mem.id }">탈퇴</a>
	</c:if>
</td></tr>
</table>
</div>
</body>
</html>
