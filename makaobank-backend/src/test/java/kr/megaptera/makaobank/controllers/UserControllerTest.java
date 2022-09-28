package kr.megaptera.makaobank.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void register() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"name\":\"노승준\"," +
                "\"accountNumber\":\"11112222\"," +
                "\"password\":\"Qwe1234!\"," +
                "\"confirmPassword\":\"Qwe1234!\"" +
                "}"))
        .andExpect(status().isCreated());
  }
}
