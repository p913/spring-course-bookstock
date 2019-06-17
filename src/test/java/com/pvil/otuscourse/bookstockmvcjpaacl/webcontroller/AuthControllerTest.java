package com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.User;
import com.pvil.otuscourse.bookstockmvcjpaacl.etalondata.EtalonUsersForTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest extends ControllerBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Вью для страницы логина")
    void getLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("Вью для страницы регистрации")
    void getRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));

    }

    @Test
    @DisplayName("Регистрация пользователя с занятым логином")
    void registerExisting() throws Exception {
        User user = EtalonUsersForTests.getExistentReader();
        when(usersService.existsByLogin(user.getLogin())).thenReturn(true);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(String.format("login=%s&password=%s&fullName=%s&email=%s&stockKeeper=false",
                        URLEncoder.encode(user.getLogin(), StandardCharsets.UTF_8),
                        URLEncoder.encode(user.getPassword(), StandardCharsets.UTF_8),
                        URLEncoder.encode(user.getFullName(), StandardCharsets.UTF_8),
                        URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8))))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors ("user", "login"));
    }

    @Test
    @DisplayName("Регистрация пользователя со свободным логином")
    void registerNew() throws Exception {
        User user = EtalonUsersForTests.getExistentReader();
        when(usersService.existsByLogin(user.getLogin())).thenReturn(false);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(String.format("login=%s&password=%s&fullName=%s&email=%s&stockKeeper=false",
                        URLEncoder.encode(user.getLogin(), StandardCharsets.UTF_8),
                        URLEncoder.encode(user.getPassword(), StandardCharsets.UTF_8),
                        URLEncoder.encode(user.getFullName(), StandardCharsets.UTF_8),
                        URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8))))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/login"));
    }

}