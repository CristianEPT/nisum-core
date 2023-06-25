package com.nisum.nisumcore.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.nisum.nisumcore.adapter.in.web.exception.ConflictException;
import com.nisum.nisumcore.adapter.in.web.model.UserResponse;
import com.nisum.nisumcore.application.port.in.CreateUserUseCase;
import com.nisum.nisumcore.domain.Phone;
import com.nisum.nisumcore.domain.User;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WebMvcTest(controllers = UserController.class, properties = "spring.cloud.config.enabled=false")
class UserControllerTest {

  private static final String BASE_URL = "/nisum/user";

  @Autowired MockMvc mockMvc;

  @MockBean CreateUserUseCase createUserUseCase;

  private Gson json;

  @BeforeEach
  void init() {
    json = new Gson();
  }

  @Test
  void givenCorrectUser_ThenReturnInformationAndResultSuccessfully() throws Exception {

    var correctUser =
        new User(
            "test", "test@domain.com", "Password@123", List.of(new Phone("123123123", "1", "57")));

    given(createUserUseCase.createUser(correctUser))
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

    var executionResult =
        mockMvc
            .perform(
                post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json.toJson(correctUser)))
            .andExpect(status().isOk())
            .andReturn();

    then(createUserUseCase).should(times(1)).createUser(correctUser);
    var executionResultContent = executionResult.getResponse().getContentAsString();
    assertThat(executionResultContent).isNotNull();
    assertThat(executionResultContent).isNotEmpty();
    var userResponse = json.fromJson(executionResultContent, UserResponse.class);
    assertThat(userResponse).isNotNull();
  }

  @Test
  void givenInvalidUser_WhenEmailIsIncorrect_ThenReturnErrorMessage() throws Exception {

    var user = new User("test", "test", "Password@123", List.of(new Phone("123123123", "1", "57")));

    var executionResult =
        mockMvc
            .perform(
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json.toJson(user)))
            .andExpect(status().isBadRequest())
            .andReturn();

    then(createUserUseCase).should(times(0)).createUser(user);
    var executionResultContent = executionResult.getResponse().getContentAsString();
    assertThat(executionResultContent).isNotNull();
    assertThat(executionResultContent).isNotEmpty();
    var errorResponnse = json.fromJson(executionResultContent, HashMap.class);
    assertThat(errorResponnse.get("status")).isEqualTo("400");
    assertThat(errorResponnse.get("error")).isEqualTo("BadRequest");
    assertThat(errorResponnse.get("message")).isEqualTo("The email is invalid");
  }

  @Test
  void givenInvalidUser_WhenPasswordIsIncorrect_ThenReturnErrorMessage() throws Exception {

    var user =
        new User("test", "test@domain.com", "password", List.of(new Phone("123123123", "1", "57")));

    var executionResult =
        mockMvc
            .perform(
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json.toJson(user)))
            .andExpect(status().isBadRequest())
            .andReturn();

    then(createUserUseCase).should(times(0)).createUser(user);
    var executionResultContent = executionResult.getResponse().getContentAsString();
    assertThat(executionResultContent).isNotNull();
    assertThat(executionResultContent).isNotEmpty();
    var errorResponnse = json.fromJson(executionResultContent, HashMap.class);
    assertThat(errorResponnse.get("status")).isEqualTo("400");
    assertThat(errorResponnse.get("error")).isEqualTo("BadRequest");
    assertThat(errorResponnse.get("message")).isEqualTo("The password is invalid");
  }

  @Test
  void givenInvalidUser_WhenEmailExist_ThenReturnErrorMessage() throws Exception {

    var user =
        new User(
            "test", "test@domain.com", "Password@123", List.of(new Phone("123123123", "1", "57")));

    given(createUserUseCase.createUser(user))
        .willThrow(new ConflictException("The email already exist"));

    var executionResult =
        mockMvc
            .perform(
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json.toJson(user)))
            .andExpect(status().isConflict())
            .andReturn();

    then(createUserUseCase).should(times(1)).createUser(user);
    var executionResultContent = executionResult.getResponse().getContentAsString();
    assertThat(executionResultContent).isNotNull();
    assertThat(executionResultContent).isNotEmpty();
    var errorResponnse = json.fromJson(executionResultContent, HashMap.class);
    assertThat(errorResponnse.get("status")).isEqualTo("409");
    assertThat(errorResponnse.get("error")).isEqualTo("Conflict");
    assertThat(errorResponnse.get("message")).isEqualTo("The email already exist");
  }
}
