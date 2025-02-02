package otus.study.cashmachine.machine.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashMachine {
    private MoneyBox moneyBox;

    public CashMachine(final MoneyBox moneyBox) {
        this.moneyBox = moneyBox;
    }

    public MoneyBox getMoneyBox() {
        return moneyBox;
    }

    public void setMoneyBox(final MoneyBox moneyBox) {
        this.moneyBox = moneyBox;
    }
}
