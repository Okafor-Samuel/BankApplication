package com.example.bankapplication.Service.ServiceImpl;

import com.example.bankapplication.Configuration.JwtTokenProvider;
import com.example.bankapplication.Dto.*;
import com.example.bankapplication.Entity.User;
import com.example.bankapplication.Repository.AccountTransactionRepository;
import com.example.bankapplication.Repository.UserRepository;
import com.example.bankapplication.Service.EmailService;
import com.example.bankapplication.Service.UserService;
import com.example.bankapplication.Utils.AccountUtils;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AccountTransactionServiceImpl transactionServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
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
                .password(passwordEncoder.encode(userDto.getPassword()))
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

    public BankResponseDto logIn(LoginDto loginDto){
        Authentication authentication = null;
        authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getPassword(), loginDto.getEmail()));

        EmailDto loginAlert = EmailDto.builder()
                .subject("You are logged in!")
                .recipient(loginDto.getEmail())
                .messageBody("You logged into your account. If you did not initiate this request, please contact your bank")
                .build();
        emailService.sendEmailAlert(loginAlert);
        return  BankResponseDto.builder()
                .responseCode("Login success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
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
        var targetUser = userToCredit.get();
        targetUser.setAccountBalance(targetUser.getAccountBalance().add(transactionDto.getAmount()));
        userRepository.save(targetUser);
        //accountTransaction implementation to keep record
        var accountTransactionDto = AccountTransactionDto.builder()
                .accountNumber(targetUser.getAccountNumber())
                .transactionType("CREDIT")
                .transactionAmount(transactionDto.getAmount() )
                .build();
        transactionServiceImpl.saveTransaction(accountTransactionDto);

        return BankResponseDto.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(targetUser.getFirstName()+" "+targetUser.getLastName()+" "+targetUser.getOtherName())
                        .accountBalance(targetUser.getAccountBalance())
                        .accountNumber(transactionDto.getAccountNumber())
                        .build())
                .build();

    }
    @Override
    public BankResponseDto debitAccount(TransactionDto transactionDto) {
        boolean isAccountExists = userRepository.existsByAccountNumber(transactionDto.getAccountNumber());
        if(!isAccountExists){
            return BankResponseDto.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        var userToDebit= userRepository.findByAccountNumber(transactionDto.getAccountNumber());
        var targetUser= userToDebit.get();
        BigInteger availableBalance= targetUser.getAccountBalance().toBigInteger();
        BigInteger debitAmount= transactionDto.getAmount().toBigInteger();
        if(availableBalance.intValue() < debitAmount.intValue()){
            return BankResponseDto.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCES_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else {
             targetUser.setAccountBalance(targetUser.getAccountBalance().subtract(transactionDto.getAmount()));
             userRepository.save(targetUser);
            //accountTransaction implementation to keep record
            var accountTransactionDto = AccountTransactionDto.builder()
                    .accountNumber(targetUser.getAccountNumber())
                    .transactionType("DEBIT")
                    .transactionAmount(transactionDto.getAmount() )
                    .build();
            transactionServiceImpl.saveTransaction(accountTransactionDto);
             return BankResponseDto.builder()
                     .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                     .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                     .accountInfo(AccountInfo.builder()
                             .accountNumber(transactionDto.getAccountNumber())
                             .accountName(targetUser.getFirstName()+" "+targetUser.getLastName()+" "+targetUser.getOtherName())
                             .accountBalance(targetUser.getAccountBalance())
                             .build())
                     .build();
        }

    }

    //Transfer
    @Override
    public BankResponseDto transfer(TransferDto transferDto) {
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(transferDto.getDestinationAccountNumber());
        if(!isDestinationAccountExists){
            return BankResponseDto.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        var sourceAccountUser = userRepository.findByAccountNumber(transferDto.getSourceAccountNumber());
        var sourceUser = sourceAccountUser.get();
        if(transferDto.getAmount().compareTo(sourceUser.getAccountBalance())>0){
            return BankResponseDto.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCES_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceUser.setAccountBalance(sourceUser.getAccountBalance().subtract(transferDto.getAmount()));
        String sourceName = sourceUser.getFirstName()+" "+sourceUser.getLastName()+" "+sourceUser.getOtherName();
        userRepository.save(sourceUser);
        //accountTransaction implementation to keep record
        var accountTransactionDto = AccountTransactionDto.builder()
                .accountNumber(sourceUser.getAccountNumber())
                .transactionType("DEBIT TRANSACTION")
                .transactionAmount(transferDto.getAmount() )
                .build();
        transactionServiceImpl.saveTransaction(accountTransactionDto);
        var debitAlert = EmailDto.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceUser.getEmail())
                .messageBody("The sum of "+transferDto.getAmount()+" has been debited from your account. Your current balance is "
                +sourceUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        var destinationAccountUser =userRepository.findByAccountNumber(transferDto.getDestinationAccountNumber());
        var destinationUser = destinationAccountUser.get();
        destinationUser.setAccountBalance(destinationUser.getAccountBalance().add(transferDto.getAmount()));
        //String destinationName = destinationUser.getFirstName()+" "+destinationUser.getLastName()+" "+destinationUser.getOtherName();
        userRepository.save(destinationUser);
        //accountTransaction implementation to keep record
        var accountTransactionDto1 = AccountTransactionDto.builder()
                .accountNumber(destinationUser.getAccountNumber())
                .transactionType("CREDIT")
                .transactionAmount(transferDto.getAmount() )
                .build();
        transactionServiceImpl.saveTransaction(accountTransactionDto);
        var creditAlert = EmailDto.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationUser.getEmail())
                .messageBody("The sum of "+transferDto.getAmount()+" has been credited to your account from "+sourceName+". Your current balance is "
                        +destinationUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);
        return BankResponseDto.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();
    }

}
