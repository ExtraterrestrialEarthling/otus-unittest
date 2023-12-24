package otus.study.cashmachine.machine.dto;

import lombok.*;

@Getter
@Setter
public class DepositDto {
    private String cardNumber;
    private String pin;
    private Integer count5000;
    private Integer count1000;
    private Integer count500;
    private Integer count100;


}
