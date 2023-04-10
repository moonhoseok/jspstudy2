<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- /jspstudy2/src/main/webapp/test/test1.jsp --%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>두개의 파라미터값을 계산하기</title>
</head>
<body>
<form method="post" >
  x:<input type="text" name="x" value="${param.x}"><br>
  y:<input type="text" name="y" value="${param.y}">
   <input type="submit" value="더하기">  
</form>
합계 : ${param.x +param.y}
<h3>if 태그를 이용하여 짝수 홀수 출력하기</h3>
<c:set var="xy" value="${param.x +param.y}" />
<c:if test="${xy>0 }">
	${xy }은 양수 입니다.
</c:if>
<c:if test="${xy<0 }">
	${xy }은 음수 입니다.
</c:if>
<br>
<h3>choose when 태그를 이용하여 짝수 홀수 출력하기</h3>
<c:choose>
	<c:when test="${xy>0 }">
		${xy }은 양수 입니다.
	</c:when>
	<c:when test="${xy<0 }">
		${xy }은 음수 입니다.
	</c:when>
</c:choose>

</body>
</html>