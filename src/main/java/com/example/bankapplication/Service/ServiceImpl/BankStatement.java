package com.example.bankapplication.Service.ServiceImpl;

import com.example.bankapplication.Entity.AccountTransaction;
import com.example.bankapplication.Repository.AccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankStatement {
    private final AccountTransactionRepository transactionRepository;

    public List<AccountTransaction> generateStatement(String accountNumber, String startDate, String endDate){
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        List<AccountTransaction> transactionList = transactionRepository.findAll()
                .stream()
                .filter(accountTransaction -> accountTransaction.getAccountNumber().equals(accountNumber))
                .filter(accountTransaction -> accountTransaction.getCreatedAt().equals(start))
                .filter(accountTransaction -> accountTransaction.getCreatedAt().equals(end)).toList();
        return transactionList;
    }
}
