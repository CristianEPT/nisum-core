package com.nisum.nisumcore.adapter.in.web.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

  private String id;
  private Long created;
  private Long modified;
  private Long lastLogin;
  private String token;
  private boolean isActive;
}
