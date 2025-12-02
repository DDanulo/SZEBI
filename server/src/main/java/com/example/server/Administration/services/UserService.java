package com.example.server.Administration.services;

import com.example.server.Administration.exceptions.InternalErrorException;
import com.example.server.Administration.model.Resident;
import com.example.server.Administration.model.User;
import com.example.server.Administration.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();

    }

    public User getUserById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new InternalErrorException());
    }

    public <T extends User> T createUser(T user) {
        try{
            return userRepo.save(user);
        } catch (DuplicateKeyException e){
            throw new InternalErrorException();
        }
    }

    public User getUserByLogin(String login) {
        return userRepo.findByLogin(login)
                .orElseThrow(() -> new InternalErrorException());
    }

    public User activateUser(UUID id) {
        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new InternalErrorException());
            user.setActive(true);
            return userRepo.save(user);
        }
        catch (DuplicateKeyException e){
            throw new InternalErrorException();

        }
    }
    public User deactivateUser(UUID id) {
        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new InternalErrorException());
            user.setActive(false);
            return userRepo.save(user);
        } catch (DuplicateKeyException e){
            throw new InternalErrorException();
        }
    }
    public List<Resident> getAllResidents() {
        return getAllUsers().stream()
                .filter(u -> u instanceof Resident)
                .map(u -> (Resident) u)
                .collect(Collectors.toList());
    }
}
