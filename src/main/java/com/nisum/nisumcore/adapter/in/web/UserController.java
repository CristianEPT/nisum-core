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

/** Controller that handles requests related to user creation. */
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
  /**
   * Handles POST requests to register a new user.
   *
   * @param userRequest The UserRequest object containing the user data to create.
   * @return The UserResponse object with the created user data.
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  public UserResponse registerUser(@RequestBody UserRequest userRequest) {
    validUserRequest(userRequest);
    var user = mapUserModelToUserDomain(userRequest);
    var savedUser = createUserUseCase.createUser(user);
    return mapUserDomainToUserModel(savedUser);
  }
  /**
   * Validates the user request.
   *
   * @param userRequest The UserRequest object to validate.
   * @throws BadRequestException if the validation fails.
   */
  private void validUserRequest(UserRequest userRequest) {
    if (!isValidEmail(userRequest.email())) throw new BadRequestException("The email is invalid");

    if (!isValidPassword(userRequest.password()))
      throw new BadRequestException("The password is invalid");
  }
  /**
   * Checks if an email matches the defined regular expression.
   *
   * @param email The email to validate.
   * @return true if the email is valid, false otherwise.
   */
  private boolean isValidEmail(String email) {
    return email.matches(emailRegex);
  }
  /**
   * Checks if a password matches the defined regular expression.
   *
   * @param password The password to validate.
   * @return true if the password is valid, false otherwise.
   */
  private boolean isValidPassword(String password) {
    return password.matches(passwordRegex);
  }
  /**
   * Maps a UserRequest object to a User object in the domain.
   *
   * @param userRequest The UserRequest object to map.
   * @return The mapped User object.
   */
  private User mapUserModelToUserDomain(UserRequest userRequest) {
    var phones = userRequest.phones().stream().map(this::mapPhoneModelToPhoneDomain).toList();
    return new User(userRequest.name(), userRequest.email(), userRequest.password(), phones);
  }
  /**
   * Maps a PhoneRequest object to a Phone object in the domain.
   *
   * @param phoneRequest The PhoneRequest object to map.
   * @return The mapped Phone object.
   */
  private Phone mapPhoneModelToPhoneDomain(PhoneRequest phoneRequest) {
    return new Phone(phoneRequest.number(), phoneRequest.cityCode(), phoneRequest.countryCode());
  }
  /**
   * Maps a User object from the domain to a UserResponse object.
   *
   * @param user The User object from the domain to map.
   * @return The mapped UserResponse object.
   */
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
