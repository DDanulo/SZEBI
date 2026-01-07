package com.example.server.Communication.controllers;

import com.example.server.Communication.dtos.ConversationDTO;
import com.example.server.Communication.dtos.CreateConversationRequestDTO;
import com.example.server.Communication.objects.Conversation;
import com.example.server.Communication.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/communication/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping
    public List<ConversationDTO> getUserConversations(@RequestParam UUID userId) {
        return conversationService.getConversationsForUser(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ConversationDTO getConversationById(@PathVariable Long id) {
        return convertToDto(conversationService.getConversationById(id));
    }

    @PostMapping
    public ConversationDTO findOrCreateConversation(@RequestBody CreateConversationRequestDTO request) {
        Conversation conversation = conversationService.findOrCreateConversation(request.user1Id(), request.user2Id());
        return convertToDto(conversation);
    }

    private ConversationDTO convertToDto(Conversation conversation) {
        return ConversationDTO.fromEntity(conversation);
    }
}
