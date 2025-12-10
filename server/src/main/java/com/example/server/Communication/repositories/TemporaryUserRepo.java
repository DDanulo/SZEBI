package com.example.server.Communication.repositories;

import com.example.server.Administration.model.Resident;
import com.example.server.Administration.model.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@Primary
public class TemporaryUserRepo implements UserRepository {

    private final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();

    public TemporaryUserRepo() {
        User user1 = new Resident();
        user1.setId(UUID.randomUUID());
        user1.setLogin("itsHardStyl3r");
        users.put(user1.getLogin(), user1);

        User user2 = new Resident();
        user2.setId(UUID.randomUUID());
        user2.setLogin("admin");
        users.put(user2.getLogin(), user2);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(users.get(login));
    }
}
