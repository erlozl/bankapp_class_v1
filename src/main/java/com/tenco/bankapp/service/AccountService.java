package com.tenco.bankapp.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bankapp.dto.DepositFormDTO;
import com.tenco.bankapp.dto.SaveFormDTO;
import com.tenco.bankapp.dto.TransferFormDTO;
import com.tenco.bankapp.dto.WithdrawFormDTO;
import com.tenco.bankapp.handler.exception.CustomRestfullException;
import com.tenco.bankapp.repository.entity.Account;
import com.tenco.bankapp.repository.entity.History;
import com.tenco.bankapp.repository.interfaces.AccountRepository;
import com.tenco.bankapp.repository.interfaces.HistoryRepository;

@Service // Ioc 대상 + 싱글콘 관리
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private HistoryRepository historyRepository;
	
	/*
	 * 계좌 생성 기능 
	 * @ param dto
	 * @ param pricipalId
	 * */
	
	@Transactional
	public void createAccount(SaveFormDTO saveFormDTO, Integer principalId) {
//		계좌 중복 여부 확인
		System.out.println("1111");
		Account account = Account.builder()
				.number(saveFormDTO.getNumber())
				.password(saveFormDTO.getPassword())
				.balance(saveFormDTO.getBalance())
				.userId(principalId)
				.build();
		
		int resultRowCount = accountRepository.insert(account);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("계좌 생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 계좌 목록 보기 기능
	public List<Account> readAccountList(Integer userId) {
		List<Account> list = accountRepository.findByUserId(userId);
		return list;
	}

	// 출금 기능 로직 고민해보기
//	1. 계좌 존재 여부 확인
//	2. 본인 계좌 여부 확인
//	3. 계좌 비번 확인
//	4. 잔액 여부 확인
//	5. 출금 처리
//	6. 거래 내역 등록
//	7. 트랜잭션 처리
	@Transactional
	public void updateAccountWithdraw(WithdrawFormDTO dto, Integer principalId) {
		Account accountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
//		1.
		if(accountEntity == null) {
//			계좌번호가 없는 상황
			throw new CustomRestfullException("해당 계좌가 없습니다", HttpStatus.BAD_REQUEST);	
		}
//		2.
		if(accountEntity.getUserId() != principalId) {
			throw new CustomRestfullException("본인 소유 계좌가 아닙니다", HttpStatus.UNAUTHORIZED);
		}
//		3.
		if(accountEntity.getPassword().equals(dto.getPassword()) == false) {
			throw new CustomRestfullException("출금 계좌 비밀번호가 틀렸습니다", HttpStatus.UNAUTHORIZED);
		}
//		4.
		if(accountEntity.getBalance() < dto.getAmount()) {
			throw new CustomRestfullException("계좌 잔액이 부족합니다.", HttpStatus.UNAUTHORIZED);
		}
//		5. (객체 모델 상태값 변경 처리 )
		accountEntity.withdraw(dto.getAmount());
		accountRepository.updateById(accountEntity);
		
//		6. 거래 내역 등록
		History history = new History();
		history.setAmount(dto.getAmount());
		// 출금 거래 내역에서는 사용자가 출금 후에 잔액을 입력함
		history.setWBalance(accountEntity.getBalance());
		history.setDBalance(null);
		history.setWAccountId(accountEntity.getId());
		history.setDAccountId(null);
		int resultRowCount = historyRepository.insert(history);
		
		if(resultRowCount != 1) {
			throw new CustomRestfullException("정상처리되지 않았습니다", HttpStatus.UNAUTHORIZED);
		}
//		ㅡ> @Mapper 어노테이션이 1이면 성공 0이면 실패를 반환함 
//		그냥 1건이라고 보면 됨 
		
//		출금된 내역이 Null이면 입금
//		입금된 내역이 null이면 출금
//		둘다 null이면 이체
	}

	@Transactional
	public void updateAccountDeposit(DepositFormDTO dto, Integer principalId) {
		Account accountEntity = accountRepository.findByNumber(dto.getDAccountNumber());
//		1.
		if(accountEntity == null) {
//			계좌번호가 없는 상황
			throw new CustomRestfullException("해당 계좌가 없습니다", HttpStatus.BAD_REQUEST);	
		}
//		2.
		if(accountEntity.getUserId() != principalId) {
			throw new CustomRestfullException("본인 소유 계좌가 아닙니다", HttpStatus.UNAUTHORIZED);
		}
//		4.
		if(accountEntity.getBalance() < dto.getAmount()) {
			throw new CustomRestfullException("계좌 잔액이 부족합니다.", HttpStatus.UNAUTHORIZED);
		}
//		5. (객체 모델 상태값 변경 처리 )
		accountEntity.deposit(dto.getAmount());
		accountRepository.updateById(accountEntity);
		
//		6. 거래 내역 등록
		History history = new History();
		history.setAmount(dto.getAmount());
		// 출금 거래 내역에서는 사용자가 출금 후에 잔액을 입력함
		history.setWBalance(history.getWBalance());
		history.setDBalance(accountEntity.getBalance());
		history.setWAccountId(null);
		history.setDAccountId(accountEntity.getId());
		int resultRowCount = historyRepository.insert(history);
		
		if(resultRowCount != 1) {
			throw new CustomRestfullException("정상처리되지 않았습니다", HttpStatus.UNAUTHORIZED);
		}
	}

	@Transactional
	public void updateAccountTransfer(TransferFormDTO dto, Integer principalId) {
//		1.
		Account withdrawAccountEntity = accountRepository.findByNumber(dto.getWAccountNumber());

//		2.
		Account depositAccountEntity = accountRepository.findByNumber(dto.getDAccountNumber());
		
		if(withdrawAccountEntity == null) {
//			계좌번호가 없는 상황
			throw new CustomRestfullException("출금 계좌가 없습니다", HttpStatus.BAD_REQUEST);	
		}
		if(depositAccountEntity == null) {
//			계좌번호가 없는 상황
			throw new CustomRestfullException("입금 계좌가 없습니다", HttpStatus.BAD_REQUEST);	
		}
		
//		3.
		withdrawAccountEntity.checkOwner(principalId);
		withdrawAccountEntity.checkPassword(dto.getPassword());
		withdrawAccountEntity.checkBalance(dto.getAmount());
		withdrawAccountEntity.withdraw(dto.getAmount());
		accountRepository.updateById(withdrawAccountEntity);
		
		depositAccountEntity.checkOwner(principalId);
		depositAccountEntity.deposit(dto.getAmount());
		accountRepository.updateById(depositAccountEntity);
		
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWBalance(withdrawAccountEntity.getBalance());
		history.setDBalance(depositAccountEntity.getBalance());
		history.setWAccountId(withdrawAccountEntity.getId());
		history.setDAccountId(depositAccountEntity.getId());
		
		int resultRowCount = historyRepository.insert(history);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("정상처리 되지 않았습니다", HttpStatus.BAD_REQUEST);
		}
		
	}	
	
	/**
	 * 단일 계좌 조회
	 * @param accountId
	 * @return
	 */
	
	public Account findById(Integer accountId) {
		Account accountEntity = accountRepository.findById(accountId);
		if(accountEntity == null) {
			throw new CustomRestfullException("해당 계좌를 찾을 수 없습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return accountEntity;
	}
	
	/**
	 * 
	 * @param type = [all, deposit, withdraw]
	 * @param accountId
	 * @return 입금내역, 출금내역, 입출금내역
	 */

	public List<History> readHistoryListByAccount(String type, Integer accountId) {
		List<History> historyEntity = historyRepository.findByIdAndDynamicType(type, accountId);
		
		return historyEntity;
	}
}
