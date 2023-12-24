package otus.study.cashmachine.machine.dto;

import lombok.*;

@Getter
@Setter
public class WithdrawDto {
    private String cardNumber;
    private String pin;
    private String amount;
}
