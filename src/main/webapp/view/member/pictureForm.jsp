<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%--/jspstudy2/src/main/webapp/view/member/pictureForm.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원사진 등록</title>
</head>
<body>
<h3>사진업로드</h3>
<table>
	<tr><td><img id="preview" src=""></td></tr>
	<tr><td>
	<form action="picture" method="post" enctype="multipart/form-data">
		<input type="file" name="picture" id="imagefile" accept="img/*">
		<input type="submit" value="사진등록">
	</form>
	</td></tr>
</table>
<script type="text/javascript">
	//imagefile : <input type="file" name="picture" id="imagefile"..> 태그객체
	let imagefile = document.querySelector('#imagefile');
	//preview : <img id="preview" src=""> 태그객체
	let preview = document.querySelector('#preview');
	//imagefile을 새로운 파일 선택시 발생되는 이벤트 등록
	imagefile.addEventListener('change',function(e){ //이벤트 핸들러
		let get_file = e.target.files; //선택된 파일
		let reader = new FileReader(); //파일을 읽기 위한 스트림
		reader.onload = (function (Img){ // Img <= preview
			return function(e){
				Img.src = e.target.result; // 선택된 파일이름 
			}
		})(preview); // 매개변수
		//get_file : 선택된 파일이 존재하면
		//get_file[0] : 첫번째 선택된 파일
		//readAsDataURL : 파일 읽기 => onload이벤트 발생
		if(get_file){ reader.readAsDataURL(get_file[0]);}
	});
</script>
</body>
</html>