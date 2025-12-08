package com.sharemusic.sharemusicserver.controllers;


import com.sharemusic.sharemusicserver.models.dtos.GetUserListResponseDto;
import com.sharemusic.sharemusicserver.models.tokens.JwtProvider;
import com.sharemusic.sharemusicserver.repository.UserRepository;
import com.sharemusic.sharemusicserver.services.GetUserService;
import com.sharemusic.sharemusicserver.services.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GetUserController {

    @Autowired
    final UserRepository userRepository;
    @Autowired
    final GetUserService getUserService;
    @Autowired
    final JwtProvider  jwtProvider;


    @GetMapping("/user_list")
    public ResponseEntity<List<GetUserListResponseDto>> getUserList(@RequestHeader("Authorization") String accessToken){

        if(accessToken == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(getUserService.getAllUserList());
    }


    @GetMapping("/authorize")
    public ResponseEntity<Map<String, Boolean>> getAuthorize(@RequestHeader("Authorization") String accessToken) {

        System.out.println("Authorization 토큰 : " + accessToken);

        Claims claims = jwtProvider.getClaims(accessToken);

        Map<String, Boolean> response = new HashMap<>();
        response.put("status", !jwtProvider.isAccessTokenExpiration(claims));

        System.out.println("반환 값 : " + response);

        return ResponseEntity.ok(response);


    }








}
