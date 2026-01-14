package com.example.server.Administration.converters;

import com.example.server.Administration.dto.AdministratorDTO;
import com.example.server.Administration.dto.EngineerDTO;
import com.example.server.Administration.model.Administrator;
import com.example.server.Administration.model.Engineer;

public class AdministratorConverter {

    public static Administrator convertAdministratorDTOtoAdministrator(AdministratorDTO administratorDTO) {
        return Administrator.builder()
                .login(administratorDTO.login())
                .email(administratorDTO.email())
                .firstName(administratorDTO.firstName())
                .lastName(administratorDTO.lastName())
                .active(administratorDTO.active())
                .passwordHash(administratorDTO.password() != null ? administratorDTO.password() : "")
                .build();

    }

    public static AdministratorDTO convertAdministratortoAdministratorDTO(Administrator administrator) {
        return AdministratorDTO.builder()
                .id(administrator.getId())
                .login(administrator.getLogin())
                .email(administrator.getEmail())
                .firstName(administrator.getFirstName())
                .lastName(administrator.getLastName())
                .active(administrator.isActive())
                .password(administrator.getPasswordHash())
                .build();

    }
}
