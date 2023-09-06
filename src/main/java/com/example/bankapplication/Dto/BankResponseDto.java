package com.example.bankapplication.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankResponseDto {
    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;
}
