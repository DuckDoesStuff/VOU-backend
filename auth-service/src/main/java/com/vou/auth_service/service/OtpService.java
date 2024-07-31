package com.vou.auth_service.service;

import com.vou.auth_service.dto.ProfileStateDto;
import com.vou.auth_service.entity.Auth;
import com.vou.auth_service.entity.Otp;
import com.vou.auth_service.enumerate.ProfileState;
import com.vou.auth_service.repository.AuthRepository;
import com.vou.auth_service.repository.OtpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class OtpService {
    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    @Autowired
    OtpRepository otpRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    WebClient.Builder webClientBuilder;

    private void validateAuthCredential(String phone) {

    }

    private String generateOtpCode() {
        return "123456";
    }

    public Otp createOtp(String phone, Auth auth) {
        Otp otp = new Otp();
        otp.setOtp(generateOtpCode());
        otp.setPhone(phone);
        otp.setExpiryDate(new Date(
                Instant.now().plus(60 * 5, ChronoUnit.SECONDS).toEpochMilli()
        ));
        otp.setAuth(auth);
        return otpRepository.save(otp);
    }

    public boolean verify(String phone, String code) {
        List<Otp> otpList = otpRepository.findByPhone(phone);
        Otp verifiedOtp = verifyOtp(otpList, code);

        if (verifiedOtp == null)
            return false;

        Auth auth = verifiedOtp.getAuth();
        auth.setProfileState(ProfileState.VERIFIED);
        otpRepository.delete(verifiedOtp);

        // Call user service to verify user profile
        String url = "http://localhost:8002/users/" + auth.getUsername();
        WebClient webClient = webClientBuilder.build();
        webClient
                .post()
                .uri(url)
                .body(Mono.just(new ProfileStateDto(ProfileState.VERIFIED)), ProfileStateDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return true;
    }

    public Otp verifyOtp(List<Otp> otpList, String code) {
        for (Otp otp : otpList) {
            Date expiryDate = otp.getExpiryDate();
            String otpCode = otp.getOtp();

            if (expiryDate.after(new Date()) && Objects.equals(otpCode, code)) {
                return otp;
            }
        }

        return null;
    }
}
