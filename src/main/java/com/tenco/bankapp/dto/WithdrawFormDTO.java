package com.tenco.bankapp.dto;

import lombok.Data;

@Data
public class WithdrawFormDTO {
	private Long amount;
	private String wAccountNumber;
	private String password;
	
}
