package sec04.ex01;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
LoginTest 서블릿(서버페이지) 역할

- test01/login.html 에서 아이디, 비밀번호를 입력하고 로그인 버튼을 눌러 로그인 요청하면
    요청을 받아 처리하는 LoginTest 서블릿 클래스.

- <form> action속성에 설정된 요청 받은 주소 -> http://톰캣서버PCIP:톰캣소프트웨어PORT/컨텍스트/요청할 자원주소
								      -> http://localhost :         8181/pro06/loginTest
*/
@WebServlet("/loginTest2")
public class LoginTest2 extends HttpServlet{

	//init , doPost, destory 메소드 오버라이딩
	
	@Override
	public void init() throws ServletException {
		System.out.println("init 호출");
	}
	
	// 클라이언트가 login.html화면에서 <form>에 의한 post 전송 요청 했을때 호출되는 doPost 메소드 
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//1. test01/login.html에서 입력하여 요청한 아이디, 비밀번호 는 HttpServletRequest객체 메모리에 보관되어 있으므로
		//   얻기 전에 한글데이터가 하나라도 있으면 나중에 얻으면 한글 문자가 모두 꺠져서 얻어 지므로
		//   HttpServetRequest객체 메모리에 저장된 요청한 문자데이터들을 UTF-8방식으로 문자처리 방식 설정
		//요약 : 한글처리 설정
		request.setCharacterEncoding("UTF-8");
		
		//2. test01/login.html에서 입력하여 요청한 아이디, 비밀번호 데이터들을 HttpServletRequest객체 메모리로부터 얻기
		//요약 : 클라이언트가 서블릿으로 요청한 데이터 얻기
		/*
		아이디 : <input type="text" name="user_id"> <br>	
		비밀번호 : <input type="password" name="user_pw"> <br>
		*/
		String id = request.getParameter("user_id"); //"admin" 관리자 아이디  또는  "xxx" 일반사용자 아이디 
		String pw = request.getParameter("user_pw"); //"1234"
		
		//3. 요청한 브라우저로 응답할 데이터종류(MIME-TYPE) 설정, 응답할 문자인코딩방식 UTF-8로 설정 및 응답할 데이터 생성
		
		//3.1. 브라우저로 응답할 데이터 종류(MIME-TYPE) 설정, 응답할 문자인코딩방식 UTF-8로 설정
		//-> HttpServletResponse객체 메모리에 설정!!!!!!
		response.setContentType("text/html; charset=UTF-8");
		
		//3.2. 브라우저로 응답할 데이터가 흘러 가는 출력 스트림 통로 PrintWriter 얻기
		//-> HttpServletResponse객체의 getWriter()메소드를 호출하면 PrintWriter객체를 반환받아 얻을 수 있다.
		PrintWriter out = response.getWriter();
		
		//3.3. 응답 할 데이터 생성 후 브라우저로 조건에 맞게 응답(출력)
		//조건 : 아이디 입력 했느냐?
		if(id != null && (id.length() != 0)) {
			
			//조건 : 입력한 아이디가 "admin" 관리자 아이디 계정과 같다면?
			if(id.equals("admin")) {
				//관리자가 로그인 한 회원관리 화면을 만들어 브라우저로 응답 
				out.print("<html>");
					out.print("<body>");
						out.print("<font size='12'>admin 관리자로 로그인 하셨습니다!</font><br>");
						out.print("<button type='button'>회원정보 수정</button>");
						out.print("<button type='button'>회원정보 삭제</button>");
					out.print("</body>");
				out.print("</html>");				
			
			//조건 : 입력한 아이디가 일반 계정 아이디와 같다면?
			}else {				
				out.print("<html>");
					out.print("<body>");
						out.print(id + "님!! 로그인 하셨습니다.");
					out.print("</body>");
				out.print("</html>");
			}
			
		}else {//조건 : 아이디가 입력되지 않았으면?
			
			out.print("<html>");
				out.print("<body>");
					out.print("아이디가 입력되지 않았어요! 아이디 입력 해 주세요<br>");
					out.print("<a href='http://localhost:8181/pro06/test01/login.html'>로그인 하러가기</a>");
				out.print("</body>");
			out.print("</html>");		
		}		
	}

	@Override
	public void destroy() {
		System.out.println("distory 호출");
	}
}









