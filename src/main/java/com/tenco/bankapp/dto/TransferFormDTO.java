package com.tenco.bankapp.dto;

import lombok.Data;

@Data
public class TransferFormDTO {
	private Long amount;
	private String dAccountNumber;
	private String wAccountNumber;
	private String password;
}
