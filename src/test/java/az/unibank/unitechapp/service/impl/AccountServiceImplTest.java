package az.unibank.unitechapp.service.impl;

import az.unibank.unitechapp.exception.CustomErrorException;
import az.unibank.unitechapp.model.Account;
import az.unibank.unitechapp.repository.AccountRepository;
import az.unibank.unitechapp.request.TransferRequest;
import az.unibank.unitechapp.response.AccountResponse;
import az.unibank.unitechapp.response.TransferResponse;
import az.unibank.unitechapp.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    private String token="eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2Nj" +
            "EwODQ5MTEsImV4cCI6MTY2MTA4ODUxMSwic3ViIjoiMSJ9.ami9TOUpVJi5X" +
            "dvXrwFE5in_f3EF8Q9cxh-tVK1-6DiWfDHsCfDTDAZ4mY-TrQ49ilDv5e1uCcoxG-BzLH1LtA";

    //@Mock
    private AccountRepository accountRepository;
    //@Mock
    private JwtTokenUtil jwtTokenUtil;
    // @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        accountRepository = mock(AccountRepository.class);
        jwtTokenUtil = mock(JwtTokenUtil.class);
        accountService = new AccountServiceImpl(accountRepository,jwtTokenUtil);
        when(jwtTokenUtil.validateToken(token)).thenReturn(true);
    }

    @Test
    public void testGetAccounts(){
        long userId=1;
        int status=1;

        Account account=Account.builder()
                .id(1)
                .balance(1.70)
                .status(1)
                .build();

        when(accountRepository.findByUserIdAndStatus(userId,status)).thenReturn(Arrays.asList(account));

        List<AccountResponse> accountResponseList=accountService.getAccounts(userId,token);

        assertNotNull(accountResponseList);
        assertEquals(1,accountResponseList.size());

    }

    @Test
    public void testTransferAccountToAccountSuccess(){

        TransferRequest transferRequest=TransferRequest.builder()
                .accountFrom(1)
                .accountTo(2)
                .amount(1.70)
                .build();

        Account accountFrom=Account.builder()
                .id(1)
                .balance(6)
                .status(1)
                .build();

        Account accountTo=Account.builder()
                .id(2)
                .balance(7)
                .status(1)
                .build();

        when(accountRepository.findById(transferRequest.getAccountFrom())).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findById(transferRequest.getAccountTo())).thenReturn(Optional.of(accountTo));
        when(accountRepository.save(accountFrom)).thenReturn(accountFrom);
        when(accountRepository.save(accountTo)).thenReturn(accountTo);

        TransferResponse transferResponse=accountService.transferAccountToAccount(transferRequest,token);

        assertNotNull(transferResponse);
        assertEquals("true",transferResponse.getSuccess());

    }

    @Test
    public void testTransferAccountToAccountWithErrorNotFoundAccount(){
        Exception ex= assertThrows(CustomErrorException.class,()-> {
            TransferRequest transferRequest = TransferRequest.builder()
                    .accountFrom(5)
                    .accountTo(2)
                    .amount(1.70)
                    .build();

            when(accountRepository.findById(transferRequest.getAccountFrom())).thenReturn(Optional.empty());

            TransferResponse transferResponse = accountService.transferAccountToAccount(transferRequest, token);

            assertEquals("true", transferResponse.getSuccess());
        });

        assertEquals("not found account",ex.getMessage());
    }

    @Test
    public void testTransferAccountToAccountWithErrorNotExistingAccount(){
        Exception ex= assertThrows(CustomErrorException.class,()-> {
            TransferRequest transferRequest = TransferRequest.builder()
                    .accountFrom(1)
                    .accountTo(6)
                    .amount(1.70)
                    .build();

            Account account = Account.builder()
                    .id(2)
                    .balance(0)
                    .status(1)
                    .build();

            when(accountRepository.findById(transferRequest.getAccountFrom())).thenReturn(Optional.of(account));
            when(accountRepository.findById(transferRequest.getAccountTo())).thenReturn(Optional.empty());

            TransferResponse transferResponse = accountService.transferAccountToAccount(transferRequest, token);
        });

        assertEquals("transfer to non existing account",ex.getMessage());

    }

    @Test
    public void testTransferAccountToAccountWithErrorBalanceLow(){
        Exception ex= assertThrows(CustomErrorException.class,()-> {
            TransferRequest transferRequest = TransferRequest.builder()
                    .accountFrom(1)
                    .accountTo(6)
                    .amount(1.70)
                    .build();

            Account account = Account.builder()
                    .id(2)
                    .balance(0)
                    .status(1)
                    .build();

            when(accountRepository.findById(transferRequest.getAccountFrom())).thenReturn(Optional.of(account));
            when(accountRepository.findById(transferRequest.getAccountTo())).thenReturn(Optional.of(account));

            TransferResponse transferResponse = accountService.transferAccountToAccount(transferRequest, token);
        });
        assertEquals("no enough money in my account balance",ex.getMessage());

    }

    @Test
    public void testTransferAccountToAccountWithErrorSameAccount(){
        Exception ex= assertThrows(CustomErrorException.class,()-> {
            TransferRequest transferRequest = TransferRequest.builder()
                    .accountFrom(1)
                    .accountTo(6)
                    .amount(1.70)
                    .build();

            Account accountFrom = Account.builder()
                    .id(2)
                    .balance(6)
                    .status(1)
                    .build();

            Account accountTo = Account.builder()
                    .id(2)
                    .balance(8)
                    .status(1)
                    .build();

            when(accountRepository.findById(transferRequest.getAccountFrom())).thenReturn(Optional.of(accountFrom));
            when(accountRepository.findById(transferRequest.getAccountTo())).thenReturn(Optional.of(accountTo));

            TransferResponse transferResponse = accountService.transferAccountToAccount(transferRequest, token);
        });
        assertEquals("transfer to same account",ex.getMessage());

    }

    @Test
    public void testTransferAccountToAccountWithErrorDeactiveAccount(){
        Exception ex= assertThrows(CustomErrorException.class,()-> {
            TransferRequest transferRequest = TransferRequest.builder()
                    .accountFrom(1)
                    .accountTo(6)
                    .amount(1.70)
                    .build();

            Account accountFrom = Account.builder()
                    .id(2)
                    .balance(6)
                    .status(1)
                    .build();

            Account accountTo = Account.builder()
                    .id(3)
                    .balance(8)
                    .status(0)
                    .build();

            when(accountRepository.findById(transferRequest.getAccountFrom())).thenReturn(Optional.of(accountFrom));
            when(accountRepository.findById(transferRequest.getAccountTo())).thenReturn(Optional.of(accountTo));

            TransferResponse transferResponse = accountService.transferAccountToAccount(transferRequest, token);
        });
        assertEquals("transfer to deactive account",ex.getMessage());

    }


}