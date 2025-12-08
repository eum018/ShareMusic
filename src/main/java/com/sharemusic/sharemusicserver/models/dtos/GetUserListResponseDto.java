package com.sharemusic.sharemusicserver.models.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetUserListResponseDto {

    Long user_id;
    String username;

}
