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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class PotsService {

    private final PotsRepository repository;
    private final UsersService usersService;

    public PotsService(PotsRepository repository, UsersService usersService) {
        this.repository = repository;
        this.usersService = usersService;
    }

    public PotDto savePot(Jwt jwt, PotDto data){
        String userSub = jwt.getClaim("sub");
        log.debug("Creating new pot for user: {} - Name: {}, Target: {}", userSub, data.name(), data.target());

        AppUser user = usersService.findUser(userSub);

        Pot pot = new Pot();
        pot.setUser(user);
        setPotData(pot, data);

        repository.save(pot);

        log.info("Pot created successfully for user with id: {} - Name: {}, Target: {}",
                user.getId(), pot.getName(), pot.getTarget());

        return new PotDto(pot.getName(), pot.getTarget(), pot.getTotal(), pot.getTheme());
    }

    public List<PotResponseDto> getPots(Jwt jwt) {
        String userSub = jwt.getClaim("sub");
        log.debug("Fetching all pots for user: {}", userSub);

        AppUser user = usersService.findUser(userSub);

        List<PotResponseDto> pots = repository.findAllByUserId(user.getId()).stream()
                .map(PotsMapper::toResponseDto).toList();

        log.info("Retrieved {} pots for user with id: {}", pots.size(), user.getId());

        return pots;
    }

    public PotResponseDto editPot(Jwt jwt, Long potId, PotDto potData) {
        String userSub = jwt.getClaim("sub");
        log.debug("Editing pot with id: {} for user: {} - Name: {}, Target: {}",
                potId, userSub, potData.name(), potData.target());

        AppUser user = usersService.findUser(userSub);

        Pot pot =
                repository.findByIdAndUser_Id(potId, user.getId()).orElseThrow(() -> {
                    log.warn("Pot with id: {} not found for user with id: {}", potId, user.getId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Pot not found");
                });

        setPotData(pot, potData);
        Pot savedPot = repository.save(pot);

        log.info("Pot with id: {} edited successfully for user with id: {} - New name: {}, Target: {}",
                potId, user.getId(), savedPot.getName(), savedPot.getTarget());

        return PotsMapper.toResponseDto(savedPot);
    }

    @Transactional
    public void deletePot(Jwt jwt, Long id) {
        String userSub = jwt.getClaim("sub");
        log.debug("Deleting pot with id: {} for user: {}", id, userSub);

        AppUser user = usersService.findUser(userSub);

        Pot pot =
                repository.findById(id).orElseThrow(() -> {
                    log.warn("Pot with id: {} not found", id);
                    return new ResourceNotFoundException("Resource not found. Id: " + id);
                });

        if (!pot.getUser().getId().equals(user.getId())) {
            log.warn("User with id: {} attempted to delete pot with id: {} which belongs to user with id: {}",
                    user.getId(), id, pot.getUser().getId());
            throw new ForbiddenException("User has not access to this resource");
        }

        repository.deleteByIdAndUser_Id(id, user.getId());

        log.info("Pot with id: {} deleted successfully for user with id: {}", id, user.getId());
    }

    private void setPotData(Pot pot, PotDto potData) {
        pot.setName(potData.name());
        pot.setTotal(potData.total());
        pot.setTarget(potData.target());
        pot.setTheme(potData.theme());
    }

}