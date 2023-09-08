package com.example.bankapplication.Controller;

import com.example.bankapplication.Dto.*;
import com.example.bankapplication.Service.ServiceImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Management APIs")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Operation(summary = "Create New User Account", description = "Creating a new user and assigning an account ID")
    @ApiResponse(responseCode = "201", description = "Http Status 201 CREATED")
    @PostMapping("/create-account")
    public BankResponseDto createAccount(@RequestBody UserDto userDto){
        return userServiceImpl.createAccount(userDto);
    }

    @Operation(summary = "Balance Enquiry", description = "Check the account balance")
    @ApiResponse(responseCode = "200", description = "Http Status 201 SUCCESS")
    @GetMapping("/balance-enquiry")
    public BankResponseDto balanceEnquiry(@RequestBody EnquiryDto enquiryDto){
        return userServiceImpl.balanceEnquiry(enquiryDto);
    }

    @Operation(summary = "Name Enquiry", description = "Check account name")
    @ApiResponse(responseCode = "200", description = "Http Status 201 SUCCESS")
    @GetMapping("/name-enquiry")
    public String nameEnquiry(@RequestBody EnquiryDto enquiryDto){
        return userServiceImpl.nameEnquiry(enquiryDto);
    }

    @Operation(summary = "Credit Account", description = "Credit account of a user")
    @ApiResponse(responseCode = "200", description = "Http Status 201 SUCCESS")
    @PostMapping("/credit")
    public BankResponseDto creditAccount(@RequestBody TransactionDto transactionDto){
        return  userServiceImpl.creditAccount(transactionDto);
    }

    @Operation(summary = "Debit Account", description = "Debit account of a user")
    @ApiResponse(responseCode = "200", description = "Http Status 201 SUCCESS")
    @PostMapping("/debit")
    public BankResponseDto debitAccount(@RequestBody TransactionDto transactionDto){
        return  userServiceImpl.debitAccount( transactionDto);
    }

    @Operation(summary = "Transfer ", description = "Making transfer that involves crediting and debiting accounts simultaneously")
    @ApiResponse(responseCode = "200", description = "Http Status 201 SUCCESS")
    @PostMapping("/transfer")
    public BankResponseDto transfer(@RequestBody TransferDto transferDto){
        return userServiceImpl.transfer(transferDto);
    }
}
