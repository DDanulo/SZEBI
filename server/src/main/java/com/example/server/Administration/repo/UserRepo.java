package com.example.server.Administration.repo;

import com.example.server.Administration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    List<User> findAllByLoginContainingIgnoreCase(String partial);


    boolean existsByLogin(String login);
}
