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

  @Override
  public User createUser(@NonNull User user) {
    validUser(user);

    var userJwt = createJwt(user.getEmail(), user.getPassword());
    return saveUser(user, userJwt);
  }

  private void validUser(User user) {
    if (user.getName() == null || user.getName().equals(""))
      throw new BadRequestException("The name is invalid");

    if (!isValidPassword(user.getPassword()))
      throw new BadRequestException("The password is invalid");

    if (!isValidEmail(user.getEmail())) throw new BadRequestException("The email is invalid");

    if (existEmail(user.getEmail())) throw new ConflictException("The email already exist");
  }

  private boolean isValidPassword(String password) {
    return password.matches(passwordRegex);
  }

  private boolean isValidEmail(String email) {
    return email.matches(emailRegex);
  }

  private boolean existEmail(String email) {
    return persistencePort.existEmail(email);
  }

  private String createJwt(String subject, String secretKey) {

    var currentDate = LocalDateTime.now();
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(convertLocalDateTimeToDate(currentDate))
        .setExpiration(convertLocalDateTimeToDate(currentDate.plusDays(2)))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  public Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

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
