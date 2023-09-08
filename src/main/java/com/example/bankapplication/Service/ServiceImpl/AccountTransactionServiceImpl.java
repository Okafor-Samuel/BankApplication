package com.example.bankapplication.Service.ServiceImpl;

import com.example.bankapplication.Entity.AccountTransaction;
import com.example.bankapplication.Service.AccountTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountTransactionServiceImpl implements AccountTransactionService {
    @Override
    public void saveTransaction(AccountTransaction accountTransaction) {

    }
}
