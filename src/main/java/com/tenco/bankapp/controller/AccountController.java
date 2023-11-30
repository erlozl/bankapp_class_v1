package com.tenco.bankapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.tokens.DocumentEndToken;

import com.tenco.bankapp.dto.DepositFormDTO;
import com.tenco.bankapp.dto.SaveFormDTO;
import com.tenco.bankapp.dto.TransferFormDTO;
import com.tenco.bankapp.dto.WithdrawFormDTO;
import com.tenco.bankapp.handler.exception.CustomPageException;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.handler.exception.UnAuthorizedException;
import com.tenco.bankapp.repository.entity.Account;
import com.tenco.bankapp.repository.entity.History;
import com.tenco.bankapp.repository.entity.User;
import com.tenco.bankapp.service.AccountService;
import com.tenco.bankapp.utils.Define;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private HttpSession session;
	@Autowired
	private AccountService accountService;

	@GetMapping({"/list","/"})
	public String list(Model model) {
//		인증된 사람만 목록 확인 가능
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		
		List<Account> accountList = accountService.readAccountList(principal.getId());
//		Slf4j=성능적으로 좋음, 비동기적으로 돌아가기 때문에 
		
		if(accountList.isEmpty()) {
			model.addAttribute("accountList",null);
		} else {
			model.addAttribute("accountList",accountList);
		}
		
		return "account/list";
	}
	
	@GetMapping("/save")
	public String save() {
		return "account/save";
	}
	
	@PostMapping("/save")
	public String saveProc(SaveFormDTO saveFormDTO) {
		// 1.인증검사
		
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		
		// 2.유효성검사
		if(saveFormDTO.getNumber() == null || saveFormDTO.getNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(saveFormDTO.getPassword() == null || saveFormDTO.getPassword().isEmpty()) {
			throw new CustomRestfullException("계좌비밀번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(saveFormDTO.getBalance() == null || saveFormDTO.getBalance() <= 0) {
			throw new CustomRestfullException("잘못된 입력입니다", HttpStatus.BAD_REQUEST);
		}
		
		accountService.createAccount(saveFormDTO, principal.getId());
		
		return "redirect:/account/list";
	}
	
	//출금 페이지 요청
	
	@GetMapping("/withdraw")
	public String withdraw() {
		
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		
		return "account/withdraw";
				 
	}
	
	@PostMapping("/withdraw")
	public String withdrawProc(WithdrawFormDTO dto) {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount().longValue() <=0) {
			throw new CustomRestfullException("출금 금액이 0원 이하일 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountNumber() == null || dto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("계좌 번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("비밀 번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		
		accountService.updateAccountWithdraw(dto,principal.getId());
		return "redirect:/account/list";
	}
	
	@GetMapping("/deposit")
	public String deposit() {
		return "account/deposit";
				 
	}
	
	@PostMapping("/deposit")
	public String depositProc(DepositFormDTO dto) {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount().longValue() <=0) {
			throw new CustomRestfullException("입금 금액이 0원 이하일 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if(dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("계좌 번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		
		accountService.updateAccountDeposit(dto,principal.getId());
		return "redirect:/account/list";
	}
	
	
	@GetMapping("/transfer")
	public String transfer() {
	
		return "account/transfer";		 
	}
	
	
	@PostMapping("/transfer")
	public String transferProc(TransferFormDTO dto) {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);

		if(dto.getAmount() == null) {
			throw new CustomRestfullException("이체 금액을 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount().longValue() <=0) {
			throw new CustomRestfullException("이체 금액이 0원 이하일 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountNumber() == null || dto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("출금 계좌번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("이체 계좌번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("비밀번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		
		accountService.updateAccountTransfer(dto,principal.getId());
		return "redirect:/account/list";
	}
	// 계좌 상세보기 화면 요청 처리 - 데이터를 입력 받는 방법 정리
	// http://localhost/account/detail/1
	// http://localhost/account/detail/1?type=deposit
	// http://localhost/account/detail/1?type=withdraw
	// 기본값 세팅 가능 
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable(name="id") Integer accountId, @RequestParam(name= "type",defaultValue = "all", required = false) String type, Model model) {
		// 인증검사, 유효성 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);

		// 상세 보기 화면 요청시 --> 데이터를 내려주어야 함
		// account 데이터, 접근 주체, 거래 내역 정보 
		Account account = accountService.findById(accountId);
		List<History> historyList = accountService.readHistoryListByAccount(type, accountId); 
		
		model.addAttribute("account", account);
		model.addAttribute(Define.PRINCIPAL, principal);
		model.addAttribute("historyList", historyList);
		return "account/detail";
	}
	
}



