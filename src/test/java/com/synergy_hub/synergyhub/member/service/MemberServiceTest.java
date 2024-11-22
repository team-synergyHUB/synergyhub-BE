package com.synergy_hub.synergyhub.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.synergy_hub.synergyhub.member.dto.AddMemberRequest;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.exception.EmailAlreadyExistException;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@SpringBootTest
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() throws IOException {

        //given
        AddMemberRequest request = new AddMemberRequest("member1", "test1@gmail.com", "qwer123");

        String encodePassword = "qewkjlsdkf1wekljdsfio";
        when(passwordEncoder.encode(anyString())).thenReturn(encodePassword);

        Member member = Member.createMember(request.getNickname(), request.getEmail(),
            request.getPassword());
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        //when
        memberService.save(request);

        //then
        verify(passwordEncoder).encode(anyString());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입시 이메일이 중복될 경우 EmailAlreadyExistException 발생")
    void EmailAlreadyExistExceptionTest() throws IOException {

        //given
        AddMemberRequest request = new AddMemberRequest("member2", "test1@gmail.com", "qwer123");

        //when
        when(memberRepository.existsByEmail("test1@gmail.com")).thenReturn(true);

        //then
        Assertions.assertThrows(EmailAlreadyExistException.class, () -> {
            memberService.save(request);
        });

        verify(memberRepository, times(1)).existsByEmail("test1@gmail.com");


    }
}