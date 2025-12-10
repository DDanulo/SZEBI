package com.example.server.Communication.controllers;

import com.example.server.Communication.dtos.MessageDTO;
import com.example.server.Communication.objects.Message;
import com.example.server.Communication.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/communication/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public List<MessageDTO> getAllMessages() {
        return messageService.getAllMessages().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@RequestBody CreateMessageRequest request) {
        Message message = messageService.createMessage(request.content(), request.authorLogin());
        return new ResponseEntity<>(convertToDto(message), HttpStatus.CREATED);
    }

    private MessageDTO convertToDto(Message message) {
        return new MessageDTO(message.getId(), message.getContent(), message.getAuthorLogin(), message.getTimestamp());
    }

    public record CreateMessageRequest(String content, String authorLogin) {}
}
