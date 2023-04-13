<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/jspstudy2/src/main/webapp/view/member/passwordForm.jsp --%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 수정</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<script type="text/javascript">
	function inchk(){
		if(f.pass.value.trim() == "" 
		|| f.chgpass.value.trim() == "" 
		|| f.chgpass2.value.trim() == ""){
			alert("빈칸 입력해주세요")
			return false;
		}
		if(f.chgpass.value != f.chgpass2.value){
			alert("변경비밀번호와 재입력 비밀번호를를 다시 확인하세요")
			f.chgpass2.value = "";
			f.chgpass2.focus();
			return false;
		}
	}
</script>
</head>
<body>
<form action="password" method="post" name="f"
		onsubmit="return inchk(this)"> 
		<%--1. 모든입력등록 2. 변경비밀번호 == 변경비밀번호재입력  --%>
	<table><caption>비밀번호변경</caption>
		<tr><th>현재비밀번호</th>
		<td><input type="password" name="pass"></td></tr>
		<tr><th>변경비밀번호</th>
		<td><input type="password" name="chgpass"></td></tr>
		<tr><th>변경비밀번호재입력</th>
		<td><input type="password" name="chgpass2"></td></tr>
		<tr><td colspan="2">
		<input type="submit" value="비밀번호 변경">
		<input type="reset" value="초기화">
		</td></tr>	
	</table>
</form>
</body>
</html>