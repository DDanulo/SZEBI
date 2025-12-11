package com.example.server.Administration.converters;

import com.example.server.Administration.dto.RegisterDTO;
import com.example.server.Administration.dto.ResidentDTO;
import com.example.server.Administration.model.Resident;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResidentConverter {

    public static ResidentDTO convertResidentToResidentDTO(Resident resident){
        return new ResidentDTO(resident.getId(),resident.getLogin(),resident.getPasswordHash(), resident.getFirstName(), resident.getLastName(),resident.isActive(), resident.getEmail(), resident.getRoom());
    }

    public static Resident convertResidentDTOToResident(ResidentDTO residentDTO){
        return Resident.builder()
                .id(residentDTO.id())
                .login(residentDTO.login())
                .passwordHash(residentDTO.password())
                .firstName(residentDTO.firstName())
                .lastName(residentDTO.lastName())
                .active(residentDTO.active())
                .email(residentDTO.email())
                .room(residentDTO.room())
                .build();

    }

    public static Resident convertRegisterDTOtoResident(RegisterDTO registerDTO){
        return Resident.builder()
                .login(registerDTO.login())
                .passwordHash(registerDTO.password())
                .firstName(registerDTO.firstName())
                .lastName(registerDTO.lastName())
                .email(registerDTO.email())
                .room(registerDTO.room())
                .build();
    }

    public static List<ResidentDTO> convertResidentListToResidentDTOList(List<Resident> residents){
        return null == residents ? null : (List)residents.stream().filter(Objects::nonNull).map(ResidentConverter::convertResidentToResidentDTO).collect(Collectors.toList());
    }



}
