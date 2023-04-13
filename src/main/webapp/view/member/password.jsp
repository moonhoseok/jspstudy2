
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /jspstudy2/src/main/webapp/view/member/password.jsp
	1. 파라미터저장 (pass, chgpass)
	2. 로그인한 사용자의 비밀번호 변경만 가능.=> 로그인부분 검증
		로그아웃상태 : 로그인하세요 메세지 출력 후 
				   opener창을 loginForm.jsp로 이동. 현재페이지 닫기
	3. 비밀번호검증 : 현재 비밀번호로 비교
		비밀번호오류 : 비밀번호오류 메세지 출력 후 passwordForm.jsp로 이동
	4. 비밀번호일치 : DB에 비밀번호 수정
		boolean MemberDao.updatePass(id, 변경비밀번호 )
		- 수정성공 : 성공메세지 출력 후 로그아웃하고 loginForm.jsp로 이동. 현제페이지 닫기 
				//opener페이지 info.jsp로 이동. 현제페이지닫기
		- 수정실패 : 실패메세지 출력 후 opener페이지 updateForm.jsp로 이동. 현제페이지닫기	
	
--%>

<script>
	alert("${msg}");
	if(${oepner}){
		opener.location.href ="${url}";
		self.close();
	}else{
		location.href = "${url}";
	}
</script>	
