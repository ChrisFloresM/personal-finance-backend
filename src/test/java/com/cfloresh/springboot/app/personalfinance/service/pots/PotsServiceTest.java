package com.cfloresh.springboot.app.personalfinance.service.pots;

import com.cfloresh.springboot.app.personalfinance.dto.pots.PotDto;
import com.cfloresh.springboot.app.personalfinance.dto.pots.PotResponseDto;
import com.cfloresh.springboot.app.personalfinance.exception.ForbiddenException;
import com.cfloresh.springboot.app.personalfinance.exception.ResourceNotFoundException;
import com.cfloresh.springboot.app.personalfinance.model.pots.Pot;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.pots.PotsRepository;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PotsServiceTest {

    @Mock
    private PotsRepository repository;

    @Mock
    private UsersService usersService;

    @InjectMocks
    private PotsService potsService;

    private Jwt mockJwt() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("sub")).thenReturn("user123");
        return jwt;
    }

    private AppUser mockUser() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setAuthId("user123");
        return user;
    }

    private Pot mockPot() {
        Pot pot = new Pot();
        pot.setId(1L);
        pot.setUser(mockUser());
        pot.setName("Emergency Fund");
        pot.setTarget(new BigDecimal("5000"));
        pot.setTotal(new BigDecimal("2500"));
        pot.setTheme("#FF6B6B");
        return pot;
    }

    @Test
    @DisplayName("Save a pot successfully")
    void savePotSuccessTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        PotDto inputData = new PotDto("Emergency Fund", new BigDecimal("5000"),
                new BigDecimal("2500"), "#FF6B6B");

        when(usersService.findUser("user123")).thenReturn(user);

        Pot savedPot = new Pot();
        savedPot.setId(1L);
        savedPot.setUser(user);
        savedPot.setName("Emergency Fund");
        savedPot.setTarget(new BigDecimal("5000"));
        savedPot.setTotal(new BigDecimal("2500"));
        savedPot.setTheme("#FF6B6B");

        when(repository.save(any(Pot.class))).thenReturn(savedPot);

        PotDto result = potsService.savePot(jwt, inputData);

        assertNotNull(result);
        assertEquals("Emergency Fund", result.name());
        assertEquals(new BigDecimal("5000"), result.target());
        assertEquals(new BigDecimal("2500"), result.total());
        assertEquals("#FF6B6B", result.theme());

        verify(usersService, times(1)).findUser(any(String.class));
        verify(repository, times(1)).save(any(Pot.class));
    }

    @Test
    @DisplayName("Get all pots for user")
    void getPotsTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        when(usersService.findUser("user123")).thenReturn(user);

        Pot pot = mockPot();
        when(repository.findAllByUserId(1L))
                .thenReturn(List.of(pot));

        List<PotResponseDto> result = potsService.getPots(jwt);

        assertEquals(1, result.size());
        assertEquals("Emergency Fund", result.getFirst().name());

        verify(repository, times(1)).findAllByUserId(1L);
    }

    @Test
    @DisplayName("Get empty list when user has no pots")
    void getPotsEmptyTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        when(usersService.findUser("user123")).thenReturn(user);
        when(repository.findAllByUserId(1L))
                .thenReturn(List.of());

        List<PotResponseDto> result = potsService.getPots(jwt);

        assertEquals(0, result.size());

        verify(repository, times(1)).findAllByUserId(1L);
    }

    @Test
    @DisplayName("Edit pot successfully")
    void editPotSuccessTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        PotDto updateData = new PotDto("Vacation Fund", new BigDecimal("10000"),
                new BigDecimal("5000"), "#4ECDC4");

        when(usersService.findUser("user123")).thenReturn(user);

        Pot pot = mockPot();
        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(pot));
        when(repository.save(any(Pot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PotResponseDto result = potsService.editPot(jwt, 1L, updateData);

        assertNotNull(result);
        assertEquals("Vacation Fund", result.name());
        assertEquals(new BigDecimal("10000"), result.target());

        verify(repository, times(1)).findByIdAndUser_Id(1L, 1L);
        verify(repository, times(1)).save(any(Pot.class));
    }

    @Test
    @DisplayName("Throw exception when pot not found for edit")
    void editPotNotFoundTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        PotDto updateData = new PotDto("Vacation Fund", new BigDecimal("10000"),
                new BigDecimal("5000"), "#4ECDC4");

        when(usersService.findUser("user123")).thenReturn(user);
        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> potsService.editPot(jwt, 1L, updateData));

        verify(repository, times(1)).findByIdAndUser_Id(1L, 1L);
        verify(repository, never()).save(any(Pot.class));
    }

    @Test
    @DisplayName("Delete pot successfully")
    void deletePotSuccessTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        Pot pot = mockPot();
        when(usersService.findUser("user123")).thenReturn(user);
        when(repository.findById(1L))
                .thenReturn(Optional.of(pot));

        potsService.deletePot(jwt, 1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteByIdAndUser_Id(1L, 1L);
    }

    @Test
    @DisplayName("Throw exception when deleting non-existent pot")
    void deletePotNotFoundTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        when(usersService.findUser("user123")).thenReturn(user);
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> potsService.deletePot(jwt, 1L));

        verify(repository, times(1)).findById(1L);
        verify(repository, never()).deleteByIdAndUser_Id(any(), any());
    }

    @Test
    @DisplayName("Throw ForbiddenException when user tries to delete another user's pot")
    void deletePotForbiddenTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();
        user.setId(999L);

        Pot pot = mockPot();

        when(usersService.findUser("user123")).thenReturn(user);
        when(repository.findById(1L))
                .thenReturn(Optional.of(pot));

        assertThrows(ForbiddenException.class,
                () -> potsService.deletePot(jwt, 1L));

        verify(repository, times(1)).findById(1L);
        verify(repository, never()).deleteByIdAndUser_Id(any(), any());
    }

}

