package com.tenco.bankapp.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.devtools.remote.server.HttpHeaderAccessManager;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.tokens.DocumentEndToken;

import com.tenco.bankapp.dto.SignInFormDTO;
import com.tenco.bankapp.dto.SignUpFormDTO;
import com.tenco.bankapp.dto.response.KakaoProfile;
import com.tenco.bankapp.dto.response.OAuthToken;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.service.UserService;
import com.tenco.bankapp.utils.Define;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user") // 대문달기
@Slf4j
public class UserController {
	
	@Autowired // DI처리
	private UserService userService;
	
	@Autowired
	private HttpSession session;
	
	@Value("${tenco.key}")
	private String tencoKey;
	
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
		
		MultipartFile file = signUpFormDTO.getFile();
		if(file.isEmpty() == false) {
			// 파일 사이즈 체크
			if(file.getSize() > Define.MAX_FILE_SIZE) {
				throw new CustomRestfullException("파일 크기는 20MB 이상 클 수 없습니다", HttpStatus.BAD_REQUEST);
			}
		}
		
		try {
			// 업로드 파일 경로
			String saveDirectory = Define.UPLOAD_DIRECTORY;
//			폴더가 없다면 오류 발생
			File dir = new File(saveDirectory);
			if(dir.exists() == false) {
				dir.mkdir(); // 폴더가 없다면 생성
			} 
			// 파일 이름 (중복 처리 예방)
			
//			랜덤한 값 제공
			UUID uuid = UUID.randomUUID();
			String fileName = uuid + "_" + file.getOriginalFilename();
			// 새로운 경로 지정 생성
			String uploadPath = Define.UPLOAD_DIRECTORY + File.separator + fileName;
			System.out.println("uploadPath" + uploadPath);
			File destinationFile = new File(uploadPath);
			
			// 반드시 사용
			file.transferTo(destinationFile); // 실제 생성!!!!!!!!!!!!
			System.out.println("-------------"+destinationFile);
//			객체 상태 변경 ( insert 처리하기 위함이라 수정해야 함 )
			signUpFormDTO.setOrginFileName(file.getOriginalFilename());
			signUpFormDTO.setUploadFileName(fileName);
			
			
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		int resultRowCount = userService.signUp(signUpFormDTO);
		if(resultRowCount !=1 ) {
			// 다른처리
		}
		return "redirect:/user/sign-in";
	}
	
//	@ ResponseBody 데이터 반환 , 
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
	
	// http://localhost:80/user/kakao-callback?code=암호화
	// @ResponseBody
	@GetMapping("/kakao-callback")
	public String kakaoCallBack(@RequestParam String code) {
		System.out.println("메서드 동작 확인");
		// 액세스 토큰 요청 --> Server to Server
		
		RestTemplate rt1 = new RestTemplate();
		// 헤더 구성
		HttpHeaders headers1 = new HttpHeaders();
		headers1.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// body 구성
		MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
		params1.add("grant_type", "authorization_code");
		params1.add("client_id", "7704f1d9216c56f7151b9a631f727027");
		params1.add("redirect_uri", "http://localhost:80/user/kakao-callback");
		params1.add("code", code);
		
		// 헤더 + body 결합
		HttpEntity<MultiValueMap<String, String>> requestMsg1
			= new HttpEntity<>(params1, headers1);
		
		// 요청 처리
		ResponseEntity<OAuthToken> response1 = rt1.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, requestMsg1, OAuthToken.class);
		System.out.println("-----------------");
		System.out.println(response1.getHeaders());
		System.out.println(response1.getBody());
		System.out.println(response1.getBody().getAccess_token());
		System.out.println(response1.getBody().getRefresh_token());
		System.out.println("-----------------");
		// 여기까지 토큰 받기 위함 // 
		
		RestTemplate rt2 = new RestTemplate();
		//헤더 구성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + response1.getBody().getAccess_token());
		headers2.add("Content-type", "Content-type: application/x-www-form-urlencoded;charset=utf-8");
		// 바디 구성 - 생략 ( 필수가 아님 )

		// 헤더 바디 결합
		HttpEntity<MultiValueMap<String, String>> requestMsg2 = new HttpEntity<>(headers2);
		
		// 요청
		ResponseEntity<KakaoProfile> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, requestMsg2, KakaoProfile.class);
		System.out.println("---------------------");
		System.out.println(response2.getBody());
		System.out.println(response2.getBody().getProperties().getNickname());
		System.out.println("-----카카오 서버 정보 받기 완료------");
		
//		카카오 서버에 존재하는 정보를 요청 처리
		
		// 1. 회원 가입 여부
		KakaoProfile kakaoProfile = response2.getBody();
		// 소셜 회원 가입자는 전부 비번이 동일하게 된다.
		SignUpFormDTO signUpFormDTO = SignUpFormDTO
				.builder()
				.username("OAuth_"+kakaoProfile.getId()+"님")
				.fullname("Kakao")
				.password(tencoKey)
				.file(null)
				.orginFileName(null)
				.uploadFileName(null)
				.build();
		
		System.out.println("tencoKey :" + tencoKey);
		
		
		// null 일 때는 세션에 로그인을 하기 위해 값을 할당해주어야 한다.
		User oldUser = userService.searchUsername(signUpFormDTO.getUsername());
		System.out.println("테스트" + oldUser);
		if(oldUser == null) {
			// oldUser null 이라면 최초 회원가입 처리를 해주어야 한다 
			// 회원가입 자동 처리
			userService.signUp(signUpFormDTO); // 회원가입 처리됨
			oldUser = userService.searchUsername(signUpFormDTO.getUsername());
		}
		
		// 로그인 처리 - 보안때문에 null 처리
	
		oldUser.setPassword(null); 
		session.setAttribute(Define.PRINCIPAL, oldUser);
		
		// 최초 사용자라면 우리 사이트에 회원 가입을 자동 완료
		// 추가 정보 입력 화면 ( 추가 정보 있다면 기능을 만들기) --> DB저장 처리
		// 만약 소셜 로그인 사용자가 회원가입 처리 완료된 사용자라면
		// 바로 세션 처리 및 로그인 처리
		
		
		return "redirect:/account/list";
	}
}
