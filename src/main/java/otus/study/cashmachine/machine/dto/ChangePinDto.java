package otus.study.cashmachine.machine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePinDto {
    private String cardNumber;
    private String oldPin;
    private String newPin;
}
