package com.bono.board.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bono.board.service.BoardService;
import com.bono.board.vo.Board;
import com.bono.board.vo.BoardRequest;


@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	//전체리스트 화면
	@GetMapping(value = "/boardList")
	public String boardList(Model model,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage) {
		Map<String, Object> returnMap = boardService.getBoardList(currentPage);
		System.out.println("/boardList"); // /boardList 컨트롤러 동작 확인
		model.addAttribute("list", returnMap.get("list"));
    	model.addAttribute("boardCount",returnMap.get("boardCount"));
    	model.addAttribute("lastPage",returnMap.get("lastPage"));
    	model.addAttribute("currentPage",currentPage);
		return "boardList";
	}
	//등록화면
	@GetMapping(value = "/boardAdd") //
	public String boardAdd() {
		System.out.println("/boardAdd 등록화면"); // /boardAdd 컨트롤러 동작 확인
		return "boardAdd";
	}
	//등록처리
	@PostMapping(value = "/boardAdd") 
	public String boardAdd(BoardRequest boardRequest, HttpServletRequest request) { //등록화면에서 값을 포스트방식으로 받을때 boardAdd 메서드 실행
		
		System.out.println("/boardAdd 처리"); // /boardAdd 처리 컨트롤러 동작 확인
		String path = request.getSession().getServletContext().getRealPath(""); //매우긴데 API를 통해 쉽게 불러올수있을것같다
		boardService.addBoard(boardRequest, path);
		return "redirect:/boardList";
	} /* Service
		 1.board안에 fileList분해하여 DB들어갈수있는 형태
		 2.파일저장 : 파일 경로
	  */
	
	
	//삭제화면
    @GetMapping("/deleteBoard")
    //list화면에서 삭제 할 boardNo값을 받아와서 boardNo변수에 담고 그 값을 getBoard메서드를 호출 입력한다.
    public String deleteBoard(@RequestParam(value="boardNo")int boardNo, Model model) {
		System.out.println("deleteBoard 실행, Controller");
    	//getBoard를 호출한 후 return값을 boardUpdate변수에 담아준다.
		Board boardDelete = boardService.getBoard(boardNo);
		//model영역에 boardUpdate값을 board라는 이름으로 담아준다.
    	model.addAttribute("board", boardDelete);
    	return "boardDelete"; //단순히 forward해서 boardDelete 삭제확인 할 화면을 보여주는 메서드
    	
    }
	//삭제처리
	@PostMapping(value = "/boardRemove")
	public String boardRemove(Board board) {
		boardService.removeBoard(board);

		return "redirect:/boardList";  //삭제처리후 boardList화면으로 redirect
	}
	//수정처리 화면
	@GetMapping(value = "/modifyBoard")
	public String modifyBoard(Board board, Model model) {
		Board resultboard = boardService.getBoard(board.getBoardNo());
		System.out.println("수정화면 확인"); // /modifyBoard 컨트롤러 동작확인
		model.addAttribute("board", resultboard);
		
		return "modifyBoard"; //수정화면을 보여주기위해 포워드

	}
	//수정처리 
	@PostMapping(value = "/modifyBoard")
	public String modifyBoard(Board board) {
		boardService.modifyBoard(board);
		System.out.println("/modifyBoard"); // /modifyBoard 컨트롤러 동작확인

		return "redirect:/boardList"; // 수정처리후 boardList화면으로 redirect

	}
	//상세보기
    @GetMapping("/boardDetail")
    public String boardDetail(int boardNo, Model model) {
    	Board board = boardService.getBoard(boardNo);
    	model.addAttribute("board",board);
		return "boardDetail";

    	
    }
	@GetMapping(value = "/index") //
	public String index() {
		System.out.println("/index "); // /index 컨트롤러 동작 확인
		return "index";
	}

}