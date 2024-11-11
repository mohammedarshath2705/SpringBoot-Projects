package com.geeksforgeeks.DigitalLibrary.service;


import com.geeksforgeeks.DigitalLibrary.dto.IssueDataDto;
import com.geeksforgeeks.DigitalLibrary.entity.Book;
import com.geeksforgeeks.DigitalLibrary.entity.IssueData;
import com.geeksforgeeks.DigitalLibrary.entity.Member;
import com.geeksforgeeks.DigitalLibrary.repository.IssueDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class IssueDataService {
    private final BookService bookService;
    private  final MemberService memberService;
    private final IssueDataRepository issueDataRepository;

    @Autowired
    public IssueDataService(BookService bookService, MemberService memberService, IssueDataRepository issueDataRepository) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.issueDataRepository = issueDataRepository;
    }

    public IssueData addIssueData(IssueDataDto issueDataDto){
        Book book = this.bookService.getBookById( issueDataDto.getBookId () );
        Member member = this.memberService.getMemberById ( issueDataDto.getMemberId () );
        if (book == null || member == null) {
            throw new RuntimeException ();
        }
        IssueData issueData = IssueData.builder ()
                .book ( book )
                .member ( member )
                .build ();
        return this.addIssueData ( issueData );
    }

    public IssueData addIssueData(IssueData issueData) {
        log.info ( "Saving a new Issue Data" );
        issueData.calculateAmountPaid ();
        issueData.calculateExpirationDate ();
        IssueData savedIssueData = this.issueDataRepository.save ( issueData );
        log.info ( "Saved a new issue data with ID: {}, for book ID: {} by member ID: {}",
                savedIssueData.getId (), savedIssueData.getBook ().getId (), savedIssueData.getMember ().getId () );
        return issueData;
    }

    public List<IssueData> getIssueDataByMemberId(UUID memberId) {
        List<IssueData> issueDataList = this.issueDataRepository.findByMember_Id(memberId);
        log.info ( "Found: {} issue data for member ID: {}", issueDataList.size (), memberId );
        return issueDataList;
    }
}
