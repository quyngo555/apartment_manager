package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.User;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.jwt.JwtTokenProvider;
import com.vmo.apartment_manager.payload.request.LoginRequest;
import com.vmo.apartment_manager.payload.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthApi {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenProvider tokenProvider;
  @PostMapping("/login")
  public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    // Xác thực thông tin người dùng Request lên
    try{
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUsername(),
              loginRequest.getPassword()
          )
      );
      // Nếu không xảy ra exception tức là thông tin hợp lệ
      // Set thông tin authentication vào Security Context
      SecurityContextHolder.getContext().setAuthentication(authentication);
      // Trả về jwt cho người dùng.
      String jwt = tokenProvider.generateToken((User) authentication.getPrincipal());
      return new LoginResponse(jwt);
    }catch (Exception e){
      throw new NotFoundException(ConstantError.LOGIN_ERROR);
    }


  }

}