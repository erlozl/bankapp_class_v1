package com.tenco.bankapp.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tenco.bankapp.repository.entity.History;

@Mapper
public interface HistoryRepository {

	// 거래내역 등록
	public int insert(History history);
	public int updateById(History history);
	public int deleteById(Integer id);
	public List<History> findAll();
	public List<History> findByIdAndDynamicType(@Param("type") String type, @Param("accountId") Integer accountId);
	
	// 거래내역 조회
	//public List<History> findByAccountNumber(String id);
	
	// 동적 쿼리 생성
//	거래내역 : 입금/출금/전체
//	반드시 두 개 이상의 파라미터 사용 시 @Param을 사용해야 함

	
}
