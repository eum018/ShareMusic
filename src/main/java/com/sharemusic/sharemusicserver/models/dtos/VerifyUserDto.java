package com.sharemusic.sharemusicserver.models.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDto {

    String password;
    String email;

}
