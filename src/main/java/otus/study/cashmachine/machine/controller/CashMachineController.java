package otus.study.cashmachine.machine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import otus.study.cashmachine.machine.data.CashMachine;
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
    public String deposit(@RequestParam String cardNumber,
                          @RequestParam String pin,
                          @RequestParam Integer count5000,
                          @RequestParam Integer count1000,
                          @RequestParam Integer count500,
                          @RequestParam Integer count100,
                          Model model) {
        try {
            cashMachineService.putMoney(cashMachine, cardNumber, pin, List.of(count5000,
                    count1000, count500, count100));
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
    public String withdraw(@RequestParam String cardNumber,
                           @RequestParam String pin,
                           @RequestParam String amount,
                           Model model) {
        try {
            var list = cashMachineService.getMoney(cashMachine, cardNumber, pin, new BigDecimal(amount));
            model.addAttribute("result", "Withdrawal of:");
            model.addAttribute("count5000", String.format("5000 denomination: %d", list.get(0)));
            model.addAttribute("count1000", String.format("1000 denomination: %d", list.get(1)));
            model.addAttribute("count500", String.format("500 denomination: %d", list.get(2)));
            model.addAttribute("count100", String.format("100 denomination: %d", list.get(3)));
            return "withdraw";
        } catch (Exception e) {
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
                return "check_balance";
            } catch (Exception e) {
                return "error_form";
            }
        }
        else{
            return "check_balance";
        }

    }

    @GetMapping("/change-pin")
    public String getChangePinForm(){
        return "change_pin";
    }

    @PostMapping("/change-pin")
    public String changePin(@RequestParam String cardNumber,
                            @RequestParam String oldPin,
                            @RequestParam String newPin,
                            Model model) {

        boolean successful = cashMachineService.changePin(cardNumber, oldPin, newPin);

        if (successful) {
            model.addAttribute("status", "Your pin has been changed.");
        } else {
            model.addAttribute("status", "Wrong card number or pin.");
        }
        return "change_pin";
    }



}
