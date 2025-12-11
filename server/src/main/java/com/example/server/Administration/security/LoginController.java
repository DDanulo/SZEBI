package com.example.server.Administration.security;

import com.example.server.Administration.converters.UserConverter;
import com.example.server.Administration.dto.LoginDTO;
import com.example.server.Administration.dto.UserDTO;
import com.example.server.Administration.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginDTO login){
        return UserConverter.convertUserToUserDTO(userService.loginUser(login.login(), login.password()));
    }
}
