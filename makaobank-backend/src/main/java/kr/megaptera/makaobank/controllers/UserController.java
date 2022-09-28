package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.dtos.UserRegistrationDto;
import kr.megaptera.makaobank.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  private UserService userService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public String register(
      @RequestBody UserRegistrationDto userRegistrationDto
  ) {
    userService.create(userRegistrationDto);
    return "";
  }
}
