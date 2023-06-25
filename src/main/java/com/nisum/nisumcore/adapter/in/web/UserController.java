package com.nisum.nisumcore.adapter.in.web;

import com.nisum.nisumcore.adapter.in.web.exception.BadRequestException;
import com.nisum.nisumcore.adapter.in.web.model.PhoneRequest;
import com.nisum.nisumcore.adapter.in.web.model.UserRequest;
import com.nisum.nisumcore.adapter.in.web.model.UserResponse;
import com.nisum.nisumcore.application.port.in.CreateUserUseCase;
import com.nisum.nisumcore.domain.Phone;
import com.nisum.nisumcore.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nisum/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final CreateUserUseCase createUserUseCase;

  @Value("${nisum.configuration.user.email}")
  private String emailRegex;

  @Value("${nisum.configuration.user.password}")
  private String passwordRegex;

  @PostMapping(consumes = "application/json", produces = "application/json")
  public UserResponse registerUser(@RequestBody UserRequest userRequest) {
    validUserRequest(userRequest);
    var user = mapUserModelToUserDomain(userRequest);
    var savedUser = createUserUseCase.createUser(user);
    return mapUserDomainToUserModel(savedUser);
  }

  private void validUserRequest(UserRequest userRequest) {
    if (!isValidEmail(userRequest.email())) throw new BadRequestException("The email is invalid");

    if (!isValidPassword(userRequest.password()))
      throw new BadRequestException("The password is invalid");
  }

  private boolean isValidEmail(String email) {
    return email.matches(emailRegex);
  }

  private boolean isValidPassword(String password) {
    return password.matches(passwordRegex);
  }

  private User mapUserModelToUserDomain(UserRequest userRequest) {
    var phones = userRequest.phones().stream().map(this::mapPhoneModelToPhoneDomain).toList();
    return new User(userRequest.name(), userRequest.email(), userRequest.password(), phones);
  }

  private Phone mapPhoneModelToPhoneDomain(PhoneRequest phoneRequest) {
    return new Phone(phoneRequest.number(), phoneRequest.cityCode(), phoneRequest.countryCode());
  }

  private UserResponse mapUserDomainToUserModel(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .created(user.getCreated())
        .modified(user.getModified())
        .lastLogin(user.getLastLogin())
        .token(user.getToken())
        .isActive(user.isActive())
        .build();
  }
}
