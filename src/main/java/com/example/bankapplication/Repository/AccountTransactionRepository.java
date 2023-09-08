package com.example.bankapplication.Repository;

import com.example.bankapplication.Entity.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, String> {
}
