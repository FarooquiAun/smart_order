package com.smartorder.auth;

import com.smartorder.auth.dto.AuthResponse;
import com.smartorder.auth.dto.LoginRequest;
import com.smartorder.auth.dto.RefreshTokenRequest;
import com.smartorder.auth.dto.RegisterRequest;
import com.smartorder.security.jwt.JwtService;
import com.smartorder.security.model.Role;
import com.smartorder.security.model.User;
import com.smartorder.security.repository.RoleRepository;
import com.smartorder.security.repository.UserRepository;
import com.smartorder.security.token.Token;
import com.smartorder.security.token.TokenRepository;
import com.smartorder.security.userDetails.CustomerUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtService jwtService, TokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }


    public AuthResponse register(RegisterRequest registerRequest){
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new RuntimeException("User name already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        Role role=roleRepository.findByName("ROLE_USER")
                .orElseThrow(()->new RuntimeException("ROLE_USER not found"));
        User newUser=new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEmail(registerRequest.getEmail());
        newUser.setEnabled(true);
        newUser.setRoles(Collections.singleton(role));
         userRepository.save(newUser);
        CustomerUserDetails userDetails=new CustomerUserDetails(newUser);
        String accessToken=jwtService.generateAccessToken(userDetails);
        String refreshToken=jwtService.generateRefreshToken(userDetails);


        Token accessTokenEntity=new Token(
                Instant.now().plusMillis(jwtService.getAccessTokenExpirationMs()),
                accessToken,
                newUser.getId()
        );
        tokenRepository.save(accessTokenEntity);

        return new AuthResponse(accessToken,refreshToken);
    }
    public AuthResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        User user=userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()->new RuntimeException("User not found"));
        CustomerUserDetails userDetails=new CustomerUserDetails(user);
        String accessToken=jwtService.generateAccessToken(userDetails);
        String refreshToken=jwtService.generateRefreshToken(userDetails);
        Token accessTokenEntity=new Token(
                Instant.now().plusMillis(jwtService.getRefreshTokenExpirationMs()),
                accessToken,
                user.getId()
        );
        tokenRepository.save(accessTokenEntity);

        return new AuthResponse(accessToken,refreshToken);
    }
    public AuthResponse refresh(RefreshTokenRequest refreshTokenRequest){
        String refreshToken=refreshTokenRequest.getRefreshToken();
        String userName= jwtService.extractUsername(refreshToken);
        User user=userRepository.findByUsername(userName).
                orElseThrow(()-> new RuntimeException("User Not found exception"));
        CustomerUserDetails userDetails=new CustomerUserDetails(user);
        if (!jwtService.isTokenValid(refreshToken,userDetails)){
            throw new RuntimeException("Refresh token is not valid or is Expired");
        }
        String newAccessToken= jwtService.generateAccessToken(userDetails);
        return new AuthResponse(newAccessToken,refreshToken);
    }

    public void logout(String accessToken){
        var optionalToken=tokenRepository.findByToken(accessToken);
        if (optionalToken.isPresent()){
            Token token=optionalToken.get();
            token.setRevoked(true);
            tokenRepository.save(token);
        }
    }

}
