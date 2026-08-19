package sec01.ex01;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
[어노테이션(@)이란?]
- 클래스나 메소드 위에 붙이는 추가 정보 표시이다. (메타데이터라고 부른다)
- 컴파일러나 톰캣 같은 프로그램이 이 표시를 읽고 특정 동작을 수행한다.
- @WebServlet 은 "이 클래스를 서블릿으로 톰캣에 등록하라"는 표시이다.

[매핑 방법 비교 텍스트 모델링]

  방법1. web.xml 방식 (파일 따로 관리)      방법2. @WebServlet 방식 (이 파일)
  +-------------------------------+       +-------------------------------+
  | web.xml 파일                   |       | ThirdServlet.java 파일        |
  |  <servlet> 태그                |       |                               |
  |  <servlet-mapping> 태그        |       |  @WebServlet("/third")        |
  |  두 태그를 이름으로 연결           |       |  public class ThirdServlet   |
  |  (설정이 코드와 떨어져 있음)        |       |  (클래스 위 한 줄이면 끝)         |
  +-------------------------------+       +-------------------------------+
  단점: 서블릿 많아지면 복잡해짐                장점: 간결하고 코드와 매핑이 한곳에
                                          주의: 매핑명이 다른 것과 중복 금지

[@WebServlet("/third") 동작 순서 텍스트 모델링]

  [Client 웹브라우저]                [Tomcat 서버]
  +--------------------+           +----------------------------------------+
  | 주소창 입력:          | (1) 요청   | (2) 등록된 서블릿 중에서                   |
  | localhost:8090     | --------> |     @WebServlet("/third") 표시가         |
  | /pro05/third       |           |     붙은 클래스를 찾음                     |
  +--------------------+           |         |                              |
           ^                       |         v                              |
           |                       | (3) ThirdServlet 객체 실행              |
           |                       |     - 최초 1회: init() 호출             |
           |          (5) 응답      |     - 매 요청 : doGet() 호출            |
           +---------------------- | (4) 응답 데이터 생성 후 브라우저로 전송   |
                                   +----------------------------------------+

  핵심: web.xml 을 열 필요 없이, 클래스 위의 @WebServlet("/third") 한 줄이
        가짜 주소 /third 와 이 클래스를 직접 연결한다.
*/
// 이 한 줄이 web.xml 의 <servlet> + <servlet-mapping> 두 태그를 대신한다.
//브라우저가 /third 로 요청하면 톰캣이 이 클래스를 실행한다.
@WebServlet("/third")
public class ThirdServlet extends HttpServlet {
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("ThirdServlet init 메소드 호출");
	}

	@Override
	public void destroy() {
		System.out.println("ThirdServlet destory 메소드 호출");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ThirdServlet doGet 메소드 호출");
	}

}



