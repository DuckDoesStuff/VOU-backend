package com.vou.auth_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.vou.auth_service.dto.AuthDto;
import com.vou.auth_service.dto.AuthResponse;
import com.vou.auth_service.dto.LogoutDto;
import com.vou.auth_service.dto.RefreshDto;
import com.vou.auth_service.entity.Auth;
import com.vou.auth_service.entity.Session;
import com.vou.auth_service.exception.AuthException;
import com.vou.auth_service.exception.ErrorCode;
import com.vou.auth_service.repository.AuthRepository;
import com.vou.auth_service.repository.SessionRepository;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected int validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected int refreshableDuration;
    
    public List<Auth> getAuthList() {
        return authRepository.findAll();
    }

    public AuthResponse authenticate(AuthDto authDto) {
        Auth auth = authRepository.findByUsername(authDto.getUsername());
        if (auth == null)
            throw new AuthException(ErrorCode.USER_NOT_EXIST);

        if (!passwordEncoder.matches(authDto.getPassword(), auth.getPassword()))
            throw new AuthException(ErrorCode.UNAUTHENTICATED);

        String token = generateToken(auth.getUsername(), false);
        String refreshToken = generateToken(auth.getUsername(), true);
        Session session = new Session();
        session.setAuth(auth);
        session.setRefreshToken(refreshToken);
        sessionRepository.save(session);

        return AuthResponse.builder()
                .authorized(true)
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean verify(String token) {
        Date expiryTime;
        boolean verified;
        try {
            JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            verified = signedJWT.verify(jwsVerifier);
        } catch(ParseException e) {
            log.error("Error parsing token to verify");
            return false;
        } catch (JOSEException e) {
            log.error("Couldn't verify the token or the token is shorter than 256-bit");
            return false;
        }

        return verified && expiryTime.after(new Date());
    }

    public AuthResponse refresh(RefreshDto refreshDto) {
        boolean verified = verify(refreshDto.getRefreshToken());
        if(!verified) throw new AuthException(ErrorCode.UNAUTHENTICATED);

        String refreshToken = generateToken(refreshDto.getUsername(), true);
        Session session = sessionRepository.findOneByRefreshToken(refreshDto.getRefreshToken());

        if(session == null)
            throw new AuthException(ErrorCode.UNAUTHENTICATED);

        session.setRefreshToken(refreshToken);
        sessionRepository.save(session);

        return AuthResponse.builder()
                .refreshToken(refreshToken)
                .token(generateToken(refreshDto.getUsername(), false))
                .authorized(true)
                .build();
    }

    public void logout(LogoutDto logoutDto) {
        Session session = sessionRepository.findOneByRefreshToken(logoutDto.getRefreshToken());
        sessionRepository.delete(session);
    }

    public Auth createAuth(AuthDto authDto) {
        if (authRepository.findByUsername(authDto.getUsername()) != null)
            throw new AuthException(ErrorCode.USER_EXISTED);

        Auth auth = new Auth();

        auth.setUsername(authDto.getUsername());
        auth.setPassword(passwordEncoder.encode(authDto.getPassword()));

        return authRepository.save(auth);
    }

    public void deleteAuth(String username) {
        Auth auth = authRepository.findByUsername(username);
        if (auth == null) throw new AuthException(ErrorCode.USER_NOT_EXIST);
        authRepository.delete(auth);
    }

    private String generateToken(String username, boolean isRefreshToken) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("vou.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(isRefreshToken ? refreshableDuration : validDuration, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.warn("Unable to create jwt");

            throw new RuntimeException(e);
        }
    }
}
