package com.example.bankapplication.Controller;

import com.example.bankapplication.Dto.BankResponseDto;
import com.example.bankapplication.Dto.UserDto;
import com.example.bankapplication.Service.ServiceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    @PostMapping("/create-account")
    public BankResponseDto createAccount(@RequestBody UserDto userDto){
        return userServiceImpl.createAccount(userDto);
    }
}
