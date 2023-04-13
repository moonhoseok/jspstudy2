<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/jspstudy2/src/main/webapp/view/member/picture.jsp
	2. opener화면에 결과 전달 => javascript
	3. 현재화면 close() => javascript
 --%>   
<script>
	img = opener.document.getElementById("pic"); //id ="pic"인 태그 선택
	img.src = "/jspstudy2/picture/${fname}"; // => joinForm.jsp페이지에 이미지 보여짐
	opener.document.f.picture.value="${fname}";
	self.close();// 현재창 닫기
</script>
