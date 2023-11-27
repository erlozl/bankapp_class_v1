package com.tenco.bankapp.repository.entity;

import java.sql.Timestamp;

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
}
