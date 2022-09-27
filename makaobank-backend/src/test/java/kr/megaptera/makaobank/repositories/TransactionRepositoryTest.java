package kr.megaptera.makaobank.repositories;

import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.models.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TransactionRepositoryTest {
  @Autowired
  private TransactionRepository transactionRepository;

  @Test
  void save() {
    AccountNumber sender = new AccountNumber("1234");
    AccountNumber receiver = new AccountNumber("5678");
    Long amount = 100_000L;
    String name = "Raichu";

    Transaction transaction = new Transaction(
        sender, receiver, amount, name);

    transactionRepository.save(transaction);
  }
}
