package com.spring.oauth.sample;

import lombok.extern.slf4j.Slf4j;
import com.spring.oauth.sample.users.User;
import com.spring.oauth.sample.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
@Slf4j
public class LoadDatabase {

    @Value("${data.users:admin,userman,user1,user2,user3,user4,user5}")
    private String[] users;
    @Value("${data.nouns:sky,winter,sun,moon,spring,weekend,fall,summer,mountain,wolf,bird}")
    private String[] nouns;
    @Value("${data.verbs:falling,rising,exploding,shining,coming,lightning,howling,chirping}")
    private String[] verbs;
    @Value("${data.feelings:bad,numb,fine,good}")
    private String[] feelings;
    @Value("${data.timeOfDay:morning,afternoon,evening,night}")
    private String[] timeOfDay;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("clientSecret:secret")
    private String clientSecret;

    int rnd(int size) {
        return (int) (Math.random() * size);
    }

    @Bean
    CommandLineRunner initUsers(UserRepository repo) {

        return args -> {
            for (int i = 0; i < users.length; i++) {
                String email = users[i] + "@" + users[i] + ".com";
                User.Role role = i > 1 ? User.Role.USER : i == 0 ? User.Role.ADMIN : User.Role.USER_MANAGER;
                double minGleePerDay = rnd(1000 * feelings.length);
                String pwd = passwordEncoder.encode("pwd");
                log.info("save {}", repo.save(new User(null, email, pwd, role, minGleePerDay)));
            }
        };
    }

}
