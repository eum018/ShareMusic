package com.sharemusic.sharemusicserver.controllers;
import com.sharemusic.sharemusicserver.models.dtos.RegistUserDto;
import com.sharemusic.sharemusicserver.models.dtos.RegistUserResponseDto;
import com.sharemusic.sharemusicserver.models.dtos.VerifyUserDto;
import com.sharemusic.sharemusicserver.models.dtos.VerifyUserResponseDto;
import com.sharemusic.sharemusicserver.models.entities.UserEntity;
import com.sharemusic.sharemusicserver.models.tokens.Jwt;
import com.sharemusic.sharemusicserver.models.tokens.JwtProvider;
import com.sharemusic.sharemusicserver.repository.UserRepository;
import com.sharemusic.sharemusicserver.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;



    @PostMapping(path = "/signup")
    public ResponseEntity<RegistUserResponseDto> signup(@RequestBody RegistUserDto signupUser){
        System.out.println("횐원 가입 controller 실행");
        return ResponseEntity.ok(userService.registerUser(signupUser));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<VerifyUserResponseDto> login(@RequestBody VerifyUserDto loginUser, HttpServletResponse httpServletResponse) {

        System.out.println("로그인 controller 실행");
        System.out.println(loginUser.getEmail() + ", " + loginUser.getPassword());

        UserEntity user = userRepository.findByEmail(loginUser.getEmail().trim());

        if(user == null){
            System.out.println("사용자 조회 불가 1");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    VerifyUserResponseDto.builder().isValid(false).build()
            );
        }

        if(!user.getPassword().equals(loginUser.getPassword().trim())){
            System.out.println("비밀번호가 일치하지 않음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    VerifyUserResponseDto.builder().isValid(false).build()
            );
        }

        Jwt newJwt = userService.defineJwtByUser(user);
        System.out.println("새로운 JWT 생성 | " + "access : " + newJwt.getAssessToken() + ", refresh : " + newJwt.getRefreshToken());
        userService.updateRefreshToken(user.getEmail(), newJwt.getRefreshToken());

        // Cookie 세팅
        Cookie cookie = new Cookie("refreshToken", newJwt.getRefreshToken());
        cookie.setPath("/");
        cookie.setMaxAge(JwtProvider.refreshTokenExpiration);
        httpServletResponse.addCookie(cookie);

        httpServletResponse.addHeader("Authorization",  "Bearer " + newJwt.getAssessToken());

        // 회원 검증 정보 반환
        return ResponseEntity.ok(userService.verifyUser(loginUser));
    }


    @PostMapping("refresh_token")
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken) {


        UserEntity user = userRepository.findByRefreshToken(refreshToken);

        if(user == null){
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Jwt jwt = userService.defineJwtByUser(user);
        userService.updateRefreshToken(user.getEmail(), jwt.getRefreshToken());

        Cookie cookie = new Cookie("refresh_token", jwt.getRefreshToken());


        return ResponseEntity.ok(jwt.getAssessToken());
    }

}
