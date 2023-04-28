package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;

@WebServlet(urlPatterns= {"/ajax/*"},
initParams= {@WebInitParam(name="view",value="/view/")})

public class Ajaxcontrolloer extends MskimRequestMapping{
	@RequestMapping("select")
	public String select (HttpServletRequest request, 
			HttpServletResponse response) {
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/")
				+"file/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		String si = request.getParameter("si");
		String gu = request.getParameter("gu");
		if(si== null && gu ==null) {
			try {
				while((data=fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3)set.add(arr[0].trim());
				}
			} catch (Exception e) {
				e.printStackTrace();	
			}
		}
		request.setAttribute("list", new ArrayList<String>(set));
		request.setAttribute("len", set.size());
		return "ajax/select";
	}
	
}
