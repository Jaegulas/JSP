package sec04.ex01;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
 gugu.html 화면에서 클라이언트가 입력한 구구단의 단수를 요청해서
 구구단 출력해줘~ 요청하면  받는 GuguTest 서블릿

<form action="http://localhost:8181/pro06/guguTest" method="get">..</form>
*/
@WebServlet("/guguTest")
public class GuguTest extends HttpServlet {

//	http://localhost:8181/pro06/guguTest?dan=2
	@Override
	protected void doGet(HttpServletRequest request, 
						 HttpServletResponse response) throws ServletException, IOException {
	//재료준비	
		//1. 요청받은 데이터 문자 깨짐 방지를 위한 HttpServletRequest객체 메모리에 UTF-8로 인코딩 방식 설정 
		request.setCharacterEncoding("UTF-8");
		
		//2. 요청한 브라우저로 응답할 데이터 종류를 HttpServletResponse객체에 설정하고 응답할 데이터 인코딩 방식을 UTF-8로 설정  
		response.setContentType("text/html; charset=utf-8");
		
		//3. 요청한 브라우저로 응답할 데이터가 흘러가 출력되게 하는 출력스트림 통로 PrintWriter객체 얻기
		PrintWriter out = response.getWriter();
		
	//요청 데이터를 얻어 응답데이터 생성후 브라우저로 응답
		//1. 요청한 데이터 (입력받은 구구단의 단수) 얻기 
		int dan = Integer.parseInt(request.getParameter("dan"));
		
		//2. 요청한 데이터를 사용해 웹브라우저로 응답할 구구단 데이터들을 생성해서 응답(출력)
		out.print("<table border=1  width=800  align=center>");
		
			out.print("<tr bgcolor='#FFFF66' align=center>");
				out.print("<td colspan=2>" + dan + "단 출력</td>");
			out.print("</tr>");
		
		for(int i=1;  i<10;  i++) {		
			out.print("<tr align=center>");
				out.print("<td width=400>" + dan + "*" + i + "</td>");
				out.print("<td width=400>" + (dan * i)  + "</td>");
			out.print("</tr>");
		}
		
		out.print("</table>");
		
	}
	
	
}









