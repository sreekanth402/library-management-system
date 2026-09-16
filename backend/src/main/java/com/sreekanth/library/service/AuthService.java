package com.sreekanth.library.service;

import com.sreekanth.library.dto.LoginRequest;
import com.sreekanth.library.dto.LoginResponse;
import com.sreekanth.library.dto.RegisterRequest;

public interface AuthService {
    LoginResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
