package com.example.server.Administration.security;

import com.example.server.Administration.converters.UserConverter;
import com.example.server.Administration.dto.LoginDTO;
import com.example.server.Administration.dto.TokenDTO;
import com.example.server.Administration.dto.UserDTO;
import com.example.server.Administration.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO login){
        List<String> tokens = userService.loginUser(login.login(), login.password());
        return new TokenDTO(tokens.get(0), tokens.get(1));
    }
}
