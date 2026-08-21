package sec04.ex04;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
   MemberServlet 첫번쨰 서블릿 클래스 역할
    - 모든회원 조회 요청 받아  조회한 회원정보를 List 배열에 저장한 후 
      ViewServlet 두번째 서블릿 클래스로 공유하기 위해  다시 ~~~~~~ 
      HttpServletRequest 객체 메모리에 바인딩 하여  두번째 서블릿을 포워딩(재요청)해서 공유합니다.  
*/

@WebServlet("/member5")
public class MemberServlet extends HttpServlet{

	
	//DB 작업을 담당할 MemberDAO 객체(모든 요청이 공용으로 사용)
	private MemberDAO memberDAO = new MemberDAO();
	
		
	//Get 요청 방식으로 요청 들어오면 응답하는 콜백메소드 
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	//Post 요청 방식으로 요청 들어오면 응답하는 콜백메소드 
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	//================================================================
	// 요청 접수 창구 : command 값을 판단해 알맞은 처리 메소드 호출
	//================================================================
	//Get 또는 Post 요청 방식으로 요청 들어오면 모든 응답을 처리하는 일반 메소드 
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 요청 데이터의 한글 깨짐 방지를 위해 HttpServletRequest 객체의 문자 처리 방식을 UTF-8로 설정
		request.setCharacterEncoding("UTF-8");
		
		// 어떤 요청(회원추가? 삭제?  수정?)인지 판단할 조건값 얻기
		String command = request.getParameter("command");
		
		// command 값에 따라 알맞은 처리 메소드 호출(해당 없으면 조회만 실행)
		if("addMember".equals(command)) { //회원추가 요청을 받았다면?
			addMember(request);		
		}else if("delMember".equals(command)) {//회원삭제 요청을 받았다면?
			delMember(request);	
		}else if("modMember".equals(command)) {//회원수정 요청을 받았다면?
			modMember(request);
		}
		
		//회원추가/삭제/수정처리 후 전체회원조회 -> 화면담당 ViewServlet서블릿으로 포워딩 
		forwardListMembers(request, response);
		
	} //================ doHandle 메소드 끝
	
	//===========================================================
	//1. 새 회원 추가 (command = addMember)
	//============================================================
	private void addMember(HttpServletRequest request) {
		
		//회원가입 폼에서  입력한 가입할 값 4개를 얻어 ,   MemberVO객체 하나에 저장
		MemberVO vo = new MemberVO(request.getParameter("id"),
								   request.getParameter("pwd"),
								   request.getParameter("name"),
								   request.getParameter("email"));
		//MemberDAO에 INSERT 요청 -> 반환값 : 성공 1 / 실패 0
		int result = memberDAO.addMember(vo);
		System.out.println("회원가입 성공하면 1 출력, 실패하면 0 출력 => " + result);
	}
	
	//================================================================
	// 2. 회원 한 명 삭제 (command=delMember)
	//================================================================
	private void delMember(HttpServletRequest request) {

		// 요청 주소 예 : /pro08/member5?command=delMember&id=hong

		// 삭제할 회원의 아이디 얻기 (DELETE의 where 조건으로 사용)
		String id = request.getParameter("id");

		// MemberDAO에 DELETE 요청
		memberDAO.delMember(id);
	}

	//================================================================
	// 3. 수정할 회원 한 명 조회 (command=modMember)
	//================================================================
	private void modMember(HttpServletRequest request) {

		// 요청 주소 예 : /pro08/member5?command=modMember&id=hong

		// 수정할 회원의 아이디로 DB에서 회원 한 명 조회해 오기
		MemberVO vo = memberDAO.modMember(request.getParameter("id"));

		System.out.println(vo.getId());
		System.out.println(vo.getPwd());
		System.out.println(vo.getName());
		System.out.println(vo.getEmail());

		// 조회한 회원 정보를 수정 폼 화면으로 전달하는 단계는 아직 배우지 않았습니다.
		// 배우고 난 뒤 직접 시도해 보세요.
	}	
	
		
	//=============================================================
	//4. 공통 마무리  :  전체 회원 조회 후  ViewServlet으로 포워딩
	//==============================================================
	private void forwardListMembers(HttpServletRequest request,  HttpServletResponse response) 
									throws ServletException, IOException {
		
		// listMembers() 호출 -> 조회된 전체 회원(ArrayList배열)을 반환받음
		List  list = memberDAO.listMembers();
		
		//조회된 모든 회원정보들(MemberVO객체들)이 저장된 ArrayList배열 자체를 HttpServletRequest객체 메모리 안에! 바인딩 (저장)
		request.setAttribute("membersList", list);
		
		//RequestDispatcher객체의 forward메소드 호출 방법으로  
		//두번째 서블릿 ViewServlet으로  포워딩(재요청)시~~
		//ArrayList배열이 값 형태로 저장된 HttpServletRequest객체 메모리 전달해서 공유!
		RequestDispatcher dispatcher = request.getRequestDispatcher("viewMembers2");			
		dispatcher.forward(request, response); //실제 포워딩시 HttpServletRequest객체 메모리와 HttpServletResponse 객체 메모리 공유!
	}
	

}











