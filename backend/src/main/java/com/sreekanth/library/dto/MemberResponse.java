package com.sreekanth.library.dto;

import com.sreekanth.library.entity.MemberStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDate membershipDate;
    private MemberStatus status;
    private Long userId;
}
