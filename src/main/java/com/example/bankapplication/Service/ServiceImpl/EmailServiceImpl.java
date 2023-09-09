package com.example.bankapplication.Service.ServiceImpl;

import com.example.bankapplication.Dto.EmailDto;
import com.example.bankapplication.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailAlert(EmailDto emailDto) {
        try {
            var mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDto.getRecipient());
            mailMessage.setText(emailDto.getMessageBody());
            mailMessage.setSubject(emailDto.getSubject());
            javaMailSender.send(mailMessage);
            System.out.println("Mail sent successfully");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendEmailWithAttachment(EmailDto emailDto) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
          mimeMessageHelper =  new MimeMessageHelper(mimeMessage,true);
          mimeMessageHelper.setFrom(senderEmail);
          mimeMessageHelper.setTo(emailDto.getRecipient());
          mimeMessageHelper.setText(emailDto.getMessageBody());
          mimeMessageHelper.setSubject(emailDto.getSubject());

          var file = new FileSystemResource(new File(emailDto.getAttachment()));
          mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
          javaMailSender.send(mimeMessage);

            log.info(file.getFilename()+ " has ben sent to to user with email "+emailDto.getRecipient());
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }

    }
}
