package com.gps.pruebaTecnica.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserInfo {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    public UserInfo() {
    }

    public UserInfo(String username, String email, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserInfo(UUID id, String username, String email, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
