package kr.megaptera.makaobank.models;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
  @Test
  void creation() {
    User user = new User(1L, "노승준", "11112222");

    assertThat(user.getName()).isEqualTo("노승준");
  }

  @Test
  void authenticate() {
    User user = new User();

    PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    user.changePassword(passwordEncoder, "Qwe1234!");

    assertThat(user.authenticate(passwordEncoder, "Qwe1234!")).isTrue();
    assertThat(user.authenticate(passwordEncoder, "xxx")).isFalse();
  }
}
