package kr.megaptera.makaobank.repositories;

import kr.megaptera.makaobank.models.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AccountRepositoryTest {

  @Autowired
  private AccountRepository accountRepository;

  @Test
  void creation() {
    Account account = new Account("1234", "Pikachu");

    accountRepository.save(account);
  }
}
