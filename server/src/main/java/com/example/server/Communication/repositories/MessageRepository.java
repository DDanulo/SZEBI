package com.example.server.Communication.repositories;

import com.example.server.Communication.objects.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    List<Message> findAll();
    <S extends Message> S save(S entity);
    Optional<Message> findById(UUID id);
}
