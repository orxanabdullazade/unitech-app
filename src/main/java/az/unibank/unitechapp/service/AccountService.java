package az.unibank.unitechapp.service;

import az.unibank.unitechapp.request.TransferRequest;
import az.unibank.unitechapp.response.AccountResponse;
import az.unibank.unitechapp.response.TransferResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    List<AccountResponse> getAccounts(Long userId,String bearer);

    TransferResponse transferAccountToAccount(TransferRequest transferRequest,String bearer);
}
