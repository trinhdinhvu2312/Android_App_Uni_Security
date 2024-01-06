package StoreApp.StoreApp.repository;

import StoreApp.StoreApp.entity.Token;
import StoreApp.StoreApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllByUser_Id(String userId);
    Optional<Token> findByUserAndExpiredIsFalse(User user);

}
