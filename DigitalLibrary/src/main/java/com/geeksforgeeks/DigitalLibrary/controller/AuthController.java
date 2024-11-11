package com.geeksforgeeks.DigitalLibrary.controller;


import com.geeksforgeeks.DigitalLibrary.dto.AuthDto;
import com.geeksforgeeks.DigitalLibrary.dto.AuthResponse;
import com.geeksforgeeks.DigitalLibrary.entity.Member;
import com.geeksforgeeks.DigitalLibrary.exception.IncorrectCredentialsException;
import com.geeksforgeeks.DigitalLibrary.service.AuthenticationService;
import com.geeksforgeeks.DigitalLibrary.service.MemberService;
import com.geeksforgeeks.DigitalLibrary.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, MemberService memberService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody AuthDto authDto) {
        try {
            Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = this.jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(userDetails, jwtToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username or password");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> addMember(@RequestBody Member member) {
        Member memberToAdd = this.memberService.addMember(member);
        UserDetails userDetails = new User(memberToAdd.getUsername(), memberToAdd.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRole())));

        String jwtToken = this.jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(userDetails, jwtToken));
    }

}