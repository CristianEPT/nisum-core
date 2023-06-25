package com.nisum.nisumcore.adapter.out.persistence;

import com.nisum.nisumcore.application.port.out.PersistencePort;
import com.nisum.nisumcore.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersistenceAdapter implements PersistencePort {

  private final PersistenceProvider persistenceProvider;

  @Override
  public User saveUser(User user) {
    return persistenceProvider.saveUser(user);
  }

  @Override
  public boolean existEmail(String email) {
    return persistenceProvider.existEmail(email);
  }
}
