package com.example.server.Administration.modcom;

import com.example.server.Administration.model.User;
import com.example.server.Administration.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
public class ResidentAuth implements IAuth {

    private final UserService userService;

    //Mock hehe
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

}
