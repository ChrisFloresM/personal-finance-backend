package com.cfloresh.springboot.app.personalfinance.service.overview;

import com.cfloresh.springboot.app.personalfinance.dto.overview.OverviewDto;
import com.cfloresh.springboot.app.personalfinance.dto.overview.OverviewProjection;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.transactions.TransactionsRespository;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import org.hibernate.sql.ast.tree.expression.Over;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OverviewService {
    private final TransactionsRespository repository;
    private final UsersService usersService;

    public OverviewService(TransactionsRespository repository, UsersService usersService) {
        this.repository = repository;
        this.usersService = usersService;
    }

    public OverviewDto getOverview(Jwt jwt) {
        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        OverviewProjection overviewData = repository.getOverviewData(user.getId());

        return new OverviewDto(overviewData.getBalance(), overviewData.getIncome(),
                overviewData.getExpenses().abs());
    }
}
