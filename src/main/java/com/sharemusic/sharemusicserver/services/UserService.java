package com.sharemusic.sharemusicserver.services;

import com.sharemusic.sharemusicserver.models.dtos.*;
import com.sharemusic.sharemusicserver.models.entities.Role;
import com.sharemusic.sharemusicserver.models.entities.RoleEntity;
import com.sharemusic.sharemusicserver.models.entities.UserEntity;
import com.sharemusic.sharemusicserver.models.tokens.Jwt;
import com.sharemusic.sharemusicserver.models.tokens.JwtProvider;
import com.sharemusic.sharemusicserver.models.tokens.TokenClaimModel;
import com.sharemusic.sharemusicserver.repository.RoleRepository;
import com.sharemusic.sharemusicserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    final JwtProvider jwtProvider;
    final UserRepository userRepository;
    final RoleRepository roleRepository;


    @Transactional
    public RegistUserResponseDto registerUser(RegistUserDto registerUserDto) {

        System.out.println("화원 가입 서비스");

        RegistUserResponseDto badResponse = RegistUserResponseDto.builder()
                .status(false)
                .build();

        if(userRepository.existsByEmail(registerUserDto.getEmail().trim())) {
            System.out.println("해당하는 이메일은 이미 존재 합니다. : " + registerUserDto.getEmail().trim());
            return badResponse;
        }


        try {
            UserEntity user = userRepository.save(registerUserDto.toEntity());
            RoleEntity role = RoleEntity.builder()
                    .user(user)
                    .role(Role.USER)
                    .build();
            user.addRole(role);
            roleRepository.save(role);
            return new RegistUserResponseDto(user.getEmail(), user.getName(), true);
        }catch (Exception e){

            return badResponse;

        }
    }

    public VerifyUserResponseDto verifyUser(VerifyUserDto verifyUserDto) {

        System.out.println("로그인 서비스 실행");

        VerifyUserResponseDto noVerifyResponse = VerifyUserResponseDto.builder().isValid(false).build();

        UserEntity user = userRepository.findByEmail(verifyUserDto.getEmail().trim());

        if(user == null) {
            System.out.println("해당하는 email은 존재하지 않습니다.");
            return noVerifyResponse;
        }

        if(!user.getPassword().equals(verifyUserDto.getPassword().trim())) {
            System.out.println("원래 비밀번호 : " + user.getPassword());
            System.out.println("사용자가 입력한 비밀번호 : " + verifyUserDto.getPassword());
            System.out.println("비밀번호가 일치하지 않음");
            return noVerifyResponse;
        }

        System.out.println("반환 할 user 정보 | " + "name : " + user.getName() + ", id : " + user.getUser_id());

        return VerifyUserResponseDto.builder()
                .isValid(true)
                .userRoles(user.getUser_roles().stream().map(RoleEntity::getRole).collect(Collectors.toSet()))
                .name(user.getName())
                .user_id(user.getUser_id().toString())
                .build();
    }


    public Jwt defineJwtByUser(UserEntity user) {

        System.out.println("Jwt 발행");

        Map<String, Object> claims = new HashMap<>();

        claims.put(JwtProvider.jwtKey, TokenClaimModel.builder()
                .email(user.getEmail())
                .roles(user.getUser_roles().stream().map(RoleEntity::getRole).collect(Collectors.toSet()))
                .build());

        return jwtProvider.createJwt(claims);
    }

    @Transactional
    public void updateRefreshToken(String email, String newRefreshToken){

        System.out.println("DB내 refresh Token 업데이트 | " + "새로운 refresh token : " + newRefreshToken);

        UserEntity user = userRepository.findByEmail(email);
        if(user == null){
            return;
        }
        user.updateRefreshToken(newRefreshToken);
    }



    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Transactional
    public boolean addUserRole(String email, Role addRole) {
        UserEntity user = userRepository.findByEmail(email);

        if(user.getUser_roles().stream().anyMatch(role -> role.getRole().equals(addRole)))
            return false;


        RoleEntity newRole = RoleEntity.builder()
                .role(addRole)
                .user(user)
                .build();
        user.addRole(newRole);
        roleRepository.save(newRole);
        return true;
    }












}
