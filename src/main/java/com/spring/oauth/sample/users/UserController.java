package com.spring.oauth.sample.users;

import com.spring.oauth.sample.errors.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.HashSet;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Validated
class UserController {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    UserController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    Page<User> all(@RequestParam(value = "page", defaultValue = "0") int page,
                   @RequestParam(value = "size", defaultValue = "20") int size,
                   OAuth2Authentication authentication) {
        String auth = ((org.springframework.security.core.userdetails.User) authentication.getUserAuthentication().getPrincipal()).getUsername();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        Pageable pageable = new PageRequest(page, size);

        if (role.equals(User.Role.USER.name())) {
            return repository.findAllByEmail(auth, pageable);
        }
        return repository.findAll(pageable);
    }
}
