package com.tenco.bankapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.tokens.DocumentEndToken;

import com.tenco.bankapp.dto.SignInFormDTO;
import com.tenco.bankapp.dto.SignUpFormDTO;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.repository.interfaces.UserRepository;

@Service
public class UserService {

	@Autowired // 의존주입(생성자, 메서드)
	private UserRepository userRepository;
	
	@Transactional
	public int signUp(SignUpFormDTO signUpFormDTO) {
		// User, // SignUpFormDTO
//		username 중복 여부 확인 생략
		User user = User.builder()
			.username(signUpFormDTO.getUsername())
			.password(signUpFormDTO.getPassword())
			.fullname(signUpFormDTO.getFullname())
			.build();  // build() 반드시 호출
		
		
		int resultRowCount = userRepository.insert(user);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("회원가입실패",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resultRowCount;
	}

	public User signIn(SignInFormDTO signInFormDTO) {
		User userEntity = userRepository.findByUsernameAndPassword(signInFormDTO);
		if(userEntity == null) {
			throw new CustomRestfullException("아이디 혹은 비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);
		} return userEntity;
	}
	
}
