package com.pspmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.dto.responsedto.RegistrationResponseDto;
import com.pspmanagement.model.constant.UserRole;
import com.pspmanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateAdmin() throws Exception {
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        requestDto.setId(1L);
        requestDto.setUsername("newuser");
        requestDto.setEmail("newuser@example.com");
        requestDto.setPassword("password123");
        requestDto.setRoles(Collections.singleton("ADMIN"));
        requestDto.setCompanyName("Acme Inc.");

        RegistrationResponseDto responseDto = new RegistrationResponseDto();
        responseDto.setId(1L);
        responseDto.setUsername("newuser");
        responseDto.setEmail("newuser@example.com");
        responseDto.setRoles(Collections.singleton("ADMIN"));
        responseDto.setCompanyName("Acme Inc.");

        when(userService.registerAsAdmin(any(RegistrationRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/psp-management-tool/user/register-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.id").value(1))
                .andExpect((ResultMatcher) jsonPath("$.username").value("newuser"))
                .andExpect((ResultMatcher) jsonPath("$.email").value("newuser@example.com"));

        verify(userService).registerAsAdmin(any(RegistrationRequestDto.class));
    }
}

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
//import com.pspmanagement.dto.responsedto.RegistrationResponseDto;
//import com.pspmanagement.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
//                .apply(springSecurity())
//                .build();
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void testCreateAdmin() throws Exception {
//        RegistrationRequestDto requestDto = new RegistrationRequestDto();
//        requestDto.setId(1L);
//        requestDto.setUsername("admin");
//        requestDto.setEmail("newuser@example.com");
//        requestDto.setPassword("password123");
//        requestDto.setRoles(Collections.singleton("ADMIN"));
//        requestDto.setCompanyName("Acme Inc.");
//
//        RegistrationResponseDto responseDto = new RegistrationResponseDto();
//        responseDto.setId(1L);
//        responseDto.setUsername("admin");
//        responseDto.setEmail("newuser@example.com");
//        responseDto.setRoles(Collections.singleton("ADMIN"));
//        responseDto.setCompanyName("Acme Inc.");
//
//        when(userService.registerAsAdmin(any(RegistrationRequestDto.class))).thenReturn(responseDto);
//
//        mockMvc.perform(post("/api/v1/psp-management-tool/user/register-admin")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.username").value("admin"))
//                .andExpect(jsonPath("$.email").value("newuser@example.com"));
//
//        verify(userService).registerAsAdmin(any(RegistrationRequestDto.class));
//    }
//}
