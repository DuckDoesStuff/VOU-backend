package com.vou.api.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected int validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected int refreshableDuration;

    public String generateToken(String username, String role, String profileID, boolean isRefreshToken) {
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

    public boolean validateToken(String token) {
        Date expiryTime;
        boolean verified;
        try {
            JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            verified = signedJWT.verify(jwsVerifier);
        } catch (ParseException e) {
            log.error("Error parsing token to verify");
            return false;
        } catch (JOSEException e) {
            log.error("Couldn't verify the token or the token is shorter than 256-bit");
            return false;
        }

        return verified && expiryTime.after(new Date());
    }

    public String extractRole(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        MACVerifier verifier = new MACVerifier(SIGNER_KEY);

        if (signedJWT.verify(verifier)) {
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claims.getStringClaim("role");
        }

        return null;
    }
}
