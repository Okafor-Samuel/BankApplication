package com.example.bankapplication.Service.ServiceImpl;

import com.example.bankapplication.Dto.*;
import com.example.bankapplication.Entity.User;
import com.example.bankapplication.Repository.UserRepository;
import com.example.bankapplication.Service.EmailService;
import com.example.bankapplication.Service.UserService;
import com.example.bankapplication.Utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    @Override
    public BankResponseDto createAccount(UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())){
            var response = BankResponseDto.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
            return response;
        }

        var newUser = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .otherName(userDto.getOtherName())
                .gender(userDto.getGender())
                .address(userDto.getAddress())
                .stateOfOrigin(userDto.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .alternativePhoneNumber(userDto.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();
        var savedUser = userRepository.save(newUser);

        //Send email alert
        var emailDto = EmailDto.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations, your account have been successfully created. \nAccount Details: \n"+
                "Account Name: "+savedUser.getFirstName()+" "+savedUser.getLastName()+" "+
                        savedUser.getOtherName()+" \nAccount Number: "+
                        savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDto);

        return BankResponseDto.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+ " "+savedUser.getLastName()+ " "+savedUser.getOtherName())
                        .build())
                .build();

    }

    //Balance Enquiry, Name Enquiry, Credit, Debit, Transfer

    @Override
    public BankResponseDto balanceEnquiry(EnquiryDto enquiryDto) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryDto.getAccountNumber());
        if(!isAccountExists){
            return BankResponseDto.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        var foundUser = userRepository.findByAccountNumber(enquiryDto.getAccountNumber());
        var targetUser = foundUser.get();
        return BankResponseDto.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(targetUser.getAccountBalance())
                        .accountNumber(targetUser.getAccountNumber())
                        .accountName(targetUser.getFirstName()+" "+targetUser.getLastName()+" "+targetUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryDto enquiryDto) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryDto.getAccountNumber());
        if(!isAccountExists){
            return AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE;
        }
        var foundUser = userRepository.findByAccountNumber(enquiryDto.getAccountNumber());
        var targetUser = foundUser.get();
        return targetUser.getFirstName()+" "+targetUser.getLastName()+" "+targetUser.getOtherName();
    }

    //Transaction
    @Override
    public BankResponseDto creditAccount(TransactionDto transactionDto) {
        //if account exists or not
        boolean isAccountExists = userRepository.existsByAccountNumber(transactionDto.getAccountNumber());
        if(!isAccountExists){
            return BankResponseDto.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        var userToCredit = userRepository.findByAccountNumber(transactionDto.getAccountNumber());
        var tartgetUser = userToCredit.get();
        tartgetUser.setAccountBalance(tartgetUser.getAccountBalance().add(transactionDto.getAmount()));
        return BankResponseDto.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(tartgetUser.getFirstName()+" "+tartgetUser.getLastName()+" "+tartgetUser.getOtherName())
                        .accountBalance(tartgetUser.getAccountBalance())
                        .accountNumber(transactionDto.getAccountNumber())
                        .build())
                .build();
    }
}
