package com.example.server.Communication.services;

import com.example.server.Communication.exceptions.UserNotFoundException;
import com.example.server.Communication.objects.Message;
import com.example.server.Communication.repositories.MessageRepository;
import com.example.server.Communication.repositories.TemporaryUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final TemporaryUserRepo mockUserRepo;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message createMessage(String content, String authorLogin) {
        mockUserRepo.findByLogin(authorLogin)
                .orElseThrow(() -> new UserNotFoundException("User with login " + authorLogin + " not found."));

        Message message = Message.builder()
                .id(UUID.randomUUID())
                .content(content)
                .authorLogin(authorLogin)
                .timestamp(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }
}
