package com.service;

import com.repository.StudentRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;
    private final String baseUrl;
    private final StudentRepository studentRepository;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.base-url}") String baseUrl,
                        StudentRepository studentRepository){
        this.mailSender = mailSender;
        this.baseUrl = baseUrl;
        this.studentRepository = studentRepository;
    }

    public void sendConfirmationEmail(String email, String subject, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom("noreply@yourdomain.com");
            helper.setText(content, true);
            mailSender.send(mimeMessage);
            log.info("Sent confirmation email to {}", email);

        } catch (MailException | MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
