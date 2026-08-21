package sec02.ex02;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
memberForm.html(회원가입을 위해 가입할 정보를 입력후 DB의 테이블에 INSERT 요청 하는 화면)
에서 가입할 정보를 입력하고 가입요청버튼을 클릭하면 입력된 정보들을 
모두 HttpServletRequest객체 메모리에  저장된후 공유받아 사용하는 서블릿으로 
회원 추가후 추가된 정보를 확인 하기 위해
다시~~~ 모든 회원정보들을 조회해서 클라이언트의 웹브라우저로 출력(응답) 해서 보여줍니다.

순서1. input type="hidden" name="command" value="addMember" 의 
      addMember값을  HttpServletRequest객체 메모리에서 얻습니다.
	  
	  얻는 코드 :   request.getParameter("command"); -> "addMember"
	  
	  얻는 이유 : 어떤 요청을 했는지 서블릿은 판단하기 위해서 입니다. 
	  			여기서는 addMember 값 자체가 회원가입 요청임을 판단하는 값이 됩니다. 
	  			
순서2. addMember <- 요청한 값은 t_member테이블에 회원추가 요청임을 판단해
	  가입시 입력한 회원정보들을 HttpServletRquest객체 메모리로부터 얻어서
	  MemeberVO객체 생성후 각인스턴스변수에 저장 

순서3. 실제 t_member테이블에 insert하기 위해 MemberDAO의 addMember메소드 호출시~
  	  매개변수로 MemberVO객체의 주소를 전달하여 MemberDAO의 addMember메소드 내부에서
  	  insert문장을 만들고 insert문을 실행할수 있도록 합니다.
  	  
순서4. insert에 성공하면 다시~모든 회원정보 조회 요청을 하기위해
  	  MemberDAO객체의 listMembers메소드를 호출하여 ArrayList배열을 받고
  	  웹브라우저 화면에 조회된 정보들을 응답합니다.
  	  
	  			
*/
//모든 회원 정보 조회 GET요청 주소 :  http://localhost:8181/pro07/member3   

//회원 가입 양식 디자인 입력 화면 GET요청 주소  :http://localhost:8181/pro07/memberForm.html

//새 회원 추가 POST요청 주소 : http://localhost:8181/pro07/member3
//					회원추가 요청 조건 값 : addMember 

//조회된 모든 회원정보를 보여주는 화면에서  삭제 <a>를 클릭하여 삭제 GET요청 하는 주소 : 
//http://localhost:8181/pro07/member3?command=delMember&id=hong
//							  회원삭제 요청 조건값 : delMember	

@WebServlet("/member3")
public class MemberServlet extends HttpServlet{

	
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
	
	//Get 또는 Post 요청 방식으로 요청 들어오면 모든 응답을 처리하는 일반 메소드 
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 요청 데이터의 한글 깨짐 방지를 위해 HttpServletRequest 객체의 문자 처리 방식을 UTF-8로 설정
		request.setCharacterEncoding("UTF-8");
		
		//t_member 테이블에 조회 작업을 맡기기 위해 MemberDAO 객체 생성
		MemberDAO dao = new MemberDAO();  //<- DB 연결 작업 + DB작업 하는 사원

	//=====>	<a href='/pro07/member3?command=modMember&id="+memberVO.getId()+"'>수정</a>
		
		// ----->  MemberServlet이 어떤 요청(회원 조회?  회원추가?  회원수정? 중 회원추가요청)을 받았는지 판단
		String command = request.getParameter("command");
		//회원 추가 요청한 조건값  "addMember"얻기 
		//회원 삭제 요청한 조건값  "delMember"얻기 
		//회원 수정을 위해 수정할 회원아이디로 회원조회 요청한 조건값  "modeMember" 얻기 
		//회원 수정 요청한 조건값  "modMember2" 얻기 
		
			
		//----->  DB의  t_member 테이블에 새회원 추가 요청("addMember")을 서블릿이 받았다면?
		if(command != null  &&  command.equals("addMember")) {
			
			//----> 회원가입(t_member 테이블에 클라이언트가 입력한 회원정보 추가)을 위해서 요청한 데이터들 얻기
			//=============== 회원가입 폼에서 입력한 값들을 HttpServletRequest객체 메모리에서 얻기=========
			String _id = request.getParameter("id");
			String _pwd = request.getParameter("pwd");
			String _name = request.getParameter("name");
			String _email = request.getParameter("email");
			
			//----> 회원가입을 위해 입력한 정보들을 한꺼번에 MemberDAO.java로 전달 하기 위해 
			//      MemberVO클래스의 객체를 생성하여 각 인스턴스변수에 저장
			MemberVO vo = new MemberVO(_id, _pwd, _name, _email);
			
			//-----> MemberDAO 에 작성해 놓은 addMember 메소드 호출시! 매개변수로 가입시 입력한 정보가 저장된 MemberVO 객체 주소하나 전달해서
			//		 INSERT 명령!  INSERT에 성공하면 1을 반환, INSERT에 실패하면 0을 반환 받게 하자.
			int result = dao.addMember(vo);
			
			System.out.println("회원가입(추가)에 성공하면 1 출력, 회원가입(추가)에 실패하면 0 출력 = " + result);
		
		// 회원 한사람의 삭제 요청을 서블릿이 받았다면?
		// command 변수에 저장된 요청 조건 값이 "delMember" 과 같다면? 	
		} else if(command != null &&  command.equals("delMember")) {
			
		// http://localhost:8181/pro07/member3?command=delMember&id=hong  <- 삭제 요청을 받은 요청 주소 
			
			//요청한 삭제할 회원 아이디 얻기
				//삭제할 회원의 아이디를 HttpservletRequest객체 메모리에서 꺼내오기
				//얻는 이유 : DELETE 구문 작성시 어떤 아이디를 가진 회원 레코드를 삭제 할것인지 판단해서 회원 레코드 하나만 삭제 시키기 위함
				//		예 : delete from t_member where id='hong';
			String id = request.getParameter("id"); //'hong'
			
			//요청 받은 삭제할 회원 아이디를 이용해  DB의 t_member테이블에 저장된 하나의 회원 레코드 삭제하기 위해
			//MemberDAO에 만들어 놓은 delMember메소드 호출!
			dao.delMember(id);
				
		//============================================================
		//회원 한 사람 "수정 화면" 요청을 서블릿이 받았다면?
		//command 변수에 저장된 값이 "modMember" 와 같다면?
		//============================================================
		//[이 분기가 실행되는 순간]
		//  회원 목록 화면에서 hong 행의 수정<a>를 클릭
		//  -> <a>링크는 항상 GET 방식이므로 아래 주소로 요청이 들어온다.
		//     http://localhost:8181/pro07/member3?command=modMember&id=hong
		//  -> getParameter("command") = "modMember" 이므로 이 블록 진입!	
		} else if(command != null  &&  command.equals("modMember")) {
			
			//순서1. 수정할 회원의 "현재 정보"를 DB에서 조회해 오기 
			// - 수정할 회원 id 를 얻는다.  request.getParameter("id");
			// - 얻은 id 로 DB에서 회원의 현재 정보를 조회 해오기 위해 MemberDAO의 modMember(id); 호출해서 조회해 온다
			MemberVO vo = dao.modMember( request.getParameter("id") );
			//-> 이 조회된 값들을 수정 화면(memberModForm.html)의 입력칸에 미리 채워 보여줘야
			//   사용자가 "현재 값을 보면서" 고칠 수 있다.
			
			//순서2. [핵심] 자바스크립트 재요청 기술로 수정 요청 화면으로 이동시키기
			//--------------------------------------------------------
			//문제 : 서블릿(자바 코드)은 브라우저의 화면을 직접 바꿀 수 없다.
			//       화면 이동은 언제나 "브라우저가 새 주소를 요청"해야만 일어난다.
			//해결 : 서블릿이 응답으로 <script> 코드를 보내면,
			//       브라우저는 받은 내용을 HTML로 해석하다가 스크립트를 "실행"한다.
			//       그 스크립트가 location.href를 바꾸면 -> 브라우저가 스스로
			//       새 주소를 요청(재요청)하게 된다. 이것이 재요청 기술이다.
			//
			//[텍스트 모델링 (2)] 전체 동작 순서
			// [서블릿] --응답--> <script>location.href='memberModForm.html?id=hong&...';</script>
			//                          |
			//                          v (브라우저가 스크립트 실행)
			// [브라우저] 주소창이 바뀌며 재요청 발생!
			//            GET /pro07/memberModForm.html?id=hong&pwd=1212&name=%ED..&email=...
			//                          |
			//                          v
			// [수정 화면] 열리면서 그 화면의 JS(URLSearchParams)가
			//             주소 뒤의 값들을 꺼내 <input>에 자동으로 채운다.			
			
			//순서2-1. 한글 이름을 주소에 실을 수 있게 변환 (URL 인코딩)
			//--------------------------------------------------------
			//왜 필요한가?
			//- 주소(URL)에는 규칙상 영문/숫자/일부 기호만 실을 수 있다.
			//- "홍길동" 같은 한글을 그대로 붙이면 브라우저/서버에 따라 깨진다.
			//URLEncoder.encode(값, "UTF-8") 가 하는 일
			//- 한글을 UTF-8 바이트로 쪼갠 뒤 %기호+16진수 형태로 바꿔 준다.
			//
			//[텍스트 모델링 (3)] 변환 전 -> 후
			//  "홍길동"  --encode-->  "%ED%99%8D%EA%B8%B8%EB%8F%99"
			//  (한글 3글자)           (주소에 안전하게 실리는 형태)
			//- 수정 화면의 URLSearchParams.get("name")이 꺼낼 때
			//  자동으로 다시 "홍길동"으로 되돌려(디코딩) 준다. 왕복 완성!
			String encName = URLEncoder.encode(vo.getName(), "UTF-8");
			
			//----------------------------------------------------------------------
			//순서2-2.  응답 준비 : 브라우저로 응답할 데이터 유형(MIME-TYPE) 설정후 출력스트림 얻기
			//----------------------------------------------------------------------
			response.setContentType("text/html; charset=utf-8");
			PrintWriter  out = response.getWriter();
			
			/*순서2-3. 재요청 자바스크립트를 문자열로 조립해서 브라우저 응답으로 출력*/
			out.print("<script>");
			out.print("location.href='/pro07/memberModForm.html"
					+ "?id=" + vo.getId()   //?id=hong
					+ "&pwd="+ vo.getPwd()  //&pwd=1212 
					+ "&name=" + encName    // &name=%ED%99%8D... (인코딩된 한글)
					+ "&email=" + vo.getEmail()  
					+ "';");
			out.print("</script>");
			
			//--------------------------------------------------------
			//순서2-4. [중요] return으로 메소드 즉시 종료!
			//--------------------------------------------------------
			//만약 return이 없다면?
			//- 아래쪽의 "모든 회원 조회 출력" 코드까지 이어서 실행되어
			//  응답이  <script>...</script><html><body><table>회원목록...  처럼
			//  스크립트 + 회원목록이 한 응답에 뒤섞여 나간다.
			//- 결과 : 이동 직전에 회원 목록이 잠깐 보였다 사라지는 등 화면이 오염된다.
			//- 재요청 응답은 스크립트 "하나"로만 끝나야 깔끔하다.
			return;
			
			
		//===================================================================
		//회원 한사람의 수정 요청을 받았다면? command 변수 값이  "modMember2" 와 같으면?
		//===================================================================
		} else if(command != null  &&  command.equals("modMember2")) {
			
			
			//순서1.  memberModForm.html(수정 요청하는 화면)에서 수정을 위해 입력한 정보들을
			//		 HttpServletRequest 객체 메모리에서 얻기
			//command=modMember2&id=hong&pwd=9999&name=홍길동2&email=hong2@a.com
			//요약 : 요청한 데이터 얻기
			String _id = request.getParameter("id");
			String _pwd = request.getParameter("pwd");
			String _name = request.getParameter("name");
			String _email = request.getParameter("email");
			
			//순서2.  수정시 입력한 정보들을 MemberVO객체 하나 생성해서 저장
			MemberVO  vo = new MemberVO(_id, _pwd, _name, _email);
			
			//순서3.  DAO에게 UPDATE 명령해서 수정 맡기기
			//반환값 :  수정된 행 갯수 1 반환 또는  수정에 실패하면 0 반환 
			int result = dao.updateMember(vo);
			
			System.out.println("회원 수정 성공 하면 1 출력, 회원 수정 실패 하면 0 출력 = " + result);
			
			//순서4. 자바스크립트 재요청 기술 (2번째 사용) : 수정 후  수정된 모든 회원정보를 조회 요청
			response.setContentType("text/html; charset=utf-8");
			PrintWriter  out = response.getWriter();
			out.print("<script>");
			out.print(" alert('회원 정보가 수정되었습니다.');");  //알림창 (확인 버튼 누를때까지 대기 한다)
			out.print(" location.href='/pro07/member3';");   //알림창 확인 버튼 누른 직후  모든 회원정보 조회 재요청!
			out.print("</script>");			
			return; //UPDATE 응답도 스크립트 하나로만 끝내야 하므로 즉시 종료.
		}
			
		
		// listMembers() 호출 -> 조회된 전체 회원(ArrayList배열)을 반환받음
		List  list = dao.listMembers();
		/*
	    반환받은 ArrayList 배열 모습 (조회된 레코드(행) 1개당 MemberVO 객체 1개)
	    [ new MemberVO(), new MemberVO(), new MemberVO() ]
	         		0           1               2          <---- index
		 */		
		//3.2.1. 브라우저로 응답할 데이터의 유형(MIME-TYPE)을 HTML로 , 인코딩을 UTF-8로 설정
		response.setContentType("text/html; charset=utf-8");
		
		//3.2.2. 요청한 클라이언트의 브라우저와 연결된 출려스트림(PrintWriter) 객체 얻기
		PrintWriter  out = response.getWriter();
		
		//3.3. 조회된 회원 정보를 HTML 표 형태로 만들어 브라우저에 출력(응답)
		out.print("<html>");
		out.print("<body>");
			out.print("<table border=1>");
				// 표의 제목 행 출력
				out.print("<tr align='center' bgcolor='lightgreen'>");
					out.print("<th>아이디</th>");
					out.print("<th>비밀번호</th>");
					out.print("<th>이름</th>");
					out.print("<th>이메일</th>");
					out.print("<th>가입일</th>");
					out.print("<th>삭제</th>");
					out.print("<th>수정</th>");				
				out.print("</tr>");

				/*
			    반환받은 ArrayList 배열 모습 (조회된 레코드(행) 1개당 MemberVO 객체 1개)
			    [ new MemberVO(), new MemberVO(), new MemberVO() ]
			         		0           1               2          <---- index
				 */		
				
				// ArrayList에 저장된 MemberVO 객체 개수(list.size())만큼 반복 출력
				for (int i = 0; i < list.size(); i++) {

					// i번 칸의 MemberVO 객체를 꺼내 변수에 저장
					// (raw 타입 List라서 Object로 반환되므로 MemberVO로 형변환 필요)
					MemberVO memberVO = (MemberVO) list.get(i);

					// 회원 1명 = 표의 행 1개로 출력 (getter로 값을 꺼내 <td>에 삽입)
					out.print("<tr align='center'>");
						out.print("<td>" + memberVO.getId() + "</td>");
						out.print("<td>" + memberVO.getPwd() + "</td>");
						out.print("<td>" + memberVO.getName() + "</td>");
						out.print("<td>" + memberVO.getEmail() + "</td>");
						out.print("<td>" + memberVO.getJoinDate() + "</td>");
						out.print("<td><a href='/pro07/member3?command=delMember&id="+memberVO.getId()+"'>삭제</a></td>");
						out.print("<td><a href='/pro07/member3?command=modMember&id="+memberVO.getId()+"'>수정</a></td>");
					out.print("</tr>");
				}

			out.print("</table>");
			
			out.print("<a href='/pro07/memberForm.html'>회원가입</a>");
			
		out.print("</body>");
	out.print("</html>");		
		
		
	}
	

	

}











