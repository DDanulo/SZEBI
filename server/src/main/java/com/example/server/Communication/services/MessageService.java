package com.example.server.Communication.services;

import com.example.server.Communication.exceptions.UserNotFoundException;
import com.example.server.Communication.objects.Message;
import com.example.server.Communication.repositories.MessageRepository;
import com.example.server.Communication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message createMessage(String content, String authorLogin) {
        if (userRepository.getUser(authorLogin) == null) {
            throw new UserNotFoundException("User with login " + authorLogin + " not found.");
        }

        Message message = Message.builder()
                .id(UUID.randomUUID())
                .content(content)
                .authorLogin(authorLogin)
                .timestamp(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }
}
