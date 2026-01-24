package com.authtest.atuthTest.service.implementatin;

import com.authtest.atuthTest.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String sender;

    public EmailServiceImpl(JavaMailSender javaMailSender,
                            @Value("${spring.mail.sender}")String sender){
        this.mailSender=javaMailSender;
        this.sender=sender;
    }

    @Async
    @Override
    public void sendMail(String to, String subject, String url) {
        try{
            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper messageHelper=new MimeMessageHelper(message,true,"UTF-8");
            messageHelper.setTo(to);
            messageHelper.setFrom(sender);
            messageHelper.setSubject(subject);
            String body=this.generateEmail(url,subject);
            messageHelper.setText(body,true);
            log.info("Sending the mail to {}",to);
            mailSender.send(message);
            log.info("Sent the mail to {}",to);

        } catch (Exception e) {
            log.error("Failed to send email to {}, Reason: {}",to,e.getMessage(),e);
        }
    }

    String generateEmail(String url,String message) {
        return String.format("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Verify User</title>
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                        height: 100vh;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        font-family: Arial, sans-serif;
                        background-color: #f4f6f8;
                    }

                    .verify-button {
                        padding: 14px 28px;
                        font-size: 16px;
                        font-weight: bold;
                        color: #ffffff;
                        background-color: #007bff;
                        border: none;
                        border-radius: 6px;
                        cursor: pointer;
                        text-decoration: none;
                        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
                    }

                    .verify-button:hover {
                        background-color: #0056b3;
                    }
                </style>
            </head>
            <body>

                <a href="%s" target="_blank" class="verify-button">
                    %s
                </a>

            </body>
            </html>
            """, url,message);
    }

}
