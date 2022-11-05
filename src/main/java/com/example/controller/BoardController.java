package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.annotation.RequestConfig;
import com.example.controller.form.BoardSaveForm;
import com.example.domain.Board;
import com.example.domain.BoardType;
import com.example.security.SecurityUserDetails;
import com.example.service.BoardService;

import lombok.RequiredArgsConstructor;

/**
 * @author oiio6
 *
 */
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private final BoardService boardService;

	/**
	 * 게시물 목록 조회 및 화면
	 * 
	 * @param model
	 * @return
	 */

//	@GetMapping
//	@RequestConfig(menu = "BOARD")
//	public String list(Model model) { // 객체를 인스턴스해서 ModelMap 주입시켜줌
//		// 파라미터에 HttpServletRequest넣고
//		// request.getMethod().equals("GET")
//		// 게시물 목록 조회를 boardService에 호출하고 결과값을 boarList key값으로 request에 저장한다.
//		model.addAttribute("boardList", boardService.selectBoardList());
//		// jsp를 호출
//		return "/board/list";
//
//	}
	
	@GetMapping("/{boardType}")
	@RequestConfig(menu = "BOARD")
	public String list(Model model, @PathVariable BoardType boardType, @RequestParam(required = false) String query) {
		// 게시물 목록 조회 후 model에 boardList key로 저장
		model.addAttribute("boardList", boardService.selectBoardList(boardType, query));
		model.addAttribute("boardType", boardType);
		model.addAttribute("boardTypes", BoardType.values());
		// jsp를 호출
		return "board/list";
	}

	@GetMapping("/{boardType}/{boardSeq}")
	@RequestConfig(menu = "BOARD")
	public String detail(Model model, @PathVariable BoardType boardType, @PathVariable(required = true) int boardSeq) {
		logger.debug("detail");
		Board board = boardService.selectBoard(boardSeq);
		Assert.notNull(board, "게시글 정보가 없습니다.");
		// 게시물 상세정보 set
		model.addAttribute("board", board);
		model.addAttribute("boardType", boardType);
		model.addAttribute("boardTypes", BoardType.values());
		// jsp를 호출
		return "board/detail";
	}

	/**
	 * 게시물 조회 화면
	 * 
	 * @param model
	 * @param boardSeq
	 * @return
	 */
//	@GetMapping("/{boardSeq}")
//	@RequestConfig(menu="BOARD")
//	public String form(Model model, @PathVariable(required = false) int boardSeq) { // 객체를 인스턴스해서 Model 주입시켜줌
//		//게시물 조회
//		Board board=boardService.selectBoard(boardSeq);
//		//board가 null일 경우 에러메시지 출력
//		Assert.notNull(board,"게시글 정보가 없습니다.");
//		//detail.html에서 board를 사용하기 위해 모델에 넣는다
//		model.addAttribute("board",board);
//		return "/board/detail";
//
//	}



	/**
	 * 게시물 등록 화면
	 * 
	 * @param model
	 * @return
	 */

//	@GetMapping("/form")
//	@RequestConfig(menu="BOARD")
//	public String form(Model model) { // 객체를 인스턴스해서 ModelMap 주입시켜줌	
//		return "/board/form";
//	}

	/**
	 * 게시물 등록 화면
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/{boardType}/form")
	@RequestConfig(menu = "BOARD")
	public String form(Model model, @PathVariable BoardType boardType) {
		model.addAttribute("boardType", boardType);
		model.addAttribute("boardTypes", BoardType.values());
		return "board/form";
	}

	/**
	 * 게시물 등록 화면 (비동기로 전송)
	 * 
	 * @param model
	 * @return
	 */

	@GetMapping("/form-body")
	public String formBody(Model model) { // 객체를 인스턴스해서 ModelMap 주입시켜줌
		return "board/form-body";
	}

//	/**
//	 * 게시물 수정화면 
//	 * @param model
//	 * @param boardSeq
//	 * @return
//	 */
//	@GetMapping("/edit/{boardSeq}")
//	//비동기 api에서 많이 쓰는 요청.
//	public String edit(Model model, @PathVariable int boardSeq,
//			Authentication authentication){
//		//데이터 조회
//		Board board=boardService.selectBoard(boardSeq);
//		// board가 null일 경우 에러 메시지 출력
//		Assert.notNull(board,"게시글 정보가 없습니다.");
//		//AOP 적용전
//		SecurityUserDetails details = (SecurityUserDetails) 
//				authentication.getPrincipal();
//		Assert.isTrue(board.getMemberSeq() == details.getMemberSeq(),
//				"본인이 작성한 글만 수정이 가능합니다.");
//		model.addAttribute("board",board);
//		return "/board/form";
//		
//	}

	/**
	 * 게시물 수정화면 (AOP이후)
	 * 
	 * @param model
	 * @param boardSeq
	 * @return
	 */
//	@GetMapping("/edit/{boardSeq}")
//	@RequestConfig(menu = "BOARD")
//	// 비동기 api에서 많이 쓰는 요청.
//	public String edit(Model model, @PathVariable int boardSeq) {
//		// 데이터 조회
//		model.addAttribute("board", boardService.selectBoard(boardSeq));
//		return "/board/form";
//
//	}
	/**
	 * 게시물 수정화면
	 * @param model
	 * @param boardType
	 * @param boardSeq
	 * @return
	 */
	@GetMapping("/{boardType}/edit/{boardSeq}")
	@RequestConfig(menu = "BOARD")
	public String edit(Model model, @PathVariable BoardType boardType, 
			@PathVariable int boardSeq) {
		// 게시물 상세정보 set
		model.addAttribute("board", boardService.selectBoard(boardSeq));
		model.addAttribute("boardType", boardType);
		model.addAttribute("boardTypes", BoardType.values());
		// jsp를 호출
		return "board/form";
	}


	/**
	 * 게시물 저장
	 * 
	 * @param form
	 * @return
	 */
//	@PostMapping("/save")
//	public String save(@Validated BoardSaveForm form, 
//			Authentication authentication) {
//		//hasLength가 아니면 예외를 던짐
////		Assert.hasLength(board.getUserName(),"회원 이름을 입력해주세요");
////		Assert.hasLength(board.getTitle(),"제목을 입력해주세요");
////		Assert.hasLength(board.getBoardType(),"종류를 입력해주세요");
////		Assert.hasLength(board.getContents(),"내용을 입력해주세요");
//		
//		
//		Board selectBoard = null;
//		//게시글 수정으로 요청인 경우
//		if(form.getBoardSeq() > 0) {
//			selectBoard=boardService.selectBoard(form.getBoardSeq());
//		}
//		// 수정인 경우 업데이트
//		if(selectBoard != null) {
//			boardService.updateBoard(form);
//		}else {
//			//새로 작성인 경우 인서트
//			boardService.insertBoard(form);
//		}
//			
//		return "redirect:/board";
//	}

	/**
	 * 게시물 저장
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping("/save")
	@RequestConfig(menu = "BOARD")
	public String save(@Validated BoardSaveForm form, Authentication authentication) {
		Board board = boardService.save(form, authentication);
		// 목록 화면으로 이동
		return "redirect:/board/" + form.getBoardType().name() + "/" + board.getBoardSeq();
	}

	/**
	 * 업데이트 처리
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping("/update")
	@RequestConfig(menu = "BOARD")
	public String update(@Validated BoardSaveForm form) {
		boardService.update(form);
		// 상세화면으로 이동
		return "redirect:/board/" + form.getBoardType().name() + "/" + form.getBoardSeq();

	}

	/**
	 * 게시물 저장 (Clinet body에 json으로 받기)
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping("/save-body")
	@ResponseBody
	@RequestConfig(menu = "BOARD")
	public HttpEntity<Integer> saveBody(@Validated @RequestBody BoardSaveForm form, Authentication authentication) { // integer넣은
																														// 이유
																														// :
																														// 리턴값을
																														// 인티저
																														// 주기위해
		// hasLength가 아니면 예외를 던짐
//		Assert.hasLength(board.getUserName(),"회원 이름을 입력해주세요");
//		Assert.hasLength(board.getTitle(),"제목을 입력해주세요");
//		Assert.hasLength(board.getBoardType(),"종류를 입력해주세요");
//		Assert.hasLength(board.getContents(),"내용을 입력해주세요");

		Board selectBoard = null;
		// 게시글 수정으로 요청인 경우
		if (form.getBoardSeq() > 0) {
			selectBoard = boardService.selectBoard(form.getBoardSeq());
		}
		// 수정인 경우 업데이트
		if (selectBoard != null) {
//			boardService.updateBoard(form);
			boardService.update(form);
		} else {
			// 새로 작성인 경우 인서트
//			boardService.insertBoard(form);
			boardService.save(form, authentication);
		}
		// response header에 OK를 주고, boardSeqence를 준다.
		return new ResponseEntity<Integer>(form.getBoardSeq(), HttpStatus.OK);
	}

	/**
	 * 게시물 삭제
	 * 
	 * @param boardSeq
	 * @return
	 */
//	@PostMapping("/delete")
//	@ResponseBody
//	//responseBody : 비동기로 넘길때 사용하는 방법
//	// 객체가 json화 되면서 responsebody에 띄워짐
//	//비동기 api에서 많이 쓰는 요청.
//	public HttpEntity<Boolean> delete(@RequestParam int boardSeq){
//		//데이터 조회
//		Board board=boardService.selectBoard(boardSeq);
//		// board가 null일 경우 에러 메시지 출력
//		Assert.notNull(board,"게시글 정보가 없습니다.");
//		boardService.deleteBoard(boardSeq);
//		//response에 status라는 코드가 return으로 들어감.
//		return new ResponseEntity<Boolean>(HttpStatus.OK);
//	}
//	

	/**
	 * 게시물 삭제(AOP이후)
	 * 
	 * @param boardSeq
	 * @return
	 */
	@PostMapping("/delete")
	@ResponseBody
	@RequestConfig(menu = "BOARD")
	// responseBody : 비동기로 넘길때 사용하는 방법
	// 객체가 json화 되면서 responsebody에 띄워짐
	// 비동기 api에서 많이 쓰는 요청.
	public HttpEntity<Boolean> delete(@RequestParam int boardSeq) {
		boardService.deleteBoard(boardSeq);
		// response에 status라는 코드가 return으로 들어감.
		return ResponseEntity.ok().build();
	}

	/**
	 * Exception 발생에 예외처리
	 * 
	 * @param e
	 * @return
	 */

	@PostMapping("/test")
	@ResponseBody
	@RequestConfig(menu = "BOARD")
	// responseBody : 비동기로 넘길때 사용하는 방법
	// 객체가 json화 되면서 responsebody에 띄워짐
	// 비동기 api에서 많이 쓰는 요청.
	// @ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception e) {
		logger.error("BoardController handleException", e);
		ModelAndView view = new ModelAndView("/error/error");
		view.addObject("exception", e);
		return view;
	}

	// boardController에 정의된 이 부분이 먼저 찍히고
	// BaseErrorController를 타면서
	// default로 정의한 500.html에서 에러가 찍힘.
//	@ExceptionHandler(Exception.class)
//	public Exception handleException(Exception e) {
//		logger.error("BoardController handleException",e);
//		return e;
//	}

}
