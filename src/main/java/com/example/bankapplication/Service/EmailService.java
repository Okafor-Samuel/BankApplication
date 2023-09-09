package com.example.bankapplication.Service;

import com.example.bankapplication.Dto.EmailDto;

public interface EmailService {
    void sendEmailAlert(EmailDto emailDto);
    void sendEmailWithAttachment(EmailDto emailDto);
}
