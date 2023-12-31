package com.example.bankapplication.Service;

import com.example.bankapplication.Dto.*;

public interface UserService {
    BankResponseDto createAccount(UserDto userDto);
    BankResponseDto balanceEnquiry(EnquiryDto enquiryDto);
    String nameEnquiry(EnquiryDto enquiryDto);
    BankResponseDto creditAccount(TransactionDto transactionDto);
    BankResponseDto debitAccount(TransactionDto transactionDto);
    BankResponseDto transfer(TransferDto transferDto);
}
