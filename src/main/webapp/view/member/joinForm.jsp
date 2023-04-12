<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%--/jspstudy2/src/main/webapp/view/member/joinForm.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script type="text/javascript">
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
		if(f.name.value.trim() ==""){
			alert("이름을 입력하세요");
			f.name.focus();
			return false;
		}
		return true;
		
	}
	function win_upload(){
		var op = "width=500, height=500, left=50, top=50";
		open("pictureForm","",op);
	}
	function win_open(page){
		let op = "width=350, height=350, left=50, top=150"
		open(page,"",op)
	}

</script>
</head>
<body>
<form action="join" method="post" name="f" onsubmit="return input_check(this)">
<input type="hidden" name="picture" value="">
<div class="container">
	<h2 id="center">회원가입</h2>
	<div class="row">
		<div class="col-3 bg-light">
			<img src="" width="100" height="120" id="pic">
		</div>
		<div class="col-9">
			<div class="form-group">
				<label for="id">아이디 :</label><input type="text"
					class="form-control" name="id">
				<label for="pwd">비밀번호 :</label><input type="password"
					class="form-control" name="pass">
				<label for="name">이름 :</label><input type="text"
					class="form-control" name="name">
				<label for="gender">성별 :</label>
				<label class="radio-inline"></label>
					<input type="radio" name="gender" checked value="1">남	
				<label class="radio-inline"></label>
					<input type="radio" name="gender" checked value="2">여	
			</div>
		</div>
	</div>
		<div class="form-group">
			<label for="tel">전화번호</label>
				<input type="text" class="form-control" name="tel" id="tel">
		</div>
		<div class="form-group">
			<label for="email">이메일</label>
				<input type="text" class="form-control" name="email" id="email">
		</div>
		<div id="center" style="padding: 3px;">
			<button type="submit" class="btn btn-dark">회원가입</button>
			<button type="reset" class="btn btn-dark">다시작성</button>
		</div>
</div>
</form>
</body>
</html>