package az.unibank.unitechapp.repository;

import az.unibank.unitechapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByPin(String pin);

}
