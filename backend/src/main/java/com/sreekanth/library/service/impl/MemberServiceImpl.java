package com.sreekanth.library.service.impl;

import com.sreekanth.library.dto.MemberRequest;
import com.sreekanth.library.dto.MemberResponse;
import com.sreekanth.library.entity.Member;
import com.sreekanth.library.exception.ApiException;
import com.sreekanth.library.exception.MemberNotFoundException;
import com.sreekanth.library.mapper.MemberMapper;
import com.sreekanth.library.repository.MemberRepository;
import com.sreekanth.library.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream().map(memberMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getById(Long id) {
        return memberMapper.toResponse(find(id));
    }

    @Override
    @Transactional
    public MemberResponse create(MemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(HttpStatus.CONFLICT, "Member email already exists");
        }
        return memberMapper.toResponse(memberRepository.save(memberMapper.toEntity(request)));
    }

    @Override
    @Transactional
    public MemberResponse update(Long id, MemberRequest request) {
        Member member = find(id);
        if (memberRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Member email already exists");
        }
        memberMapper.updateEntity(member, request);
        return memberMapper.toResponse(member);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        memberRepository.delete(find(id));
    }

    private Member find(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }
}
