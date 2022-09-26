package kr.megaptera.makaobank.backdoor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("backdoor")
@Transactional
public class BackdoorController {
  private final JdbcTemplate jdbcTemplate;

  public BackdoorController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping("setup-database")
  public String setupDatabase() {
    jdbcTemplate.execute("DELETE FROM account");

    jdbcTemplate.execute("" +
        "INSERT INTO account(id, name, account_number, amount)" +
        " VALUES(1, 'Pikachu', '1234', 123000)");

    jdbcTemplate.execute("" +
        "INSERT INTO account(id, name, account_number, amount)" +
        " VALUES(2, 'Raichu', '5678', 123456000)");

    return "OK";
  }

  @GetMapping("change-amount")
  public String changeAmount(
      @RequestParam Long userId,
      @RequestParam Long amount
  ) {
    jdbcTemplate.update("UPDATE account SET amount=? WHERE id=?",
        amount, userId);

    return "OK";
  }
}
