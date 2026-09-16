package com.sreekanth.library.service.impl;

import com.sreekanth.library.dto.LoginRequest;
import com.sreekanth.library.dto.LoginResponse;
import com.sreekanth.library.dto.RegisterRequest;
import com.sreekanth.library.entity.Member;
import com.sreekanth.library.entity.MemberStatus;
import com.sreekanth.library.entity.Role;
import com.sreekanth.library.entity.User;
import com.sreekanth.library.exception.ApiException;
import com.sreekanth.library.repository.MemberRepository;
import com.sreekanth.library.repository.UserRepository;
import com.sreekanth.library.security.JwtService;
import com.sreekanth.library.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final com.sreekanth.library.security.CustomUserDetailsService userDetailsService;

    public AuthServiceImpl(
            UserRepository userRepository,
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            com.sreekanth.library.security.CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail()) || memberRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .build();
        userRepository.save(user);

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .membershipDate(LocalDate.now())
                .status(MemberStatus.ACTIVE)
                .user(user)
                .build();
        memberRepository.save(member);

        return toLoginResponse(user, member.getId());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));
        Long memberId = memberRepository.findByUserId(user.getId())
                .map(Member::getId)
                .orElse(null);
        return toLoginResponse(user, memberId);
    }

    private LoginResponse toLoginResponse(User user, Long memberId) {
        UserDetails details = userDetailsService.loadUserByUsername(user.getUsername());
        return LoginResponse.builder()
                .token(jwtService.generateToken(details))
                .tokenType("Bearer")
                .username(user.getUsername())
                .role(user.getRole())
                .memberId(memberId)
                .build();
    }
}
