package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.services.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@ActiveProfiles("test")
class TransactionControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TransferService transferService;


  @Test
  void transfer() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"5678\"," +
                "\"amount\":100000" +
                "}"))
        .andExpect(status().isCreated());

    verify(transferService).transfer("1234" ,"5678", 100_000L);
  }

  @Test
  void transferWithIncorrectAccountNumber() throws Exception {
    String accountNumber = "123456789";
    given(transferService.transfer(any(), any(), any()))
        .willThrow(new AccountNotFound(accountNumber));


    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"" + accountNumber + "\"," +
                "\"amount\":100000" +
                "}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(
            containsString("\"code\":1001")
        ));
  }

  @Test
  void transferWithIncorrectAmount() throws Exception {
    Long amount = 100L;
    given(transferService.transfer(any(), any(), any()))
        .willThrow(new IncorrectAmount(amount));


    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"5678\"," +
                "\"amount\":" + amount +
                "}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(
            containsString("\"code\":1002")
        ));
  }
}