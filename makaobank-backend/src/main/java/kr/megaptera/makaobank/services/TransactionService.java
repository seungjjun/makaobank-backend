package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.models.Transaction;
import kr.megaptera.makaobank.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
  private final TransactionRepository transactionRepository;

  public TransactionService(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public List<Transaction> list(AccountNumber accountNumber) {

    return transactionRepository.findAllBySenderOrReceiverOrderByCreatedAtDesc(
        accountNumber, accountNumber);
  }
}
