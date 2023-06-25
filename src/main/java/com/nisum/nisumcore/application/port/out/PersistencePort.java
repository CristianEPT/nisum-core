package com.nisum.nisumcore.application.port.out;

import com.nisum.nisumcore.domain.User;

public interface PersistencePort {

  User saveUser(User user);

  boolean existEmail(String email);
}
