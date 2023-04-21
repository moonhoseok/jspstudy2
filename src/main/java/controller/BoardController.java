package controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.MultipartRequest;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.Board;
import model.BoardMybatisDao;
import model.Comment;
import model.CommentDao;

@WebServlet(urlPatterns= {"/board/*"},
initParams= {@WebInitParam(name="view",value="/view/")})

public class BoardController extends MskimRequestMapping{
	private BoardMybatisDao dao = new BoardMybatisDao();
	private CommentDao cdao = new CommentDao();
	
	@RequestMapping("writeForm")
	public String writeForm(HttpServletRequest request, 
							HttpServletResponse response) {
		String boardid = (String)request.getSession().getAttribute("boardid");
		if(boardid == null) {
			boardid = "1";
		}
		String login = (String)request.getSession().getAttribute("login");
		if(boardid.equals("1")) {
			if(login == null || !login.equals("admin")) {
				request.setAttribute("msg", "관리자만 공지사항 글쓰기 가능합니다.");
				request.setAttribute("url", 
						request.getContextPath()+"/board/list?boardid="+boardid);
								//getContextPath() 절대경로 =>/board/list?boardid="+boardid 가능
				return "alert";
			}
		}
		return "board/writeForm";
	}
	
	@RequestMapping("write")
	public String write(HttpServletRequest request,  
							HttpServletResponse response) {
		// request 객체 사용불가 => MultipartRequest객체 생성
		String path = request.getServletContext().getRealPath("/")+"/upload/";
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request,path,10*1024*1024,"utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//파라미터 Board객체에 저장
		Board b = new Board();
		b.setWriter(multi.getParameter("writer"));
		b.setPass(multi.getParameter("pass"));
		b.setTitle(multi.getParameter("title"));
		b.setContent(multi.getParameter("content"));
		b.setFile1(multi.getFilesystemName("file1"));
		String boardid = (String)request.getSession().getAttribute("boardid");
		if(boardid==null) boardid="1";
		   b.setBoardid(boardid);
		   if(b.getFile1()==null) b.setFile1("");
		   int num = dao.maxnum(); //등록된 게시글의 최대 num값
		   b.setNum(++num); 
		   b.setGrp(num);
		   String msg = "게시물 등록 실패";
		   String url = "writeForm";
		   if(dao.insert(b)) { 
			   return "redirect:list?boardid="+boardid;
		   }
		   //게시물 등록 실패
		   request.setAttribute("msg", msg);
		   request.setAttribute("url", url);
		   return "alert";
		}
	
	/*
	 * 	1. 한페이지당 10건의 게시물을 출력하기
			pageNum 파라미터값을 저장=> 없는 경우는 1로 설정하기
		2. 최근 등록된 게시물이 가장 위에 배치함
		3. DB에서 해당 페이지에 출력될 내용을 조회하여 화면에 출력
			게시물을 출력부분
			페이지 구분 출력부분
		4. 페이지별 게시물번호 출력하기(boardnum)
		5. 첨부파일이 있는경우 @표시
		6. 오늘 등록한 게시물의 등록일 형식 변경하기
			HH:mm:ss
			이전일자 등록한 게시물의 등록일 형식변경하기
			yyyy-MM-dd HH-mm
	
	 */
	@RequestMapping("list")
	public String list (HttpServletRequest request,  
			HttpServletResponse response ) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if(request.getParameter("boardid") != null){
	 		//session에 게시판 종류 정보 등록
	 		request.getSession().setAttribute("boardid",
	 				request.getParameter("boardid"));
	 		request.getSession().setAttribute("pageNum", "1"); //현재페이지번호 
	 	}
	 	String boardid = (String)request.getSession().getAttribute("boardid");
	 	if(boardid == null)boardid = "1";
	 	int pageNum =1;
	 	try{
	 		pageNum = Integer.parseInt(request.getParameter("pageNum"));
	 	}catch (NumberFormatException e){}
	 	String column = request.getParameter("column");
	 	String find = request.getParameter("find");
	 	/*
	 	 * column,find 파라미터중 한개만 존재하는 경우 두개의 파라미터값은
	 	 * 없는상태로 설정
	 	 */
	 	if(column==null || column.trim().equals("")) {
	 		column = null;
	 		find = null;
	 	}
	 	if(find==null || find.trim().equals("")) {
	 		column = null;
	 		find = null;
	 	}
	 	int limit = 10; //한페이지에 보여질 게시글 건수
	 	// boardcount : 게시물 종류별 게시물 등록건수
	 	
	 	int boardcount = dao.boardCount(boardid,column,find); //전체 게시물등록 건수리턴
	 	// list : 현재 페이지에 보여질 게시글 목록
	 	List<Board> list = dao.list(boardid,pageNum,limit,column,find);
	 	/*
	 		maxpage : 필요한 페이지 갯수
	 		게시물건수  필요한 페이지
	 			3        1
	 				3.0/10 => 0.3+0.95=>(int)1.25 => 1
	 			13       2
	 				13.0/10 => 1.3+0.95=>(int)2.25 => 2
	 			501      51
	 				501.0/10 => 50.1+0.95=> (int)51.05 => 51
	 			
	 	*/
	 	int maxpage = (int)((double)boardcount/limit +0.95);
	 	/*
	 		startpage : 화면에 출력될 시작 페이지
	 		현재 페이지			시작페이지
	 			1				1
	 				1/10.0 => 0.1 +0.9 => (int)1.0 -1 => 0*10 +1 => 1
	 			10				1
	 				10/10.0 => 1.0 +0.9 => (int)1.9 -1 => 0*10 +1 => 1
	 			11				11
	 				11/10.0 => 1.1 +0.9 => (int)2.0 -1 => 1*10 +1 => 11
	 			505				501
	 				505/10.0 => 50.5 +0.9 => (int)51.4 -1 => 50 *10 +1 => 501 
	 	*/
	 	int startpage = ((int)(pageNum/10.0 + 0.9)-1)*10+1;
	 	/*
	 		endpage : 화면에 출력할 마지막 페이지 번호 한 화면에 10개의 페이지를 보여줌
	 	*/
	 	int endpage = startpage +9;
	 	if(endpage > maxpage) endpage = maxpage;
	 	//boardName : 게시판 이름 화면에 출력
	 	String boardName = "공지사항";
	 	switch(boardid){
	 		case "2" :
	 			boardName = "자유게시판";break;
	 		case "3" :
	 			boardName = "QnA"; break; 
	 	}
	 	//가짜 번호
	 	int boardnum = boardcount - (pageNum - 1) * limit; 
	 	request.setAttribute("boardName", boardName);
	 	request.setAttribute("boardcount", boardcount);
	 	request.setAttribute("boardid", boardid);
	 	request.setAttribute("pageNum", pageNum);
	 	request.setAttribute("boardnum", boardnum);
	 	request.setAttribute("list", list);
	 	request.setAttribute("startpage", startpage);
	 	request.setAttribute("endpage", endpage);
	 	request.setAttribute("maxpage", maxpage);
	 	request.setAttribute("today", new Date());
	 	return "board/list";
	}
/*1. num파라미터 저장.
	   session에서 boardid 조회하기
	2. num값의 게시물을 db에서 조회
		Board b = BoardDao.selectOne(num)
	3. num값의 게시물의 조회수 증가시키기
		void BoardDao.readcntAdd(num)
	4. 조회된 게시물 화면에 출력
*/
	@RequestMapping("info")
	public String info (HttpServletRequest request,  
			HttpServletResponse response ) {
		//1
		int num = Integer.parseInt(request.getParameter("num"));
		String readcnt = request.getParameter("readcnt");
		String boardid = (String)request.getSession().getAttribute("boardid");
		if(boardid ==null) boardid = "1";
		//2
	
		// b : board테이블에 num(조회하고자하는 게시물 번호)에 해당하는 레코드를 저장
		Board b = dao.selectOne(num);
		if(readcnt ==null || !readcnt.equals("f")) {
			dao.readcntAdd(num);
		}
		//3
		request.setAttribute("boardid", boardid);
		request.setAttribute("num", num);
		request.setAttribute("b", b);
		String boardName = "공지사항";
		switch(boardid) {
		case "2" : boardName = "자유게시판"; break;
		case "3" : boardName = "QnA"; break;
		}
		request.setAttribute("boardName", boardName);
		// 댓글목록 화면에 전달
		List<Comment> commlist = cdao.list(num);
		request.setAttribute("commlist", commlist);
		return "board/info";
		
	}
	
	/*	1. 원글의 num을 파라미터 저장 : num 원글의 게시물 번호
  		2. DB에서 num의 게시물 조회하여 원글의 num, grp, grplevel, grpstep, boardid
  	 	정보를 저장
  		3. 입력화면 표시
    */
	
	@RequestMapping("replyForm")
	public String replyForm (HttpServletRequest request,  
			HttpServletResponse response ) {
		int num = Integer.parseInt(request.getParameter("num"));//파라미터값 읽기
		Board board = dao.selectOne(num); // 원글정보
		request.setAttribute("board", board);
		return "board/replyForm";
	}
 /*1. 파라미터 값을 Borad객체에 저장하기
			원글정보 : num, grp, grplevel, grpstep, boardid
			답글정보 : writer, pass, title, content =>등록정보
	2. 같은 grp 값을 사용하는 게시물들의 grpstep값을 1증가하기
		void BoradDao.grpStepAdd(grp, grpstep)
	3. Board 객체를 DB에 insert하기.
		num : maxnum +1
		grp : 원글과 동일.
		grplevel : 원글 +1
		grpstep : 원글 +1
		boardid : 원글과 동일
	4. 등록성공시 list.jsp로 이동
		등록 실패시 : "답변등록시 오류발생" 메시지 출력후, replyForm.jsp로 이동
*/
	@RequestMapping("reply")
	public String reply (HttpServletRequest request,  
			HttpServletResponse response ) throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
	 	Board b = new Board(); // 답글에 등록될 정보 저장
	 	b.setWriter(request.getParameter("writer"));
	 	b.setPass(request.getParameter("pass"));
	 	b.setTitle(request.getParameter("title"));
	 	b.setContent(request.getParameter("content"));
	 	b.setBoardid(request.getParameter("boardid"));
	 	b.setGrp(Integer.parseInt(request.getParameter("grp")));
	 	b.setGrplevel(Integer.parseInt(request.getParameter("grplevel")));
	 	b.setGrpstep(Integer.parseInt(request.getParameter("grpstep")));
	 	//2
	 	
	 	dao.grpStepAdd(b.getGrp(),b.getGrpstep()); // grpstep 변경
	 	//3 답글 추가
	 	int grplevel = b.getGrplevel();
	 	int grpstep = b.getGrpstep();
	 	int num = dao.maxnum();
	 	String msg = "답변등록시 오류발생";
	 	String url = "replyForm?num="+b.getNum();
	 	b.setNum(++num); // 답글 정보
	 	b.setGrplevel(grplevel+1); // 원글grplevel +1 => 답글 데이터
	 	b.setGrpstep(grpstep+1);  // 원글grpstep +1 => 답글 데이터
	 	if(dao.insert(b)){
	 		return "redirect:list?boardid="+b.getBoardid();
	 	}
	 	request.setAttribute("msg", msg);
	 	request.setAttribute("url", url);
	 	
	 	return "alert";
	}

	@RequestMapping("updateForm")
	public String updateForm (HttpServletRequest request,  
			HttpServletResponse response ) throws UnsupportedEncodingException {
		String boardid = (String)request.getSession().getAttribute("boardid");
		if(boardid ==null)boardid ="1";
		String login = (String)request.getSession().getAttribute("login");
		String msg = "관리자만 수정 가능합니다.";
		String url = "list?boardid=1";
		if(boardid.equals("1")) {
			if(login==null|| !login.equals("admin")) {
				request.setAttribute("msg",msg);
				request.setAttribute("url",url);
				return "alert";
			}
		}
		int num = Integer.parseInt(request.getParameter("num"));
		Board b = dao.selectOne(num);
		String boardName = "공지사항";
		switch(boardid) {
		case"2":boardName = "자유게시판"; break;
		case"3":boardName = "QnA"; break;
		}
		request.setAttribute("b", b);
		request.setAttribute("boardName", boardName);
		return "board/updateForm";
	}
/*	1. 파라미터정보들을 Board 객체 저장 = request 객체 사용불가
	2. 비밀번호 검증
		비밀번호 오류 메세지 출력후 updateForm로 이동
	3. 수정
		첨부파일의 변경이 없는 경우 file2 파라미터의 내용을 다시 저장하기
		boolean BoardDao.update(Board)
			수정 성공 : info.jsp 로 이동
			수정 실패 : 수정실패메세지 출력 후 updateForm로 이동	
*/
	
	@RequestMapping("update")
	public String update (HttpServletRequest request,  
			HttpServletResponse response ) throws UnsupportedEncodingException {
		Board b = new Board();
		String path = request.getServletContext().getRealPath("/")+"upload/board/";
		int size = 10*1024*1024;
		File f = new File(path);
		String login = (String)request.getSession().getAttribute("login");
		if(!f.exists())f.mkdirs();
		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request, path, size, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		b.setNum(Integer.parseInt(multi.getParameter("num")));
		b.setWriter(multi.getParameter("writer"));
		b.setPass(multi.getParameter("pass"));
		b.setTitle(multi.getParameter("title"));
		b.setContent(multi.getParameter("content"));
		b.setFile1(multi.getFilesystemName("file1"));
		if(b.getFile1()==null || b.getFile1().equals("")){
			b.setFile1(multi.getParameter("file2"));
		}
		//2. 비번검증
	
		Board dbboard = dao.selectOne(b.getNum());
		String msg = "비밀번호틀림";
		String url = "updateForm?num="+b.getNum();
		if(b.getPass().equals(dbboard.getPass())){
			//3
			if(dao.update(b)){//db 수정
				url = "info?num="+b.getNum();
				return "redirect:"+url;
			}else{
				msg = "게시물 수정 실패";		
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url",url);
		return "alert";
	}
	
	@RequestMapping("deleteForm")
	public String deleteForm (HttpServletRequest request,  
			HttpServletResponse response ) throws UnsupportedEncodingException {
		
		String boardid = (String)request.getSession().getAttribute("boardid");	
		if(boardid == null) boardid ="1";
		String login = (String)request.getSession().getAttribute("login");
		if(boardid.equals("1")){
			if(login==null || !login.equals("admin")){
				request.setAttribute("msg", "관리자만 삭제가능합니다.");
				request.setAttribute("url", "list?boardid=1");
				return "alert";
			}
	  }
		return "board/deleteForm";		
	}
	/*	
	1. num, pass 파라미터를 변수에 저장
	2. 비밀번호 검증
		틀린경우 : 비밀번호 오류 메시지 출력, deleteForm.jsp로 이동
	3. 해당게시물이 공지사항 게시물인 경우 관리자만 삭제가능
	4. 게시물 삭제
		boolean BoardDao.delete(num)
		삭제성공 : list.jsp 로 이동
		삭제실패 : 삭제실패 메시지 출력, info.jsp로이동
		*/
	@RequestMapping("delete")
	public String delete (HttpServletRequest request,  
			HttpServletResponse response ) throws UnsupportedEncodingException {
		int num = Integer.parseInt(request.getParameter("num"));
	 	String pass = request.getParameter("pass");
	 	String login =(String)request.getSession().getAttribute("login");

	 	Board b = dao.selectOne(num);
	 	
	 	String msg = "비밀번호가 틀렸습니다.";
	 	String url = "deleteForm?num="+num;
	 	
	 	if(pass.equals(b.getPass()) ){
	 		if(b.getBoardid().equals("1")&&
	 				(login==null || !login.equals("admin"))){
	 	 		msg = "관리자만 접근 가능합니다.";
	 	 		url = "list?boardid="+b.getBoardid();
	 	 	}else{
	 			if(dao.delete(num)){
	 				msg="삭제성공";
	 				url="list?boardid="+b.getBoardid();
	 				return "redirect:"+url;
				}else{
	 				msg="삭제실패";
	 				url="deleteForm?num="+num;
	 			}
	 	 	}	
	 	}
	 	request.setAttribute("msg", msg);
	 	request.setAttribute("url", url);
		return "alert";
	}
	@RequestMapping("imgupload")
	public String imgupload (HttpServletRequest request,  
			HttpServletResponse response ) {
		String path = request.getServletContext().getRealPath("/")
				+"/upload/imgfile/";
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request,path,10*1024*1024,"utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ckeditor에서 file의 이름이 upload 임
		String fileName = multi.getFilesystemName("upload");
		request.setAttribute("fileName", fileName);
		
		return "ckeditor";
	}
	@RequestMapping("comment")
	public String comment(HttpServletRequest request,  
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// num : 등록한 해당 게시글 번호
		int num = Integer.parseInt(request.getParameter("num"));
		String url = "info?num="+num+"&readcnt=f";
		// 파라미터값 Comment 객체에 저장
		Comment c = new Comment();
		c.setNum(num);
		c.setWriter(request.getParameter("writer"));
		c.setContent(request.getParameter("content"));
		int seq = cdao.maxseq(num); //num에 해당하는 최대 seq 컬럼의 값
		// 나의 번호는 현재 seq보다 1커야함
		c.setSeq(++seq);
		if(cdao.insert(c)) { //comment 테이블에 insert
			return "redirect:"+url;
		}
		request.setAttribute("msg", "답글 등록시 오류발생");
		request.setAttribute("url", url);
		return "alert";
	}
	
	@RequestMapping("commdel")
	public String commdel(HttpServletRequest request,  
			HttpServletResponse response) {
		int num = Integer.parseInt(request.getParameter("num"));
		int seq = Integer.parseInt(request.getParameter("seq"));
		String url = "info?num="+num+"&readcnt=f";
		if(cdao.delete(num,seq)) {
			return "redirect:"+url;
		}
		request.setAttribute("msg", "삭제 실패");
		request.setAttribute("url", url);
		
		return "alert";
	}
}



















