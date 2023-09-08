package com.example.bankapplication.Service;

import com.example.bankapplication.Dto.BankResponseDto;
import com.example.bankapplication.Dto.EnquiryDto;
import com.example.bankapplication.Dto.TransactionDto;
import com.example.bankapplication.Dto.UserDto;

public interface UserService {
    BankResponseDto createAccount(UserDto userDto);
    BankResponseDto balanceEnquiry(EnquiryDto enquiryDto);
    String nameEnquiry(EnquiryDto enquiryDto);
    BankResponseDto creditAccount(TransactionDto transactionDto);
    BankResponseDto debitAccount(TransactionDto transactionDto);
}
