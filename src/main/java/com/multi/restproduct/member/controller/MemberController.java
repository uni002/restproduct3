package com.multi.restproduct.member.controller;

import com.multi.restproduct.common.ResponseDto;
import com.multi.restproduct.member.dto.MemberDto;
import com.multi.restproduct.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/members/{memberid}")
    public ResponseEntity<ResponseDto> findByMemberId(@PathVariable ("memberid") String memberId) {
        Optional<MemberDto> member = memberService.findByMemberId(memberId);

        if(member.isEmpty()){
            return ResponseEntity.ok(new ResponseDto(HttpStatus.NO_CONTENT, "회원정보를 찾을수없습니다", null));
        }
        return ResponseEntity.ok(new ResponseDto(HttpStatus.OK , "회원조회성공" , member));
    }

}
