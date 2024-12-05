package com.moblie.management.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moblie.management.member.dto.MemberDto;
import com.moblie.management.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("아이디 생성")
    void createNewMember() throws Exception {
        //given
        MemberDto.SignUp signDto = new MemberDto.SignUp();
        signDto.setEmail("test@gmail.com");
        signDto.setNickname("testUser");
        signDto.setPassword("testPw");
        signDto.setPassword_confirm("testPw");

        String requestBody = objectMapper.writeValueAsString(signDto);

        //when
        doNothing().when(memberService).signUpMember(signDto);

        //then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입 완료"));
    }

}