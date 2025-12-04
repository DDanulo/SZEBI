package com.example.server.Administration.rest;

import com.example.server.Administration.converters.UserConverter;
import com.example.server.Administration.dto.LoginDTO;
import com.example.server.Administration.dto.UserDTO;
import com.example.server.Administration.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginDTO login){
        return UserConverter.convertUserToUserDTO(userService.loginUser(login.login(), login.password()));
    }
}
