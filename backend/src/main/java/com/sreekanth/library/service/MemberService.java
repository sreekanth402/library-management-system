package com.sreekanth.library.service;

import com.sreekanth.library.dto.MemberRequest;
import com.sreekanth.library.dto.MemberResponse;

import java.util.List;

public interface MemberService {
    List<MemberResponse> findAll();
    MemberResponse getById(Long id);
    MemberResponse create(MemberRequest request);
    MemberResponse update(Long id, MemberRequest request);
    void delete(Long id);
}
