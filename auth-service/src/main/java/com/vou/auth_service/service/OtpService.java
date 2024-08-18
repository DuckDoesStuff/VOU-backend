package com.vou.auth_service.service;

import com.vou.auth_service.dto.ProfileStateDto;
import com.vou.auth_service.dto.request.ActivateProfileRequest;
import com.vou.auth_service.entity.Auth;
import com.vou.auth_service.entity.Otp;
import com.vou.auth_service.enumerate.ProfileState;
import com.vou.auth_service.enumerate.Role;
import com.vou.auth_service.exception.ErrorCode;
import com.vou.auth_service.exception.OtpException;
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

    @Autowired
    KafkaService<ActivateProfileRequest> kafkaActivateProfileService;

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
            throw new OtpException(ErrorCode.INVALID_OTP);

        Auth auth = verifiedOtp.getAuth();
        auth.setProfileState(ProfileState.VERIFIED);
        otpRepository.delete(verifiedOtp);

        // Call user service to verify user profile
        ActivateProfileRequest activateProfileRequest = new ActivateProfileRequest(auth.getUsername(), ProfileState.VERIFIED);
        if(auth.getRole() == Role.USER)
            kafkaActivateProfileService.send("user-profile-topic", activateProfileRequest);
        else if (auth.getRole() == Role.BRAND) {
            kafkaActivateProfileService.send("brand-profile-topic", activateProfileRequest);
        }
        return true;
    }

    private Otp verifyOtp(List<Otp> otpList, String code) {
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
