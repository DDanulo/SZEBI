package com.example.server.Administration.security;

import com.example.server.Administration.converters.ResidentConverter;
import com.example.server.Administration.dto.RegisterDTO;
import com.example.server.Administration.dto.ResidentDTO;
import com.example.server.Administration.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;


    @PostMapping("/register")
    public ResidentDTO createResident(@RequestBody RegisterDTO resident){
        return ResidentConverter.convertResidentToResidentDTO(userService.registerResident(ResidentConverter.convertRegisterDTOtoResident(resident)));
    }

}
