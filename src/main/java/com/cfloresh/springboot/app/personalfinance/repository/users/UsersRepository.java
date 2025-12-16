package com.cfloresh.springboot.app.personalfinance.repository.users;

import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<AppUser, Long>{
    public boolean existsByAuthId(String authId);
    public AppUser findByAuthId(String authId);
}
