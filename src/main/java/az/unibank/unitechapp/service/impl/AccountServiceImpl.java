package az.unibank.unitechapp.service.impl;

import az.unibank.unitechapp.exception.CustomErrorException;
import az.unibank.unitechapp.exception.ErrorCodeEnum;
import az.unibank.unitechapp.model.Account;
import az.unibank.unitechapp.repository.AccountRepository;
import az.unibank.unitechapp.request.TransferRequest;
import az.unibank.unitechapp.response.AccountResponse;
import az.unibank.unitechapp.response.TransferResponse;
import az.unibank.unitechapp.service.AccountService;
import az.unibank.unitechapp.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    public final AccountRepository accountRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public List<AccountResponse> getAccounts(Long userId,String bearer) {
        if ( !jwtTokenUtil.validateToken(bearer)) throw new CustomErrorException(ErrorCodeEnum.UNAUTHORIZED);

        List<Account> accountsList = accountRepository.findByUserIdAndStatus(userId,1);

        return accountsList.stream().map(account -> new AccountResponse(account)).collect(Collectors.toList());
    }

    @Override
    public TransferResponse transferAccountToAccount(TransferRequest transferRequest, String bearer) {
        if ( !jwtTokenUtil.validateToken(bearer)) throw new CustomErrorException(ErrorCodeEnum.UNAUTHORIZED);

        Account accountFrom = accountRepository.findById(transferRequest.getAccountFrom())
                .orElseThrow(() -> new CustomErrorException(ErrorCodeEnum.NOT_FOUND_ACCOUNT));
        Account accountTo = accountRepository.findById(transferRequest.getAccountTo())
                .orElseThrow(() -> new CustomErrorException(ErrorCodeEnum.TRANSFER_NON_EXISTING_ACCOUNT));

        if (accountFrom.getBalance() < transferRequest.getAmount()) {
            throw new CustomErrorException(ErrorCodeEnum.BALANCE_LOW);
        }
        if (accountFrom.getId() == accountTo.getId()) {
            throw new CustomErrorException(ErrorCodeEnum.TRANSFER_SAME_ACCOUNT);
        }
        if (accountTo.getStatus() == 0) {
            throw new CustomErrorException(ErrorCodeEnum.TRANSFER_DEACTIVE_ACCOUNT);
        }

        accountFrom.setBalance(accountFrom.getBalance() - transferRequest.getAmount());
        accountRepository.save(accountFrom);
        accountTo.setBalance(accountTo.getBalance() + transferRequest.getAmount());
        accountRepository.save(accountTo);

        return TransferResponse
                .builder()
                .success("true")
                .build();

    }


}
