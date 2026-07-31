package com.avi;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    private final Map<String, TokenDetails> tokenStore = new ConcurrentHashMap<>();

    private static final long EXPIRE_SECONDS = 3000;

    //Generate a new token valid for 30 sec
    public String generateToken(String username){
        String token = UUID.randomUUID().toString();
        Instant expiryTime = Instant.now().plusSeconds(EXPIRE_SECONDS);

        tokenStore.put(token, new TokenDetails(username,expiryTime));
        return token;
    }

    //validate a token
    public boolean isValid(String token){
        TokenDetails details = tokenStore.get(token);
        if(details==null) return false;

        if(Instant.now().isAfter(details.getExpiryTime())) {
            tokenStore.remove(token);
            return false;
        }
        return true;
    }


    private static class TokenDetails{
        private final String username;
        private final Instant expiryTime;

        public TokenDetails(String username, Instant expiryTime) {
            this.username = username;
            this.expiryTime = expiryTime;
        }

        public Instant getExpiryTime() {
            return expiryTime;
        }

        public String getUsername() {
            return username;
        }
    }
}
