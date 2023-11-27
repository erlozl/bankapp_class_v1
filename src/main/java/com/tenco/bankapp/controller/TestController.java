package com.tenco.bankapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // IoC에 대상 
@RequestMapping("/temp")
//RequestMapping어노테이션을 붙이면 자동으로 temp폴더가 지정이 됨
//실행의 제어권이 없으면 라이브러리, 실행의 제어권이 있으면 프레임워크
public class TestController {
	
	
	//GET
	//주소설계 - http://localhost:80/temp/temp-test
	@GetMapping("/temp-test")
	public String tempTest() {
		return "temp"; 
	}
	
	//GET
	//주소설계 - http://localhost:80/temp/main-page
	@GetMapping("/main-page")
	public String tempMainPage() {
		return "main";
	}
	
}
