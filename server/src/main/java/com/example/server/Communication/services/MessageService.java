package com.example.server.Communication.services;

import com.example.server.Administration.model.User;
import com.example.server.Communication.exceptions.UserNotFoundException;
import com.example.server.Communication.objects.Conversation;
import com.example.server.Communication.objects.Message;
import com.example.server.Communication.repositories.MessageRepository;
import com.example.server.Communication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationService conversationService;

    @Transactional
    public Message createMessage(String content, UUID senderId, UUID recipientId) {
        User sender = userRepository.findById(senderId);
        if (sender == null) {
            throw new UserNotFoundException("Sender with id " + senderId + " not found.");
        }

        Conversation conversation = conversationService.findOrCreateConversation(senderId, recipientId);

        Message message = Message.builder()
                .content(content)
                .sender(sender)
                .timestamp(LocalDateTime.now())
                .conversation(conversation)
                .build();

        return messageRepository.save(message);
    }
}
