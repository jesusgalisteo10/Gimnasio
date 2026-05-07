package com.example.utils;

import com.example.model.entity.Client;

public class Session {

    private static Session _instance;
    private static Client userLogged;

    private Session() {

    }

    public static Session getInstance() {
        if (_instance == null) {
            _instance = new Session();
            _instance.logIn(userLogged);
        }
        return _instance;
    }

    public void logIn(Client client) {
        userLogged = client;
    }

    public Client getUserLogged() {
        return userLogged;
    }

    public void logOut() {
        userLogged = null;
    }
}

