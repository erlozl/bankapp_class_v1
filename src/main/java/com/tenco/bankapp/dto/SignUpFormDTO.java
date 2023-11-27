package com.tenco.bankapp.dto;

import lombok.Data;

@Data // data를 보내려면 setter가 필요함 
public class SignUpFormDTO {
	private String username; // form에 있는 name 속성과 똑같이
	private String password;
	private String fullname;
	
}
