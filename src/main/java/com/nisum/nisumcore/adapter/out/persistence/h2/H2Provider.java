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

/** H2Provider is a persistence provider implementation that interacts with the H2 database. */
@Component
@Slf4j
@RequiredArgsConstructor
public class H2Provider implements PersistenceProvider {

  private final UserRepository userRepository;
  private final PhoneRepository phoneRepository;
  /**
   * Saves the user in the H2 database.
   *
   * @param user The User object to save.
   * @return The saved User object.
   */
  @Override
  public User saveUser(User user) {
    var userModel = mapUserDomainToModel(user);
    var savedUser = userRepository.save(userModel);
    savePhones(savedUser, user.getPhones());
    return user;
  }
  /**
   * Maps a User domain object to a UserEntity model object.
   *
   * @param user The User object to map.
   * @return The mapped UserEntity object.
   */
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
  /**
   * Saves the phones associated with a user in the H2 database.
   *
   * @param savedUser The saved UserEntity object.
   * @param phones The list of Phone objects to save.
   */
  private void savePhones(UserEntity savedUser, List<Phone> phones) {
    phones.forEach(phone -> phoneRepository.save(mapPhoneDomainToEntity(savedUser, phone)));
  }
  /**
   * Maps a Phone domain object to a PhoneEntity model object.
   *
   * @param userEntity The UserEntity object associated with the phone.
   * @param phone The Phone object to map.
   * @return The mapped PhoneEntity object.
   */
  private PhoneEntity mapPhoneDomainToEntity(UserEntity userEntity, Phone phone) {
    return PhoneEntity.builder()
        .number(phone.number())
        .cityCode(phone.cityCode())
        .countryCode(phone.countryCode())
        .user(userEntity)
        .build();
  }
  /**
   * Checks if an email exists in the H2 database.
   *
   * @param email The email to check.
   * @return true if the email exists, false otherwise.
   */
  @Override
  public boolean existEmail(String email) {
    return userRepository.findAllByEmail(email).isPresent();
  }
}
