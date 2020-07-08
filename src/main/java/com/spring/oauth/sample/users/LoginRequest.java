package com.spring.oauth.sample.users;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;

    private String password;
}
