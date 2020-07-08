package com.spring.oauth.sample.users;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.security.Principal;

@Slf4j
@Validated
@RestController
public class LoginController {


    @Value("${jwt.clientId:oauth-sample}")
    private String clientId;

    @Value("${jwt.client-secret:secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/api/login")
    Object login(@RequestBody LoginRequest loginRequest) {
        MultiValueMap<String , Object> body = new LinkedMultiValueMap<>();
        body.add("username", loginRequest.getUsername());
        body.add("password", loginRequest.getPassword());
        body.add("grant_type", "password");
        HttpEntity<MultiValueMap<String , Object>> requestEntity = new HttpEntity<>(body,createHeaders(clientId, clientSecret));
        return restTemplate.exchange("http://localhost:8080/oauth/token", HttpMethod.POST, requestEntity, new Object().getClass());
    }


    @GetMapping("/api/auth/user")
    public Principal user (Principal principal) {
        return principal;
    }

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
