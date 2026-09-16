package com.sreekanth.library.mapper;

import com.sreekanth.library.dto.MemberRequest;
import com.sreekanth.library.dto.MemberResponse;
import com.sreekanth.library.entity.Member;
import com.sreekanth.library.entity.MemberStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MemberMapper {

    public Member toEntity(MemberRequest request) {
        return Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .membershipDate(LocalDate.now())
                .status(request.getStatus() == null ? MemberStatus.ACTIVE : request.getStatus())
                .build();
    }

    public void updateEntity(Member member, MemberRequest request) {
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setAddress(request.getAddress());
        if (request.getStatus() != null) {
            member.setStatus(request.getStatus());
        }
    }

    public MemberResponse toResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .membershipDate(member.getMembershipDate())
                .status(member.getStatus())
                .userId(member.getUser() == null ? null : member.getUser().getId())
                .build();
    }
}
