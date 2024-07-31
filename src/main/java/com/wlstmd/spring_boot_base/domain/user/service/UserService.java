package com.wlstmd.spring_boot_base.domain.user.service;

import com.wlstmd.spring_boot_base.domain.user.dto.UserDto;
import com.wlstmd.spring_boot_base.domain.user.entity.User;
import com.wlstmd.spring_boot_base.domain.user.repository.UserRepository;
import com.wlstmd.spring_boot_base.global.error.ErrorCode;
import com.wlstmd.spring_boot_base.global.exception.BusinessException;
import com.wlstmd.spring_boot_base.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Map<String, String> signUp(UserDto.SignUp signUpDto) {
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }

        User user = new User();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "회원가입이 완료되었습니다.");
        return response;
    }

    public Map<String, String> signIn(UserDto.SignIn signInDto) {
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "로그인이 성공적으로 되었습니다.");
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);

        return response;
    }
}