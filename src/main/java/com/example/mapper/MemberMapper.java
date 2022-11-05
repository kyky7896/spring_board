package com.example.mapper;

import com.example.domain.Member;

public interface MemberMapper {
	
	int selectMemberAccountCount(String account);
	
	void insertMember(Member form);
	
	Member selectMemberAccount(String username);
	
	

}
