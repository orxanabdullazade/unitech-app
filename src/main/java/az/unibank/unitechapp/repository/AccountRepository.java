package az.unibank.unitechapp.repository;

import az.unibank.unitechapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> findByUserIdAndStatus(long user_id,int status);

}
