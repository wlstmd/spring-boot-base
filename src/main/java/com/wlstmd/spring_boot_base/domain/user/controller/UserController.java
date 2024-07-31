package com.wlstmd.spring_boot_base.domain.user.controller;

import com.wlstmd.spring_boot_base.domain.user.dto.UserDto;
import com.wlstmd.spring_boot_base.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 API")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody UserDto.SignUp signUpDto) {
        return ResponseEntity.ok(userService.signUp(signUpDto));
    }

    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "로그인 API")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody UserDto.SignIn signInDto) {
        return ResponseEntity.ok(userService.signIn(signInDto));
    }
}