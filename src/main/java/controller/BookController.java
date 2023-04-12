package controller;
import java.io.UnsupportedEncodingException;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.Book;
import model.BookDao;

@WebServlet(urlPatterns= {"/book/*"},
		initParams= {@WebInitParam(name="view",value="/view/")})

public class BookController extends MskimRequestMapping {
	private BookDao dao = new BookDao();

	@RequestMapping("testForm")
	public String testForm(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		return "/book/testForm";
	}
	
	@RequestMapping("test1")
	public String test1(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Book b = new Book();
		b.setWriter(request.getParameter("writer"));
		b.setTitle(request.getParameter("title"));
		b.setContent(request.getParameter("content"));
		dao.insert(b);
		request.setAttribute("book", b);
		return "/book/test1";
	}
}
