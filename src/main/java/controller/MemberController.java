package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import gdu.mskim.MSLogin;
import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.Member;
import model.MemberMybatisDao;
// /member/* =>http://localhost:8080/jspstudy2/member/이후의 어떤 요청이
//				들어와도 MemberController 서블릿이 호출됨
@WebServlet(urlPatterns= {"/member/*"},
		initParams= {@WebInitParam(name="view",value="/view/")})
//http://localhost:8080/jspstudy2/member/loginForm
public class MemberController extends MskimRequestMapping{
	private MemberMybatisDao dao = new MemberMybatisDao();
	
	//로그인 검증. id 파라미터와 로그인정보 검증
	public String loginIdCheck(HttpServletRequest request, 
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String id = request.getParameter("id");
		String login=(String)request.getSession().getAttribute("login");
		if(login ==null) {
			request.setAttribute("msg", "로그인하세요1");
			request.setAttribute("url", "loginForm");
			return "alert";
		}else if(!login.equals("admin")&& !id.equals(login)) {
			request.setAttribute("msg", "본인만 거래 가능합니다.");
			request.setAttribute("url", "main");
			return "alert";
		}
		return null;
	}
	
	public String loginAdminCheck(HttpServletRequest request, 
			HttpServletResponse response) {
		String login=(String)request.getSession().getAttribute("login");
		if(login ==null) {
			request.setAttribute("msg", "로그인하세요");
			request.setAttribute("url", "loginForm");
			return "alert";
		}else if(!login.equals("admin")) {
			request.setAttribute("msg", "관리자만 가능합니다.");
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
			request.setAttribute("msg", "관리자는 탈퇴 불가능");
			request.setAttribute("url", "list");
			return "alert";
		}
		//MemberDao dao = new MemberDao();
		Member dbmem = dao.selectOne(login); //로그인된 사용자의 비밀번호로 검증
		if(!pass.equals(dbmem.getPass())) {
			request.setAttribute("msg", "비밀번호가 틀립니다.");
			request.setAttribute("url", "deleteForm?id="+id);
			return "alert";
		}//비밀번호 일치 => 고객정보삭제
		if(dao.delete(id)) { //삭제성공
			msg=id+"고객님 탈퇴성공";
			if(login.equals("admin")){//관리자
				url="list";
			}else {//일반사용자
				request.getSession().invalidate(); //세션변경 로그아웃
				url="loginForm";
			}
		}else { // 삭제 실패 
			msg=id+"고객님 탈퇴 실패";
			if(login.equals("admin")) url="list";
			else					 url="info?id="+id;
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return "alert";	
	}
	
/*
 *     	1. 관리자만 사용가능 페이지 => 검증
    		- 로그아웃상태 : 로그인이 필요합니다. 메세지 출력 후 loginForm.jsp로 이동
    		- 로그인 상태 : 일반사용자 로그인시 "관리자만 가능합니다." 메세지 출력 후
    					main.jsp 페이지로 이동
    	2. DB에서 모든 회원 정보를 조회. List<Member> 객체로 리턴
    		List<Member> MemberDao.list()
    	3. List객체를 화면에 전달

 */
		@RequestMapping("list")
		@MSLogin("loginAdminCheck")
		public String list(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			List<Member> list = dao.list();
			request.setAttribute("list", list);
			return "member/list";
		}
		/*
		 * 	1. 이미지파일 업로드, request객체 사용불가
			이미지파일 업로드의 위치 : 현재 URL/picture 폴더로 설정
			2. opener화면에 결과 전달 => javascript
			3. 현재화면 close() => javascript
		 */
		@RequestMapping("picture")
		public String picture(HttpServletRequest request,
				HttpServletResponse response) {
			//request.getServletContext() : application 객체
			//request.getServletContext().getRealPath("/") : 실제 웹어플리케이션 경로
			String path = request.getServletContext().getRealPath("/")
													+"/picture/";
			String fname = null;
			File f = new File(path);
			if(!f.exists()){ f.mkdir(); } // 업로드 폴더가 없는 경우 폴더생성
			
			MultipartRequest multi = null;
			try {
				//request	: 요청객체. 파라미터, 파일의내용, 파일이름
				//path		: 업로드된 파일이 저장될 폴더
				//10*1024*1024 : 업로드 파일의 최대 크기 바이트수 10MB최대크기
				//utf-8 : 인코딩
				multi = new MultipartRequest
								(request,path,10*1024*1024,"utf-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			fname = multi.getFilesystemName("picture"); //업로드된 파일의 이름
			request.setAttribute("fname", fname);
			return "member/picture"; 
		}
		//중복체크
		@RequestMapping("idchk")
		public String idchk(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String id = request.getParameter("id");
			Member mem = dao.selectOne(id);
			String msg=null;
			boolean able = true;
			if(mem == null) {
				msg="사용가능한 아이디입니다";
			}else {
				msg="사용중인 아이디입니다.";
				able = false;
			}
			request.setAttribute("able",able);
			request.setAttribute("msg",msg);
			return "member/idchk";
		}
		
		@RequestMapping("id")
		public String id(HttpServletRequest request, 
				HttpServletResponse response) throws Exception {
			request.setCharacterEncoding("utf-8");
			String tel = request.getParameter("tel");
			String email = request.getParameter("email");
			String id = dao.idSearch(email, tel);
			if(id != null) {
				String showId = id.substring(0,id.length()-2);
				request.setAttribute("id", showId);
				return"member/id";
			}else {
				request.setAttribute("msg", "아이디를 찾을 수 없습니다.");
				request.setAttribute("url", "idForm");
				return "alert";
			}

		}
		
		@RequestMapping("pw")
		public String pw(HttpServletRequest request, 
				HttpServletResponse response) throws Exception {
			request.setCharacterEncoding("utf-8");
			String tel = request.getParameter("tel");
			String email = request.getParameter("email");
			String id = request.getParameter("id");
			String pw = dao.pwSearch(id,email, tel);
			if(pw != null) {
				String showPw = pw.substring(0,pw.length()-2);
				request.setAttribute("pw", "회원 비밀번호 :" +showPw);
				return "member/pw";
			}else {
				request.setAttribute("msg", "비밀번호를 찾을 수 없습니다.");
				request.setAttribute("url", "pwForm");
				return "alert";
			}
		}
		//비밀번호변경
		/*
		 *  1. 파라미터저장 (pass, chgpass)
			
			2. 비밀번호검증 : 현재 비밀번호로 비교
						비밀번호오류 : 비밀번호오류 메세지 출력 후 passwordForm.jsp로 이동
			3. 비밀번호일치 : DB에 비밀번호 수정
					boolean MemberDao.updatePass(id, 변경비밀번호 )
					- 수정성공 : 성공메세지 출력 후 로그아웃하고 loginForm.jsp로 이동. 현제페이지 닫기 
								//opener페이지 info.jsp로 이동. 현제페이지닫기
					- 수정실패 : 실패메세지 출력 후 opener페이지 updateForm.jsp로 이동. 현제페이지닫기	
		 */
		@RequestMapping("password")
		public String password(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String pass = request.getParameter("pass");
			String chgpass = request.getParameter("chgpass");
			String login = (String)request.getSession().getAttribute("login");
			Member mem = dao.selectOne(login);
			boolean oepner = true;
			String msg = null;
			String url = null;
			if(login==null) {
				msg="로그인하세요";
				url="loginForm";
				oepner = true;
			}else {
				if(mem.getPass().equals(pass)) {//등록비밀번호 입력비밀번호 일치
					if(dao.updatePass(login, chgpass)) { //비번 수정
						//msg = "비밀번호가 변경되었습니다";
						//url = "info.jsp?id=" + login;
						msg = "비밀번호가 변경되었습니다 \\n  다시로그인 하세요";
						request.getSession().invalidate(); //세션변경 로그아웃
						url = "loginForm";
						oepner = true;
					}else { //변경실패
						msg = "비밀번호가 변경시 오류발생";
						url = "updateForm?id=" + login;
						oepner = true;
					}
				}else { //비번 오류
					msg = "비밀번호 오류입니다.";
				    url = "passwordForm";
					oepner = false;
				}
			}
			request.setAttribute("msg", msg);
			request.setAttribute("url", url);
			request.setAttribute("oepner", oepner);
			return "member/password";
			
		}
		@RequestMapping("passworeForm")
		@MSLogin("loginIdCheck")
		public String passworeForm(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			
			return "member/password";
		}
		/*
		 * 네이버 smtp 서버 이용하기 샌드메일트랜스퍼프로토콜
		 * 1. 네이버 2단계 로그인 해제
		 * 2. SMTP 서버 사용 설정 => 네이버메일
		 * 3. lms에 mail.properties 다운받기
		 * 4. mvnrepository.com 접속
		 * 5. mail 검색=> javax.mail => mail-1.4.7.jar 다운받기
		 * 		activation검색 => javax.activation » activation 선택
		 * 			=> activation-1.1.1.jar 다운
		 * WEB-INF/lib 2개의 jar파일 붙여넣기
		 */
		@RequestMapping("mailForm")   
		@MSLogin("loginAdminCheck")
		public String mailForm(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			String[] ids = request.getParameterValues("box");
			List<Member> list = dao.selectEmail(ids);
			request.setAttribute("list", list);
			return "member/mailForm";
		}
		
		@RequestMapping("mailSend")   
		@MSLogin("loginAdminCheck")
		public String mailSend(HttpServletRequest request,
				HttpServletResponse response) throws UnsupportedEncodingException {
			request.setCharacterEncoding("utf-8");
			String sender = request.getParameter("naverid");
			String pass = request.getParameter("naverpw");
			String recipient = request.getParameter("recipient");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String mtype = request.getParameter("mtype");
			/*
			 * Properties 클래스 : Hashtable(Map구현 클래스)의 하위클래스
			 * 				(key(String),value(String))
			 * 				=> 제네릭표현안함<>
			 * prop : 메일 전송을 위한 환경설정 객체
			 */
			Properties prop = new Properties();
			try {
				FileInputStream fis = 
					new FileInputStream("C:/html/workspace/jspstudy2/mail.properties");
				prop.load(fis); // key = value 으로 읽어서 Map객체로 저장
				prop.put("mail.stmp.user", sender);
				System.out.println(prop);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//메일 전송 전에 인증객체 생성=> 네이버인증
			MyAuthenticator auth = new MyAuthenticator(sender,pass); //인증객체
			// 메일전송을 위한 연결 객체
			Session session = Session.getInstance(prop,auth);
			//Message 객체 준비 : 메일의 내용을 저장하는 객체
			MimeMessage msg = new MimeMessage(session);
			List<InternetAddress> addrs = new ArrayList<InternetAddress>();
			try {
				String[] emails = recipient.split(",");
				for(String email : emails) {
					try {
						//수신인 이름에 한글을 꺠지지 않도록 주소값 변경
						addrs.add(new InternetAddress
								(new String(email.getBytes("utf-8"),"8859_1")));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				InternetAddress[] address = new InternetAddress[emails.length];
				for(int i=0; i<addrs.size(); i++) {
					address[i] = addrs.get(i);
				}
				// 보내는사람 이메일 주소 설정
				InternetAddress from = new InternetAddress(sender+"@naver.com");
				//보내는 사람 이메일 주소
				msg.setFrom(from);  
				//받는사람 이메일 주소 설정
				msg.setRecipients(Message.RecipientType.TO, address);
				// 메일 제목
				msg.setSubject(title);
				// 전송 시간
				msg.setSentDate(new Date());
				// 메일 내용
				msg.setText(content);
				//
				MimeMultipart multipart = new MimeMultipart();
				//
				MimeBodyPart body = new MimeBodyPart();
				body.setContent(content, mtype);
				multipart.addBodyPart(body);
				msg.setContent(multipart);
				Transport.send(msg); // 메일전송
				//메일전송
			} catch (MessagingException me) {
				System.out.println(me.getMessage());
				me.printStackTrace();
			}
			return "redirect:list";
		}
		
		public final class MyAuthenticator extends Authenticator{
			private String id;
			private String pw;
			public MyAuthenticator(String id, String pw) {
				this.id = id;
				this.pw = pw;
			}
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(id,pw);
			}
		}
}	


 

























