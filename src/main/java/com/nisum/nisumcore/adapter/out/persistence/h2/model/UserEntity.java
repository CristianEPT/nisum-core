package com.nisum.nisumcore.adapter.out.persistence.h2.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "UserNisum")
@Table(name = "user_nisum")
public class UserEntity {

  @Id private String userIdentifier;
  private String name;
  private String email;
  private String password;
  private Long created;
  private Long modified;
  private Long lastLogin;
  private String token;
  private boolean isActive;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PhoneEntity> phones;
}
