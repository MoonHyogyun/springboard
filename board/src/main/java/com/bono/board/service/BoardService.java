package com.bono.board.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bono.board.mapper.BoardMapper;
import com.bono.board.vo.Board;
import com.bono.board.vo.BoardRequest;
import com.bono.board.vo.Boardfile;

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
		map.put("currentPage", (currentPage-1)*ROW_PER_RAGE);
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
	public void addBoard(BoardRequest boardRequest,String path) {
		// 1.BoardRequest를 분리 : board , file , file정보
		// 2.board -> boardVo
		// 3.file정보 -> boardFileVo
		// 4.file -> +path를 이용해 물리적 장치 저장
		
		//1
		Board board = new Board();
		board.setBoardTitle(boardRequest.getBoardTitle());
		
		boardMapper.insertBoard(board); // jjdev cafe 1280번 글 확인
		//2
		Board boar = new Board();
		board.setBoardNo(board.getBoardNo());
		List<MultipartFile> files = boardRequest.getFiles();
		for(MultipartFile f : files) {
			// f->  boardFile
			Boardfile boardfile = new Boardfile();
			boardfile.setBoardNo(board.getBoardNo());
			boardfile.setFilesize(f.getSize());
			boardfile.setFileType(f.getContentType());
			
			String origicalFilename = f.getOriginalFilename();
			int i = origicalFilename.lastIndexOf(".");
			String ext = origicalFilename.substring(i+1);
			boardfile.setFileExt(ext);
			String fileName = UUID.randomUUID().toString(); // 16진수를 랜덤문자만들기 
			boardfile.setFileName(fileName);
			// 전체작업이 롤백되면 파일삭제작업 직접
			
			//3 파일저장
			try {
				f.transferTo(new File(path+"/"+ fileName +"."+ ext));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//boardFileMapper.insertBoard(boardFile);

	
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
