package com.example.bankapplication.Utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE= "001";
    public static final String ACCOUNT_EXISTS_MESSAGE =  "This user already has an account created";
    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created";
    public static final String ACCOUNT_DOES_NOT_EXISTS_CODE = "003";
    public static final String ACCOUNT_DOES_NOT_EXISTS_MESSAGE = "User with the provided Account number does not exists";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "User found ";
    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Your Account have been credited";
    public static String generateAccountNumber(){
        Year currentYear = Year.now();
        int min = 100_000;
        int max = 999_999;
        //Generate a random number between min and max
        int randNumber = (int)Math.floor (Math.random() * (max - min + 1) + min);
        //convert currentYear and randomNumber to a string and concatenate them
        String year = String.valueOf(currentYear);
        String randomNumber= String.valueOf(randNumber);
        return year + randomNumber;
    }
}
