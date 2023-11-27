package com.tenco.bankapp.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bankapp.handler.exception.CustomPageException;

/*
 * view렌더링을 위해 ModelView
 * 객체를 반환하도록 설정 되어 있다.
 * 예외 처리 page 리턴할 때 사용
 * */

//CustomPageException이라는 게 뜬다면 ExcepionHandler에서 저걸 낚아채서 넘김

@ControllerAdvice
//전역 컨트롤러에서 발생하는 예외를 처리하기 위한 클래스
public class MyPageExceptionHandler {
	
	@ExceptionHandler(CustomPageException.class)
//	특정 예외가 발생했을 때, CustomPageException이 Throw로 다른 곳에서 뜬다면
//	발생했을 때 호출될 메서드 정의
	public ModelAndView handleRunTimeException(CustomPageException e) {
//		ModelAndView = 예외 처리 결과를 보여줄 뷰와 함께 사용, 그게 errorPage
		ModelAndView modelAndView = new ModelAndView("errorPage");
		modelAndView.addObject("statusCode",HttpStatus.NOT_FOUND.value());
//		addObject = 뷰에 전달할 데이터를 추가 
		modelAndView.addObject("message", e.getMessage());
		return modelAndView;
	}
}
