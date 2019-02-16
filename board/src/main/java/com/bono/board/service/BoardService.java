package com.bono.board.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bono.board.mapper.BoardMapper;
import com.bono.board.vo.Board;

// interface로 디커플링 가능
// Transaction -> 예외 발생시 실행 취소
// (DB를 변경할 때만. SELECT는 DB에 영향을 주지 않기 때문에 Transaction 상황이 아님)
@Service
@Transactional
public class BoardService {
	@Autowired
	private BoardMapper boardMapper;
	//업데이트 폼 보여주기
	public Board getBoard(int boardNo) {
		return boardMapper.selectBoardOne(boardNo);
	}
	// 전체리스트 
	public Map<String, Object> getBoardList(int currentPage) {
		final int ROW_PER_RAGE = 10;
		Map<String, Integer> map = new HashMap<String, Integer>();
		// 1.
		// vo.setCurrentPage(currentPage)
		map.put("currentPage", currentPage);
		map.put("rowPerPage", ROW_PER_RAGE);
		// 2.
		int boardCount = boardMapper.selectBoardCount();
		// Math.ceil -> 올림 함수
		int lastPage = (int)(Math.ceil(boardCount / ROW_PER_RAGE));
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("list", boardMapper.selectBoardList(map));
		returnMap.put("boardCount", boardCount);
		returnMap.put("lastPage", lastPage);
		
		return returnMap;
	}
	// 페이징
	public int getBoardCount() {
		return boardMapper.selectBoardCount();
	}
	//등록처리
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
	}
	//삭제처리
	public int removeBoard(Board board) {
		return boardMapper.deleteBoard(board);
	}
	//수정처리
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);
	}

}
