package com.example.server.Administration.converters;


import com.example.server.Administration.dto.UserDTO;
import com.example.server.Administration.model.Resident;
import com.example.server.Administration.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserConverter {
    public static UserDTO convertUserToUserDTO(User user) {
        Map<String, Object> details = new HashMap<>();

        if (user instanceof Resident r) {
            details.put("roomNumber", r.getRoom());
        }

        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive(),
                RoleConverter.roleFor(user),
                details.isEmpty() ? null : details
        );
    }



    public static List<UserDTO> convertUsersToUserDTO(List<User> users){
        return null == users ? null : (List)users.stream().filter(Objects::nonNull).map(UserConverter::convertUserToUserDTO).collect(Collectors.toList());

    }


}
