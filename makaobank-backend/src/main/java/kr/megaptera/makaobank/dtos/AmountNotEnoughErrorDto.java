package kr.megaptera.makaobank.dtos;

public class AmountNotEnoughErrorDto extends ErrorDto {
  public AmountNotEnoughErrorDto() {
    super(1003, "계좌 잔액이 부족합니다");
  }
}
