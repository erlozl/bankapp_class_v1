package com.tenco.bankapp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaml.snakeyaml.tokens.DocumentEndToken;

import com.tenco.bankapp.dto.SignInFormDTO;
import com.tenco.bankapp.dto.SignUpFormDTO;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.service.UserService;
import com.tenco.bankapp.utils.Define;

@Controller
@RequestMapping("/user") // 대문달기
public class UserController {
	
	@Autowired // DI처리
	private UserService userService;
	
	@Autowired
	private HttpSession session;
	
//	DI 처리
//	public UserController(UserService userService) {
//		this.userService = userService;
//	}
	
	// 회원 가입 페이지 요청
	// http://localhost:80/user/sign-up
	@GetMapping("/sign-up")
	public String signUp() {
		return "user/signUp";
	}
	
	
	// 로그인 페이지 요청
	// http://localhost:80/user/sign-in
	@GetMapping("/sign-in")
	public String signIn() {
		return "user/signIn";
	}
	
	/* 회원 가입 처리
	 * @param dto
	 * @return 리다이렉트 로그인 페이지 처리
	 * */
	// DTO - ObjectMapper
//	유효성검사와 인증검사중에 인증검사 중에!! 
	@PostMapping("/sign-up")
	public String signUpProc(SignUpFormDTO signUpFormDTO) {
		// 1. 유효성검사
		if(signUpFormDTO.getUsername() == null || signUpFormDTO.getUsername().isEmpty()) {
			throw new CustomRestfullException("username을 입력하세요", HttpStatus.BAD_REQUEST);
		}
		
		if(signUpFormDTO.getPassword() == null || signUpFormDTO.getPassword().isEmpty()) {
			throw new CustomRestfullException("password를 입력하세요", HttpStatus.BAD_REQUEST);
		}
		
		if(signUpFormDTO.getFullname() == null || signUpFormDTO.getFullname().isEmpty()) {
			throw new CustomRestfullException("fullname을 입력하세요", HttpStatus.BAD_REQUEST);
		}
		
		int resultRowCount = userService.signUp(signUpFormDTO);
		if(resultRowCount !=1 ) {
			// 다른처리
		}
		return "redirect:/user/sign-in";
	}
	
	@PostMapping("/sign-in")
	public String signInProc(SignInFormDTO signInFormDTO) {
		// 1. 유효성 검사
		if(signInFormDTO.getUsername() == null || signInFormDTO.getUsername().isEmpty()) {
			throw new CustomRestfullException("username을 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(signInFormDTO.getPassword() == null || signInFormDTO.getPassword().isEmpty()) {
			throw new CustomRestfullException("password를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		
		// 서비스 호출
		User printcipal = userService.signIn(signInFormDTO);
//		접근 주체 - printcipal
		session.setAttribute(Define.PRINCIPAL, printcipal); // 세션메모리지에 사용자 정보 저장
		
		
		System.out.println("principal"+printcipal.toString());
		return "redirect:/account/list";
	}
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/user/sign-in";
	}
}
