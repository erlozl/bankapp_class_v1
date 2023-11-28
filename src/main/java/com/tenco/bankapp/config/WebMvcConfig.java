package com.tenco.bankapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tenco.bankapp.handler.AuthIntercepter;

//  @Configuration 스프링부트 클래스이다라는 의미 
@Configuration // IoC 등록 ㅡㅡ> 2개 이상 Ioc 등록처리
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Autowired
	private AuthIntercepter authIntercepter;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authIntercepter)
		.addPathPatterns("/account/**")
		.addPathPatterns("/auth/**"); // 추가하는 방법 
	}
}
