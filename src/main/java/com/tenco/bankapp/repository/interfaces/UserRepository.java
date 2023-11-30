package com.tenco.bankapp.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bankapp.dto.SignInFormDTO;
import com.tenco.bankapp.repository.entity.User;

@Mapper
public interface UserRepository {
	public int insert(User user); // 사용자 등록
	public int updateById(User user); // 사용자 수정
	public int deleteById(Integer id); // 사용자 삭제
	public User findById(Integer id); // 사용자 한명 조회
	public List<User> findAll(); // 사용자 전체 조회
	
	// 사용자 이름과 비밀번호로 조회
	public User findByUsernameAndPassword(SignInFormDTO signInFormDTO);
	
	// 사용자 이름만 조회
	public User findByUsername(String username);
}
