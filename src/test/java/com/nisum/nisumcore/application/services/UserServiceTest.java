package com.nisum.nisumcore.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.nisum.nisumcore.adapter.in.web.exception.BadRequestException;
import com.nisum.nisumcore.adapter.in.web.exception.ConflictException;
import com.nisum.nisumcore.application.port.out.PersistencePort;
import com.nisum.nisumcore.domain.Phone;
import com.nisum.nisumcore.domain.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock PersistencePort persistencePort;

  @InjectMocks UserService userService;

  @BeforeEach
  void init() {
    userService.setEmailRegex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    userService.setPasswordRegex(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
  }

  @Test
  void givenCorrectUser_ThenSaveUser_ReturnInformation() {
    var correctUser =
        new User(
            "test", "test@domain.com", "Password@123", List.of(new Phone("123123123", "1", "57")));

    given(persistencePort.existEmail("test@domain.com")).willReturn(false);
    given(persistencePort.saveUser(Mockito.any()))
        .willReturn(
            new User(
                "398b6992-81f5-41e1-bd75-b3f5808a5be6",
                "test",
                "test@domain.com",
                "Password@123",
                1687684166103L,
                1687684166103L,
                1687684166103L,
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2aXZpYW5hQHRlc3QuY29tIiwiaWF0IjoxNjg3Njg0MTY2LCJleHAiOjE2ODc4NTY5NjZ9.B9EwlDxd-e-j4XQwCwI1LbiKIsa-UBSZWZwaO3tm4HqcG_EfB1sPwe8fi5TmCmKm2MmwmEE6inn6gh6gO57zSw",
                true,
                List.of(new Phone("123123123", "1", "57"))));

    var savedUser = userService.createUser(correctUser);
    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getId()).isNotNull().isNotEmpty();
    assertThat(savedUser.getCreated()).isNotNull().isGreaterThan(0);
    assertThat(savedUser.getModified()).isNotNull().isGreaterThan(0);
    assertThat(savedUser.getLastLogin()).isNotNull().isGreaterThan(0);
    assertThat(savedUser.getToken()).isNotNull().isNotEmpty();
    assertThat(savedUser.isActive()).isTrue();
  }

  @Test
  void givenIncorrectUser_WhenNameIsEmpty_ThenDontSaveUser_ThrowException() {
    var correctUser =
        new User(
            "", "test@domain.com", "Password@123", List.of(new Phone("123123123", "1", "57")));

    assertThrows(BadRequestException.class, () -> userService.createUser(correctUser));
  }

  @Test
  void givenIncorrectUser_WhenEmailIsIncorrect_ThenDontSaveUser_ThrowException() {
    var correctUser =
        new User("test", "testdomain.com", "Password@123", List.of(new Phone("123123123", "1", "57")));

    assertThrows(BadRequestException.class, () -> userService.createUser(correctUser));
  }

  @Test
  void givenIncorrectUser_WhenPasswordIsIncorrect_ThenDontSaveUser_ThrowException() {
    var correctUser =
        new User("test", "test@domain.com", "Ss@1", List.of(new Phone("123123123", "1", "57")));

    assertThrows(BadRequestException.class, () -> userService.createUser(correctUser));
  }

  @Test
  void givenIncorrectUser_WhenEmailExist_ThenDontSaveUser_ThrowException() {
    var correctUser =
        new User("test", "test@domain.com", "Password@123", List.of(new Phone("123123123", "1", "57")));
    given(persistencePort.existEmail("test@domain.com")).willReturn(true);
    assertThrows(ConflictException.class, () -> userService.createUser(correctUser));
  }
}
