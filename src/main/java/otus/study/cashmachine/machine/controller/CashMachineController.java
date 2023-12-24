package otus.study.cashmachine.machine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import otus.study.cashmachine.machine.data.CashMachine;
import otus.study.cashmachine.machine.dto.ChangePinDto;
import otus.study.cashmachine.machine.dto.DepositDto;
import otus.study.cashmachine.machine.dto.WithdrawDto;
import otus.study.cashmachine.machine.service.CashMachineService;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CashMachineController {
    private CashMachineService cashMachineService;
    private CashMachine cashMachine;

    @Autowired
    public CashMachineController(CashMachineService cashMachineService, CashMachine cashMachine) {
        this.cashMachineService = cashMachineService;
        this.cashMachine = cashMachine;
    }

    @GetMapping("/main-page")
    public String getMainPage(){
        return "main_page";
    }

    @GetMapping("/deposit")
    public String getDepositForm(){
        return "deposit";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestBody DepositDto depositDto,
                          Model model) {
        try {
            cashMachineService.putMoney(cashMachine, depositDto.getCardNumber(),
                    depositDto.getPin(), List.of(depositDto.getCount5000(),
                    depositDto.getCount1000(), depositDto.getCount500(), depositDto.getCount100()));
            model.addAttribute("status", "Money deposited successfully!");

            return "deposit";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/withdraw")
    public String getFillForm(){
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody WithdrawDto withdrawDto,
                           Model model) {
        try {
            var list = cashMachineService.getMoney(cashMachine, withdrawDto.getCardNumber(),
                    withdrawDto.getPin(), new BigDecimal(withdrawDto.getAmount()));
            model.addAttribute("result", "Withdrawal of:");
            model.addAttribute("count5000", String.format("5000 denomination: %d", list.get(0)));
            model.addAttribute("count1000", String.format("1000 denomination: %d", list.get(1)));
            model.addAttribute("count500", String.format("500 denomination: %d", list.get(2)));
            model.addAttribute("count100", String.format("100 denomination: %d", list.get(3)));
            return "withdraw";
        } catch (IllegalStateException e) {
            model.addAttribute("status", "Not enough money");
            return "withdraw";
        }
    }


    @GetMapping("/check-balance")
    public String checkBalance(@RequestParam (required = false) String cardNumber,
                               @RequestParam (required = false) String pin, Model model) {

        if(cardNumber != null && pin != null) {
            try {
                String balance = String.format("Current balance: %s",
                        cashMachineService.checkBalance(cashMachine, cardNumber, pin).toString());
                model.addAttribute("balance", balance);
            } catch (Exception e) {
                return "error_form";
            }
        }
        return "check_balance";
    }

    @GetMapping("/change-pin")
    public String getChangePinForm(){
        return "change_pin";
    }

    @PostMapping("/change-pin")
    public String changePin(@RequestBody ChangePinDto changePinDto,
                            Model model) {

        boolean successful = cashMachineService.changePin(changePinDto.getCardNumber(),
                changePinDto.getOldPin(), changePinDto.getNewPin());

        if (successful) {
            model.addAttribute("status", "Your pin has been changed.");
        } else {
            model.addAttribute("status", "Wrong card number or pin.");
        }
        return "change_pin";
    }



}
