package com.synergy_hub.synergyhub.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synergy_hub.synergyhub.member.dto.MemberAddRequest;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import com.synergy_hub.synergyhub.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
    MemberService memberService;

    private final String url = "/members";

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


}