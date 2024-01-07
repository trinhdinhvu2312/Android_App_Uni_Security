package StoreApp.StoreApp.service;

import StoreApp.StoreApp.entity.Token;
import StoreApp.StoreApp.entity.User;
import StoreApp.StoreApp.exception.DataNotfoundException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;


public interface JwtTokenService {

    void saveToken(String userId, String token);

    List<Token> findAllByUser_Id(String userId);

    Token findTokensByToken(String token);

    void deleteTokenByUserId(String userId);
}
