package otus.study.cashmachine.machine.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePinDto {
    @NotBlank
    private String cardNumber;
    @NotBlank
    private String oldPin;
    @NotBlank
    private String newPin;
}
