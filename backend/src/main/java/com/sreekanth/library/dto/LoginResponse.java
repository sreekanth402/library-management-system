package com.sreekanth.library.dto;

import com.sreekanth.library.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String token;
    private String tokenType;
    private String username;
    private Role role;
    private Long memberId;
}
