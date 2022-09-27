package kr.megaptera.makaobank.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountNumberTest {
  @Test
  void equals() {
    assertThat(new AccountNumber("1234")).isEqualTo(new AccountNumber("1234"));
    assertThat(new AccountNumber("1234")).isNotEqualTo(new AccountNumber("1235"));

    assertThat(new AccountNumber("1234")).isNotEqualTo(new AccountNumber(null));
    assertThat(new AccountNumber("1234")).isNotEqualTo("1324");
  }
}
