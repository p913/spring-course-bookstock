package com.pvil.otuscourse.bookstockmvcformlogin.service;

import com.github.cloudyrock.mongock.SpringMongock;
import com.pvil.otuscourse.bookstockmvcformlogin.changelog.EtalonUsersForTests;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UsersServiceTest {
    @Autowired
    private UsersService usersService;

    @Autowired
    private SpringMongock mongock;

    @BeforeEach
    public void prepareDatabase() {
        mongock.execute();
    }

    @Test
    @DisplayName("Получение пользователя по id")
    public void getAuthorByIdTest() {
        User existent = EtalonUsersForTests.getExistentReader();
        Optional<User> user = usersService.getById(existent.getId());
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get()).isEqualTo(existent);
    }
}
