package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.exceptions.LoginFailed;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.services.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@ActiveProfiles("test")
class SessionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LoginService loginService;

  @BeforeEach
  void setUp() {
    Account account = Account.fake("1234");

    AccountNumber accountNumber = new AccountNumber("1234");

    given(loginService.login(accountNumber, "password"))
        .willReturn(account);

    given(loginService.login(accountNumber, "xxx"))
        .willThrow(new LoginFailed());
  }

  @Test
  void login() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"accountNumber\":\"1234\"," +
                "\"password\":\"password\"" +
                "}"))
        .andExpect(status().isCreated())
        .andExpect(content().string(
            containsString("\"amount\":")
        ));
  }

  @Test
  void loginFail() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"accountNumber\":\"1234\"," +
                "\"password\":\"xxx\"" +
                "}"))
        .andExpect(status().isBadRequest());
  }
}
