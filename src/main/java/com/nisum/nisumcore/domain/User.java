package com.nisum.nisumcore.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class User {
  private String id;
  private String name;
  private String email;
  private String password;
  private Long created;
  private Long modified;
  private Long lastLogin;
  private String token;
  private boolean isActive;
  private List<Phone> phones;

  public User(
      @NonNull String name,
      @NonNull String email,
      @NonNull String password,
      @NonNull List<Phone> phones) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.phones = phones;
  }
}
