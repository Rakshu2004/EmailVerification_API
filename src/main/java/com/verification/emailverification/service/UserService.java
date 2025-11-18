package com.verification.emailverification.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.verification.emailverification.model.OtpVerification;
import com.verification.emailverification.model.User;
import com.verification.emailverification.repository.OtpVerificationRepository;
import com.verification.emailverification.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpVerificationRepository otpRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "Email already registered!";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String otp = generateOtp();
        OtpVerification otpEntity = new OtpVerification();
        otpEntity.setEmail(user.getEmail());
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000)); // 5 mins
        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(user.getEmail(), otp);

        return "Registration successful! OTP sent to your email.";
    }

    public String verifyOtp(String email, String otp) {
        Optional<OtpVerification> otpRecord = otpRepository.findByEmail(email);
        if (otpRecord.isEmpty()) {
            return "No OTP found for this email!";
        }

        OtpVerification otpEntity = otpRecord.get();

        if (new Date().after(otpEntity.getExpiryTime())) {
            return "OTP expired! Please request again.";
        }

        if (!otpEntity.getOtp().equals(otp)) {
            return "Invalid OTP!";
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return "User not found!";
        }

        User u = user.get();
        u.setEnabled(true);
        userRepository.save(u);

        otpRepository.delete(otpEntity); // remove OTP after success

        return "Email verified successfully!";
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}
