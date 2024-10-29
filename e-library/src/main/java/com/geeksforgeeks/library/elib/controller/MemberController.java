package com.geeksforgeeks.library.elib.controller;


import com.geeksforgeeks.library.elib.entity.Book;
import com.geeksforgeeks.library.elib.entity.Member;
//import com.geeksforgeeks.library.elib.service.BookService;
import com.geeksforgeeks.library.elib.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/member")
public class MemberController {


    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService=memberService;
    }

    @PostMapping("/add")
    public ResponseEntity<Member> addMember(@RequestBody Member member){
        Member savedmember=this.memberService.addMember(member);
        return new ResponseEntity<>(savedmember, HttpStatus.CREATED);
    }

    @GetMapping("/List")
    public ResponseEntity<List<Member>> getMembers(){
        List<Member> memberList = this.memberService.getAllMembers();
        return new ResponseEntity<>(memberList,HttpStatus.OK);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMemberById(@PathVariable UUID memberId){
        Member member = this.memberService.getMemberById(memberId);
        return new ResponseEntity<>(member,HttpStatus.OK);
    }

    @PostMapping("/Update")
    public ResponseEntity<Member> updateMember(@RequestBody Member member){
        Member UpdatedMember = this.memberService.updateMember(member);
        return new ResponseEntity<>(UpdatedMember,HttpStatus.CREATED);
    }


}
