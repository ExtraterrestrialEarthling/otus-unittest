package otus.study.cashmachine.machine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class DepositDto {
    @NotBlank
    private String cardNumber;
    @NotBlank
    private String pin;
    @NotNull
    private Integer count5000;
    @NotNull
    private Integer count1000;
    @NotNull
    private Integer count500;
    @NotNull
    private Integer count100;


}
