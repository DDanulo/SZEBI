package com.example.server.Administration.rest;


import com.example.server.Administration.converters.AdministratorConverter;
import com.example.server.Administration.converters.EngineerConverter;
import com.example.server.Administration.converters.ResidentConverter;
import com.example.server.Administration.converters.UserConverter;
import com.example.server.Administration.dto.*;
import com.example.server.Administration.model.Administrator;
import com.example.server.Administration.model.Resident;
import com.example.server.Administration.model.User;
import com.example.server.Administration.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/residents")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ResidentDTO> getAllResidents(){
        return ResidentConverter.convertResidentListToResidentDTOList(userService.getAllResidents());
    }

    @GetMapping("/residents/{login}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getResidentsByLogin(@PathVariable("login") String login){
        return UserConverter.convertUserToUserDTO(userService.getUserByLogin(login));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserById(@PathVariable("id") UUID id){
        return UserConverter.convertUserToUserDTO(userService.getUserById(id));
    }

    @GetMapping("/residents/search/{login}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> searchUsersbyLogin(@PathVariable("login") String login){
        return  UserConverter.convertUsersToUserDTO(userService.searchByLogin(login)) ;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers(){
        return UserConverter.convertUsersToUserDTO(userService.getAllUsers());
    }



    @PatchMapping("/residents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResidentDTO updateResident(@RequestBody ResidentDTO residentDTO){
        return ResidentConverter.convertResidentToResidentDTO(userService.updateUser(ResidentConverter.convertResidentDTOToResident(residentDTO)));
    }

    @PostMapping("/residents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResidentDTO createResident(@RequestBody ResidentDTO resident){
        return ResidentConverter.convertResidentToResidentDTO(userService.createUser(ResidentConverter.convertResidentDTOToResident(resident)));
    }

    @PostMapping("/engineers")
    @PreAuthorize("hasRole('ADMIN')")
    public EngineerDTO createEngineers(@RequestBody EngineerDTO engineerDTO){
        return EngineerConverter.convertEngineertoEngineerDTO(userService.createUser(EngineerConverter.convertEngineerDTOtoEngineer(engineerDTO) ));
    }

    @PatchMapping("/engineers")
    @PreAuthorize("hasRole('ADMIN')")
    public EngineerDTO updateEngineers(@RequestBody EngineerDTO engineerDTO){
        return EngineerConverter.convertEngineertoEngineerDTO(userService.updateUser(EngineerConverter.convertEngineerDTOtoEngineer(engineerDTO)));
    }

    @PostMapping("/administrators")
    @PreAuthorize("hasRole('ADMIN')")
    public AdministratorDTO createAdministrators(@RequestBody AdministratorDTO administratorDTO){
        return AdministratorConverter.convertAdministratortoAdministratorDTO(userService.createUser(AdministratorConverter.convertAdministratorDTOtoAdministrator(administratorDTO)));
    }

    @PatchMapping("/administrators")
    @PreAuthorize("hasRole('ADMIN')")
    public AdministratorDTO updateEngineers(@RequestBody AdministratorDTO administratorDTO){
        return AdministratorConverter.convertAdministratortoAdministratorDTO(userService.updateUser(AdministratorConverter.convertAdministratorDTOtoAdministrator(administratorDTO)));
    }

    @PutMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteResident(@PathVariable("id") UUID id){
        userService.removeUserById(id);
    }




    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public void activateUser(@PathVariable UUID id){
        userService.activateUser(id);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public void deactivateUser(@PathVariable UUID id){
        userService.deactivateUser(id);
    }







}
