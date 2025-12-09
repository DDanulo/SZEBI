package com.example.server.Communication.repositories;

import com.example.server.Communication.objects.Message;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@Primary
public class MockMessageRepository implements MessageRepository {

    private final ConcurrentMap<UUID, Message> messages = new ConcurrentHashMap<>();

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public <S extends Message> S save(S message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID());
        }
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messages.get(id));
    }
}
