package com.nisum.nisumcore.application.services;

import com.nisum.nisumcore.adapter.in.web.exception.BadRequestException;
import com.nisum.nisumcore.adapter.in.web.exception.ConflictException;
import com.nisum.nisumcore.application.port.in.CreateUserUseCase;
import com.nisum.nisumcore.application.port.out.PersistencePort;
import com.nisum.nisumcore.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Service class that implements the CreateUserUseCase interface to handle user creation. */
@Service
@Slf4j
@RequiredArgsConstructor
@Setter
public class UserService implements CreateUserUseCase {

  private final PersistencePort persistencePort;

  @Value("${nisum.configuration.user.email}")
  private String emailRegex;

  @Value("${nisum.configuration.user.password}")
  private String passwordRegex;
  /**
   * Creates a new user.
   *
   * @param user The User object representing the user to create.
   * @return The created User object.
   * @throws BadRequestException if the user data is invalid.
   * @throws ConflictException if the email already exists.
   */
  @Override
  public User createUser(@NonNull User user) {
    validUser(user);

    var userJwt = createJwt(user.getEmail(), user.getPassword());
    return saveUser(user, userJwt);
  }
  /**
   * Validates the user data.
   *
   * @param user The User object to validate.
   * @throws BadRequestException if the validation fails.
   * @throws ConflictException if the email already exists.
   */
  private void validUser(User user) {
    if (user.getName() == null || user.getName().equals(""))
      throw new BadRequestException("The name is invalid");

    if (!isValidPassword(user.getPassword()))
      throw new BadRequestException("The password is invalid");

    if (!isValidEmail(user.getEmail())) throw new BadRequestException("The email is invalid");

    if (existEmail(user.getEmail())) throw new ConflictException("The email already exist");
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
   * Checks if an email matches the defined regular expression.
   *
   * @param email The email to validate.
   * @return true if the email is valid, false otherwise.
   */
  private boolean isValidEmail(String email) {
    return email.matches(emailRegex);
  }
  /**
   * Checks if an email already exists in the persistence.
   *
   * @param email The email to check.
   * @return true if the email exists, false otherwise.
   */
  private boolean existEmail(String email) {
    return persistencePort.existEmail(email);
  }

  /**
   * Creates a JWT token based on the subject and secret key.
   *
   * @param subject The subject of the JWT token.
   * @param secretKey The secret key for signing the token.
   * @return The created JWT token.
   */
  private String createJwt(String subject, String secretKey) {

    var currentDate = LocalDateTime.now();
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(convertLocalDateTimeToDate(currentDate))
        .setExpiration(convertLocalDateTimeToDate(currentDate.plusDays(2)))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }
  /**
   * Converts a LocalDateTime object to a Date object.
   *
   * @param localDateTime The LocalDateTime object to convert.
   * @return The converted Date object.
   */
  public Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
  /**
   * Saves the user in the persistence.
   *
   * @param user The User object to save.
   * @param userJwt The JWT token associated with the user.
   * @return The saved User object.
   */
  private User saveUser(User user, String userJwt) {
    var id = UUID.randomUUID().toString();
    var currentDate = Instant.now().toEpochMilli();
    var userToSave =
        new User(
            id,
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            currentDate,
            currentDate,
            currentDate,
            userJwt,
            true,
            user.getPhones());
    return persistencePort.saveUser(userToSave);
  }
}
