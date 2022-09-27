package kr.megaptera.makaobank.models;

import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

  final Long AMOUNT1 = 100_000L;
  final Long AMOUNT2 = 10L;

  Account account1;
  Account account2;

  @BeforeEach
  void setup() {
    account1 = new Account(1L, "1234", "Pikachu", AMOUNT1);
    account2 = new Account(2L, "5678", "Raichu", AMOUNT2);
  }
  
  @Test
  void transferTo() {
    Long transferAmount = 100L;

    account1.transferTo(account2, transferAmount);

    assertThat(account1.amount()).isEqualTo(100_000L - transferAmount);
    assertThat(account2.amount()).isEqualTo(10L + transferAmount);
  }

  @Test
  void transferWithNegativeAmount() {
    Long transferAmount = -100L;

    assertThrows(IncorrectAmount.class, () -> {
      account1.transferTo(account2, transferAmount);
    });
  }

  @Test
  void transferWithTooLargeAmount() {
    Long transferAmount = AMOUNT1 + 100_000L;

    assertThrows(IncorrectAmount.class, () -> {
      account1.transferTo(account2, transferAmount);
    });
  }

}