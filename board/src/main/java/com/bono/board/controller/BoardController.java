package com.bono.board.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bono.board.service.BoardService;
import com.bono.board.vo.Board;

//@RequestMapping(value="/boardList", method = RequestMethod.GET)
@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	
	@GetMapping(value="/boardList")
	public String boardList(Model model,
    						
    						@RequestParam(value="currentPage", required=false, defaultValue="1") int currentPage) {
		Map<String, Object> returnMap = boardService.getBoardList(currentPage);
		System.out.println("/boardList");
		model.addAttribute("list", returnMap.get("list"));
        return "boardList";
	}
	
	@GetMapping(value="/boardAdd")
		public String boardAdd() {
			return "boardAdd";
	}

   @PostMapping(value="/boardAdd")
   public String boardAdd(Board board) {
	   
	   int result = boardService.addBoard(board);
	   System.out.println("insert 쿼리 실행 여부 -> " + result);
	   return "redirect:/boardList"; 
   }
   
   @GetMapping(value="/boardRemove")	
  public String boardRemove(Board board) {
	  boardService.removeBoard(board);				
	  
	  return "redirect:/boardList";
  }
   @GetMapping(value="/modifyBoard")
  public String modifyBoard(Board board, Model model) {
	  Board resultboard = boardService.getBoard(board.getBoardNo());
	  model.addAttribute("board", resultboard);
	return "modifyBoard";
	  
   }
   @PostMapping(value="/modifyBoard")
   public String modifyBoard(Board board) {
	   boardService.modifyBoard(board);
	   
	   return "redirect:/boardList";
	   
   }
  
   
  
  
  
}