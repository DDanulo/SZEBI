package com.example.server.Administration.converters;

import com.example.server.Administration.dto.EngineerDTO;
import com.example.server.Administration.model.Engineer;

public class EngineerConverter {

    public static Engineer convertEngineerDTOtoEngineer(EngineerDTO engineerDTO) {
        return Engineer.builder()
                .login(engineerDTO.login())
                .email(engineerDTO.email())
                .firstName(engineerDTO.firstName())
                .lastName(engineerDTO.lastName())
                .active(engineerDTO.active())
                .passwordHash(engineerDTO.password() != null ? engineerDTO.password() : "")
                .build();

    }

    public static EngineerDTO convertEngineertoEngineerDTO(Engineer engineer) {
        return EngineerDTO.builder()
                .id(engineer.getId())
                .login(engineer.getLogin())
                .email(engineer.getEmail())
                .firstName(engineer.getFirstName())
                .lastName(engineer.getLastName())
                .active(engineer.isActive())
                .build();
    }

}
