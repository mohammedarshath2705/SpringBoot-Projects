package com.geeksforgeeks.DigitalLibrary.controller;


import com.geeksforgeeks.DigitalLibrary.entity.Member;
import com.geeksforgeeks.DigitalLibrary.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Member")
public class MemberController {


    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping("/addmember")
    public ResponseEntity<Member> addMember(@RequestBody Member member){
        Member savedmember=this.memberService.addMember(member);
        return new ResponseEntity<>(savedmember, HttpStatus.CREATED);
    }

    @GetMapping("/listmember")
    public  ResponseEntity <List<Member>> listAllMember(){
        List<Member> memberList = this.memberService.listmember();
        return new ResponseEntity<>(memberList,HttpStatus.OK);

    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMemberById(@PathVariable UUID memberId){
        Member member = this.memberService.getMemberById(memberId);
        return new ResponseEntity<>(member,HttpStatus.OK);
    }

    @PutMapping("/updatemember")
    public ResponseEntity<Member> updateMember(@RequestBody Member member){
        Member updateMember = this.memberService.updateMember(member);
        return new ResponseEntity<>(updateMember,HttpStatus.OK);
    }
}
