package com.multi.restproduct.member.service;


import com.multi.restproduct.member.dao.MemberMapper;
import com.multi.restproduct.member.dto.MemberDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    public final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public Optional<MemberDto> findByMemberId(String memberId) {


        Optional<MemberDto> member = memberMapper.findByMemberId(memberId);
        return member;
    }
}
