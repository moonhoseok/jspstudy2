package controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.MultipartRequest;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.Board;
import model.BoardDao;

@WebServlet(urlPatterns= {"/board/*"},
initParams= {@WebInitParam(name="view",value="/view/")})

public class BoardController extends MskimRequestMapping{
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
		String path = request.getServletContext().getRealPath("/")+"view/board/file";
		File f = new File(path);
		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request,path,10*1024*1024,"utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Board b = new Board();
		b.setWriter(multi.getParameter("write"));
		b.setPass(multi.getParameter("pass"));
		b.setTitle(multi.getParameter("title"));
		b.setContent(multi.getParameter("content"));
		b.setFile1(multi.getParameter("file1"));
		String boardid = (String)request.getSession().getAttribute("boardid");
		if(boardid ==null) {
			boardid = "1";
		}
		b.setBoardid(boardid);
		BoardDao dao = new BoardDao();
		int num =dao.maxnum();
		b.setNum(++num);
		b.setGrp(num);
		if(dao.insert(b)) {
			try {
				response.sendRedirect("list");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			request.setAttribute("msg", "게시물등록 실패");
			request.setAttribute("url", "writeForm");
			return "alert";
		}
		
		return "board/list";
	}
}


















