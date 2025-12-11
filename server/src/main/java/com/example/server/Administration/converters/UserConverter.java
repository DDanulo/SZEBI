package com.example.server.Administration.converters;


import com.example.server.Administration.dto.UserDTO;
import com.example.server.Administration.model.User;

public class UserConverter {
    public static UserDTO convertUserToUserDTO(User user){
        return new UserDTO(user.getLogin(), user.getFirstName(), user.getLastName(),user.getEmail());
    }



}
