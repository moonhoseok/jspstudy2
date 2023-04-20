<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/jspstudy2/src/main/webapp/view/board/writeForm.jsp
	1. writeForm.jsp :
		공지사항 게시물인 경우 관리자가 아닌경우 
		등록 못하도록 조건 설정하기
--%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판글쓰기</title>
<script>
	function inputcheck(){
		f=document.f;
		if(f.writer.value.trim() ==""){
			alert("글쓴이를 입력하세요.")
			f.writer.focus();
			return false;
		}
		if(f.pass.value.trim() ==""){
			alert("비밀번호를 입력하세요.")
			f.pass.focus();
			return false;
		}
		if(f.title.value.trim() ==""){
			alert("제목을 입력하세요.")
			f.title.focus();
			return false;
		}
	/* 	if(f.content.value.trim() ==""){
			alert("내용을 입력하세요.")
			f.content.focus();
			return false;
		} 에디터에서 에러 */ 
		f.submit();// submit 발생=> form의 action페이지로 요청
	}
</script>
</head>
<body>

<form action="write" method="post"
		enctype="multipart/form-data" name="f">
<h2 class="w3-center">게시판 글쓰기</h2>
<table class="w3-table w3-border" >
	<tr><td>글쓴이</td><td><input type="text" name="writer" class="w3-input"></td></tr>
	<tr><td>비밀번호</td><td><input type="password" name="pass" class="w3-input"></td></tr>
	<tr><td>제목</td><td><input type="text" name="title" class="w3-input"></td></tr>
	<tr><td>내용</td><td><textarea rows="15" name="content" class="w3-input" id="content"></textarea></td></tr>
	<%--
		http://localhost:8080/jspstudy2/board/writerForm
		http://localhost:8080/jspstudy2/board/imgupload 요청 
	--%>
	<script>CKEDITOR.replace("content",{
		filebrowserImageUploadUrl : "imgupload"
	})</script>
	<tr><td>첨부파일</td><td><input type="file" name="file1" class="w3-input"></td></tr>
	<tr><td colspan="2">
		<a href="javascript:inputcheck()" >[게시물등록]</a></td></tr>
</table>
</form>
</body>
</html>