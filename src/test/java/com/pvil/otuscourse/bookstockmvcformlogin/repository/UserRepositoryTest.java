package com.pvil.otuscourse.bookstockmvcformlogin.repository;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserRepositoryTest {

    @Test
    public void documentMappingTest(@Autowired UserRepository userRepository) {
        User user = new User("Login", "Password", "Full Name", "e@mail.com", true);

        userRepository.save(user);

        assertThat(user.getId()).isNotNull();
        Optional<User> founded = userRepository.findById(user.getId());
        assertThat(founded.isPresent()).isTrue();
        assertThat(founded.get()).isEqualTo(user);
    }

}
