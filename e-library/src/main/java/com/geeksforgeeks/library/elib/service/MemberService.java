package com.geeksforgeeks.library.elib.service;

import com.geeksforgeeks.library.elib.entity.Member;
import com.geeksforgeeks.library.elib.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member addMember(Member member) {
        log.info("Saving a new member");
        Member savedMember = this.memberRepository.save(member);
        log.info("Saved a new member with ID: {}", savedMember.getId());
        return savedMember;
    }

    public List<Member> getAllMembers() {
        return this.memberRepository.findAll();
    }

    public Member getMemberById(UUID memberId) {
        Optional<Member> memberOptional = this.memberRepository.findById(memberId);
        return memberOptional.orElse(null);
    }

    public Member updateMember(Member member) {
        log.info("Updating a member by ID: {}", member.getId());
        Optional<Member> exMemberOptional = this.memberRepository.findById(member.getId());

        if (exMemberOptional.isPresent()) {
            Member exMember = exMemberOptional.get();

            // Updated to use subscriptionStatus instead of subStatus
            Member updatedMember = exMember.toBuilder()
                    .firstName(member.getFirstName() != null ? member.getFirstName() : exMember.getFirstName())
                    .lastName(member.getLastName() != null ? member.getLastName() : exMember.getLastName())
                    .email(member.getEmail() != null ? member.getEmail() : exMember.getEmail())
                    .mobileNumber(member.getMobileNumber() != null ? member.getMobileNumber() : exMember.getMobileNumber())
                    .subscriptionStatus(member.getSubscriptionStatus() != null ? member.getSubscriptionStatus() : exMember.getSubscriptionStatus())
                    .build();

            return this.memberRepository.save(updatedMember);
        } else {
            log.warn("Member with ID {} not found, update failed", member.getId());
            throw new EntityNotFoundException("Member not found with ID: " + member.getId());
        }
    }
}
