<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%--/jspstudy1/src/main/webapp/view/member/loginForm.jsp --%>
<!DOCTYPE html>
<!-- 주석처리 -->
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>
<form action="login" method="post" name="f" onsubmit="return input_check(this)">
	<table><caption>로그인</caption>
	<tr><th>아이디</th><td><input type="text" name="id"></td></tr>
	<tr><th>비밀번호</th><td><input type="password" name="pass"></td></tr>
	<tr><td colspan="2">
		<input type="submit" value="로그인">
		<input type="button" value="회원가입" onclick="location.href='joinForm'">
		<input type="button" value="아이디찾기" onclick="win_open('idForm')">
		<input type="button" value="비밀번호찾기" onclick="win_open('pwForm')">
		</td></tr>
		
	</table>
</form>
<script type="text/javascript">
	function win_open(page){
		let op = "width=350, height=350, left=50, top=150"
		open(page,"",op)
	}
	function input_check(f){
		if(f.id.value.trim() ==""){
			alert("아이디를 입력하세요");
			f.id.focus();
			return false;
		}
		if(f.pass.value.trim() ==""){
			alert("비밀번호를 입력하세요");
			f.pass.focus();
			return false;
		}
		return true;
		
	}
</script>
</body>
</html>