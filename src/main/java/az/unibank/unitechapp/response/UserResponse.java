package az.unibank.unitechapp.response;


import az.unibank.unitechapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    Long id;
    String pin;
    List<AccountResponse> accountResponseList;

    public UserResponse(User user, List<AccountResponse> accountResponseList) {
        this.id = user.getId();
        this.pin=user.getPin();
        this.accountResponseList=accountResponseList;
    }
}
