package kr.megaptera.makaobank.models;

import kr.megaptera.makaobank.dtos.AccountDto;
import kr.megaptera.makaobank.exceptions.AmountNotEnough;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Account {
  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  private AccountNumber accountNumber;

  private String name;

  private Long amount;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Account() {
  }

  public Account(AccountNumber accountNumber, String name) {
    this.accountNumber = accountNumber;
    this.name = name;
    this.amount = 0L;
  }

  public Account(
      Long id,
      AccountNumber accountNumber,
      String name,
      Long amount) {
    this.id = id;
    this.accountNumber = accountNumber;
    this.name = name;
    this.amount = amount;
  }

  public void transferTo(Account other, Long amount) {
    if (amount <= 0) {
      throw new IncorrectAmount(amount);
    }

    if (this.amount < amount) {
      throw new AmountNotEnough(amount);
    }

    this.amount -= amount;
    other.amount += amount;
  }

  public Long getId() {
    return id;
  }

  public AccountNumber accountNumber() {
    return accountNumber;
  }

  public String getName() {
    return name;
  }

  public Long amount() {
    return amount;
  }

  public AccountDto toDto() {
    return new AccountDto(accountNumber.value(), name, amount);
  }

  public static Account fake(String accountNumber) {
    return new Account(1L, new AccountNumber(accountNumber),
        "Pikachu", 100L);
  }
}
