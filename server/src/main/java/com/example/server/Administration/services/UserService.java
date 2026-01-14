package com.example.server.Administration.services;

import com.example.server.Administration.exceptions.LoginAlreadyExists;
import com.example.server.Administration.exceptions.UserInactiveException;
import com.example.server.Administration.exceptions.UserNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.server.Administration.exceptions.InternalErrorException;
import com.example.server.Administration.model.Resident;
import com.example.server.Administration.model.User;
import com.example.server.Administration.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepo.findAll();

    }

    public User getUserById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new InternalErrorException());
    }

    public List<User> searchByLogin(String partial) {
        return userRepo.findAllByLoginContainingIgnoreCase(partial);
    }

    public <T extends User> T createUser(T user) {
        try {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
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



        if (updated.getLogin() != null && !updated.getLogin().isEmpty()) {
            user.setLogin(updated.getLogin());
        }


        if (updated.getPasswordHash() != null && !updated.getPasswordHash().isEmpty()) {
            updated.setPasswordHash(passwordEncoder.encode(updated.getPasswordHash()));
            user.setPasswordHash(updated.getPasswordHash());
        }


        if (updated.getFirstName() != null && !updated.getFirstName().isEmpty()) {
            user.setFirstName(updated.getFirstName());
        }


        if (updated.getLastName() != null && !updated.getLastName().isEmpty()) {
            user.setLastName(updated.getLastName());
        }


        if (updated.getEmail() != null && !updated.getEmail().isEmpty()) {
            user.setEmail(updated.getEmail());
        }


        user.setActive(updated.isActive());


        if (user instanceof Resident resident && updated instanceof Resident r) {
            if (r.getRoom() != null && !r.getRoom().isEmpty()) {
                resident.setRoom(r.getRoom());
            }
        }

        return (T) userRepo.save(user);
    }


    public User removeUserById(UUID id) {
        User user = getUserById(id);
        userRepo.delete(user);
        return user;
    }


    public List<String> loginUser(String login, String password) {
        User user = userRepo.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));

        if (!(passwordEncoder.matches(password, user.getPasswordHash()))) {
            throw new UserNotFoundException(login);
        }
        if (!user.isActive())
        {
            throw new UserInactiveException(login);
        }
        return Arrays.asList(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user)
        );

    }


    public Resident registerResident(Resident resident) {
        try{
            resident.setPasswordHash(passwordEncoder.encode(resident.getPasswordHash()));
            return userRepo.save(resident);
        } catch (DuplicateKeyException e){
            throw new LoginAlreadyExists(resident.getLogin());
        }
        catch (Exception e){
            throw new InternalErrorException();
        }
    }


}
