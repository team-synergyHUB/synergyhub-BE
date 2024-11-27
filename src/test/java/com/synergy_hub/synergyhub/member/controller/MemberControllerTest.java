package com.synergy_hub.synergyhub.member.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synergy_hub.synergyhub.member.dto.MemberAddRequest;
import com.synergy_hub.synergyhub.member.dto.MemberUpdateRequest;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import com.synergy_hub.synergyhub.member.service.MemberService;
import com.synergy_hub.synergyhub.team.entity.Label;
import com.synergy_hub.synergyhub.team.entity.MemberTeam;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberService memberService;

    private final String url = "/members";

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext(); // 각 테스트 전에 SecurityContext 초기화
    }

    @DisplayName("회원가입 테스트")
    @Test
    public void signUpTest() throws Exception {

        //given
        String nickname = "gildong";
        String email = "gildong@gmail.com";
        String password = "qwer1234";

        MemberAddRequest addMemberRequest = new MemberAddRequest(nickname, email, password);

        //when
        ResultActions result = mockMvc.perform(
            post(url + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addMemberRequest))
        );

        //then
        result
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("Member created successfully"))
            .andExpect(jsonPath("$.payload.id").value(1L));
    }

    @DisplayName("모든 회원 조회 테스트")
    @Test
    public void getAllMembersTest() throws Exception {

        //given
        memberRepository.save(Member.createMember("member1", "member1@gmail.com", "1234"));
        memberRepository.save(Member.createMember("member2", "member2@gmail.com", "12345"));
        memberRepository.save(Member.createMember("member3", "member3@gmail.com", "12345"));

        //when
        ResultActions result = mockMvc.perform(get(url));

        //then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Get All Members successfully"))
            .andExpect(jsonPath("$.payload.[0].nickname").value("member1"))
            .andExpect(jsonPath("$.payload.[1].email").value("member2@gmail.com"));
    }

    @DisplayName("내 정보 조회 테스트")
    @Test
    public void getMyInfoTest() throws Exception {
        // given
        String email = "member1@gmail.com";
        Member member = Member.createMember("member1", email, "1234");
        memberRepository.save(member);

        // 인증 객체 생성
        MemberDetails memberDetails = new MemberDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        ResultActions result = mockMvc.perform(get(url + "/me")
            .with(user(memberDetails))); // 인증된 사용자로 요청

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Get My Info successfully"))
            .andExpect(jsonPath("$.payload.nickname").value("member1"))
            .andExpect(jsonPath("$.payload.email").value(email));
    }

    @DisplayName("인증되지 않은 사용자가 내 정보 조회를 할시 MemberNotAuthenticatedException 발생")
    @Test
    public void getMyInfoUnauthorizedTest() throws Exception {

        // when
        ResultActions result = mockMvc.perform(get(url + "/me"));

        // then
        result.andExpect(status().isUnauthorized()) // 401 상태 코드 확인
            .andExpect(jsonPath("$.message").value("인증되지 않은 사용자입니다")); // 예외 메시지 확인
    }

    @DisplayName("특정 팀에 속한 모든 회원 조회 테스트")
    @Test
    public void getTeamMemberTest() throws Exception {
        // given
        Member member1 = Member.createMember("member1", "member1@gmail.com", "1234");
        Member member2 = Member.createMember("member2", "member2@gmail.com", "1234");
        Member member3 = Member.createMember("member3", "member3@gmail.com", "1234");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Team teamA = Team.builder()
            .name("teamA")
            .inviteCode("invitec")
            .inviteSecret("inviteSecretCode")
            .isDeleted(Boolean.FALSE)
            .build();
        Team savedTeam = teamRepository.save(teamA);

        MemberTeam memberTeam1 = member1.joinTeam(teamA);
        MemberTeam memberTeam2 = member2.joinTeam(teamA);
        MemberTeam memberTeam3 = member3.joinTeam(teamA);
//        memberTeam1.updateColor("blue");
//        memberTeam2.updateColor("red");
//        memberTeam3.updateColor("yellow");

        // when
        ResultActions result = mockMvc.perform(get(url + "/{teamId}", savedTeam.getId()));

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Get Team Members successfully"))
            .andExpect(jsonPath("$.payload.[0].nickname").value("member1"))
            .andExpect(jsonPath("$.payload.[1].nickname").value("member2"))
            .andExpect(jsonPath("$.payload.[2].email").value("member3@gmail.com"));
    }

    @DisplayName("특정 팀에 속한 회원이 없을 시 MemberNotFoundException 발생")
    @Test
    public void getTeamMemberNotFoundExceptionTest() throws Exception {
        // given
        Team teamA = Team.builder()
            .name("teamA")
            .inviteCode("invitec")
            .inviteSecret("inviteSecretCode")
            .isDeleted(Boolean.FALSE)
            .build();
        Team savedTeam = teamRepository.save(teamA);

        // when
        ResultActions result = mockMvc.perform(get(url + "/{teamId}", savedTeam.getId()));

        // then
        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("해당하는 정보의 사용자를 찾을 수 없습니다."));
    }

    @DisplayName("회원 프로필 정보 수정 테스트")
    @Test
    void updateMemberInfoTest() throws Exception {

        //given
        Member member = Member.createMember("originMember", "member@gmail.com", "qwer123");
        Member savedMember = memberRepository.save(member);

        // 인증 객체 생성
        MemberDetails memberDetails = new MemberDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setNickname("updateMember");


        //when
        ResultActions result = mockMvc.perform(put(url + "/me")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(user(memberDetails)));

        //then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Update MyInfo successfully"));

        Member updatedMember = memberRepository.findByEmail(member.getEmail()).orElseThrow();
        assertThat(updatedMember.getNickname()).isEqualTo("updateMember");
    }


    @DisplayName("인증되지 않은 사용자가 내 정보를 수정할 시 MemberNotAuthenticatedException 발생")
    @Test
    public void updateMyInfoUnauthorizedTest() throws Exception {
        //given
        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setNickname("updateMember");

        // when
        ResultActions result = mockMvc.perform(put(url + "/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isUnauthorized()) // 401 상태 코드 확인
            .andExpect(jsonPath("$.message").value("인증되지 않은 사용자입니다")); // 예외 메시지 확인
    }

    @DisplayName("회원 탈퇴 테스트")
    @Test
    public void MemberDeleteTest() throws Exception {
        //given
        Member member = Member.createMember("member", "member@gmail.com", "qwer123");
        Member savedMember = memberRepository.save(member);

        MemberDetails memberDetails = new MemberDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //when
        ResultActions result = mockMvc.perform(delete(url + "/me")
            .with(user(memberDetails)));

        //then
        result
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.message").value("Delete Account successfully"));

        Member deletedMember = memberRepository.findByEmail(member.getEmail()).orElseThrow();
        assertThat(deletedMember.getDeletedAt()).isNotNull();
    }





}