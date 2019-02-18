package com.bono.board.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bono.board.service.BoardService;
import com.bono.board.vo.Board;


@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	//전체리스트 화면
	@GetMapping(value = "/boardList")
	public String boardList(Model model,
			@RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage) {
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
	public String boardAdd(Board board) { //등록화면에서 값을 포스트방식으로 받을때 boardAdd 메서드 실행
		boardService.addBoard(board);
		System.out.println("/boardAdd 처리"); // /boardAdd 처리 컨트롤러 동작 확인
		return "redirect:/boardList";
	}
	//삭제처리
	@GetMapping(value = "/boardRemove")
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

}