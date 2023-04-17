<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%--/jspstudy2/src/main/webapp/view/board/updateForm 
    1. 공지사항인 경우 관리자만 수정가능
	2. num값에 해당하는 게시물을 조회
	3. 조회된 게시물을 화면에 출력
    --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 수정 화면</title>

<script type="text/javascript">
	function file_delete(){
		document.f.file2.value="";
		file_desc.style.display="none";
	}
</script>
</head>
<body>
<form action="update" method="post"
	  enctype="multipart/form-data" name="f">
	<input type="hidden" name="num" value="${ b.num}">
	<input type="hidden" name="file2"
			value="${b.file1 }">
<div class="container">
<h2 class="w3-center">${boardName}수정</h2>
<table class="w3-table-all">
<tr><td>글쓴이</td><td>
	<input type="text" name="writer" value="${b.writer }" class="w3-input"></td></tr>
<tr><td>비밀번호</td><td>
	<input type="password" name="pass" class="w3-input"></td></tr>
<tr><td>제목</td><td>
	<input type="text" name="title" value="${b.title}" class="w3-input"></td></tr>
<tr><td>내용</td><td>
	<textarea rows="15" name="content" class="w3-input" >${b.content}</textarea></td></tr>
<tr><td>첨부파일</td><td style="text-align: left;">
<c:if test="${!empty b.file1 }">
	<div id="file_desc">${b.file1}
		<a href="javascript:file_delete()">[첨부파일삭제]</a></div>
</c:if>
<input type="file" name="file1"></td></tr>
<tr><td colspan="2">
<a href="javascript:document.f.submit()">[게시물수정]</a></td></tr>
</table>
</div>
</form>
</body>
</html>
















