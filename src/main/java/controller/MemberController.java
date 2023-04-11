package controller;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		if(mem==null) {
			msg ="아이디를 확인하세요";
			url ="loginForm";
		}else if(!pass.equals(mem.getPass())) {
			msg ="비밀번호가 틀렸어";
			url ="loginForm";
		}else { //정상적인 로그인
			request.getSession().setAttribute("login", id);
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
		String login = (String)request.getSession().getAttribute("login");
		if(login==null) {
			request.setAttribute("msg", "login하세용");
			request.setAttribute("url", "loginForm");
			return "alert"; // /view/alert.jsp 페이지 forward 됨
		}
		return "member/main";
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
	public String info(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String login=(String)request.getSession().getAttribute("login");
		if(login ==null) {
			request.setAttribute("msg", "로그인하세요");
			request.setAttribute("url", "loginForm");
			return "alert";
		}else if(!login.equals("admin")&& !id.equals(login)) {
			request.setAttribute("msg", "본인정보만 조회 가능합니다.");
			request.setAttribute("url", "main");
			return "alert";
		}
		Member mem = dao.selectOne(id);
		request.setAttribute("mem", mem);
		return "member/info";
	}
	
}
	
