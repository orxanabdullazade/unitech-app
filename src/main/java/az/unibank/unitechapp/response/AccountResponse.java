package az.unibank.unitechapp.response;

import az.unibank.unitechapp.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

     long id;

     double balance;

     int status;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance();
        this.status = account.getStatus();
    }
}
