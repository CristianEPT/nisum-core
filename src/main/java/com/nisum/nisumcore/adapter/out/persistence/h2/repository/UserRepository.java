package com.nisum.nisumcore.adapter.out.persistence.h2.repository;

import com.nisum.nisumcore.adapter.out.persistence.h2.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

  Optional<UserEntity> findAllByEmail(String email);
}
