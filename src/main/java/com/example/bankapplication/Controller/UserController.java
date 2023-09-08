package com.example.bankapplication.Controller;

import com.example.bankapplication.Dto.*;
import com.example.bankapplication.Service.ServiceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    @PostMapping("/create-account")
    public BankResponseDto createAccount(@RequestBody UserDto userDto){
        return userServiceImpl.createAccount(userDto);
    }

    @GetMapping("/balance-enquiry")
    public BankResponseDto balanceEnquiry(@RequestBody EnquiryDto enquiryDto){
        return userServiceImpl.balanceEnquiry(enquiryDto);
    }
    @GetMapping("/name-enquiry")
    public String nameEnquiry(@RequestBody EnquiryDto enquiryDto){
        return userServiceImpl.nameEnquiry(enquiryDto);
    }
    @PostMapping("/credit")
    public BankResponseDto creditAccount(@RequestBody TransactionDto transactionDto){
        return  userServiceImpl.creditAccount(transactionDto);
    }
    @PostMapping("/debit")
    public BankResponseDto debitAccount(@RequestBody TransactionDto transactionDto){
        return  userServiceImpl.debitAccount( transactionDto);
    }
    @PostMapping("/transfer")
    public BankResponseDto transfer(@RequestBody TransferDto transferDto){
        return userServiceImpl.transfer(transferDto);
    }
}
