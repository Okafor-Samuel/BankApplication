package com.example.bankapplication.Service;

import com.example.bankapplication.Dto.BankResponseDto;
import com.example.bankapplication.Dto.UserDto;

public interface UserService {
    BankResponseDto createAccount(UserDto userDto);
}
