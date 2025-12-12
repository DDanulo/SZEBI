package com.example.server.Administration.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepo.findAll();

    }

    public User getUserById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new InternalErrorException());
    }


    public <T extends User> T createUser(T user) {
        try {
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

    public <T extends User> T updateUser(T updated) {
        User user = getUserById(updated.getId());

        user.setLogin(updated.getLogin());
        user.setPasswordHash(updated.getPasswordHash());
        user.setFirstName(updated.getFirstName());
        user.setLastName(updated.getLastName());
        user.setEmail(updated.getEmail());
        user.setActive(updated.isActive());

        if (user instanceof Resident && updated instanceof Resident r) {
            ((Resident) user).setRoom(r.getRoom());
        }

        return (T) userRepo.save(user);
    }


    public User removeUserById(UUID id) {
        User user = getUserById(id);
        userRepo.delete(user);
        return user;
    }


    public User loginUser(String login, String password) {
        User user = getUserByLogin(login);
        if ( passwordEncoder.matches(password,user.getPasswordHash()) ) {
            return  user;
        } else {
            throw new InternalErrorException();
        }

    }


    public Resident registerResident(Resident resident) {
        try{
            resident.setPasswordHash(passwordEncoder.encode(resident.getPasswordHash()));
            return userRepo.save(resident);
        } catch (DuplicateKeyException e){
            throw new InternalErrorException();
        }
    }


}
