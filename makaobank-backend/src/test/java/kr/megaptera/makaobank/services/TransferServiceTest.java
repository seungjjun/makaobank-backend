package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TransferServiceTest {
  private AccountRepository accountRepository;

  private TransferService transferService;

  @BeforeEach
  void setup() {
    accountRepository = mock(AccountRepository.class);

    transferService = new TransferService(accountRepository);
  }

  @Test
  void transfer() {
    Long transferAmount = 100L;
    Account account1 = new Account(1L, "1234", "Pikachu", 100_000L);
    Account account2 = new Account(2L, "5678", "Raichu", 10L);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));

    given(accountRepository.findByAccountNumber(account2.accountNumber()))
        .willReturn(Optional.of(account2));

    transferService.transfer("1234", "5678", transferAmount);

    assertThat(account1.amount()).isEqualTo(100_000L - transferAmount);
    assertThat(account2.amount()).isEqualTo(10L + transferAmount);
  }

  @Test
  void transferWithIncorrectFromAccountNumber() {
    Long transferAmount = 100L;

    Account account2 = new Account(2L, "5678", "Raichu", 10L);

    given(accountRepository.findByAccountNumber(account2.accountNumber()))
        .willReturn(Optional.of(account2));

    assertThrows(AccountNotFound.class, () -> {
      transferService.transfer("1234", "5678", transferAmount);
    });
  }

  @Test
  void transferWithIncorrectToAccountNumber() {
    Long transferAmount = 100L;

    Account account1 = new Account(2L, "1234", "Pikachu", 100_000L);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));

    assertThrows(AccountNotFound.class, () -> {
      transferService.transfer("1234", "5678", transferAmount);
    });
  }

  @Test
  void transferWithNegativeAmount() {
    Long transferAmount = -100L;

    Account account1 = new Account(1L, "1234", "Pikachu", 100_000L);
    Account account2 = new Account(2L, "5678", "Raichu", 10L);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));

    given(accountRepository.findByAccountNumber(account2.accountNumber()))
        .willReturn(Optional.of(account2));

    assertThrows(IncorrectAmount.class, () -> {
      transferService.transfer("1234", "5678", transferAmount);
    });
  }

  @Test
  void transferWithTooLargeAmount() {
    Long amount1 = 100_000L;
    Long amount2 = 10L;
    Long transferAmount = amount1 + 100_000L;

    Account account1 = new Account(1L, "1234", "Pikachu", amount1);
    Account account2 = new Account(2L, "5678", "Raichu", amount2);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));

    given(accountRepository.findByAccountNumber(account2.accountNumber()))
        .willReturn(Optional.of(account2));

    assertThrows(IncorrectAmount.class, () -> {
      transferService.transfer("1234", "5678", transferAmount);
    });
  }
}