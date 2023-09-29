package otus.study.cashmachine.bank.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import otus.study.cashmachine.bank.dao.AccountDao;
import otus.study.cashmachine.bank.data.Account;
import otus.study.cashmachine.bank.service.impl.AccountServiceImpl;
import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class AccountServiceTest {


    static AccountDao accountDao;

    static AccountService accountService;


    @BeforeEach
    void init(){
        accountDao = mock(AccountDao.class);
        accountService = new AccountServiceImpl(accountDao);

    }

    @Test
    void createAccountMock() {
//        @TODO test account creation with mock and ArgumentMatcher
        when(accountDao.saveAccount(any(Account.class))).thenReturn(new Account(0, new BigDecimal(1000)));
        assertEquals(accountService.createAccount(new BigDecimal(1000)).getAmount(),
                new BigDecimal(1000));
    }



    @Test
    void createAccountCaptor() {
//  @TODO test account creation with ArgumentCaptor

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        when(accountDao.saveAccount(captor.capture())).thenReturn(new Account(0, new BigDecimal(2)));
        assertEquals(accountService.createAccount(new BigDecimal(2)), captor.getValue());


    }

    @Test
    void addSum() {
        Account actualAccount = new Account(1, new BigDecimal(2));
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(accountDao.getAccount(longArgumentCaptor.capture())).thenReturn(actualAccount);
        accountService.putMoney(1L, new BigDecimal(2));
        Account expectedAccount = new Account(1L, new BigDecimal(4));
        verify(accountDao, only()).getAccount(anyLong());
        assertEquals(expectedAccount, actualAccount);
        assertEquals(1, longArgumentCaptor.getValue().longValue());


    }

    @Test
    void getSum() {
        Account accountBeforeGetSum = new Account(1, new BigDecimal(2));
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(accountDao.getAccount(longArgumentCaptor.capture())).thenReturn(accountBeforeGetSum);
        accountService.getMoney(1L, new BigDecimal(2));
        Account accountAfterGetSum = new Account(longArgumentCaptor.getValue(), new BigDecimal(0));
        verify(accountDao, only()).getAccount(longArgumentCaptor.getValue());
        assertEquals(accountBeforeGetSum, accountAfterGetSum);
    }

    @Test
    void getSumException(){
        Account account = new Account(1, new BigDecimal(10));
        when(accountDao.getAccount(1L)).thenReturn(account);
        try{
            accountService.getMoney(1L, new BigDecimal(100_000));
        } catch (IllegalArgumentException e){
            assertEquals("Not enough money", e.getMessage());
            return;
        }
        fail();

    }

    @Test
    void getAccount() {
        Account expectedAccount = new Account(1L, new BigDecimal(1));
        when(accountDao.getAccount(1L)).thenReturn(expectedAccount);
        assertEquals(expectedAccount, accountService.getAccount(1L));

    }

    @Test
    void checkBalance() {
        BigDecimal expectedBalance = new BigDecimal(1_000_000);
        when(accountDao.getAccount(1L)).thenReturn(new Account(1L, expectedBalance));
        assertEquals(expectedBalance, accountService.checkBalance(1L));
    }
}
