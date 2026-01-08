package com.example.server.Administration.rest;


import com.example.server.Administration.converters.ResidentConverter;
import com.example.server.Administration.dto.LoginDTO;
import com.example.server.Administration.dto.ResidentDTO;
import com.example.server.Administration.dto.UserDTO;
import com.example.server.Administration.model.Resident;
import com.example.server.Administration.model.User;
import com.example.server.Administration.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/residents")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ResidentDTO> getAllResidents(){
        return ResidentConverter.convertResidentListToResidentDTOList(userService.getAllResidents());
    }

    @GetMapping("")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/residents")
    public ResidentDTO createResident(@RequestBody ResidentDTO resident){
        return ResidentConverter.convertResidentToResidentDTO(userService.createUser(ResidentConverter.convertResidentDTOToResident(resident)));
    }




}
