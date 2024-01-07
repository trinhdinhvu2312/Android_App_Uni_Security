package StoreApp.StoreApp.service.impl;

import StoreApp.StoreApp.entity.Token;
import StoreApp.StoreApp.entity.User;
import StoreApp.StoreApp.repository.TokenRepository;
import StoreApp.StoreApp.repository.UserRepository;
import StoreApp.StoreApp.service.JwtTokenService;
import StoreApp.StoreApp.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    UserRepository userRepository;
    TokenRepository tokenRepository;
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    public JwtTokenServiceImpl(JwtTokenUtils jwtTokenUtils, UserRepository userRepository, TokenRepository tokenRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void saveToken(String userId, String token) {
        // Retrieve the User from the userRepository
        Optional<User> userOptional = userRepository.findById(userId);

        // Check if the user is present
        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if a token already exists for the user with a non-expired expiration date
        Optional<Token> existingTokenOptional = tokenRepository.findByUserAndExpiredIsFalse(user);

        if (existingTokenOptional.isPresent()) {
            // If a token already exists, update its properties
            Token existingToken = existingTokenOptional.get();
            existingToken.setToken(token);
            existingToken.setExpirationDate(jwtTokenUtils.getEXPIRATION_TIME());

            // Save the updated Token to the database
            tokenRepository.save(existingToken);
        } else {
            // If no token exists, create a new Token instance
            Token tokenEntity = new Token();
            tokenEntity.setUser(user);
            tokenEntity.setToken(token);
            tokenEntity.setExpirationDate(jwtTokenUtils.getEXPIRATION_TIME());
            tokenEntity.setExpired(false);

            // Save the Token to the database
            tokenRepository.save(tokenEntity);
        }
    }



    @Override
    public List<Token> findAllByUser_Id(String userId) {
        return tokenRepository.findAllByUser_Id(userId);
    }

    @Override
    public Token findTokensByToken(String token) {
        return tokenRepository.findTokenByToken(token).orElseThrow();
    }

    @Override
    public void deleteTokenByUserId(String userId) {
        tokenRepository.deleteTokenByUser_Id(userId);
    }

    // END OF CREATE JWT TOKEN METHOD.
}
// END OF JWT TOKEN GENERATOR / SERVICE.
