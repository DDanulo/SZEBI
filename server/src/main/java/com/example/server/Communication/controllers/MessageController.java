package com.example.server.Communication.controllers;

import com.example.server.Communication.dtos.MessageDTO;
import com.example.server.Communication.objects.Message;
import com.example.server.Communication.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/communication/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@RequestBody CreateMessageRequest request) {
        Message message = messageService.createMessage(request.content(), request.senderId(), request.recipientId());
        return new ResponseEntity<>(MessageDTO.fromEntity(message), HttpStatus.CREATED);
    }

    public record CreateMessageRequest(String content, UUID senderId, UUID recipientId) {}
}
