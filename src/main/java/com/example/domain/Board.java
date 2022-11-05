package com.example.domain;

import lombok.Data;

@Data
public class Board {
	
	private int boardSeq; 
	private BoardType boardType;
	private String title;
	private String contents;
	private String regDate;
	private String userName; //회원이름 
	private int memberSeq;
	
}
