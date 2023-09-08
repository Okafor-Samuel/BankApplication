package com.example.bankapplication.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTransactionDto {
    private String transactionType;
    private BigDecimal transactionAmount;
    private String accountNumber;
}
