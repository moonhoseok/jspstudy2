<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%--/jspstudy2/src/main/webapp/view/member/mailForm.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메일 작성</title>
<script type="text/javascript">
	function naverinputchk(f){
		if(f.naverid.value==""){
			alert("네이버 아이디를 입력하세요");
			f.naverid.focus();
			return false;
		}
		if(f.naverpw.value ==""){
			alert("비밀번호를 입력하세요")
			f.naverpw.focus();
			return false;
		}
		return true;
	}
</script>
</head>
<body>
<div class="container">
<h2 id="center">메일보내기</h2>
<form action="mailSend" method="post" name="form1"
		onsubmit="return naverinputchk(this)">
	<table class="table" >
		<tr>
			<td>보내는 사람</td>
			<td>본인 네이버 ID : <input type="text" name="naverid" class="form-control">
				본인 비밀번호 : <input type="password" name="naverpw" class="form-control">
			</td>
		</tr>
		<tr>
			<td>받는사람</td>
			<td>
				<input type="text" name="recipient" class="form-control"
				value="<c:forEach items="${list }" var="m">${m.name}&lt;${m.email}&gt;,</c:forEach>">
			</td>
		</tr>
		<tr>
			<td>제목</td>
			<td> <input type="text" name="title" class="form-control"> </td>
		</tr>
		<tr>
			<td>메시지 형식</td>
			<td>
				<select name="mtype" class="form-control">
					<option value="text/html;charset=utf-8">HTML
					<option value="text/plain;charset=utf-8">TEXT
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<textarea rows="10" cols="40" class="form-control" name="content">
				</textarea>
			</td>
		</tr>
		<tr>
			<td colspan="2" id="center">
				<button type="submit" class="btn btn-dark">전송</button>
			</td>	
		</tr>
	</table>
</form>
</div>

</body>
</html>