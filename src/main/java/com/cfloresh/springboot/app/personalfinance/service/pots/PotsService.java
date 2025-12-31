package com.cfloresh.springboot.app.personalfinance.service.pots;

import com.cfloresh.springboot.app.personalfinance.dto.pots.PotDto;
import com.cfloresh.springboot.app.personalfinance.dto.pots.PotResponseDto;
import com.cfloresh.springboot.app.personalfinance.exception.ForbiddenException;
import com.cfloresh.springboot.app.personalfinance.exception.ResourceNotFoundException;
import com.cfloresh.springboot.app.personalfinance.mapper.PotsMapper;
import com.cfloresh.springboot.app.personalfinance.model.pots.Pot;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.pots.PotsRepository;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PotsService {

    private final PotsRepository repository;
    private final UsersService usersService;

    public PotsService(PotsRepository repository, UsersService usersService) {
        this.repository = repository;
        this.usersService = usersService;
    }

    public PotDto savePot(Jwt jwt, PotDto data){
        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        Pot pot = new Pot();
        pot.setUser(user);
        pot.setName(data.name());
        pot.setTotal(data.total());
        pot.setTarget(data.target());
        pot.setTheme(data.theme());

        repository.save(pot);

        /* Save*/
        return new PotDto(pot.getName(), pot.getTarget(), pot.getTotal(), pot.getTheme());
    }

    public List<PotResponseDto> getPots(Jwt jwt) {
        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        return repository.findAllByUserId(user.getId()).stream().map(PotsMapper::toResponseDto).toList();
    }

    @Transactional
    public void deletePot(Jwt jwt, Long id) {
        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        Pot pot =
                repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not " +
                        "found. Id: " + id));

        if (!pot.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("User has not access to this resource");
        }

        repository.deleteByIdAndUser_Id(id, user.getId());
    }

}