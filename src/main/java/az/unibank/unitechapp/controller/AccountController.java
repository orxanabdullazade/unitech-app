package az.unibank.unitechapp.controller;


import az.unibank.unitechapp.request.TransferRequest;
import az.unibank.unitechapp.response.AccountResponse;
import az.unibank.unitechapp.response.TransferResponse;
import az.unibank.unitechapp.service.AccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@ApiOperation("Account Service")
public class AccountController {

    public final AccountService accountService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccounts(@PathVariable Long userId, @RequestHeader("Authorization") String bearer) {
        return ResponseEntity.ok(accountService.getAccounts(userId, bearer));
    }

    @PutMapping
    public ResponseEntity<TransferResponse> transferAccountToAccount(@RequestBody TransferRequest transferRequest,
                                                                     @RequestHeader("Authorization") String bearer) {
        return ResponseEntity.ok(accountService.transferAccountToAccount(transferRequest, bearer));
    }


}
