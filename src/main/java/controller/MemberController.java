package controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gdu.mskim.MSLogin;
import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.Member;
import model.MemberDao;
// /member/* =>http://localhost:8080/jspstudy2/member/이후의 어떤 요청이
//				들어와도 MemberController 서블릿이 호출됨
@WebServlet(urlPatterns= {"/member/*"},
		initParams= {@WebInitParam(name="view",value="/view/")})
//http://localhost:8080/jspstudy2/member/loginForm
public class MemberController extends MskimRequestMapping{
	private MemberDao dao = new MemberDao();
	
	//로그인 검증. id 파라미터와 로그인정보 검증
	public String loginIdCheck(HttpServletRequest request, 
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String login=(String)request.getSession().getAttribute("login");
		if(login ==null) {
			request.setAttribute("msg", "로그인하세요");
			request.setAttribute("url", "loginForm");
			return "alert";
		}else if(!login.equals("admin")&& !id.equals(login)) {
			request.setAttribute("msg", "본인만 거래 가능합니다.");
			request.setAttribute("url", "main");
			return "alert";
		}
		return null;
	}
	@RequestMapping("loginForm")
	public String loginForm(HttpServletRequest request, 
			HttpServletResponse response) {
		return "member/loginForm"; //view 선택
		//	/view/member/loginForm.jsp => view 이름
	}
	
	@RequestMapping("login")
	public String login(HttpServletRequest request, 
			HttpServletResponse response) {
		//1. 파라미터 변수 저장하기
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		//2. 비밀번호 검증
		Member mem = dao.selectOne(id);
		String msg = null;
		String url = null;
		if(mem==null) { // id 없음.
			msg ="아이디를 확인하세요";
			url ="loginForm";
		}else if(!pass.equals(mem.getPass())) { // 아이디존재. 비밀번호 검증
			msg ="비밀번호가 틀렸어";
			url ="loginForm";
		}else { //정상적인 로그인
			request.getSession().setAttribute("login", id); //로그인정보 등록
			msg = "반갑습니다. "+ mem.getName()+"님";
			url = "main";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return "alert"; // view 이름 : /view/alert.jsp
	}
	
	@RequestMapping("main")
	public String main(HttpServletRequest request, 
			HttpServletResponse response) {
		//request.getSession() : session 객체
		String login = (String)request.getSession().getAttribute("login");
		if(login==null) { //로그아웃 상태
			request.setAttribute("msg", "login하세용");
			request.setAttribute("url", "loginForm");
			return "alert"; // /view/alert.jsp 페이지 forward 됨
		}
		return "member/main"; // /view/member/main.jsp.페이지로 forward됨
	}
	/*
	 * 1. session에 등록된 로그인 정보 제거
	 * 2. loginForm으로 페이지 이동
	 */
	@RequestMapping("logout")
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		request.getSession().invalidate();
		return "redirect:loginForm"; //
	}
	/*
	 * 1. id파라미터값을 조회.
    	2. 로그인 상태 검증
    	  - 로그아웃상태 : '로그인하세요' 메세지 출력 후 loginForm.jsp페이지 호출
    	  - 로그인 상태 
    	    - 다른 id 조회시(관리자 제외) : 
    	  	'내 정보 조회만 가능합니다.' 메세지 출력 후 main.jsp페이지 호출
    	3. DB에서 id에 해당하는 데이터 조회
    	4. view로 데이터를 전송 => request 객체의 속성등록
	 */
	@RequestMapping("info")
	@MSLogin("loginIdCheck") 
	public String info(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		Member mem = dao.selectOne(id);
		request.setAttribute("mem", mem);
		return "member/info";
	}
	
	/*
	1. 파라미터 정보를 Member객체에 저장
	2. Member객체를 이용하여 DB에 insert(member 테이블) 저장
	3. 가입성공 : 성공메세지 출력 후 loginForm 페이지 이동
	   가입실패 : 실패메세지 출력 후 joinForm 페이지 이동
	*/
	
	@RequestMapping("join")
	public String join(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Member mem = new Member();
		mem.setId(request.getParameter("id"));
		mem.setPass(request.getParameter("pass"));
		mem.setName(request.getParameter("name"));
		mem.setGender(Integer.parseInt(request.getParameter("gender")));
		mem.setTel(request.getParameter("tel"));
		mem.setEmail(request.getParameter("email"));
		mem.setPicture(request.getParameter("picture"));
		if(dao.insert(mem)) {
			request.setAttribute("msg", mem.getName()+"님 회원가입 되었습니다.");
			request.setAttribute("url", "loginForm");
		}else {
			request.setAttribute("msg", "회원가입 실패");
			request.setAttribute("url", "joinForm");
		}
		return"alert";
	}
	
	/*
	 * 1. id파라미터값을 조회.
    	2. 로그인 상태 검증
    	  - 로그아웃상태 : '로그인하세요' 메세지 출력 후 loginForm.jsp페이지 호출
    	  - 로그인 상태 
    	    - 다른 id 수정(관리자 제외) : 
    	  	'내 정보만 수정 가능합니다.' 메세지 출력 후 main.jsp페이지 호출
    	3. DB에서 id에 해당하는 데이터 수정
    	4. 수정된 내용을 화면에 출력하기=> 이전데이터를 화면출력. 수정전화면 출력
	 */
	@RequestMapping("updateForm")
	@MSLogin("loginIdCheck")
	public String updateForm(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		
		Member mem = dao.selectOne(id);
		request.setAttribute("mem", mem);
		return "member/updateForm";
	}
	
	/*
	 * 	1. 모든파라미터를 Member 객체에 저장하기
   	2. 입력된 비밀번호와 DB에 저장된 비밀번 비교. 
   		관리자인 경우 관리자비밀번호로 비교 
   		불일치 : '비밀번호 오류' 메세지 출력 후 updateForm페이지로 이동
   	3. Member객체의 내용으로 DB를 수정하기
   		- 성공 : 회원정보 수정완료 메세지 출력 후 info페이지로 이동
   		- 실패 : 회원정보 수정실패 메세지 출력 후 updateForm로 페이지 이동
	 */
	@RequestMapping("update")
	public String update(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Member mem = new Member();
		mem.setId(request.getParameter("id"));
		mem.setPass(request.getParameter("pass"));
		mem.setName(request.getParameter("name"));
		mem.setGender(Integer.parseInt(request.getParameter("gender")));
		mem.setTel(request.getParameter("tel"));
		mem.setEmail(request.getParameter("email"));
		mem.setPicture(request.getParameter("picture"));
		
		String login = (String)request.getSession().getAttribute("login");
		Member dbmem = dao.selectOne(login);
		//System.out.println(dbmem);
		String msg = "비밀번호가 틀렸습니다.";
		String url = "updateForm?id="+mem.getId();
		if(mem.getPass().equals(dbmem.getPass())) {
			if(dao.update(mem)) {
				msg = "회원정보 수정완료";
				url = "info?id="+mem.getId();
			}else {
				msg = "회원정보 수정실패";
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
				return "alert";
	}
	/*
	 * 	1. id파라미터 저장
    	2. 로그인 정보 검즘
    		- 로그아웃 상태 : 로그인하세요 메세지 출력. loginForm로 페이지 이동
    		- 관리자제외. 다른사용자 탈퇴 불가
    			본인만 탈퇴가능합니다. 메세지 출력 후.main로 이동
    	3. deleteForm 호출
	 */
	@RequestMapping("deleteForm")
	@MSLogin("loginIdCheck")
	public String deleteForm(HttpServletRequest request,
			HttpServletResponse response) {
		
		return "member/deleteForm";
	}
	
/*
	1. 파라미터 정보 저장
    	2. 로그인정보 검증 // @MSLogin 어노테이션 처리
    		- 로그아웃상태 : 로그인하세요 메세지 출력 후 loginForm로 이동
    		- 다른사람탈퇴(관리자제외) : 본인만 탈퇴 가능 메세지 출력 후 main로 이동
    	3. 관리자 탈퇴는 불가능
    		-관리자정보 탈퇴 : 관리자는 탈퇴불가. list로 이동
    	4. 비밀번호 검증
    		로그인 정보로 비밀번호 검증
    		- 비밀번호 불일치 : 비밀번호 오류 메세지 출력 후 deleteForm로 페이지 이동
    	5. DB에서 delete실행
    		- boolean MemberDao.delete(id)
    		- 탈퇴성공 : 
    			- 일반사용자 : 로그아웃 실행. 탈퇴완료 메세지 출력 후 loginForm로 이동
    			- 관리자	: 탈퇴완료 메세지 출력 후 list로 이동
    		- 탈퇴실패 : 
    			- 일반사용자 : 탈퇴실패 메세지 출력 후 info로 이동
    			- 관리자	: 탈퇴실패 메세지 출력 후 list로 이동 
 */
	@RequestMapping("delete")
	@MSLogin("loginIdCheck")
	public String delete(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		//String login = (String)request.getSession().getAttribute("login");
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String login = (String)request.getSession().getAttribute("login");
		String msg = null;
		String url = null;
		if(id.equals("admin")) {
			request.setAttribute("msg", msg);
			request.setAttribute("url", url);
			return "alert";
		}
		Member dbmem = dao.selectOne(login);
		if(!pass.equals(dbmem.getPass())) {
			
		}
			
		
		return "member/deleteForm";
	}
}	
















