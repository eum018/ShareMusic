package com.sharemusic.sharemusicserver.services;

import com.sharemusic.sharemusicserver.models.dtos.GetUserListResponseDto;
import com.sharemusic.sharemusicserver.models.entities.UserEntity;
import com.sharemusic.sharemusicserver.models.tokens.JwtProvider;
import com.sharemusic.sharemusicserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GetUserService {

    final UserRepository userRepository;
    final JwtProvider jwtProvider;

    public List<GetUserListResponseDto> getAllUserList(){

        List<UserEntity> users = userRepository.findAll();

        List<GetUserListResponseDto> result = new ArrayList<>();


        for(UserEntity user : users){
            result.add(GetUserListResponseDto.builder()
                    .user_id(user.getUser_id())
                    .username(user.getName())
                    .build());
        }
        return result;
    }







}
