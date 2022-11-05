package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.annotation.RequestConfig;
import com.example.controller.form.MemberJoinForm;
import com.example.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	final Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping("/form")
	@RequestConfig(menu = "MEMBER")
	public String form() {
		return "member/form";
	}

//	@PostMapping("/join")
//	@ResponseBody
//	public HttpEntity<Boolean> join(@Validated @RequestBody MemberJoinForm form) {
//		// 계정 중복체크 : true면 사용중인 계정
//		boolean isUseAccount=memberService.selectMemberAccount(form.getAccount()) > 0 ;
//		Assert.state(!isUseAccount, "이미 사용중인 계정입니다");
//		memberService.insertMember(form);
//		
//		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
//	}

	@PostMapping("/join")
	@RequestConfig(menu = "MEMBER", realname = true)
	// 클라이언트에서 파일첨부를 한다면 서버에서 데이터를 받을경우 @RequestBody를제거하고받아
	public HttpEntity<Boolean> join(@Validated MemberJoinForm form) {
		// 계정 중복체크 : true면 사용중인 계정
		boolean isUseAccount = memberService.selectMemberAccount(form.getAccount()) > 0;
		logger.info("isUseAccount : {} ", isUseAccount);
		Assert.state(!isUseAccount, "이미 사용중인 계정입니다");
		logger.info("form profileImage : {} ", form.getProfileImage());
		memberService.insertMember(form);
		;

		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	/**
	 * 가입 완료 화면
	 * 
	 * @return
	 */
	@GetMapping("/join-complete")
	@RequestConfig(menu = "MEMBER")
	public String joinComplete() {
		return "member/join-complete";
	}

	/**
	 * 본인인증 콜백이 오는경우 처리
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/realname/callback")
	@RequestConfig(menu = "MEMBER")
	public HttpEntity<?> realnameCallback(Model model, HttpServletRequest request) {
		// 실제 기능구현에는 요청이온 파라메터 값을 체크해서 성공여부를 해야함
		request.getSession().setAttribute("realnameCheck", true);
		return ResponseEntity.ok().build();
	}

	/**
	 * 회원 로그아웃
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/logout")
	@RequestConfig(menu = "MEMBER")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

}
