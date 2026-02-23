package com.cfloresh.springboot.app.personalfinance.service.overview;

import com.cfloresh.springboot.app.personalfinance.dto.overview.OverviewDto;
import com.cfloresh.springboot.app.personalfinance.dto.overview.OverviewProjection;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.transactions.TransactionsRespository;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OverviewService {
    private final TransactionsRespository repository;
    private final UsersService usersService;

    public OverviewService(TransactionsRespository repository, UsersService usersService) {
        this.repository = repository;
        this.usersService = usersService;
    }

    public OverviewDto getOverview(Jwt jwt) {
        String userSub = jwt.getClaim("sub");
        log.debug("Fetching overview data for user: {}", userSub);

        AppUser user = usersService.findUser(userSub);

        OverviewProjection overviewData = repository.getOverviewData(user.getId());

        log.info("Overview data retrieved for user with id: {} - Balance: {}, Income: {}, Expenses: {}",
                user.getId(), overviewData.getBalance(), overviewData.getIncome(),
                overviewData.getExpenses().abs());

        return new OverviewDto(overviewData.getBalance(), overviewData.getIncome(),
                overviewData.getExpenses().abs());
    }
}
