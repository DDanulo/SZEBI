package com.example.server.Communication.repositories;

import com.example.server.Administration.modcom.IAuth;
import com.example.server.Administration.model.User;
import com.example.server.Administration.repo.UserRepo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Primary
public class UserRepository implements IAuth {

    private final UserRepo userRepo;

    public UserRepository(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User getUser(String login) {
        return getUsers().stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }

    public User findById(UUID id) {
        return userRepo.findById(id).orElse(null);
    }
}
