package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.dtos.UserRegistrationDto;
import kr.megaptera.makaobank.models.User;
import kr.megaptera.makaobank.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User create(UserRegistrationDto userRegistrationDto) {
    User user = new User(null,
        userRegistrationDto.getName(),
        userRegistrationDto.getAccountNumber());

    user.changePassword(passwordEncoder, userRegistrationDto.getPassword());

    userRepository.save(user);

    return user;
  }
}
