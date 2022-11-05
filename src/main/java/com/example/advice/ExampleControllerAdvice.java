package com.example.advice;


import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import groovyjarjarantlr4.v4.parse.ANTLRParser.finallyClause_return;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//롬복 로거 자동생성
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExampleControllerAdvice {
	
	private final MappingJackson2JsonView jsonView;
	
	/**
	 * Exception 발생에 예외처리
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception e, HttpServletRequest request) {
		log.error("handleException",e);
		String requested=request.getHeader("X-Requested-With");
		//응답값을 json포맷으로 처리
		if(requested != null && requested.equals("XMLHttpRequest")) {
			log.info("해당 조건에는 json으로 응답처리");
			//jsonView를 렌더링으로 쓴다는걸 알려줌 
			ModelAndView view=new ModelAndView(jsonView);
			//응답을 오류상태로 
			view.setStatus(HttpStatus.BAD_REQUEST);
			view.addObject("message",e.getMessage());
			return view;
		}
		
		ModelAndView view=new ModelAndView("/error/message.html");
		
		view.addObject("message", e.getMessage());
		return view;
	}
	
	/**
	 * BindException 발생에 예외처리
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	public ModelAndView handleBindException(BindException e, HttpServletRequest request) {
		log.error("handleBindException",e);
		//에러가 발생했을때 json형태로 값을 받기위한 코드
		FieldError fieldError = e.getFieldError(); //커서 맨끝에 놓고 ctrl+1하면 자동완ㅅ어
		//비동기나 ajax방식은 페이지 전환이 안되고 response를 줘야하기 때문에 헤더를 찾는것
		String requested=request.getHeader("X-Requested-With");
		//응답값을 json포맷으로 처리
		if(requested != null && requested.equals("XMLHttpRequest")) {
			log.info("해당 조건에는 json으로 응답처리");
			//jsonView를 렌더링으로 쓴다는걸 알려줌 
			ModelAndView view=new ModelAndView(jsonView);
			//응답으ㄹㄹ 오류상태로 
			view.setStatus(HttpStatus.BAD_REQUEST);
			view.addObject("message",fieldError.getDefaultMessage());
			return view;
		}
		
		ModelAndView view=new ModelAndView("/error/message.html");
		
		view.addObject("message", fieldError.getDefaultMessage());
		return view;
	}

}
