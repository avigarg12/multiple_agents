package com.avi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestHeader(value="Authorization",required = false) String authHeader){
        //1. check if the auth header is present and starts with basic??
        if(authHeader==null || !authHeader.startsWith("Basic")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Missing Basic auth header"));
        }

        //2. Decode the Base64 creds
        try {
            String base64Creds = authHeader.substring(6); // remove Basic
            byte[] decoded = Base64.getDecoder().decode(base64Creds);
            String creds = new String(decoded, StandardCharsets.UTF_8); // "username:password"
            String[] values = creds.split(":",2);
            String username = values[0];
            String password = values[1];

            //3. validate creds
            if("admin".equals(username) && "password".equals(password)){
                String token = tokenService.generateToken(username);
                return ResponseEntity.ok(Map.of("token",token, "expiresIn","30 sec"));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Invalid auth format"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Invalid username or password"));
    }
}
