package com.vou.auth_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.vou.auth_service.dto.*;
import com.vou.auth_service.dto.response.AuthRegisterResponse;
import com.vou.auth_service.entity.Auth;
import com.vou.auth_service.entity.Otp;
import com.vou.auth_service.entity.Session;
import com.vou.auth_service.enumerate.ProfileState;
import com.vou.auth_service.enumerate.Role;
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
import java.util.Objects;

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

    public TokenDto authenticate(AuthDto authDto) {
        Auth auth = authRepository.findByUsername(authDto.getUsername());
        if (auth == null)
            throw new AuthException(ErrorCode.USER_NOT_EXIST);

        if (auth.getRole() == Role.USER && authDto.getRole() != Role.USER)
            throw new AuthException(ErrorCode.UNAUTHENTICATED);

        if (!passwordEncoder.matches(authDto.getPassword(), auth.getPassword()))
            throw new AuthException(ErrorCode.UNAUTHENTICATED);

        String token = generateToken(auth.getUsername(), auth.getRole().toString(), auth.getId(), false);
        String refreshToken = generateToken(auth.getUsername(), auth.getRole().toString(), auth.getId(), true);
//        Session session = new Session();
//        session.setRole(auth.getRole());
//        session.setAuth(auth);
//        session.setRefreshToken(refreshToken);
//        sessionRepository.save(session);

        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(token);
        tokenDto.setRefreshToken(refreshToken);
        tokenDto.setProfileID(auth.getId());
        tokenDto.setRole(auth.getRole());
        return tokenDto;
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

    public TokenDto refresh(RefreshDto refreshDto, String rt) {
        boolean verified = verify(rt);
        if(!verified) throw new AuthException(ErrorCode.UNAUTHENTICATED);

//        Session session = sessionRepository.findOneByRefreshToken(rt);
//        if (session == null)
//            throw new AuthException(ErrorCode.UNAUTHENTICATED);
//
//        if (session.getRole() != refreshDto.getRole())
//            throw new AuthException(ErrorCode.UNAUTHENTICATED);
//
//        String newRefreshToken = generateToken(refreshDto.getUsername(), session.getRole().toString(), true);
//        session.setRefreshToken(newRefreshToken);
//        sessionRepository.save(session);
//        String newToken = generateToken(refreshDto.getUsername(), session.getRole().toString(), false);
//
//        TokenDto tokenDto = new TokenDto();
//        tokenDto.setToken(newToken);
//        tokenDto.setRefreshToken(newRefreshToken);
//        tokenDto.setProfileID(session.getAuth().getId());
//        tokenDto.setRole(session.getAuth().getRole());
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

        String newRefreshToken = generateToken(refreshDto.getUsername(), role, profileID, true);
        String newToken = generateToken(refreshDto.getUsername(), role, profileID, false);

        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(newToken);
        tokenDto.setRefreshToken(newRefreshToken);
        tokenDto.setProfileID(profileID);
        tokenDto.setRole(Role.valueOf(role));

        return tokenDto;
    }

    public void logout(LogoutDto logoutDto) {
//        Session session = sessionRepository.findOneByRefreshToken(logoutDto.getRefreshToken());
//        sessionRepository.delete(session);
    }

    public AuthRegisterResponse createAuth(RegisterDto registerDto) {
        // Check duplicate info
        if (authRepository.findByUsername(registerDto.getUsername()) != null)
            throw new AuthException(ErrorCode.USERNAME_EXISTED);

        // Create auth credential with "pending" state
        Auth auth = new Auth();
        auth.setId(registerDto.getId());
        auth.setUsername(registerDto.getUsername());
        auth.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        auth.setRole(registerDto.getRole());
        auth.setProfileState(ProfileState.PENDING);
        Auth savedAuth = authRepository.save(auth);

        // Generate OTP
        Otp otp = otpService.createOtp(registerDto.getPhone(), savedAuth);

        // Call SMS Service
        // ??????

        AuthRegisterResponse response = new AuthRegisterResponse();
        response.setName(registerDto.getUsername());
        response.setPhone(registerDto.getPhone());
        response.setState(savedAuth.getProfileState());
        response.setRole(savedAuth.getRole());

        return response;
    }

    public void deleteAuth(String username) {
        Auth auth = authRepository.findByUsername(username);
        if (auth == null) throw new AuthException(ErrorCode.USER_NOT_EXIST);
        authRepository.delete(auth);
    }

    private String generateToken(String username, String role, String profileID, boolean isRefreshToken) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .claim("role", role)
                .claim("profileID", profileID)
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
