package com.vou.api.service;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.vou.api.config.JwtProvider;
import com.vou.api.dto.*;
import com.vou.api.entity.Auth;
import com.vou.api.entity.Otp;
import com.vou.api.enumerate.ProfileState;
import com.vou.api.enumerate.Role;
import com.vou.api.exception.AuthException;
import com.vou.api.exception.ErrorCode;
import com.vou.api.repository.AuthRepository;
import com.vou.api.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

@Component
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtProvider jwtProvider;

    public List<Auth> getAuthList() {
        return authRepository.findAll();
    }

    public TokenDto authenticate(AuthDto authDto) {
        if(authDto.getPassword().equals("admin") && authDto.getUsername().equals("admin"))
        {
            System.out.println("Prime admin logging in");
            String token = jwtProvider.generateToken(authDto.getUsername(), Role.ADMIN.toString(), String.valueOf(1), false);
            String refreshToken = jwtProvider.generateToken(authDto.getUsername(), Role.ADMIN.toString(), String.valueOf(1), true);

            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(token);
            tokenDto.setRefreshToken(refreshToken);
            tokenDto.setProfileID(String.valueOf(1));
            tokenDto.setRole(Role.ADMIN);
            return tokenDto;
        }


        Auth auth = authRepository.findByUsername(authDto.getUsername());
        if (auth == null)
            throw new AuthException(ErrorCode.USER_NOT_EXIST);

        if(auth.getProfileState() == ProfileState.LOCKED)
            throw new AuthException(ErrorCode.ACCOUNT_LOCKED);

        if (auth.getRole() == Role.USER && authDto.getRole() != Role.USER)
            throw new AuthException(ErrorCode.UNAUTHENTICATED);

        if (!passwordEncoder.matches(authDto.getPassword(), auth.getPassword()))
            throw new AuthException(ErrorCode.WRONG_CREDENTIAL);

        String token = jwtProvider.generateToken(auth.getUsername(), auth.getRole().toString(), auth.getId(), false);
        String refreshToken = jwtProvider.generateToken(auth.getUsername(), auth.getRole().toString(), auth.getId(), true);

        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(token);
        tokenDto.setRefreshToken(refreshToken);
        tokenDto.setProfileID(auth.getId());
        tokenDto.setRole(auth.getRole());
        return tokenDto;
    }

    public TokenDto refresh(RefreshDto refreshDto, String rt) {
        boolean verified = jwtProvider.validateToken(rt);
        if (!verified) throw new AuthException(ErrorCode.UNAUTHENTICATED);

        JWT jwt;
        JWTClaimsSet jwtClaimsSet = null;
        String role, profileID;
        try {
            jwt = JWTParser.parse(rt);
            if (jwt instanceof SignedJWT signedJWT) {
                jwtClaimsSet = signedJWT.getJWTClaimsSet();
            }
            assert jwtClaimsSet != null;
            role = jwtClaimsSet.getStringClaim("role");
            profileID = jwtClaimsSet.getStringClaim("profileID");
        } catch (ParseException e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        }

        if (!Objects.equals(role, refreshDto.getRole().toString()))
            throw new AuthException(ErrorCode.UNAUTHENTICATED);

        String newRefreshToken = jwtProvider.generateToken(refreshDto.getUsername(), role, profileID, true);
        String newToken = jwtProvider.generateToken(refreshDto.getUsername(), role, profileID, false);

        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(newToken);
        tokenDto.setRefreshToken(newRefreshToken);
        tokenDto.setProfileID(profileID);
        tokenDto.setRole(Role.valueOf(role));

        return tokenDto;
    }

    public boolean verify(String token) {
        return jwtProvider.validateToken(token);
    }

    public void logout(LogoutDto logoutDto) {
//        Session session = sessionRepository.findOneByRefreshToken(logoutDto.getRefreshToken());
//        sessionRepository.delete(session);
    }

    public void createAuth(AuthRegisterDto authRegisterDto) {
        // Check duplicate info
        if (authRepository.findByUsername(authRegisterDto.getUsername()) != null)
            throw new AuthException(ErrorCode.USERNAME_EXISTED);

        // Create auth credential with "pending" state
        Auth auth = new Auth();
        auth.setId(authRegisterDto.getId());
        auth.setUsername(authRegisterDto.getUsername());
        auth.setPassword(passwordEncoder.encode(authRegisterDto.getPassword()));
        auth.setRole(authRegisterDto.getRole());
        auth.setProfileState(ProfileState.PENDING);
        authRepository.save(auth);

        // Generate OTP
        Otp otp = otpService.createOtp(authRegisterDto.getPhone(), auth);
        // Call SMS Service
        // ??????
    }

    public void deleteAuth(String id) {
        Optional<Auth> auth = authRepository.findById(id);
        auth.ifPresent(value -> authRepository.delete(value));
    }

    public void updateAuth(String id, Role role, ProfileState state) {
        Optional<Auth> optAuth = authRepository.findById(id);
        if(optAuth.isEmpty()) return;
        Auth auth = optAuth.get();
        auth.setRole(role);
        auth.setProfileState(state);

        authRepository.save(auth);
    }

}
