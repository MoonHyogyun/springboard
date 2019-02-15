package com.bono.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.bono.board.vo.Board;


@Mapper
public interface BoardMapper {
	
	Board selectBoardOne(int boardNo);
	
	// 페이징
	List<Board> selectBoardList(Map<String, Integer> map);
	
	// 페이징을 위해 전체 라인 수를 세기 위함
	int selectBoardCount();
	
	int insertBoard(Board board);

	int deleteBoard(Board board);
	
	int updateBoard(Board board);
}
