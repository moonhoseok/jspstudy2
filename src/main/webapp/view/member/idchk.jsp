
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%--/jspstudy2/src/main/webapp/view/member/idchk.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>중복검사</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container">
<table class="table">
  <tr><th>아이디</th>
	<td>${param.id}</td>
  </tr>
  <tr>
  	<td colspan="2">
    <div id="msg">${msg }</div>
  	</td>
  </tr>
  <tr>
  	<td colspan="2"><button onclick="self.close()"
  				class="btn btn-primary">닫기</button>
  	</td>
  </tr>
</table>
</div>
<script type="text/javascript">
	if(${able}){
		document.quertSelector("#msg").setAttribute("class","able");
	}else{
		opener.document.f.id.value="";
		document.quertSelector("#msg").setAttribute("class","able");
	}
	
</script>
</form>
</body>
</html>







