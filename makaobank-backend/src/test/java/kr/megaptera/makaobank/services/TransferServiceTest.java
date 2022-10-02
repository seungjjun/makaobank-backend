package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.AmountNotEnough;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.exceptions.MyAccountFound;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.repositories.AccountRepository;
import kr.megaptera.makaobank.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TransferServiceTest {
  private AccountRepository accountRepository;

  private TransactionRepository transactionRepository;

  private TransferService transferService;

  @BeforeEach
  void setup() {
    accountRepository = mock(AccountRepository.class);
    transactionRepository = mock(TransactionRepository.class);

    transferService = new TransferService(
        accountRepository, transactionRepository);
  }

  @Test
  void transfer() {
    Long transferAmount = 100L;
    AccountNumber accountNumber1 = new AccountNumber("1234");
    AccountNumber accountNumber2 = new AccountNumber("5678");
    String name = "Pikachu";

    Account account1 = new Account(1L, accountNumber1, "Pikachu", 100_000L);
    Account account2 = new Account(2L, accountNumber2, "Raichu", 10L);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));

    given(accountRepository.findByAccountNumber(account2.accountNumber()))
        .willReturn(Optional.of(account2));

    transferService.transfer(
        accountNumber1, accountNumber2,
        transferAmount, name);

    assertThat(account1.amount()).isEqualTo(100_000L - transferAmount);
    assertThat(account2.amount()).isEqualTo(10L + transferAmount);

    verify(transactionRepository).save(any());
  }

  @Test
  void transferWithIncorrectFromAccountNumber() {
    Long transferAmount = 100L;
    AccountNumber accountNumber1 = new AccountNumber("1234");
    AccountNumber accountNumber2 = new AccountNumber("5678");
    String name = "Pikachu";

    Account account2 = new Account(2L, accountNumber2, "Raichu", 10L);

    given(accountRepository.findByAccountNumber(account2.accountNumber()))
        .willReturn(Optional.of(account2));

    assertThrows(AccountNotFound.class, () -> {
      transferService.transfer(
          accountNumber1, accountNumber2,
          transferAmount, name);
    });
  }

  @Test
  void transferWithIncorrectToAccountNumber() {
    Long transferAmount = 100L;
    AccountNumber accountNumber1 = new AccountNumber("1234");
    AccountNumber accountNumber2 = new AccountNumber("5678");
    String name = "Pikachu";

    Account account1 = new Account(2L, accountNumber1, "Pikachu", 100_000L);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));

    assertThrows(AccountNotFound.class, () -> {
      transferService.transfer(
          accountNumber1, accountNumber2,
          transferAmount, name);
    });
  }

  @Test
  void transferWithNegativeAmount() {
    Long transferAmount = -100L;
    AccountNumber accountNumber1 = new AccountNumber("1234");
    AccountNumber accountNumber2 = new AccountNumber("5678");
    String name = "Pikachu";

    Account account1 = new Account(1L, accountNumber1, "Pikachu", 100_000L);
    Account account2 = new Account(2L, accountNumber2, "Raichu", 10L);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));

    given(accountRepository.findByAccountNumber(account2.accountNumber()))
        .willReturn(Optional.of(account2));

    assertThrows(IncorrectAmount.class, () -> {
      transferService.transfer(
          accountNumber1, accountNumber2,
          transferAmount, name);
    });
  }

  @Test
  void transferWithTooLargeAmount() {
    AccountNumber accountNumber1 = new AccountNumber("1234");
    AccountNumber accountNumber2 = new AccountNumber("5678");
    String name = "Pikachu";

    Long amount1 = 100_000L;
    Long amount2 = 10L;
    Long transferAmount = amount1 + 100_000L;


    Account account1 = new Account(1L, accountNumber1, "Pikachu", amount1);
    Account account2 = new Account(2L, accountNumber2, "Raichu", amount2);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));

    given(accountRepository.findByAccountNumber(account2.accountNumber()))
        .willReturn(Optional.of(account2));

    assertThrows(AmountNotEnough.class, () -> {
      transferService.transfer(
          accountNumber1, accountNumber2,
          transferAmount, name);
    });
  }

  @Test
  void transferToMyAccount() {
    AccountNumber accountNumber1 = new AccountNumber("1234");
    String name = "Pikachu";

    Long amount1 = 100_000L;

    Account account1 = new Account(1L, accountNumber1, "Pikachu", amount1);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));

    assertThrows(MyAccountFound.class, () -> {
      transferService.transfer(
          accountNumber1, accountNumber1,
          100L, name);
    });
  }
}
