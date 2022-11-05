package com.example.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.controller.form.MemberJoinForm;
import com.example.domain.Member;
import com.example.mapper.MemberMapper;
import com.example.security.SecurityUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService{
	
	private final MemberMapper memberMapper;
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final PasswordEncoder passwordEncoder;
	
	@Value("${file.root-path}")
	private String rootPath;
	
	public int selectMemberAccount(String account) {
		return memberMapper.selectMemberAccountCount(account);
	}
	
//	public void insertMember(MemberJoinForm form) {
//		memberMapper.insertMember(form);
//	}
	
	public void insertMember(MemberJoinForm form) {
		MultipartFile profileImage = form.getProfileImage();
		String originalFilename=profileImage.getOriginalFilename();
		String ext=originalFilename.substring(originalFilename.lastIndexOf(".")+1,
				originalFilename.length()); 
		// 탐색하는 문자열이 마지막으로 등장하는 위치에 대한 index를 반환  ex).jpg, .png의 .
		// ext : .jpg 출력
		
		String randomFilename=UUID.randomUUID().toString()+"."+ext; 
		//UUID 클래스를 사용해 유일한 식별자 생성가능. 
		// 1. 업로드된 파일명의 중복을 방지하기 위해 파일명을 변경할 때 사용.
		// 2. 첨부파일 파일다운로드시 다른 파일을 예측하여 다운로드하는것을 방지하는데 사용.
		// 3. 일련번호 대신 유추하기 힘든 식별자를 사용하여 다른 컨텐츠의 임의 접근을 방지하는데 사용.
		
		String addPath="/"+LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
		// 현재날짜 포매팅 /2021-09-02 표시
		// 출처 : https://suyou.tistory.com/287 
		
		// 저장경로
		String savePath = new StringBuilder(rootPath).append(addPath).toString();
		String imagePath = addPath + "/" + randomFilename; // imagePath:/2021-09-02/파일명.jpg 
		File saveDir = new File(savePath); // pathname가 생성자의 파라미터 
		logger.info("originalFilename : {}", originalFilename);
		logger.info("ext : {}", ext);
		logger.info("randomFilename : {}", randomFilename);
		
		// 폴더가 없는 경우
		// 폴더가 없는경우
		if (!saveDir.isDirectory()) {
			// 폴더 생성
			saveDir.mkdirs();
		}
		File out = new File(saveDir, randomFilename);
		
		try {
			FileCopyUtils.copy(profileImage.getInputStream(), new FileOutputStream(out));
			//in의 내용을 out에 복사하고 스트림 닫는다. byte수 return
		} catch (IOException e) {
			log.error("fileCopy", e);
			throw new RuntimeException("파일을 저장하는 과정에 오류가 발생하였습니다.");
		}
		
		
		Member member = new Member();
		member.setAccount(form.getAccount());
		//패스워드 암호화 
		String encodePassword=passwordEncoder.encode(form.getPassword());
		log.info("encodePassword : {}" , encodePassword);
		member.setPassword(encodePassword);
		
		member.setNickname(form.getNickname());
		member.setProfileImagePath(imagePath);
		member.setProfileImageName(originalFilename);

		memberMapper.insertMember(member);

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername : {}", username);
		//화면에서 받은 username
		Member member = memberMapper.selectMemberAccount(username);
		if (member == null) {
			throw new UsernameNotFoundException("회원이 존재하지 않습니다.");
		}
		
		log.info("member : {}", member);
		return SecurityUserDetails.builder()
			.memberSeq(member.getMemberSeq())
			.nickname(member.getNickname())
			.username(username)
			.password(member.getPassword())
			.build();

	}
	

}
