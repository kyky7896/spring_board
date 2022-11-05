package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.annotation.RequestConfig;
import com.example.security.SecurityUserDetails;

@Controller
public class HomeController {
	
	//현재 클래스를 넣고 로거 준비
	final Logger logger=LoggerFactory.getLogger(getClass());
	
	@GetMapping(value = { "/", "/home"})
	@RequestConfig(menu = "HOME")
	public String home(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 익명사용자인경우
		if (authentication instanceof AnonymousAuthenticationToken) {
			logger.info("익명사용자 접근");
		} else {
			SecurityUserDetails details = (SecurityUserDetails) authentication.getPrincipal();
			model.addAttribute("details", details);
		}
		return "home";
	}



}
