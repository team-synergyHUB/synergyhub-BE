package com.synergy_hub.synergyhub.member.service;

import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.dto.MemberAddRequest;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberRole;
import com.synergy_hub.synergyhub.member.exception.EmailAlreadyExistException;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Long save(MemberAddRequest request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String password = passwordEncoder.encode(request.getPassword()); //비밀번호 암호화
        Member member = Member.createMember(request.getNickname(), request.getEmail(), password);
        member.changeRole(MemberRole.USER);
        return memberRepository.save(member).getId();

    }

}
