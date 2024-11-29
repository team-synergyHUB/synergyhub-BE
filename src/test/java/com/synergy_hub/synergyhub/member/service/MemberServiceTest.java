package com.synergy_hub.synergyhub.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.synergy_hub.synergyhub.member.dto.MemberAddRequest;
import com.synergy_hub.synergyhub.member.dto.MemberResponseDto;
import com.synergy_hub.synergyhub.member.dto.MemberUpdateRequest;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.exception.EmailAlreadyExistException;
import com.synergy_hub.synergyhub.member.exception.MemberNotFoundException;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.config.ConfigData.Options;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        MemberAddRequest request = new MemberAddRequest("member1", "test1@gmail.com", "qwer123");

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
        MemberAddRequest request = new MemberAddRequest("member2", "test1@gmail.com", "qwer123");

        //when
        when(memberRepository.existsByEmail("test1@gmail.com")).thenReturn(true);

        //then
        assertThrows(EmailAlreadyExistException.class, () -> {
            memberService.save(request);
        });

        verify(memberRepository, times(1)).existsByEmail("test1@gmail.com");
    }

    @Test
    @DisplayName("모든 회원 조회 테스트")
    void getAllMembersTest() {

        //given
        Member member1 = Member.createMember("member1", "member1@gmail.com", "qwer123");
        Member member2 = Member.createMember("member2", "member2@gmail.com", "qwer123");
        List<Member> mockMembers = List.of(member1, member2);

        when(memberRepository.findAllByDeletedAtIsNull()).thenReturn(mockMembers);

        //when
        List<MemberResponseDto> members = memberService.findAllMembers();

        //then
        assertThat(members.size()).isEqualTo(2);
        assertThat(members.get(0).getNickname()).isEqualTo("member1");
        assertThat(members.get(1).getEmail()).isEqualTo("member2@gmail.com");
        verify(memberRepository, times(1)).findAllByDeletedAtIsNull();
    }

    @Test
    @DisplayName("모든 회원 조회 - 빈 리스트 반환 시 예외 발생 테스트")
    void getAllMembers_EmptyListTest() {

        // given
        when(memberRepository.findAllByDeletedAtIsNull()).thenReturn(Collections.emptyList());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> {
            memberService.findAllMembers();
        });

        verify(memberRepository, times(1)).findAllByDeletedAtIsNull();
    }

    @Test
    @DisplayName("특정 팀에 속한 모든 회원 조회 테스트")
    void getAllTeamMembersTest() {

        //given
        Long teamId = 1L;
        Member member1 = Member.createMember("member1", "member1@gmail.com", "qwer123");
        Member member2 = Member.createMember("member2", "member2@gmail.com", "qwer123");
        List<Member> mockMembers = List.of(member1, member2);

        when(memberRepository.findMembersByTeam(teamId)).thenReturn(mockMembers);

        //when
        List<MemberResponseDto> members = memberService.findAllByTeam(teamId);

        //then
        assertThat(members.size()).isEqualTo(2);
        assertThat(members.get(1).getEmail()).isEqualTo("member2@gmail.com");
        verify(memberRepository, times(1)).findMembersByTeam(teamId);
    }

    @Test
    @DisplayName("특정 팀에 속한 모든 회원 조회 - 빈 리스트 반환 시 예외 발생 테스트")
    void getAllTeamMembers_EmptyListTest() {

        // given
        when(memberRepository.findMembersByTeam(anyLong())).thenReturn(Collections.emptyList());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> {
            memberService.findAllByTeam(1L);
        });

        verify(memberRepository, times(1)).findMembersByTeam(1L);
    }

    @Test
    @DisplayName("이메일로 회원 조회 테스트")
    void getMemberByEmailTest() {

        //given
        Member member1 = Member.createMember("member1", "member1@gmail.com",
            "qwer123");
        Member member2 = Member.createMember("member2", "member2@gmail.com",
            "qwer123");

        when(memberRepository.findByEmailAndDeletedAtIsNull("member2@gmail.com")).thenReturn(
            Optional.of(member2));

        //when
        MemberResponseDto member = memberService.findByEmail("member2@gmail.com");

        //then
        assertThat(member.getEmail()).isEqualTo("member2@gmail.com");
        assertThat(member.getNickname()).isEqualTo("member2");
        verify(memberRepository, times(1)).findByEmailAndDeletedAtIsNull(
            "member2@gmail.com");

    }

    @Test
    @DisplayName("이메일로 회원 조회 테스트 - 회원이 존재하지 않을시 MemberNotFoundException 발생")
    void getMemberByEmail_OptionalTest() {

        //given
        final String email = "member@gmail.com";
        when(memberRepository.findByEmailAndDeletedAtIsNull(email)).thenReturn(Optional.empty());

        //when & then
        assertThrows(MemberNotFoundException.class, () -> memberService.findByEmail(email));
        verify(memberRepository, times(1)).findByEmailAndDeletedAtIsNull(
            email);

    }

    @Test
    @DisplayName("회원 프로필 업데이트 테스트")
    void updateMemberInfoTest() {

        //given
        Long memberId = 1L;
        String memberEmail = "member1@gmail.com";
        Member member1 = Member.createMember("member1", memberEmail,
            "qwer123");

        MemberUpdateRequest updateRequest = new MemberUpdateRequest();
        updateRequest.setNickname("updateMember1");

        when(memberRepository.findByEmailAndDeletedAtIsNull(memberEmail)).thenReturn(
            Optional.of(member1));
        when(memberRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(
            Optional.of(member1));

        //when
        memberService.updateMemberInfo(memberEmail, updateRequest);

        //then
        Member updatedMember = memberRepository.findByIdAndDeletedAtIsNull(memberId).get();

        assertThat(updatedMember.getNickname()).isEqualTo("updateMember1");
    }

    @Test
    @DisplayName("회원 이메일이 유효하지 않을 경우 memberNotFoundException 발생")
    void updateMemberInfoMemberNotFoundExceptionTest() {
        // given
        String memberEmail = "nonexistent@gmail.com";
        MemberUpdateRequest updateRequest = new MemberUpdateRequest();
        updateRequest.setNickname("updateMember1");

        when(memberRepository.findByEmailAndDeletedAtIsNull(memberEmail)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> {
            memberService.updateMemberInfo(memberEmail, updateRequest);
        });
    }

    @DisplayName("회원 탈퇴 테스트")
    @Test
    void memberDeleteTest() throws Exception {

        //given
        Member member = Member.createMember("member", "member@naver.com", "qwer123");

        when(memberRepository.findByEmailAndDeletedAtIsNull(member.getEmail())).thenReturn(
            Optional.of(member));

        //when
        memberService.deleteMember(member.getEmail());

        //then
        verify(memberRepository).findByEmailAndDeletedAtIsNull(member.getEmail());
        assertThat(member.getDeletedAt()).isNotNull();
    }



}