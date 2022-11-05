package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.controller.form.BoardSaveForm;
import com.example.domain.Board;
import com.example.domain.BoardType;
import com.example.mapper.BoardMapper;
import com.example.security.SecurityUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardMapper boardMapper;
	/**
	 * 게시물 목록 조회후 리턴
	 * 
	 * @return
	 *
	 */
	
	public List<Board> selectBoardList(BoardType boardType, String query) {
		return boardMapper.selectBoardList(boardType, query);
	}

	
	public Board selectBoard(int boardSeq){
		return boardMapper.selectBoard(boardSeq);
	}
	
	/*
	 * public void insertBoard(BoardSaveForm board) {
	 * boardMapper.insertBoard(board); }
	 */
	
	public void deleteBoard(int boardSeq) {
		boardMapper.deleteBoard(boardSeq);
	}

	/*
	 * public void updateBoard(BoardSaveForm board) {
	 * boardMapper.updateBoard(board); }
	 */
	
	public Board save(BoardSaveForm form, Authentication authentication) {
		SecurityUserDetails details = (SecurityUserDetails) 
			authentication.getPrincipal();
		Board board = new Board();
		board.setBoardSeq(form.getBoardSeq());	
		board.setBoardType(form.getBoardType());
		board.setTitle(form.getTitle());
		board.setContents(form.getContents());
		board.setUserName(details.getNickname());
		board.setMemberSeq(details.getMemberSeq());
		boardMapper.insertBoard(board);
		return board;
	}
	
	public void update(BoardSaveForm form) {
		Board board = new Board();
		board.setBoardSeq(form.getBoardSeq());	
		board.setBoardType(form.getBoardType());
		board.setTitle(form.getTitle());
		board.setContents(form.getContents());
		boardMapper.updateBoard(board);
	}


}
