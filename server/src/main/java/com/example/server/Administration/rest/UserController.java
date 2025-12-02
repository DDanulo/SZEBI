package com.example.server.Administration.rest;


import com.example.server.Administration.converters.ResidentConverter;
import com.example.server.Administration.dto.ResidentDTO;
import com.example.server.Administration.model.Resident;
import com.example.server.Administration.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //add validation
    @GetMapping("/residents")
    public List<ResidentDTO> getAllResidents(){
        return ResidentConverter.convertResidentListToResidentDTOList(userService.getAllResidents());
    }

    @PostMapping("/residents")
    public ResidentDTO createResident(@RequestBody ResidentDTO resident){
        return ResidentConverter.convertResidentToResidentDTO(userService.createUser(ResidentConverter.convertResidentDTOToResident(resident)));
    }


}
