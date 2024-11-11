package com.geeksforgeeks.DigitalLibrary.service;


import com.geeksforgeeks.DigitalLibrary.entity.Member;
import com.geeksforgeeks.DigitalLibrary.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MemberService {

    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository,PasswordEncoder passwordEncoder){
        this.memberRepository=memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member addMember(Member member){
        log.info("Saving a new member");
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        Member savedMember = this.memberRepository.save(member);
        log.info("Saved a new member with ID: {}", member.getId());
        return savedMember;
    }

    public List<Member> listmember(){
        return this.memberRepository.findAll();
    }

    public Member getMemberById(UUID memberId) {
        Optional<Member> memberOptional = this.memberRepository.findById(memberId);
        return memberOptional.orElse(null);
    }

    public Member updateMember(Member member){
        Member existingMember = this.getMemberById(member.getId());
        if (existingMember == null) {
            log.info("Update Failed!");
            return null;
        }
        member = this.memberRepository.save(member);
        log.info("Update success!");
        return member;
    }
}
