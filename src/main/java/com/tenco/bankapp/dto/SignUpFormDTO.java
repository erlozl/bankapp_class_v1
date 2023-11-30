package com.tenco.bankapp.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // data를 보내려면 setter가 필요함 
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpFormDTO {
	private String username; // form에 있는 name 속성과 똑같이
	private String password;
	private String fullname;
	private MultipartFile file; // name속성과 일치 !!
	private String orginFileName;
	private String uploadFileName;
//	사용자가 올린 사진 이름과, 올린 파일과
//	서버에 저장을 하려고 할 때 file처리를 할 때 기본적으로 name을가지고 있어야 할
	
}
