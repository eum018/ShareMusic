package com.sharemusic.sharemusicserver.models.dtos;

import com.sharemusic.sharemusicserver.models.entities.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistUserResponseDto {

    String email;
    String name;
    boolean status;

    public RegistUserResponseDto(String email, String name, boolean status) {
        this.email = email;
        this.name = name;
        this.status = status;
    }


}
