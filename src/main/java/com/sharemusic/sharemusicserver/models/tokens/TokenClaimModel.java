package com.sharemusic.sharemusicserver.models.tokens;

import com.sharemusic.sharemusicserver.models.entities.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Builder
@ToString
@Getter
@Setter
public class TokenClaimModel{

    String email;
    Set<Role> roles;

}

