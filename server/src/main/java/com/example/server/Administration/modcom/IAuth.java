package com.example.server.Administration.modcom;

import com.example.server.Administration.model.User;

import java.util.List;

public interface IAuth {

    public List<User> getUsers();

}