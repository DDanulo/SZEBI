package com.example.server.Administration.services;

import com.example.server.Administration.exceptions.InternalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordMail(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("ioias@wp.pl");
        message.setSubject("Reset hasła");
        message.setText("""
                Otrzymaliśmy prośbę o reset hasła.

                Kliknij w link poniżej, aby ustawić nowe hasło:
                %s

                Jeśli to nie Ty, zignoruj tę wiadomość.
                """.formatted(link));
        try {
            mailSender.send(message);
        }catch(MailException me) {
            throw new InternalErrorException();
        }

    }
}
