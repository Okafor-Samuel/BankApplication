package com.example.bankapplication.Service;

import com.example.bankapplication.Dto.AccountTransactionDto;
import com.example.bankapplication.Entity.AccountTransaction;

public interface AccountTransactionService {
    void saveTransaction(AccountTransactionDto accountTransactionDto);
}
