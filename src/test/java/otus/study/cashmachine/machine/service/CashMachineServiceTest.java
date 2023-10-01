package otus.study.cashmachine.machine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import otus.study.cashmachine.TestUtil;
import otus.study.cashmachine.bank.dao.CardsDao;
import otus.study.cashmachine.bank.data.Card;
import otus.study.cashmachine.bank.service.AccountService;
import otus.study.cashmachine.bank.service.impl.CardServiceImpl;
import otus.study.cashmachine.machine.data.CashMachine;
import otus.study.cashmachine.machine.data.MoneyBox;
import otus.study.cashmachine.machine.service.impl.CashMachineServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static otus.study.cashmachine.TestUtil.getHash;

@ExtendWith(MockitoExtension.class)
class CashMachineServiceTest {

    @Spy
    @InjectMocks
    private CardServiceImpl cardService;

    @Mock
    private CardsDao cardsDao;

    @Mock
    private AccountService accountService;

    @Mock
    private MoneyBoxService moneyBoxService;

    private CashMachineServiceImpl cashMachineService;

    private CashMachine cashMachine = new CashMachine(new MoneyBox());

    @BeforeEach
    void init() {
        cashMachineService = new CashMachineServiceImpl(cardService, accountService, moneyBoxService);
    }


    @Test
    void getMoney() {
// @TODO create get money test using spy as mock
        ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

        when(cardsDao.getCardByNumber("100"))
                .thenReturn(new Card(1L, "100", 100L, TestUtil.getHash("0000")));

        when(accountService.getMoney(idCaptor.capture(), amountCaptor.capture()))
                .thenReturn(BigDecimal.TEN);


        cashMachineService.getMoney(cashMachine, "100", "0000", BigDecimal.ONE);

        verify(cardService, only()).getMoney("100", "0000", amountCaptor.getValue());
        verify(accountService, only()).getMoney(anyLong(), any());
        assertEquals(BigDecimal.ONE, amountCaptor.getValue());
        assertEquals(100L, idCaptor.getValue().longValue());

    }

    @Test
    void putMoney() {
        when(cardsDao.getCardByNumber("100"))
                .thenReturn(new Card(1L, "100", 1L, TestUtil.getHash("0000")));
        when(cardService.getBalance("100", "0000")).thenReturn(BigDecimal.ZERO);
        BigDecimal expected = new BigDecimal(6600);
        when(cardService.putMoney("100", "0000", expected))
                .thenReturn(expected);
        BigDecimal actual = cashMachineService.putMoney(cashMachine, "100", "0000", Arrays.asList(1, 1, 1, 1));
        verify(moneyBoxService).putMoney(cashMachine.getMoneyBox(), 1, 1, 1, 1);
        verify(cardService).putMoney("100", "0000", new BigDecimal(6600));
        assertEquals(expected, actual);

    }

    @Test
    void checkBalance() {
        BigDecimal expected = BigDecimal.ONE;
        when(cardsDao.getCardByNumber("100"))
                .thenReturn(new Card(1, "100", 1L, getHash("0000")));
        when(cardService.getBalance("100", "0000")).thenReturn(expected);
        BigDecimal actual = cashMachineService.checkBalance(cashMachine, "100", "0000");
        assertEquals(expected, actual);

    }

    @Test
    void changePin() {
// @TODO create change pin test using spy as implementation and ArgumentCaptor and thenReturn
        when(cardsDao.getCardByNumber(any()))
                .thenReturn(new Card(1, "100", 1L, getHash("0000")));
        when(cardService.cnangePin(eq("100"), eq("0000"), eq("1111"))).thenReturn(true);
        boolean result = cashMachineService.changePin("100", "0000", "1111");
        assertTrue(result);
    }

    @Test
    void changePinWithAnswer() {
// @TODO create change pin test using spy as implementation and mock an thenAnswer

        when(cardsDao.getCardByNumber(any()))
                .thenAnswer(invocation -> {
                    String cardNumber = invocation.getArgument(0);
                    if (cardNumber.equals("100")) {
                        return new Card(1, cardNumber, 1L, getHash("0000"));
                    } else {
                        return null;
                    }
                });
        assertTrue(cashMachineService.changePin("100", "0000", "1111"));
    }
}