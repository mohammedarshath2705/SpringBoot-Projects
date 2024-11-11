package com.geeksforgeeks.DigitalLibrary.service;


import com.geeksforgeeks.DigitalLibrary.entity.Member;
import com.geeksforgeeks.DigitalLibrary.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private MemberRepository memberRepository;

    @Autowired
    public MyUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = this.memberRepository.findByUsername(username);
        if(member.isEmpty()){
            throw new UsernameNotFoundException(String.format("Member not found: %s",username));
        }

        Member m = member.get();
        return User.builder()
                .username(m.getUsername())
                .password(m.getPassword())
                .roles(m.getRole())
                .build();

    }

}
