package com.nisum.nisumcore.application.port.in;

import com.nisum.nisumcore.domain.User;

public interface CreateUserUseCase {

  User createUser(User user);
}
