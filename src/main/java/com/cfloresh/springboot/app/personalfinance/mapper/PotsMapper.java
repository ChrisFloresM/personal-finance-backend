package com.cfloresh.springboot.app.personalfinance.mapper;

import com.cfloresh.springboot.app.personalfinance.dto.pots.PotDto;
import com.cfloresh.springboot.app.personalfinance.dto.pots.PotResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.pots.Pot;
import org.springframework.stereotype.Component;

public class PotsMapper {

    public static PotResponseDto toResponseDto(Pot pot) {
        return new PotResponseDto(pot.getId(), pot.getName(), pot.getTarget(), pot.getTotal(),
                pot.getTheme());
    }
    public static PotDto toDto(Pot pot) {
        return new PotDto(pot.getName(), pot.getTarget(), pot.getTotal(), pot.getTheme());
    }
}
