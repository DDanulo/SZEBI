package com.example.server.Communication.services;

import com.example.server.Administration.model.User;
import com.example.server.Communication.exceptions.ConversationNotFoundException;
import com.example.server.Communication.exceptions.UserNotFoundException;
import com.example.server.Communication.objects.Conversation;
import com.example.server.Communication.repositories.ConversationRepository;
import com.example.server.Communication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Conversation> getConversationsForUser(UUID userId) {
        if (userRepository.findById(userId) == null) {
            throw new UserNotFoundException("User with id " + userId + " not found.");
        }
        return conversationRepository.findByParticipantId(userId);
    }

    @Transactional(readOnly = true)
    public Conversation getConversationById(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation with id " + conversationId + " not found."));
    }

    @Transactional
    public Conversation findOrCreateConversation(UUID user1Id, UUID user2Id) {
        User user1 = userRepository.findById(user1Id);
        if (user1 == null) {
            throw new UserNotFoundException("User with id " + user1Id + " not found.");
        }
        User user2 = userRepository.findById(user2Id);
        if (user2 == null) {
            throw new UserNotFoundException("User with id " + user2Id + " not found.");
        }

        return conversationRepository.findConversationBetweenUsers(user1, user2)
                .orElseGet(() -> {
                    Conversation newConversation = Conversation.builder()
                            .participants(List.of(user1, user2))
                            .build();
                    return conversationRepository.save(newConversation);
                });
    }
}
