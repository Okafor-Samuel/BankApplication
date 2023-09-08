package com.example.bankapplication.Repository;

import com.example.bankapplication.Entity.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, String> {
}
