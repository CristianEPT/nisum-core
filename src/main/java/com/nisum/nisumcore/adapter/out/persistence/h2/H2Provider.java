package com.nisum.nisumcore.adapter.out.persistence.h2;

import com.nisum.nisumcore.adapter.out.persistence.PersistenceProvider;
import com.nisum.nisumcore.adapter.out.persistence.h2.model.PhoneEntity;
import com.nisum.nisumcore.adapter.out.persistence.h2.model.UserEntity;
import com.nisum.nisumcore.adapter.out.persistence.h2.repository.PhoneRepository;
import com.nisum.nisumcore.adapter.out.persistence.h2.repository.UserRepository;
import com.nisum.nisumcore.domain.Phone;
import com.nisum.nisumcore.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class H2Provider implements PersistenceProvider {

  private final UserRepository userRepository;
  private final PhoneRepository phoneRepository;

  @Override
  public User saveUser(User user) {
    var userModel = mapUserDomainToModel(user);
    var savedUser = userRepository.save(userModel);
    savePhones(savedUser, user.getPhones());
    return user;
  }

  private UserEntity mapUserDomainToModel(User user) {
    return UserEntity.builder()
        .userIdentifier(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .created(user.getCreated())
        .modified(user.getModified())
        .lastLogin(user.getLastLogin())
        .token(user.getToken())
        .isActive(user.isActive())
        .build();
  }

  private void savePhones(UserEntity savedUser, List<Phone> phones) {
    phones.forEach(phone -> phoneRepository.save(mapPhoneDomainToEntity(savedUser, phone)));
  }

  private PhoneEntity mapPhoneDomainToEntity(UserEntity userEntity, Phone phone) {
    return PhoneEntity.builder()
        .number(phone.number())
        .cityCode(phone.cityCode())
        .countryCode(phone.countryCode())
        .user(userEntity)
        .build();
  }

  @Override
  public boolean existEmail(String email) {
    return userRepository.findAllByEmail(email).isPresent();
  }
}
