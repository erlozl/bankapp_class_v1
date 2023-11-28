package com.tenco.bankapp.repository.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import com.tenco.bankapp.utils.CommonUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//거래 내역
public class History { 
	private Integer id;
	private Long amount; // 거래 금액
	private Long wBalance; // 출금 후 계좌 잔액
	private Long dBalance; // 입금 후 계좌 잔액 
	private Integer wAccountId;
	private Integer dAccountId;
	private Timestamp createdAt;
	
	// 거래 내역 정보 추가
	private String sender;
	private String receiver;
	private Long balance;
	
	public String formatCreatedAt() {
		return CommonUtil.timestampToString(createdAt);
	}

	public String formatBalance() {
		return CommonUtil.unitFormat(balance);
	}

}
