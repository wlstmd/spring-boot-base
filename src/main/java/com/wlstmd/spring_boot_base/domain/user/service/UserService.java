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

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signUp(UserDto.SignUp signUpDto) {
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }

        User user = new User();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        userRepository.save(user);
    }

    public String signIn(UserDto.SignIn signInDto) {
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        return jwtTokenProvider.createToken(user.getEmail());
    }
}