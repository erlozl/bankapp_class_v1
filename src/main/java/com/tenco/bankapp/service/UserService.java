package com.tenco.bankapp.service;

import org.eclipse.jdt.internal.compiler.classfmt.NonNullDefaultAwareTypeAnnotationWalker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.tokens.DocumentEndToken;

import com.mysql.cj.callback.UsernameCallback;
import com.tenco.bankapp.dto.SignInFormDTO;
import com.tenco.bankapp.dto.SignUpFormDTO;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.repository.interfaces.UserRepository;

@Service
public class UserService {

	@Autowired // 의존주입(생성자, 메서드)
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
//	실제 구현 클래스는 WebMvcConfig에 있는 passwordEncoder
	

	public int signUp(SignUpFormDTO signUpFormDTO) {
//		String rawPwd = signUpFormDTO.getPassword();
//		String hashPwd = passwordEncoder.encode(rawPwd);
////		ㅡ> 사용자가 넣었던 값을 암호화 처리
//		System.out.println("hashPwd :" + hashPwd);
//		
		
		
		// User, // SignUpFormDTO
//		username 중복 여부 확인 생략
		User user = User.builder()
			.username(signUpFormDTO.getUsername())
			.password(passwordEncoder.encode(signUpFormDTO.getPassword()))
			.fullname(signUpFormDTO.getFullname())
			.originFileName(signUpFormDTO.getOrginFileName())
			.uploadFileName(signUpFormDTO.getUploadFileName())
			.build();  // build() 반드시 호출
		
		
		int resultRowCount = userRepository.insert(user);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("회원가입실패",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resultRowCount;
	}
	
	@Transactional
	public User signIn(SignInFormDTO signInFormDTO) {
//		1. Username 아이디 존재 여부 확인
		User userEntity = userRepository.findByUsername(signInFormDTO.getUsername());
		if(userEntity == null) {
			throw new CustomRestfullException("존재하지 않는 계정입니다", HttpStatus.BAD_REQUEST);
		}
		
//		2. 객체 상태값에 비번과 암호화된 비번 일치 여부 확인
		
		boolean isPwdMatched = passwordEncoder.matches(signInFormDTO.getPassword(), userEntity.getPassword());
		System.out.println("내가입력한값"+ signInFormDTO.getPassword());
		System.out.println("회원가입한값" + userEntity.getPassword());
		
		if(isPwdMatched == false) {
			throw new CustomRestfullException("비밀번호가 잘못되었습니다", HttpStatus.BAD_REQUEST);
		}
		
		if(userEntity == null) {
			throw new CustomRestfullException("아이디 혹은 비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);
		} return userEntity;
	}

	
	public User searchUsername(String username) {
		
		return userRepository.findByUsername(username);
	}
	
}
