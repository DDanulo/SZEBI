package com.example.server.Administration.services;

import com.example.server.Administration.model.PasswordResetToken;
import com.example.server.Administration.repo.PasswordResetTokenRepository;
import com.example.server.Administration.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepo userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final MailService mailService;

    public void sendResetLink(String email) {

        try {
            userRepository.findByEmail(email).ifPresent(user -> {

                try {
                    String token = UUID.randomUUID().toString();

                    PasswordResetToken resetToken = new PasswordResetToken();
                    resetToken.setToken(token);
                    resetToken.setUser(user);
                    resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));

                    tokenRepository.save(resetToken);

                    String link = "http://localhost:5173/reset-password?token=" + token;
                    mailService.sendResetPasswordMail(user.getEmail(), link);

                } catch (Exception e) {
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Nie udało się wygenerować lub wysłać linku resetującego hasło", e
                    );
                }
            });
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Błąd podczas przetwarzania żądania resetu hasła", e
            );
        }
    }

    }

