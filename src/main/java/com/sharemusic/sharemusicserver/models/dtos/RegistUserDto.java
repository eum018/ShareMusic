package com.sharemusic.sharemusicserver.models.dtos;


import com.sharemusic.sharemusicserver.models.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistUserDto {

    private String email;
    private String password;
    private String name;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
    }


}
