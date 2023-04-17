package controller;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
		if(dao.insert(b)) {
			Book dbbook =dao.selectOne(b.getWriter());
			request.setAttribute("book", dbbook);
			return "/book/test1";
		}else {
			request.setAttribute("msg","방명록 등록시 오류발생");
			request.setAttribute("url","testForm");
			return "alert";
		}
		
	}
	
	@RequestMapping("testlist")
	public String testlist(HttpServletRequest request ,
			HttpServletResponse response ) {
	    List<Book> list = dao.list();  
	    request.setAttribute("list",list);
        return "book/testlist";// /view/book/testlist.jsp 를 뷰로 요청
	}
}
