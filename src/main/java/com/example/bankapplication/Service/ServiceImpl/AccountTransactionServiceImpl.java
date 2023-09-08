package com.example.bankapplication.Service.ServiceImpl;

import com.example.bankapplication.Dto.AccountTransactionDto;
import com.example.bankapplication.Entity.AccountTransaction;
import com.example.bankapplication.Repository.AccountTransactionRepository;
import com.example.bankapplication.Service.AccountTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountTransactionServiceImpl implements AccountTransactionService {
    private final AccountTransactionRepository transactionRepository;
    @Override
    public void saveTransaction(AccountTransactionDto accountTransactionDto) {
    var accountTransaction = AccountTransaction.builder()
            .transactionType(accountTransactionDto.getTransactionType())
            .accountNumber(accountTransactionDto.getAccountNumber())
            .transactionAmount(accountTransactionDto.getTransactionAmount())
            .status("SUCCESS")
            .build();
    transactionRepository.save(accountTransaction);
        System.out.println("Transaction saved successfully");
    }



}
