package com.example.server.Administration.converters;


import com.example.server.Administration.dto.UserDTO;
import com.example.server.Administration.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserConverter {
    public static UserDTO convertUserToUserDTO(User user){
        return new UserDTO(user.getLogin(), user.getFirstName(), user.getLastName(),user.getEmail());
    }



    public static List<UserDTO> convertUsersToUserDTO(List<User> users){
        return null == users ? null : (List)users.stream().filter(Objects::nonNull).map(UserConverter::convertUserToUserDTO).collect(Collectors.toList());

    }


}
