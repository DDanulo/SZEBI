package com.example.server.Communication.repositories;

import com.example.server.Administration.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLogin(String login);
}
