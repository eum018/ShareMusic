package com.sharemusic.sharemusicserver.models.dtos;

import com.sharemusic.sharemusicserver.models.entities.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;


@Builder
@Getter
public class VerifyUserResponseDto {
    boolean isValid;
    Set<Role> userRoles;
    String name;
    String user_id;
}

