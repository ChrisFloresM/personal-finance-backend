package com.cfloresh.springboot.app.personalfinance.service.users;

import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsersService {

    private final UsersRepository repository;

    public UsersService(UsersRepository usersRepository) {
        this.repository = usersRepository;
    }

    /* TODO this method might not be used more than for testing purposes */
    public String registerUser( Jwt jwt) {
        String authId = jwt.getClaim("sub");

        if (repository.existsByAuthId(authId)) return "User already exists!";

        AppUser newUser = new AppUser();
        newUser.setAuthId(authId);

        repository.save(newUser);

        return "User succesfully registered";
    }

    public AppUser findUser(String authId) {
        AppUser user = repository.findByAuthId(authId);
        Long userId;

        if (user != null) {
            userId = user.getId();
            log.info("User found for authId: {} with Id: {}", authId, userId);
            return user;
        };

        log.debug("Creating new user for authId: {}", authId);
        user = new AppUser();
        user.setAuthId(authId);

        AppUser savedUser = repository.save(user);
        userId = savedUser.getId();
        log.info("Created new user for authId {} with Id {}", authId, userId);

        return user;
    }
}
