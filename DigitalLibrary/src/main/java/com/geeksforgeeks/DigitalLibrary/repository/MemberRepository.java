package com.geeksforgeeks.DigitalLibrary.repository;

import com.geeksforgeeks.DigitalLibrary.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

   Optional <Member> findByUsername(String username);
}