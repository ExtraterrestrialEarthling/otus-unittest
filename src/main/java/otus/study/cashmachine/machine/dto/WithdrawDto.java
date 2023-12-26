package otus.study.cashmachine.machine.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class WithdrawDto {
    @NotBlank
    private String cardNumber;
    @NotBlank
    private String pin;
    @NotBlank
    private String amount;
}
