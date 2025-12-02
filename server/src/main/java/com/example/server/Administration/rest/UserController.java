package com.example.server.Administration.rest;

import com.example.server.Administration.converters.ResidentConverter;
import com.example.server.Administration.dto.ResidentDTO;
import com.example.server.Administration.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/residents")
    public List<ResidentDTO> getAllResidents(){
        return ResidentConverter.convertResidentListToResidentDTOList(userService.getAllResidents());
    }

}
