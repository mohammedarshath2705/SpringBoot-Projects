package com.geeksforgeeks.DigitalLibrary.service;


import com.geeksforgeeks.DigitalLibrary.dto.AuthDto;
import com.geeksforgeeks.DigitalLibrary.entity.Member;
import com.geeksforgeeks.DigitalLibrary.exception.IncorrectCredentialsException;
import com.geeksforgeeks.DigitalLibrary.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthenticationService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(MemberRepository memberRepository,PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member login(AuthDto authDto){
        Optional<Member> memberOptional = this.memberRepository.findByUsername(authDto.getUsername());
        if(memberOptional.isEmpty()){
            throw new UsernameNotFoundException(String.format("Member not found  %s ",authDto.getUsername()));
        }
       Member member = memberOptional.get();
        if(!this.passwordEncoder.matches(authDto.getPassword(), member.getPassword())){
            log.info("password incorrect");
            throw new IncorrectCredentialsException("Incorrect password");
        }
        return member;


    }
}
