package com.nisum.nisumcore.adapter.out.persistence;

import com.nisum.nisumcore.domain.User;

public interface PersistenceProvider {

  User saveUser(User user);

  boolean existEmail(String email);
}
