package com.cfloresh.springboot.app.personalfinance.service.users;

import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.users.UsersRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository repository;

    public UsersService(UsersRepository usersRepository) {
        this.repository = usersRepository;
    }

    public String registerUser( Jwt jwt) {
        String authId = jwt.getClaim("sub");

        if (repository.existsByAuthId(authId)) return "User already exists!";

        AppUser newUser = new AppUser();
        newUser.setAuthId(authId);

        repository.save(newUser);

        return "User succesfully registered";
    }
}
