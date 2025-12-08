package com.sharemusic.sharemusicserver.models.tokens;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Jwt {

    private String assessToken;
    private String refreshToken;

}

